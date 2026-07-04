package com.langdada.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.langdada.backend.annotion.AuthCheck;
import com.langdada.backend.common.BaseResponse;
import com.langdada.backend.common.DeleteRequest;
import com.langdada.backend.common.PageRequest;
import com.langdada.backend.common.ResultUtils;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.model.dto.UserAnswerAddRequest;
import com.langdada.backend.model.dto.UserAnswerUpdateRequest;
import com.langdada.backend.model.entity.UserAnswer;
import com.langdada.backend.service.IUserAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.langdada.backend.model.constant.UserConstant.ADMIN_ROLE;

@Api(tags = "答题记录管理")
@RestController
@RequestMapping("/userAnswer")
public class UserAnswerController {

    @Resource
    private IUserAnswerService userAnswerService;

    // ==================== 管理员 ====================

    @ApiOperation("添加答题记录（管理员）")
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> addUserAnswer(@RequestBody UserAnswerAddRequest userAnswerAddRequest) {
        ThrowUtils.throwIf(userAnswerAddRequest == null, ErrorCode.PARAMS_ERROR);
        long id = userAnswerService.addUserAnswer(userAnswerAddRequest);
        return ResultUtils.success(id);
    }

    @ApiOperation("获取答题记录（管理员）")
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<UserAnswer> getUserAnswerById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        UserAnswer userAnswer = userAnswerService.getById(id);
        ThrowUtils.throwIf(userAnswer == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(userAnswer);
    }

    @ApiOperation("分页查询答题记录列表（管理员）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Page<UserAnswer>> listUserAnswers(@RequestBody PageRequest pageRequest) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<UserAnswer> page = userAnswerService.listUserAnswers(current, pageSize);
        return ResultUtils.success(page);
    }

    @ApiOperation("更新答题记录（管理员）")
    @PutMapping("/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserAnswer(@RequestBody UserAnswerUpdateRequest userAnswerUpdateRequest) {
        ThrowUtils.throwIf(userAnswerUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = userAnswerService.updateUserAnswer(userAnswerUpdateRequest);
        return ResultUtils.success(result);
    }

    @ApiOperation("删除答题记录（管理员）")
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserAnswer(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = userAnswerService.deleteUserAnswer(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    // ==================== 用户侧 ====================

    @ApiOperation("提交答题记录（用户）")
    @PostMapping("/user/add")
    public BaseResponse<Long> addUserAnswerFromUser(@RequestBody UserAnswerAddRequest userAnswerAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userAnswerAddRequest == null, ErrorCode.PARAMS_ERROR);
        long id = userAnswerService.addUserAnswerFromUser(userAnswerAddRequest, request);
        return ResultUtils.success(id);
    }

    @ApiOperation("查看自己的答题记录（用户）")
    @GetMapping("/user/get/{id}")
    public BaseResponse<UserAnswer> getUserAnswerFromUser(@PathVariable Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        UserAnswer userAnswer = userAnswerService.getById(id);
        ThrowUtils.throwIf(userAnswer == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(userAnswer);
    }

    @ApiOperation("分页查询自己的答题记录（用户）")
    @PostMapping("/user/my/list")
    public BaseResponse<Page<UserAnswer>> listMyAnswers(@RequestBody PageRequest pageRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<UserAnswer> page = userAnswerService.listMyAnswers(current, pageSize, request);
        return ResultUtils.success(page);
    }

    @ApiOperation("删除自己的答题记录（用户）")
    @DeleteMapping("/user/delete")
    public BaseResponse<Boolean> deleteUserAnswerFromUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = userAnswerService.deleteUserAnswerFromUser(deleteRequest.getId(), request);
        return ResultUtils.success(result);
    }
}
