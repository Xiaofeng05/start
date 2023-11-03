package com.xiao.start.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiao.start.entity.Category;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 9:11
 * @Description:
 */
public interface CategoryService extends IService<Category> {


    /**
     *  根据id删除分类信息
     * @param id 分类id
     */
    void remove(Long id);



}
