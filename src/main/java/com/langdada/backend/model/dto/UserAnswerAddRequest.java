package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("添加答题记录请求")
@Data
public class UserAnswerAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("应用 id")
    private Long appId;

    @ApiModelProperty("应用类型（0-得分类，1-角色测评类）")
    private Integer appType;

    @ApiModelProperty("评分策略（0-自定义，1-AI）")
    private Integer scoringStrategy;

    @ApiModelProperty("用户答案（JSON 数组）")
    private String choices;

    @ApiModelProperty("评分结果 id")
    private Long resultId;

    @ApiModelProperty("结果名称")
    private String resultName;

    @ApiModelProperty("结果描述")
    private String resultDesc;

    @ApiModelProperty("结果图标")
    private String resultPicture;

    @ApiModelProperty("得分")
    private Integer resultScore;

    @ApiModelProperty("用户 id")
    private Long userId;
}
