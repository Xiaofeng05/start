package com.xiao.start.controller;

import com.xiao.start.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/2 15:52
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/ordersDetails")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;
}
