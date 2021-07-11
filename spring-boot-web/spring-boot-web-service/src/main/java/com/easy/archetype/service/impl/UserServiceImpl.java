package com.easy.archetype.service.impl;

import com.easy.archetype.service.UserService;
import com.easy.framework.core.base.BaseService;
import com.easy.archetype.dao.UserMapper;
import com.easy.archetype.domain.User;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mybatis plus
 * @since 2019-11-13
 */
@Service
public class UserServiceImpl extends BaseService<UserMapper, User> implements UserService {
}
