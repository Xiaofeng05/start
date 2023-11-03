package com.xiao.start.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiao.start.common.R;
import com.xiao.start.dto.DishDto;
import com.xiao.start.entity.Dish;
import com.xiao.start.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/29 12:13
 * @Description:
 *      菜品 控制器
 */
@Slf4j
@RestController
@RequestMapping("/dish")
@Api(tags = "菜品相关接口")
public class DishController {


    @Resource
    private DishService dishService;



    /**
     * 新增菜品
     * @param dishDto 封装的对象
     * @return  返回消息提示
     */
    @PostMapping
    @ApiOperation(value = "新增菜品接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dishDto",value = "菜品封装的对象",required = true)
    })
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return  R.success("新增菜品成功");
    }

    /**
     * 分页查询菜品信息
     * @param page     当前页数
     * @param pageSize 每页的记录数
     * @param name 菜品名称
     * @return 查询菜品信息
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询菜品(封装)信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页的记录数",required = true),
            @ApiImplicitParam(name = "name",value = "菜品名称", required = false)
    })
    public R<Page<DishDto>> page(Integer page, Integer pageSize, String name){
        Page<DishDto> dishDtoPage = dishService.page(page, pageSize, name);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询(封装)菜品信息
     * @param id  菜品id
     * @return  (封装)菜品信息
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询(封装)菜品信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "菜品id",required = true)
    })
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }



    /**
     * 修改(封装)菜品信息
     * @param dishDto (封装)菜品信息
     * @return  返回消息提示
     */
    @PutMapping
    @ApiOperation(value = "修改(封装)菜品信息接口")
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return  R.success("修改菜品成功！");
    }


    /**
     * 根据条件查询对应的(封装)菜品数据
     * @param dish 菜品
     * @return 菜品列表信息
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据条件查询对应的(封装)菜品数据接口")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> list = dishService.list(dish);
        return R.success(list);
    }

    /**
     * 根据ids删除菜品
     * @param ids   ids
     * @return 返回提示信息
     */
    @DeleteMapping
    @ApiOperation(value = "根据ids删除菜品信息接口")
    public R<String> delete(String ids){
        dishService.deleteWithFlavor(ids);
        return  R.success("删除菜品成功！");
    }


    /**
     * 更新菜品信息
     *
     * @param status  菜品信息状态
     * @param ids     ids
     * @return  返回提示信息
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "更新菜品信息接口")
    public R<String> update(@PathVariable Integer status,String ids){
        dishService.update(status,ids);
        return  R.success("菜品状态更成功！");
    }
}
