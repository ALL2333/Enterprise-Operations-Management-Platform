package com.huibai.eomp.controller;

import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.entity.Departments;
import com.huibai.eomp.service.IDepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@RestController
@RequestMapping("/system/depts")
public class DepartmentsController {

    @Autowired
    private IDepartmentsService deptService;

    @GetMapping("/tree")
    public Result<List<Departments>> tree() {
        return Result.success(deptService.getTreeList());
    }

    // 新增
    @PostMapping
    public Result<?> add(@RequestBody Departments dept) {
        deptService.addDept(dept);
        return Result.success();
    }

    // 修改
    @PutMapping
    public Result<?> update(@RequestBody Departments dept) {
        deptService.updateDept(dept);
        return Result.success();
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        deptService.removeById(id);
        return Result.success("删除成功");
    }

    // 启用或禁用部门
    @PutMapping("/{id}/status/{status}")
    public Result<?> changeStatus(@PathVariable Integer id, @PathVariable String status) {
        deptService.changeStatus(id, status);
        return Result.success("状态更新成功");
    }
}
