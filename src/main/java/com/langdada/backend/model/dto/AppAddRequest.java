package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("添加应用请求")
@Data
public class AppAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("应用名")
    private String appName;

    @ApiModelProperty("应用描述")
    private String appDesc;

    @ApiModelProperty("应用图标")
    private String appIcon;

    @ApiModelProperty("应用类型（0-得分类，1-测评类）")
    private Integer appType;

    @ApiModelProperty("评分策略（0-自定义，1-AI）")
    private Integer scoringStrategy;

    @ApiModelProperty("创建用户 id")
    private Long userId;
}
