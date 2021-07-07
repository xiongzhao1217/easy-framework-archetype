package com.easy.framework.service.impl;

import com.easy.framework.core.base.BaseService;
import com.easy.framework.service.UserService;
import com.easy.framework.dao.UserMapper;
import com.easy.framework.domain.User;
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
