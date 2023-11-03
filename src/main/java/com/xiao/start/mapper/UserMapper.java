package com.xiao.start.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiao.start.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/30 14:15
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
