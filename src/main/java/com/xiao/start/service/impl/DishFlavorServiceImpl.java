package com.xiao.start.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xiao.start.entity.DishFlavor;
import com.xiao.start.mapper.DishFlavorMapper;
import com.xiao.start.service.DishFlavorService;
import org.springframework.stereotype.Service;


/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 23:44
 * @Description:
 */
@Service("dishFlavorService")
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
