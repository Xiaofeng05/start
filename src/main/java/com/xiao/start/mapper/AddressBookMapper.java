package com.xiao.start.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiao.start.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/1 21:43
 * @Description:
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
