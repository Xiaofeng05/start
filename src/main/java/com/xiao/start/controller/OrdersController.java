package com.xiao.start.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiao.start.common.R;
import com.xiao.start.dto.OrdersDto;
import com.xiao.start.entity.Orders;
import com.xiao.start.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/2 15:53
 * @Description:
 *      用户订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders, HttpSession session) {
        ordersService.submit(orders,session);
        return R.success("下单成功！");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(Integer page, Integer pageSize,HttpSession session){
        Page<OrdersDto> ordersDtoPage = ordersService.userPage(page, pageSize,session);
        return R.success(ordersDtoPage);
    }

    @GetMapping("/page")
    public R<Page<Orders>> page(Orders orders,String beginTime,String endTime ,Integer page, Integer pageSize){
        Page<Orders> ordersPage = ordersService.page(orders,beginTime,endTime,page, pageSize);
        return R.success(ordersPage);
    }


    @PutMapping
    public R<String> updateOrder(@RequestBody Orders orders){
        log.info("orders{}", orders);
        ordersService.updateOrder(orders);
        return R.success("订单派送成功！");
    }


}
