package com.xiao.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiao.start.common.R;
import com.xiao.start.entity.User;
import com.xiao.start.service.UserService;
import com.xiao.start.utils.BaseContext;
import com.xiao.start.utils.SMSUtils;
import com.xiao.start.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.xiao.start.constant.UserConstant.USER_STATUS;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/30 14:17
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            // 生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}", code);
            // SMSUtils.sendMessage("test","",phone,code);
            // session.setAttribute(phone,code);

            // 将生成的验证码存入redis中，并且设置有效时间5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return R.success("手机验证码发送成功");
        }
        // 调用阿里云的api
        return R.success("手机验证码发送失败");
    }

    @PostMapping("/loginout")
    public R<String>  loginout(HttpSession session){
        session.removeAttribute(USER_STATUS);
        return R.success("用户登出成功");
    }


    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info("map:{}",map);
        // 获取手机号
        String phone = map.get("phone").toString();

        // 获取验证码
        String code = map.get("code").toString();

        // 从session种拿到验证码
        // Object sessionCode = session.getAttribute(phone);

        // 从redis中获取
        Object sessionCode = redisTemplate.opsForValue().get(phone);

        if (sessionCode != null && code.equals(sessionCode)){
            // 匹配成功，就可以登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user == null){
                // 完成用户注册（新用户直接注册一下
                user = new User();
                user.setName("启航用户");
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute(USER_STATUS,user);

            // 如果登录成功，这里把验证码删除
            redisTemplate.delete(phone);
            return R.success(user);
        }

        return R.error("登录失败！！");
    }
}
