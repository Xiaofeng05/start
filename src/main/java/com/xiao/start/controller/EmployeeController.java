package com.xiao.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiao.start.common.R;
import com.xiao.start.constant.EmployeeConstant;
import com.xiao.start.entity.Employee;
import com.xiao.start.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.xiao.start.constant.EmployeeConstant.EMPLOYEE;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/26 23:36
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 员工登录
     *
     * @param request  存贮信息
     * @param employee 用户登录参数
     * @return 统一封装 结果
     * todo 请求参数封装
     * 这里密码直接加密返回了
     * 复杂逻辑放入service 中
     * 员工存储session常量(
     */
    @PostMapping("login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 检查参数 (非空检验
        // 1. 对密码加密（md5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2.根据页面提交的用户名 username 查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        // 3.找不返回失败
        if (emp == null) {
            return R.error("登录失败");
        }
        // 对比密码，不一致登录失败
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        // 检查员工状态是否可用
        if (emp.getStatus() == 0) {
            return R.error("用户已禁用");
        }
        // 存储员工对象
        request.getSession().setAttribute(EMPLOYEE, emp);

        return R.success(emp);
    }


    /**
     * 退出
     *
     * @param request 请求体
     * @return 退出成功
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute(EMPLOYEE);
        return R.success("退出成功");
    }

    /**
     * 保存员工信息
     *
     * @param employee 员工信息
     * @return 确认
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        //设置初始密码：123123 需要加密存储
        employee.setPassword(DigestUtils.md5DigestAsHex("123123".getBytes()));
        Employee currentEmployee = (Employee) request.getSession().getAttribute(EMPLOYEE);
        employeeService.save(employee);
        log.info("新增员工，员工信息：{}", employee.toString());
        return R.success("新增员工成功！");

    }

    /**
     * 分页查询员工信息
     * @param page  当前页
     * @param pageSize  页面记录数
     * @param name 员工信息（名字）
     * @return 分页员工信息
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(Integer page,Integer pageSize,String name){
        log.info("page:{},pageSize:{},name:{}",page,pageSize,name);
        // 分页构造器
        Page pageInfo = new Page(page,pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 添加执行条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        // 添加排序条件(创建时间
        queryWrapper.orderByDesc(Employee::getCreateTime);
        // 执行代码（直接返回pageInfo
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id 修改员工信息
     * @param employee 员工信息
     * @return  返回值
     */
    @PutMapping()
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        long id = Thread.currentThread().getId();
        log.info("当前线程id：{}", id);
        Employee currentEmployee = (Employee) request.getSession().getAttribute(EMPLOYEE);
        employeeService.updateById(employee);
        return  R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("查询到的id：{}",id);
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }

}



