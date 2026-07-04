package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("添加评分结果请求")
@Data
public class ScoringResultAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("结果名称，如物流师")
    private String resultName;

    @ApiModelProperty("结果描述")
    private String resultDesc;

    @ApiModelProperty("结果图片")
    private String resultPicture;

    @ApiModelProperty("结果属性集合 JSON，如 [I,S,T,J]")
    private String resultProp;

    @ApiModelProperty("结果得分范围")
    private Integer resultScoreRange;

    @ApiModelProperty("应用 id")
    private Long appId;

    @ApiModelProperty("创建用户 id")
    private Long userId;
}
