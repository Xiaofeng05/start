package com.xiao.start.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.start.entity.ShoppingCart;
import com.xiao.start.mapper.ShoppingCartMapper;
import com.xiao.start.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/2 14:15
 * @Description:
 */
@Service("/shoppingCartService")
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
