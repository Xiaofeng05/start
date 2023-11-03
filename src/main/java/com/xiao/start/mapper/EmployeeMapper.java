package com.xiao.start.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiao.start.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/26 23:32
 * @Description:
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
