package com.xiao.start.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xiao.start.entity.Employee;
import com.xiao.start.entity.User;
import com.xiao.start.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/27 16:39
 * @Description: 自定义元数据处理器
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    /**
     * 插入操作自动填充
     *
     * @param metaObject 拦截对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填【insert】");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Object object = BaseContext.get();
        if (object instanceof Employee){
            Employee  employee = (Employee)object;
            metaObject.setValue("createUser", employee.getId());
            metaObject.setValue("updateUser", employee.getId());
        }else if(object instanceof User){
            User  currentUser = (User)object;
            metaObject.setValue("createUser", currentUser.getId());
            metaObject.setValue("updateUser", currentUser.getId());
        }


    }

    /**
     * 更新操作自动填充
     *
     * @param metaObject 拦截对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填【update】");
        long id = Thread.currentThread().getId();
        log.info("当前线程id：{}", id);

        // todo：优化
        Object object = BaseContext.get();
        if (object instanceof Employee){
            Employee  employee = (Employee)object;
            metaObject.setValue("updateUser", employee.getId());
        }else if(object instanceof User){
            User  currentUser = (User)object;
            metaObject.setValue("updateUser", currentUser.getId());
        }
        metaObject.setValue("updateTime", LocalDateTime.now());




    }
}
