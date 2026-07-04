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
@TableName("user_answer")
@ApiModel("用户答题记录")
public class UserAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("应用 id")
    @TableField("appId")
    private Long appId;

    @ApiModelProperty("应用类型（0-得分类，1-角色测评类）")
    @TableField("appType")
    private Integer appType;

    @ApiModelProperty("评分策略（0-自定义，1-AI）")
    @TableField("scoringStrategy")
    private Integer scoringStrategy;

    @ApiModelProperty("用户答案（JSON 数组）")
    @TableField("choices")
    private String choices;

    @ApiModelProperty("评分结果 id")
    @TableField("resultId")
    private Long resultId;

    @ApiModelProperty("结果名称，如物流师")
    @TableField("resultName")
    private String resultName;

    @ApiModelProperty("结果描述")
    @TableField("resultDesc")
    private String resultDesc;

    @ApiModelProperty("结果图标")
    @TableField("resultPicture")
    private String resultPicture;

    @ApiModelProperty("得分")
    @TableField("resultScore")
    private Integer resultScore;

    @ApiModelProperty("用户 id")
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
