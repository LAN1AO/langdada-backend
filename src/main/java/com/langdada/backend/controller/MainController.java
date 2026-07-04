package com.langdada.backend.controller;

import com.langdada.backend.common.BaseResponse;
import com.langdada.backend.common.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统")
@RestController
@RequestMapping("/")
public class MainController {

    @ApiOperation("健康检查（公开）")
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}
