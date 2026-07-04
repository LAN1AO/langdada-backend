package com.langdada.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@TableName("user")
@ApiModel("用户")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("账号")
    @TableField("userAccount")
    private String userAccount;

    @ApiModelProperty("密码")
    @TableField("userPassword")
    private String userPassword;

    @ApiModelProperty("微信开放平台id")
    @TableField("unionId")
    private String unionId;

    @ApiModelProperty("公众号openId")
    @TableField("mpOpenId")
    private String mpOpenId;

    @ApiModelProperty("用户昵称")
    @TableField("userName")
    private String userName;

    @ApiModelProperty("用户头像")
    @TableField("userAvatar")
    private String userAvatar;

    @ApiModelProperty("用户简介")
    @TableField("userProfile")
    private String userProfile;

    @ApiModelProperty("用户角色：user/admin/ban")
    @TableField("userRole")
    private String userRole;

    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除")
    @TableField("isDelete")
    private Integer isDelete;
}
