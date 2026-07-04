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
@TableName("app")
@ApiModel("应用")
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("应用名")
    @TableField("appName")
    private String appName;

    @ApiModelProperty("应用描述")
    @TableField("appDesc")
    private String appDesc;

    @ApiModelProperty("应用图标")
    @TableField("appIcon")
    private String appIcon;

    @ApiModelProperty("应用类型（0-得分类，1-测评类）")
    @TableField("appType")
    private Integer appType;

    @ApiModelProperty("评分策略（0-自定义，1-AI）")
    @TableField("scoringStrategy")
    private Integer scoringStrategy;

    @ApiModelProperty("审核状态：0-待审核, 1-通过, 2-拒绝")
    @TableField("reviewStatus")
    private Integer reviewStatus;

    @ApiModelProperty("审核信息")
    @TableField("reviewMessage")
    private String reviewMessage;

    @ApiModelProperty("审核人 id")
    @TableField("reviewerId")
    private Long reviewerId;

    @ApiModelProperty("审核时间")
    @TableField("reviewTime")
    private LocalDateTime reviewTime;

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
