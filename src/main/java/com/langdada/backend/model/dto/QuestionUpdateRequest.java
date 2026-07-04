package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("更新题目请求")
@Data
public class QuestionUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("题目 id")
    private Long id;

    @ApiModelProperty("题目内容（json格式）")
    private String questionContent;
}
