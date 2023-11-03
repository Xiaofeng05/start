package com.xiao.start.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.start.entity.SetmealDish;
import com.xiao.start.mapper.SetmealDishMapper;
import com.xiao.start.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/29 15:47
 * @Description:
 */
@Slf4j
@Service("setmealDishService")
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
