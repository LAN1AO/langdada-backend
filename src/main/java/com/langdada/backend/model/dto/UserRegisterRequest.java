package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("用户注册请求")
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("账号")
    private String userAccount;

    @ApiModelProperty("密码")
    private String userPassword;

    @ApiModelProperty("确认密码")
    private String checkPassword;
}
