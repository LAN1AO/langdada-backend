package com.langdada.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.langdada.backend.annotion.AuthCheck;
import com.langdada.backend.common.BaseResponse;
import com.langdada.backend.common.DeleteRequest;
import com.langdada.backend.common.PageRequest;
import com.langdada.backend.common.ResultUtils;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.model.dto.ScoringResultAddRequest;
import com.langdada.backend.model.dto.ScoringResultUpdateRequest;
import com.langdada.backend.model.entity.App;
import com.langdada.backend.model.entity.ScoringResult;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.model.enums.ReviewStatusEnum;
import com.langdada.backend.service.IAppService;
import com.langdada.backend.service.IScoringResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.langdada.backend.model.constant.UserConstant.ADMIN_ROLE;
import static com.langdada.backend.model.constant.UserConstant.USER_LOGIN_STATE;

@Api(tags = "评分结果管理")
@RestController
@RequestMapping("/scoringResult")
public class ScoringResultController {

    @Resource
    private IScoringResultService scoringResultService;

    @Resource
    private IAppService appService;

    // ==================== 管理员 ====================

    @ApiOperation("添加评分结果（管理员）")
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> addScoringResult(@RequestBody ScoringResultAddRequest scoringResultAddRequest) {
        ThrowUtils.throwIf(scoringResultAddRequest == null, ErrorCode.PARAMS_ERROR);
        long id = scoringResultService.addScoringResult(scoringResultAddRequest);
        return ResultUtils.success(id);
    }

    @ApiOperation("获取评分结果（管理员）")
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<ScoringResult> getScoringResultById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        ScoringResult scoringResult = scoringResultService.getById(id);
        ThrowUtils.throwIf(scoringResult == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(scoringResult);
    }

    @ApiOperation("分页查询评分结果列表（管理员）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Page<ScoringResult>> listScoringResults(@RequestBody PageRequest pageRequest) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<ScoringResult> page = scoringResultService.listScoringResults(current, pageSize);
        return ResultUtils.success(page);
    }

    @ApiOperation("更新评分结果（管理员）")
    @PutMapping("/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> updateScoringResult(@RequestBody ScoringResultUpdateRequest scoringResultUpdateRequest) {
        ThrowUtils.throwIf(scoringResultUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = scoringResultService.updateScoringResult(scoringResultUpdateRequest);
        return ResultUtils.success(result);
    }

    @ApiOperation("删除评分结果（管理员）")
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> deleteScoringResult(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = scoringResultService.deleteScoringResult(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    // ==================== 用户侧 ====================

    @ApiOperation("为自己的应用添加评分结果（用户）")
    @PostMapping("/user/add")
    public BaseResponse<Long> addScoringResultFromUser(@RequestBody ScoringResultAddRequest scoringResultAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(scoringResultAddRequest == null, ErrorCode.PARAMS_ERROR);
        long id = scoringResultService.addScoringResultFromUser(scoringResultAddRequest, request);
        return ResultUtils.success(id);
    }

    @ApiOperation("查看评分结果（公开）")
    @GetMapping("/user/get/{id}")
    public BaseResponse<ScoringResult> getScoringResultFromUser(@PathVariable Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        ScoringResult scoringResult = scoringResultService.getById(id);
        ThrowUtils.throwIf(scoringResult == null, ErrorCode.NOT_FOUND_ERROR);
        // 未审核应用的评分结果仅创建者可见
        App app = appService.getById(scoringResult.getAppId());
        if (app != null && !ReviewStatusEnum.APPROVED.getValue().equals(app.getReviewStatus())) {
            User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
            ThrowUtils.throwIf(loginUser == null || !app.getUserId().equals(loginUser.getId()),
                    ErrorCode.NOT_FOUND_ERROR, "评分结果不存在");
        }
        return ResultUtils.success(scoringResult);
    }

    @ApiOperation("按应用 ID 分页查询评分结果（公开）")
    @GetMapping("/user/list/{appId}")
    public BaseResponse<Page<ScoringResult>> listScoringResultsByApp(@PathVariable Long appId, PageRequest pageRequest,
                                                                      HttpServletRequest request) {
        ThrowUtils.throwIf(appId == null, ErrorCode.PARAMS_ERROR);
        // 未审核应用的评分结果仅创建者可见
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        if (!ReviewStatusEnum.APPROVED.getValue().equals(app.getReviewStatus())) {
            User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
            ThrowUtils.throwIf(loginUser == null || !app.getUserId().equals(loginUser.getId()),
                    ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<ScoringResult> page = scoringResultService.listScoringResultsByApp(appId, current, pageSize);
        return ResultUtils.success(page);
    }

    @ApiOperation("分页查询我的评分结果（用户）")
    @PostMapping("/user/my/list")
    public BaseResponse<Page<ScoringResult>> listMyScoringResults(@RequestBody PageRequest pageRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<ScoringResult> page = scoringResultService.listMyScoringResults(current, pageSize, request);
        return ResultUtils.success(page);
    }

    @ApiOperation("更新自己的评分结果（用户）")
    @PutMapping("/user/update")
    public BaseResponse<Boolean> updateScoringResultFromUser(@RequestBody ScoringResultUpdateRequest scoringResultUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(scoringResultUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = scoringResultService.updateScoringResultFromUser(scoringResultUpdateRequest, request);
        return ResultUtils.success(result);
    }

    @ApiOperation("删除自己的评分结果（用户）")
    @DeleteMapping("/user/delete")
    public BaseResponse<Boolean> deleteScoringResultFromUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = scoringResultService.deleteScoringResultFromUser(deleteRequest.getId(), request);
        return ResultUtils.success(result);
    }
}
