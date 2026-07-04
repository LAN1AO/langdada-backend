package com.langdada.backend.common;

import com.langdada.backend.exception.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("通用响应")
public class BaseResponse<T> implements Serializable {

    @ApiModelProperty("状态码")
    private int code;

    @ApiModelProperty("数据")
    private T data;

    @ApiModelProperty("消息")
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
