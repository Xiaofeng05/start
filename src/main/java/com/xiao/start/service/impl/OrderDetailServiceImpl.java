package com.xiao.start.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.start.entity.OrderDetail;
import com.xiao.start.mapper.OrderDetailMapper;
import com.xiao.start.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/2 15:51
 * @Description:
 */
@Service("ordersDetailsService")
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
