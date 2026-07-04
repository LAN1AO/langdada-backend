package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("添加题目请求")
@Data
public class QuestionAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("题目内容（json格式）")
    private String questionContent;

    @ApiModelProperty("应用 id")
    private Long appId;

    @ApiModelProperty("创建用户 id")
    private Long userId;
}
