package com.langdada.backend.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页请求")
public class PageRequest {

    @ApiModelProperty("当前页号")
    private int current = 1;

    @ApiModelProperty("页面大小")
    private int pageSize = 10;

    @ApiModelProperty("排序字段")
    private String sortField;

    @ApiModelProperty("排序顺序（默认降序）")
    private String sortOrder = "descend";
}
