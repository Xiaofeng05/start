package com.xiao.start.dto;


import com.xiao.start.entity.Dish;
import com.xiao.start.entity.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiao
 */
@Data()
@EqualsAndHashCode(callSuper = false)
public class DishDto extends Dish {

    /**
     * 菜品对应的口味数据
     */
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
