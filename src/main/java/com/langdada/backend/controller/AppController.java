package com.langdada.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.langdada.backend.annotion.AuthCheck;
import com.langdada.backend.common.BaseResponse;
import com.langdada.backend.common.DeleteRequest;
import com.langdada.backend.common.PageRequest;
import com.langdada.backend.common.ResultUtils;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.model.dto.AppAddRequest;
import com.langdada.backend.model.dto.AppUpdateRequest;
import com.langdada.backend.model.dto.AppUpdateUserRequest;
import com.langdada.backend.model.entity.App;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.model.enums.ReviewStatusEnum;
import com.langdada.backend.service.IAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.langdada.backend.model.constant.UserConstant.ADMIN_ROLE;
import static com.langdada.backend.model.constant.UserConstant.USER_LOGIN_STATE;

@Api(tags = "应用管理")
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private IAppService appService;

    @ApiOperation("添加应用（管理员）")
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> addApp(@RequestBody AppAddRequest appAddRequest) {
        ThrowUtils.throwIf(appAddRequest == null, ErrorCode.PARAMS_ERROR);
        long id = appService.addApp(appAddRequest);
        return ResultUtils.success(id);
    }

    @ApiOperation("获取应用信息（管理员）")
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<App> getAppById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(app);
    }

    @ApiOperation("分页查询应用列表（管理员）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Page<App>> listApps(@RequestBody PageRequest pageRequest) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<App> page = appService.listApps(current, pageSize);
        return ResultUtils.success(page);
    }

    @ApiOperation("更新应用信息（管理员）")
    @PutMapping("/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> updateApp(@RequestBody AppUpdateRequest appUpdateRequest) {
        ThrowUtils.throwIf(appUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = appService.updateApp(appUpdateRequest);
        return ResultUtils.success(result);
    }

    @ApiOperation("删除应用（管理员）")
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> deleteApp(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = appService.deleteApp(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    // ==================== 用户侧 ====================

    @ApiOperation("创建应用（用户）")
    @PostMapping("/user/add")
    public BaseResponse<Long> addAppFromUser(@RequestBody AppAddRequest appAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appAddRequest == null, ErrorCode.PARAMS_ERROR);
        long id = appService.addAppFromUser(appAddRequest, request);
        return ResultUtils.success(id);
    }

    @ApiOperation("获取应用详情（公开）")
    @GetMapping("/user/get/{id}")
    public BaseResponse<App> getAppFromUser(@PathVariable Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 未审核的应用仅创建者可见
        if (!ReviewStatusEnum.APPROVED.getValue().equals(app.getReviewStatus())) {
            User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
            ThrowUtils.throwIf(loginUser == null || !app.getUserId().equals(loginUser.getId()),
                    ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }
        return ResultUtils.success(app);
    }

    @ApiOperation("分页查询已审核应用（公开）")
    @PostMapping("/user/list/page")
    public BaseResponse<Page<App>> listAppsPublic(@RequestBody PageRequest pageRequest) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<App> page = appService.listAppsPublic(current, pageSize);
        return ResultUtils.success(page);
    }

    @ApiOperation("分页查询我的应用（用户）")
    @PostMapping("/user/my/list")
    public BaseResponse<Page<App>> listMyApps(@RequestBody PageRequest pageRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<App> page = appService.listMyApps(current, pageSize, request);
        return ResultUtils.success(page);
    }

    @ApiOperation("更新应用（用户）")
    @PutMapping("/user/update")
    public BaseResponse<Boolean> updateAppFromUser(@RequestBody AppUpdateUserRequest appUpdateUserRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appUpdateUserRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = appService.updateAppFromUser(appUpdateUserRequest, request);
        return ResultUtils.success(result);
    }

    @ApiOperation("删除应用（用户）")
    @DeleteMapping("/user/delete")
    public BaseResponse<Boolean> deleteAppFromUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = appService.deleteAppFromUser(deleteRequest.getId(), request);
        return ResultUtils.success(result);
    }
}
