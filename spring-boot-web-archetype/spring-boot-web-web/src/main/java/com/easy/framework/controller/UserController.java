package com.easy.framework.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easy.framework.core.domain.http.request.Page;
import com.easy.framework.core.domain.http.response.ApiResult;
import com.easy.framework.domain.User;
import com.easy.framework.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户Controller
 *
 * @author xiongzhao
 */
@RequestMapping("/user")
@RestController
public class UserController {

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 新增
     * @param user
     * @return
     */
    @PostMapping("/add")
    public ApiResult add(@RequestBody User user) {
        userService.save(user);
        return ApiResult.success();
    }

    /**
     * 更新
     * @param user
     * @return
     */
    @PostMapping("/update")
    public ApiResult update(@RequestBody User user) {
        userService.updateById(user);
        return ApiResult.success();
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ApiResult detail(@PathVariable Integer id){
        return ApiResult.success(userService.getById(id));
    }

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    @GetMapping("/list")
    public ApiResult list(Page page, User query){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
//                .eq(User::getName, query.getName())
//                .ge(User::getAge, query.getAge());
        return ApiResult.success(userService.queryPage(page, wrapper));
    }
}
