package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("更新评分结果请求")
@Data
public class ScoringResultUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评分结果 id")
    private Long id;

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
}
