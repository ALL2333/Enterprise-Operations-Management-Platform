package com.huibai.eomp.service.impl;

import com.huibai.eomp.entity.Projects;
import com.huibai.eomp.mapper.ProjectsMapper;
import com.huibai.eomp.service.IProjectsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Service
public class ProjectsServiceImpl extends ServiceImpl<ProjectsMapper, Projects> implements IProjectsService {

}
