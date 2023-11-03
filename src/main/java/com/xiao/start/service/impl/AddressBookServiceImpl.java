package com.xiao.start.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.start.entity.AddressBook;
import com.xiao.start.mapper.AddressBookMapper;
import com.xiao.start.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/11/1 21:45
 * @Description:
 */
@Service("addressBookService")
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
