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
@TableName("question")
@ApiModel("题目")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("题目内容（json格式）")
    @TableField("questionContent")
    private String questionContent;

    @ApiModelProperty("应用 id")
    @TableField("appId")
    private Long appId;

    @ApiModelProperty("创建用户 id")
    @TableField("userId")
    private Long userId;

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
