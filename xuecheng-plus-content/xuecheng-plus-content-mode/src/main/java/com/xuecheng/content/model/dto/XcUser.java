package com.xuecheng.content.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public  class XcUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String password;

    private String salt;

    private String name;
    private String nickname;
    private String wxUnionid;
    private String companyId;
    /**
     * 头像
     */
    private String userpic;

    private String utype;

    private LocalDateTime birthday;

    private String sex;

    private String email;

    private String cellphone;

    private String qq;

    /**
     * 用户状态
     */
    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}