package com.xiao.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiao.start.common.R;
import com.xiao.start.entity.ShoppingCart;
import com.xiao.start.entity.User;
import com.xiao.start.service.ShoppingCartService;
import com.xiao.start.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import static com.xiao.start.constant.UserConstant.USER_STATUS;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/2 14:17
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     *
     * @param shoppingCart 购物车
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart,HttpSession session) {

        User currentUser = (User) session.getAttribute(USER_STATUS);
        shoppingCart.setUserId(currentUser.getId());

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentUser.getId());
        // 查询添加进来的菜品是否在购物车中（菜品或者套餐）
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            // 添加菜品
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);


        } else {
            // 添加套餐
            Long setmealId = shoppingCart.getSetmealId();
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, setmealId);

        }
        // 如果已经存在，原来基础上+ 1。（口味呢）
        // SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ? and
        ShoppingCart cartServiceOne = shoppingCartService.getOne(lambdaQueryWrapper);

        if (cartServiceOne != null) {
            // 如果已经存在，原来基础上+ 1。（口味呢）
            // todo:口味
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            // 不存在，保留购物车，默认数量为1
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);

            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }


    /**
     * 查询当前登录用户的购物车信息
     * @return 显示购物车信息
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpSession session){
        // 设置用户id 指定谁下的订单
        User currentUser = (User) session.getAttribute(USER_STATUS);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,currentUser.getId());
        shoppingCartLambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(HttpSession session){
        User currentUser = (User) session.getAttribute(USER_STATUS);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,currentUser.getId());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return R.success("清空购物车成功！");
    }

    /**
     * 减少购物车数量
     * @param shoppingCart 购物车
     * @return
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        User currentUser = (User) session.getAttribute(USER_STATUS);
        shoppingCart.setUserId(currentUser.getId());
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentUser.getId());
        // 查询添加进来的菜品是否在购物车中（菜品或者套餐）
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            // 添加菜品
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            // 添加套餐
            Long setmealId = shoppingCart.getSetmealId();
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(lambdaQueryWrapper);

        if (cartServiceOne != null) {
            // 如果已经存在，原来基础上+ 1。（口味呢）
            // todo:口味
            Integer number = cartServiceOne.getNumber();
            // 当然如果数量 == 0 直接删除保存信息
            if (number -1 == 0){
                shoppingCartService.remove(lambdaQueryWrapper);
                return R.success("商品数量为0，已经删除该信息");
            }
            cartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(cartServiceOne);
        }

        return R.success("购物车更新完成");
    }
}
