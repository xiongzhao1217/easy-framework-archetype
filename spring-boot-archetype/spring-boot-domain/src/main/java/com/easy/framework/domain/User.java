package com.easy.framework.domain;

import lombok.Data;

/**
 * 用户对象
 *
 * @author xiongzhao
 */
@Data
public class User {
    /**
     * id
     */
    private Long id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 邮箱
     */
    private String email;
}
