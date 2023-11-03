package com.xiao.start.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.start.common.CustomException;
import com.xiao.start.entity.Category;
import com.xiao.start.entity.Dish;
import com.xiao.start.entity.Setmeal;
import com.xiao.start.mapper.CategoryMapper;
import com.xiao.start.service.CategoryService;
import com.xiao.start.service.DishService;
import com.xiao.start.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 9:13
 * @Description: 分类CategoryServiceImpl 实现类
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类信息
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        // 查询当前是否关联菜品信息，如果有则抛出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            // 已经关联菜品，如果已经关联，需要抛出异常
            throw  new CustomException("当前分类下关联的有菜品，不能直接删除！");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            // 已经关联套餐，如果已经关联，需要抛出异常
            throw  new CustomException("当前分类下关联的有套餐，不能直接删除！");
        }
        // 此时才可一删除
        this.removeById(id);

    }
}
