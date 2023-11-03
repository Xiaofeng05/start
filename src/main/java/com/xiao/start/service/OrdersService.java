package com.xiao.start.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiao.start.dto.OrdersDto;
import com.xiao.start.entity.Orders;

import javax.servlet.http.HttpSession;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/2 15:48
 * @Description:
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders 下单信息
     */
    void submit(Orders orders , HttpSession session);

    /**
     * 分页查询封装用户下单信息
     * @param page  当前页面
     * @param pageSize 每页的数量
     * @return  封装后的信息
     */
    Page<OrdersDto> userPage(Integer page,Integer pageSize,HttpSession session);


    /**
     * 分页（根据条件）查询用户下单信息
     * @param orders 用户下单信息
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param page  当前页面
     * @param pageSize 每页的数量
     * @return
     */
    Page<Orders> page(Orders orders,String beginTime,String endTime,Integer page,Integer pageSize);

    /**
     * 更新订单状态
     * @param orders 用户下单信息
     */
    void updateOrder(Orders orders);

 }
