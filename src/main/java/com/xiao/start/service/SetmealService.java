package com.xiao.start.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xiao.start.dto.SetmealDto;
import com.xiao.start.entity.Setmeal;

import java.util.List;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 9:52
 * @Description:
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时扩展套餐和菜品的关系
     *
     * @param setmealDto  封装的套餐信息
     */
    void saveWithDish(SetmealDto setmealDto);


    /**
     * 删除套餐（同时删除 菜品和套餐的关系
     *
     * @param ids  删除的ids
     */
    void removeWithDish(List<Long> ids);


    /**
     * 查询套餐列表
     *
     * @param setmeal 套餐
     * @return  套餐列表
     */
    List<SetmealDto> list(Setmeal setmeal);

    /**
     * 更新套餐状态
     * @param status 套餐状态
     * @param ids  需要更新的ids
     */
    void updateStatus(Integer status, List<Long> ids);

}
