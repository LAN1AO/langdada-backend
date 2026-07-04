package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("用户登录请求")
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("账号")
    private String userAccount;

    @ApiModelProperty("密码")
    private String userPassword;
}
