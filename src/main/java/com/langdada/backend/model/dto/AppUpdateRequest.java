package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("管理员更新应用请求")
@Data
public class AppUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("应用 id")
    private Long id;

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

    @ApiModelProperty("审核状态：0-待审核, 1-通过, 2-拒绝")
    private Integer reviewStatus;

    @ApiModelProperty("审核信息")
    private String reviewMessage;

    @ApiModelProperty("审核人 id")
    private Long reviewerId;
}
