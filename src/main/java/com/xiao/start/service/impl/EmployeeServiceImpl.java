package com.xiao.start.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.start.entity.Employee;
import com.xiao.start.mapper.EmployeeMapper;
import com.xiao.start.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/26 23:34
 * @Description:
 */
@Service("employeeService")
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
