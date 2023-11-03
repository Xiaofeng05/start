package com.xiao.start.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.start.common.CustomException;
import com.xiao.start.dto.SetmealDto;
import com.xiao.start.entity.Category;
import com.xiao.start.entity.Setmeal;
import com.xiao.start.entity.SetmealDish;
import com.xiao.start.mapper.SetmealMapper;
import com.xiao.start.service.CategoryService;
import com.xiao.start.service.SetmealDishService;
import com.xiao.start.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 10:39
 * @Description:
 */
@Slf4j
@Service("setmealService")
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisCacheManager redisCacheManager;

    /**
     * 新增套餐，同时扩展套餐和菜品的关系
     *
     * @param setmealDto  封装的套餐信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "setmealCache",allEntries = true)
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐信息，操作setmeal表
        setmealService.save(setmealDto);
        // 保存套餐和菜品的关系，操作setmealDish表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐（同时删除 菜品和套餐的关系
     *
     * @param ids 删除的ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "setmealCache",allEntries = true)
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = setmealService.count(queryWrapper);
        if (count > 0) {
            // 起售的状态不能删除
            throw new CustomException("套餐正在售卖中......，不能删除");
        }
        // 先删除，套餐表中的数据
        setmealService.removeByIds(ids);
        // 再删除，关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }

    /**
     * 查询套餐列表
     *
     * @param setmeal 套餐信息
     * @return 套餐列表
     */
    @Override
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId+'_'+#setmeal.status")
    public List<SetmealDto> list(Setmeal setmeal) {
        List<SetmealDto> setmealDtoList = null;
        if (setmealDtoList != null) {
            return setmealDtoList;
        }
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        Long categoryId = setmeal.getCategoryId();
        queryWrapper.eq(categoryId != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus, 1);
        List<Setmeal> list = setmealService.list(queryWrapper);
        setmealDtoList = list.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            // 设置口味
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, item.getId());
            List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishLambdaQueryWrapper);
            setmealDto.setSetmealDishes(setmealDishList);
            return setmealDto;
        }).collect(Collectors.toList());

        return setmealDtoList;
    }

    /**
     * 更新套餐状态
     *
     * @param status 套餐状态
     * @param ids    需要更新的ids
     */
    @Override
    @CacheEvict(value = "setmealCache",allEntries = true)
    public void updateStatus(Integer status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        List<Setmeal> list = setmealService.list(queryWrapper);
        for (Setmeal setmeal : list) {
            // 存在 起售和停售选一块了
            if (setmeal.getStatus().equals(status)) {
                throw new CustomException("存在不一致的情况，请仔细选择！");
            }
            setmeal.setStatus(status);
        }
        setmealService.updateBatchById(list);
    }
}
