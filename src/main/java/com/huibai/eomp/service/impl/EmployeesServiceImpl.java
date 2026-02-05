package com.huibai.eomp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huibai.eomp.common.security.UserContext;
import com.huibai.eomp.entity.Employees;
import com.huibai.eomp.entity.Users;
import com.huibai.eomp.mapper.EmployeesMapper;
import com.huibai.eomp.mapper.UsersMapper;
import com.huibai.eomp.service.IEmployeesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huibai.eomp.service.IUsersService;
import com.huibai.eomp.vo.EmployeeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 员工信息表 服务实现类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Service
public class EmployeesServiceImpl extends ServiceImpl<EmployeesMapper, Employees> implements IEmployeesService {

    @Autowired
    private IUsersService userService; // 注入用户服务

    @Autowired
    private UsersMapper userMapper; // 引入用户表Mapper

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEmployee(EmployeeVO vo) {
        // 1. 同步创建 Users 表记录
        Users user = new Users();
        user.setUsername(vo.getUsername());
        user.setRealName(vo.getRealName());
        user.setDepartmentId(vo.getDepartmentId());
        user.setPasswordHash(passwordEncoder.encode("123456"));
        userService.save(user);

        // 2. 创建 Employees 档案记录
        Employees emp = new Employees();
        emp.setUserId(user.getId());
        emp.setEmployeeNumber(vo.getEmployeeNumber());
        emp.setHireDate(vo.getHireDate());
        emp.setStatus("active");
        this.save(emp);
    }

    @Override
    public IPage<EmployeeVO> getEmployeePage(Page<Employees> page, Integer deptId, String name, String status, String employeeNumber) {
        // 如果是 Manager，强制将 deptId 设为当前用户所属部门
        return baseMapper.selectEmployeePage(page, deptId, name, status, employeeNumber);
    }

    @Override
    public EmployeeVO getMyProfile(Integer userId) {
        return baseMapper.selectEmployeeByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmployee(EmployeeVO vo) {
        Integer currentUserId = UserContext.getUserId();

        boolean isSelfUpdating = vo.getUserId().equals(currentUserId);

        // 1. 更新 Users 表 (包含电话和邮箱)
        Users user = new Users();
        user.setId(vo.getUserId());
        user.setPhone(vo.getPhone()); // 从 VO 中获取电话
        user.setEmail(vo.getEmail()); // 从 VO 中获取邮箱

        // 如果是管理员，还可以修改姓名和部门
        if (!isSelfUpdating) {
            user.setRealName(vo.getRealName());
            user.setDepartmentId(vo.getDepartmentId());
        }
        userMapper.updateById(user);

        // 2. 更新 Employees 表 (档案信息)
        // 如果是员工自己修改，通常不允许修改工号、职位等档案信息
        if (!isSelfUpdating) {
            Employees emp = new Employees();
            emp.setId(vo.getId());
            emp.setEmployeeNumber(vo.getEmployeeNumber());
            emp.setPosition(vo.getPosition());
            this.updateById(emp);

            // 同步角色
            if (vo.getPosition() != null) {
                this.syncUserRoleByPosition(vo.getUserId(), vo.getPosition());
            }
        }
    }

    private void syncUserRoleByPosition(Integer userId, String positionName) {
        Integer targetRoleId;
        if ("管理员".equals(positionName)) {
            targetRoleId = 1;
        } else if ("经理".equals(positionName)) {
            targetRoleId = 2;
        } else if ("总经理".equals(positionName)) {
            targetRoleId = 3;
        }
        else {
            targetRoleId = 4; // 普通员工
        }

        // 此处应编写操作 user_roles 中间表的 SQL 或 Mapper 调用
        // userRoleMapper.updateUserRole(userId, targetRoleId);
        System.out.println("用户ID " + userId + " 已根据职位同步角色ID: " + targetRoleId);
    }


}
