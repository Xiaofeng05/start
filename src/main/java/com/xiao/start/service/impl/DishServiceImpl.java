package com.xiao.start.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.start.common.CustomException;
import com.xiao.start.dto.DishDto;
import com.xiao.start.entity.Category;
import com.xiao.start.entity.Dish;
import com.xiao.start.entity.DishFlavor;
import com.xiao.start.mapper.DishMapper;
import com.xiao.start.service.CategoryService;
import com.xiao.start.service.DishFlavorService;
import com.xiao.start.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 10:38
 * @Description:
 */
@Slf4j
@Service("dishService")
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired 
    private DishService dishService;
    
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品，同时插入菜品对应的口味数据 （dish / dishFlavor
     *
     * @param dishDto dishDto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 继承关系，直接保存  新增菜品
        dishService.save(dishDto);
        Long dishId = dishDto.getId();
        // 需要对数据进行处理
        // 同时插入菜品对应的口味数据
        // 关键子段 dish
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

        // 清理某个分类下的菜品所有数据
        String key = "dish_"+ dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
    }

    /**
     * 根据id 拼装类型
     *
     * @param id 菜品id
     * @return DishDto类型
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = dishService.getById(id);
        BeanUtils.copyProperties(dish, dishDto);

        // 菜品口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    /**
     * 修改菜品，同时插入菜品对应的口味数据 （dish / dishFlavor
     *
     * @param dishDto 封装的对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish
        dishService.updateById(dishDto);
        // 先清理 数据/再添加数据 dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
        // 清理所有的菜品信息的缓存数据
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        // 清理某个分类下的菜品所有数据
        String key = "dish_"+ dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
    }


    /**
     * 修改菜品，同时插入菜品对应的口味数据 （dish / dishFlavor
     *
     * @param ids
     */
    @Override
    public void deleteWithFlavor(String ids) {
        // 拿到对应的 菜品id
        // 产出菜品信息  / 还有口味信息也要删除
        String[] arrIds = ids.split(",");
        for (String id : arrIds) {
            // 判断是否停售（停售的菜品支持删除
            Dish dish = dishService.getById(id);
            if (dish.getStatus() == 1) {
                throw new CustomException("该菜品处于在售状态，不能删除！");
            }
            // 删除菜品信息
            dishService.removeById(id);
            // 删除口味信息
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(queryWrapper);

            // 清理某个分类下的菜品所有数据
            String key = "dish_"+ dish.getCategoryId()+"_1";
            redisTemplate.delete(key);
        }


    }

    /**
     * 获取封装后的菜品列表
     *
     * @param dish 获取封装后的菜品列表
     * @return 菜品列表
     */
    @Override
    public List<DishDto> list(Dish dish) {
        List<DishDto> dishDtoList = null;

        // 动态设计设计key（业务 dish_1719957390872236033_1
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();

        // 先从redis获取缓存数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList != null) {
            // 如果有直接返回，无数据需要查询数据库
            return dishDtoList;
        }

        // 如果不存在，先查数据库，在缓存数据加入redis中
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            // 拿到分类id
            Long categoryId = item.getCategoryId();
            // 根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            // 再加上口味数据
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorQueryWrapper.eq(DishFlavor::getDishId, dishId);

            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorQueryWrapper);

            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());
        // 再缓存数据加入redis中(60 分钟过期
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return dishDtoList;
    }

    /**
     * 修改菜品，
     *
     * @param status 状态
     * @param ids    ids
     */
    @Override
    public void update(Integer status, String ids) {
        LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        dishLambdaUpdateWrapper.in(Dish::getId,ids);
        dishLambdaUpdateWrapper.set(Dish::getStatus,status);
        dishService.update(dishLambdaUpdateWrapper);
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
    }

    /**
     * 分页获取菜品（封装后的）
     *
     * @param page     当前页面
     * @param pageSize 每页的条数
     * @param name     菜品名称
     * @return 分页获取菜品信息
     */
    @Override
    public Page<DishDto> page(Integer page, Integer pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);
        // 需要处理分类名称/图片等数据
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> dishList = pageInfo.getRecords();
        List<DishDto> list = dishList.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            // 拿到分类id
            Long categoryId = item.getCategoryId();
            // 根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category!= null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return dishDtoPage;
    }

}
