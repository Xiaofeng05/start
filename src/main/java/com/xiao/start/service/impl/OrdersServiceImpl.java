package com.xiao.start.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.start.common.CustomException;
import com.xiao.start.dto.OrdersDto;
import com.xiao.start.entity.*;
import com.xiao.start.mapper.OrdersMapper;
import com.xiao.start.service.AddressBookService;
import com.xiao.start.service.OrderDetailService;
import com.xiao.start.service.OrdersService;
import com.xiao.start.service.ShoppingCartService;
import com.xiao.start.utils.BaseContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.xiao.start.constant.UserConstant.USER_STATUS;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/2 15:49
 * @Description:
 */
@Service("/ordersService")
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param orders 下单信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(Orders orders, HttpSession session) {
        // 获取当前用户id（信息
        User currentUser = (User) session.getAttribute(USER_STATUS);
        // 查询用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentUser.getId());
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lambdaQueryWrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }

        // 查询用户数据

        // 查询地址信息
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("地址信息为空，请设置派送信息");
        }
        // 产生订单编号
        long orderId = IdWorker.getId();

        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setUserId(currentUser.getId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());

        // 原子操作，线程安全
        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(item.getName());
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            orderDetail.setImage(item.getImage());
            amount.addAndGet(item.getAmount().multiply((new BigDecimal(item.getNumber()))).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        // 计算总金额
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserName(currentUser.getName());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? null : addressBook.getProvinceName()) +
                (addressBook.getCityName() == null ? "" : ", " + addressBook.getCityName()) +
                (addressBook.getDistrictName() == null ? "" : ", " + addressBook.getDistrictName()) +
                (addressBook.getDetail() == null ? "" : ", " + addressBook.getDetail()));
        orders.setConsignee(addressBook.getConsignee());


        // 完成下单信息（在订单表，订单详情表插入数据）
        this.save(orders);
        orderDetailService.saveBatch(orderDetails);
        // 清空购物车数据吧
        shoppingCartService.remove(lambdaQueryWrapper);

    }

    /**
     * 查询用户下单信息
     *
     * @param page     当前页面
     * @param pageSize 每页的数量
     * @return 封装后的信息
     */
    @Override
    public Page<OrdersDto> userPage(Integer page, Integer pageSize,HttpSession session) {
        Page<Orders> pageInfo = new Page(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        User currentUser = (User) session.getAttribute(USER_STATUS);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<Orders>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, currentUser.getId());
        this.page(pageInfo, ordersLambdaQueryWrapper);

        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");

        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            // 地址信息
            Long addressBookId = item.getAddressBookId();
            AddressBook addressBook = addressBookService.getById(addressBookId);
            if (addressBook == null) {
                throw new CustomException("地址信息为空，请设置派送信息");
            }
            ordersDto.setUserName(currentUser.getName());


            ordersDto.setPhone(addressBook.getPhone());
            ordersDto.setAddress((addressBook.getProvinceName() == null ? null : addressBook.getProvinceName()) +
                    (addressBook.getCityName() == null ? "" : ", " + addressBook.getCityName()) +
                    (addressBook.getDistrictName() == null ? "" : ", " + addressBook.getDistrictName()) +
                    (addressBook.getDetail() == null ? "" : ", " + addressBook.getDetail()));
            ordersDto.setConsignee(addressBook.getConsignee());

            // 产生订单编号
            String number = ordersDto.getNumber();
            LambdaQueryWrapper<OrderDetail> detailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            detailLambdaQueryWrapper.eq(OrderDetail::getOrderId, number);

            List<OrderDetail> list = orderDetailService.list(detailLambdaQueryWrapper);
            ordersDto.setOrderDetails(list);
            return ordersDto;

        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);
        return ordersDtoPage;
    }



    /**
     * 分页（根据条件）查询用户下单信息
     *
     * @param orders    用户下单信息
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param page      当前页面
     * @param pageSize  每页的数量
     * @return
     */
    @Override
    public Page<Orders> page(Orders orders, String beginTime, String endTime, Integer page, Integer pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        if (orders != null) {
            queryWrapper.eq(orders.getNumber() != null, Orders::getNumber, orders.getNumber());
        }
        queryWrapper.ge(StringUtils.isNotBlank(beginTime),Orders::getCheckoutTime,beginTime);

        queryWrapper.le(StringUtils.isNotBlank(endTime),Orders::getCheckoutTime,beginTime);
        // 根据结账时间排序
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        this.page(pageInfo, queryWrapper);

        return pageInfo;
    }

    @Override
    public void updateOrder(Orders orders) {
        if (orders ==null){
            throw new CustomException("当前订单不存在");
        }
        Long ordersId = orders.getId();
        LambdaUpdateWrapper<Orders> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(Orders::getId,ordersId);
        Integer status = orders.getStatus();
        lambdaQueryWrapper.set(Orders::getStatus,status);
        this.update(lambdaQueryWrapper);
    }


}
