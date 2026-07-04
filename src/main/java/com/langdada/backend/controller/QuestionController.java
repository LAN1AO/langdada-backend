package com.langdada.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.langdada.backend.annotion.AuthCheck;
import com.langdada.backend.common.BaseResponse;
import com.langdada.backend.common.DeleteRequest;
import com.langdada.backend.common.PageRequest;
import com.langdada.backend.common.ResultUtils;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.model.dto.QuestionAddRequest;
import com.langdada.backend.model.dto.QuestionUpdateRequest;
import com.langdada.backend.model.entity.App;
import com.langdada.backend.model.entity.Question;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.model.enums.ReviewStatusEnum;
import com.langdada.backend.service.IAppService;
import com.langdada.backend.service.IQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.langdada.backend.model.constant.UserConstant.ADMIN_ROLE;
import static com.langdada.backend.model.constant.UserConstant.USER_LOGIN_STATE;

@Api(tags = "题目管理")
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private IQuestionService questionService;

    @Resource
    private IAppService appService;

    // ==================== 管理员 ====================

    @ApiOperation("添加题目（管理员）")
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest) {
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        long id = questionService.addQuestion(questionAddRequest);
        return ResultUtils.success(id);
    }

    @ApiOperation("获取题目信息（管理员）")
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Question> getQuestionById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(question);
    }

    @ApiOperation("分页查询题目列表（管理员）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestions(@RequestBody PageRequest pageRequest) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<Question> page = questionService.listQuestions(current, pageSize);
        return ResultUtils.success(page);
    }

    @ApiOperation("更新题目（管理员）")
    @PutMapping("/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        ThrowUtils.throwIf(questionUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = questionService.updateQuestion(questionUpdateRequest);
        return ResultUtils.success(result);
    }

    @ApiOperation("删除题目（管理员）")
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = questionService.deleteQuestion(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    // ==================== 用户侧 ====================

    @ApiOperation("添加题目到自己的应用（用户）")
    @PostMapping("/user/add")
    public BaseResponse<Long> addQuestionFromUser(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        long id = questionService.addQuestionFromUser(questionAddRequest, request);
        return ResultUtils.success(id);
    }

    @ApiOperation("查看题目（公开）")
    @GetMapping("/user/get/{id}")
    public BaseResponse<Question> getQuestionFromUser(@PathVariable Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 未审核应用的题目仅创建者可见
        App app = appService.getById(question.getAppId());
        if (app != null && !ReviewStatusEnum.APPROVED.getValue().equals(app.getReviewStatus())) {
            User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
            ThrowUtils.throwIf(loginUser == null || !app.getUserId().equals(loginUser.getId()),
                    ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        return ResultUtils.success(question);
    }

    @ApiOperation("按应用 ID 分页查询题目（公开）")
    @GetMapping("/user/list/{appId}")
    public BaseResponse<Page<Question>> listQuestionsByApp(@PathVariable Long appId, PageRequest pageRequest,
                                                           HttpServletRequest request) {
        ThrowUtils.throwIf(appId == null, ErrorCode.PARAMS_ERROR);
        // 未审核应用的题目仅创建者可见
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        if (!ReviewStatusEnum.APPROVED.getValue().equals(app.getReviewStatus())) {
            User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
            ThrowUtils.throwIf(loginUser == null || !app.getUserId().equals(loginUser.getId()),
                    ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<Question> page = questionService.listQuestionsByApp(appId, current, pageSize);
        return ResultUtils.success(page);
    }

    @ApiOperation("分页查询我的题目（用户）")
    @PostMapping("/user/my/list")
    public BaseResponse<Page<Question>> listMyQuestions(@RequestBody PageRequest pageRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<Question> page = questionService.listMyQuestions(current, pageSize, request);
        return ResultUtils.success(page);
    }

    @ApiOperation("更新自己的题目（用户）")
    @PutMapping("/user/update")
    public BaseResponse<Boolean> updateQuestionFromUser(@RequestBody QuestionUpdateRequest questionUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = questionService.updateQuestionFromUser(questionUpdateRequest, request);
        return ResultUtils.success(result);
    }

    @ApiOperation("删除自己的题目（用户）")
    @DeleteMapping("/user/delete")
    public BaseResponse<Boolean> deleteQuestionFromUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = questionService.deleteQuestionFromUser(deleteRequest.getId(), request);
        return ResultUtils.success(result);
    }
}
