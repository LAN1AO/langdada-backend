package com.langdada.backend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("登录用户视图")
public class LoginUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户 id")
    private Long id;

    @ApiModelProperty("账号")
    private String userAccount;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("用户简介")
    private String userProfile;

    @ApiModelProperty("用户角色：user/admin")
    private String userRole;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
