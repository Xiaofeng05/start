package com.xiao.start.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiao.start.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/2 15:50
 * @Description:
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
