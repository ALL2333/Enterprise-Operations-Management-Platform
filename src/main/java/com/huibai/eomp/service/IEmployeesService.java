package com.huibai.eomp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huibai.eomp.entity.Employees;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huibai.eomp.vo.EmployeeVO;

/**
 * <p>
 * 员工信息表 服务类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface IEmployeesService extends IService<Employees> {
    // 分页查询员工列表 (UC-06)
    IPage<EmployeeVO> getEmployeePage(Page<Employees> page, Integer deptId, String name, String status, String employeeNumber);

    // 新增员工（含创建用户账号）
    void addEmployee(EmployeeVO vo);

    // 获取个人档案 (UC-07)
    EmployeeVO getMyProfile(Integer userId);

    // 修改员工信息
    void updateEmployee(EmployeeVO vo);
}
