package com.huibai.eomp.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huibai.eomp.entity.Employees;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huibai.eomp.vo.EmployeeVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 员工信息表 Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface EmployeesMapper extends BaseMapper<Employees> {

    /**
     * 分页查询员工信息（关联 users 和 departments 表）
     */
    IPage<EmployeeVO> selectEmployeePage(
            Page<?> page,
            @Param("deptId") Integer deptId,
            @Param("name") String name,
            @Param("status") String status,
            @Param("employeeNumber") String employeeNumber
    );

    /**
     * 根据用户ID查询单个员工详细信息（用于个人中心）
     */
    EmployeeVO selectEmployeeByUserId(@Param("userId") Integer userId);

}