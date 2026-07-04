package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("管理员更新用户请求")
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户 id")
    private Long id;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("用户简介")
    private String userProfile;

    @ApiModelProperty("用户角色：user/admin/ban")
    private String userRole;
}
