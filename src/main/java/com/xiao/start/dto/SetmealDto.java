package com.xiao.start.dto;

import com.xiao.start.entity.Setmeal;
import com.xiao.start.entity.SetmealDish;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author xiao
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SetmealDto extends Setmeal {

    /**
     * 套餐关系表
     */
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
