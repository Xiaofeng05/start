package com.xiao.start.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiao.start.dto.DishDto;
import com.xiao.start.entity.Dish;

import java.util.List;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 9:52
 * @Description:
 *    菜品Service 接口
 */
public interface DishService extends IService<Dish> {


    /**
     * 新增菜品，同时插入菜品对应的口味数据 （dish / dishFlavor
     *
     * @param dishDto  封装后的菜品
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id 拼装类型
     * 同时查询菜品对应的口味数据
     *
     * @param id 菜品id
     * @return DishDto类型
     */
    DishDto getByIdWithFlavor(Long id);


    /**
     * 修改菜品，同时插入菜品对应的口味数据 （dish / dishFlavor
     *
     * @param dishDto 封装后的菜品
     */
    void updateWithFlavor(DishDto dishDto);

    /**
     * 修改菜品，同时插入菜品对应的口味数据 （dish / dishFlavor
     *
     * @param ids ids
     */
    void deleteWithFlavor(String ids);

    /**
     * 获取封装后的菜品列表
     * @param dish 获取封装后的菜品列表
     * @return 菜品列表
     */
    List<DishDto> list(Dish dish);

    /**
     * 修改菜品，
     * @param status 状态
     * @param ids  ids
     */
    void update(Integer status, String ids);


    /**
     * 分页获取菜品（封装后的）
     * @param page  当前页面
     * @param pageSize 每页的条数
     * @param name 菜品名称
     * @return 分页获取菜品信息
     */
    Page<DishDto> page(Integer page, Integer pageSize, String name);
}
