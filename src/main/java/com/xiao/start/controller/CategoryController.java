package com.xiao.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiao.start.common.R;
import com.xiao.start.entity.Category;
import com.xiao.start.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 9:15
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存分类信息
     *
     * @param category 分类信息
     * @return  信息提示
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类成功！");
    }

    /**
     * 分页查询分类信息
     *
     * @param page     当前页
     * @param pageSize 页面记录数
     * @return 分页分类信息
     */
    @GetMapping("/page")
    public R<Page<Category>> page(Integer page, Integer pageSize) {
        // 构造分页条件
        Page pageInfo = new Page(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 构造排序条件
        queryWrapper.orderByAsc(Category::getSort);
        // 查询分页信息
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类信息
     *
     * @param id long 主键
     * @return 返回成功信息
     */
    @DeleteMapping
    public R<String> delete(Long id) {
        categoryService.remove(id);
        return R.success("分类信息删除成功！");
    }

    /**
     * 修改分类信息
     * @param category  分类信息
     * @return 返回成功信息
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改分类信息成功！");
    }

    /**
     * 根据条件查询 分类信息列表
     * @param category 分类信息
     * @return 分类信息列表
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        // 创建构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        queryWrapper.eq(category.getType()!= null,Category::getType,category.getType());
        // 添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return  R.success(list);
    }

}
