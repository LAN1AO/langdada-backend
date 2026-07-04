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
@TableName("scoring_result")
@ApiModel("评分结果")
public class ScoringResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("结果名称，如物流师")
    @TableField("resultName")
    private String resultName;

    @ApiModelProperty("结果描述")
    @TableField("resultDesc")
    private String resultDesc;

    @ApiModelProperty("结果图片")
    @TableField("resultPicture")
    private String resultPicture;

    @ApiModelProperty("结果属性集合 JSON，如 [I,S,T,J]")
    @TableField("resultProp")
    private String resultProp;

    @ApiModelProperty("结果得分范围，如 80，表示 80及以上的分数命中此结果")
    @TableField("resultScoreRange")
    private Integer resultScoreRange;

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
