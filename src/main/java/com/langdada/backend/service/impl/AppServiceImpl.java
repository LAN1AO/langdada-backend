package com.langdada.backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.mapper.AppMapper;
import com.langdada.backend.model.dto.AppAddRequest;
import com.langdada.backend.model.dto.AppUpdateRequest;
import com.langdada.backend.model.dto.AppUpdateUserRequest;
import com.langdada.backend.model.entity.App;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.model.enums.ReviewStatusEnum;
import com.langdada.backend.service.IAppService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.langdada.backend.model.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 应用 服务实现类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

    // ==================== 管理员 ====================

    @Override
    public long addApp(AppAddRequest appAddRequest) {
        ThrowUtils.throwIf(StrUtil.isBlank(appAddRequest.getAppName()), ErrorCode.PARAMS_ERROR, "应用名不能为空");
        App app = new App();
        app.setAppName(appAddRequest.getAppName());
        app.setAppDesc(appAddRequest.getAppDesc());
        app.setAppIcon(appAddRequest.getAppIcon());
        app.setAppType(appAddRequest.getAppType());
        app.setScoringStrategy(appAddRequest.getScoringStrategy());
        app.setUserId(appAddRequest.getUserId());
        boolean saveResult = this.save(app);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "添加应用失败，数据库错误");
        return app.getId();
    }

    @Override
    public boolean updateApp(AppUpdateRequest appUpdateRequest) {
        Long id = appUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "应用 id 不合法");
        App existApp = this.getById(id);
        ThrowUtils.throwIf(existApp == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        LambdaUpdateWrapper<App> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(App::getId, id);
        if (StrUtil.isNotBlank(appUpdateRequest.getAppName())) {
            updateWrapper.set(App::getAppName, appUpdateRequest.getAppName());
        }
        if (StrUtil.isNotBlank(appUpdateRequest.getAppDesc())) {
            updateWrapper.set(App::getAppDesc, appUpdateRequest.getAppDesc());
        }
        if (StrUtil.isNotBlank(appUpdateRequest.getAppIcon())) {
            updateWrapper.set(App::getAppIcon, appUpdateRequest.getAppIcon());
        }
        if (appUpdateRequest.getAppType() != null) {
            updateWrapper.set(App::getAppType, appUpdateRequest.getAppType());
        }
        if (appUpdateRequest.getScoringStrategy() != null) {
            updateWrapper.set(App::getScoringStrategy, appUpdateRequest.getScoringStrategy());
        }
        if (appUpdateRequest.getReviewStatus() != null) {
            updateWrapper.set(App::getReviewStatus, appUpdateRequest.getReviewStatus());
        }
        if (StrUtil.isNotBlank(appUpdateRequest.getReviewMessage())) {
            updateWrapper.set(App::getReviewMessage, appUpdateRequest.getReviewMessage());
        }
        if (appUpdateRequest.getReviewerId() != null) {
            updateWrapper.set(App::getReviewerId, appUpdateRequest.getReviewerId());
        }
        return this.update(updateWrapper);
    }

    @Override
    public boolean deleteApp(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "应用 id 不合法");
        App existApp = this.getById(id);
        ThrowUtils.throwIf(existApp == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        return this.removeById(id);
    }

    @Override
    public Page<App> listApps(long current, long pageSize) {
        return this.page(new Page<>(current, pageSize));
    }

    // ==================== 用户 ====================

    @Override
    public long addAppFromUser(AppAddRequest appAddRequest, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        ThrowUtils.throwIf(StrUtil.isBlank(appAddRequest.getAppName()), ErrorCode.PARAMS_ERROR, "应用名不能为空");
        App app = new App();
        app.setAppName(appAddRequest.getAppName());
        app.setAppDesc(appAddRequest.getAppDesc());
        app.setAppIcon(appAddRequest.getAppIcon());
        app.setAppType(appAddRequest.getAppType());
        app.setScoringStrategy(appAddRequest.getScoringStrategy());
        app.setUserId(loginUser.getId());
        boolean saveResult = this.save(app);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "添加应用失败，数据库错误");
        return app.getId();
    }

    @Override
    public boolean updateAppFromUser(AppUpdateUserRequest appUpdateUserRequest, HttpServletRequest request) {
        Long id = appUpdateUserRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "应用 id 不合法");
        User loginUser = getLoginUser(request);
        App existApp = this.getById(id);
        ThrowUtils.throwIf(existApp == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 只能修改自己的应用
        ThrowUtils.throwIf(!existApp.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "无权修改该应用");
        LambdaUpdateWrapper<App> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(App::getId, id);
        if (StrUtil.isNotBlank(appUpdateUserRequest.getAppName())) {
            updateWrapper.set(App::getAppName, appUpdateUserRequest.getAppName());
        }
        if (StrUtil.isNotBlank(appUpdateUserRequest.getAppDesc())) {
            updateWrapper.set(App::getAppDesc, appUpdateUserRequest.getAppDesc());
        }
        if (StrUtil.isNotBlank(appUpdateUserRequest.getAppIcon())) {
            updateWrapper.set(App::getAppIcon, appUpdateUserRequest.getAppIcon());
        }
        if (appUpdateUserRequest.getAppType() != null) {
            updateWrapper.set(App::getAppType, appUpdateUserRequest.getAppType());
        }
        if (appUpdateUserRequest.getScoringStrategy() != null) {
            updateWrapper.set(App::getScoringStrategy, appUpdateUserRequest.getScoringStrategy());
        }
        return this.update(updateWrapper);
    }

    @Override
    public boolean deleteAppFromUser(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "应用 id 不合法");
        User loginUser = getLoginUser(request);
        App existApp = this.getById(id);
        ThrowUtils.throwIf(existApp == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 只能删除自己的应用
        ThrowUtils.throwIf(!existApp.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "无权删除该应用");
        return this.removeById(id);
    }

    @Override
    public Page<App> listAppsPublic(long current, long pageSize) {
        LambdaQueryWrapper<App> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(App::getReviewStatus, ReviewStatusEnum.APPROVED.getValue());
        queryWrapper.orderByDesc(App::getCreateTime);
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    @Override
    public Page<App> listMyApps(long current, long pageSize, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        LambdaQueryWrapper<App> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(App::getUserId, loginUser.getId());
        queryWrapper.orderByDesc(App::getCreateTime);
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    /**
     * 从 session 获取当前登录用户
     */
    private User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        ThrowUtils.throwIf(currentUser == null || currentUser.getId() == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }
}
