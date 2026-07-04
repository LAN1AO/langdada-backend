package com.langdada.backend.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("更新答题记录请求")
@Data
public class UserAnswerUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("答题记录 id")
    private Long id;

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
}
