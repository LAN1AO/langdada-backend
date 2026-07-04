package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("管理员添加用户请求")
@Data
public class UserAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("账号")
    private String userAccount;

    @ApiModelProperty("密码")
    private String userPassword;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("用户简介")
    private String userProfile;

    @ApiModelProperty("用户角色：user/admin/ban")
    private String userRole;
}
