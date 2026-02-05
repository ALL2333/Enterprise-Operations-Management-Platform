package com.huibai.eomp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.common.security.UserContext;
import com.huibai.eomp.entity.Employees;
import com.huibai.eomp.service.IEmployeesService;
import com.huibai.eomp.vo.EmployeeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 员工信息表 前端控制器
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@RestController
@RequestMapping("/org/employees")
public class EmployeesController {

    @Autowired
    private IEmployeesService employeeService;

    // UC-06: 员工列表
    @GetMapping("/page")
    public Result<IPage<EmployeeVO>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            Integer deptId, String name, String status, String employeeNumber) {
        return Result.success(employeeService.getEmployeePage(new Page<>(current, size), deptId, name, status, employeeNumber));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody EmployeeVO employeeVO) {
        // 1. 设置 ID 确保更新目标正确
        employeeVO.setId(id);
        // 2. 调用自定义的业务方法（该方法内部包含更新 Users 表逻辑）
        employeeService.updateEmployee(employeeVO);
        return Result.success();
    }

    // UC-07: 个人信息查询
    @GetMapping("/me")
    public Result<EmployeeVO> me() {
        Integer userId = UserContext.getUserId(); // 获取当前登录人ID
        return Result.success(employeeService.getMyProfile(userId));
    }

    @PostMapping
    public Result<?> addEmployee(@RequestBody EmployeeVO employeeVO) {
        // 建议在 Service 层实现校验和多表保存逻辑
        employeeService.addEmployee(employeeVO);
        return Result.success("员工入职成功");
    }

    // 员工离职处理
    @PutMapping("/{id}/leave")
    public Result<?> leave(@PathVariable Integer id) {
        Employees emp = new Employees();
        emp.setId(id);
        emp.setStatus("left");
        employeeService.updateById(emp);
        return Result.success();
    }
}
