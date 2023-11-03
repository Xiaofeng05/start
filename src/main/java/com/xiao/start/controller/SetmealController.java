package com.xiao.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiao.start.common.R;
import com.xiao.start.dto.SetmealDto;
import com.xiao.start.entity.Category;
import com.xiao.start.entity.Setmeal;
import com.xiao.start.service.CategoryService;
import com.xiao.start.service.SetmealDishService;
import com.xiao.start.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/29 15:50
 * @Description: 套餐管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;


    @Autowired
    private CategoryService categoryService;


    /**
     * 新增套装
     *
     * @param setmealDto 封装套装参数
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功！");
    }

    /**
     * 套餐分页查询
     *
     * @param page     当前页
     * @param pageSize 页面记录数
     * @param name     名称
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(Integer page, Integer pageSize, String name) {
        Page pageInfo = new Page(page, pageSize);
        Page setmealDtoPage = new Page();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        // 查询出所需要的信息
        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }

            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功！");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        setmealService.updateStatus(status, ids);
        return R.success("套餐状态更改成功！");
    }

    @GetMapping("/list")
    public R<List<SetmealDto>> list(Setmeal setmeal) {
        List<SetmealDto> setmealDtoList = setmealService.list(setmeal);
        return R.success(setmealDtoList);
    }

}
