package com.langdada.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.langdada.backend.model.dto.AppAddRequest;
import com.langdada.backend.model.dto.AppUpdateRequest;
import com.langdada.backend.model.dto.AppUpdateUserRequest;
import com.langdada.backend.model.entity.App;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 应用 服务类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
public interface IAppService extends IService<App> {

    // ==================== 管理员 ====================

    long addApp(AppAddRequest appAddRequest);

    boolean updateApp(AppUpdateRequest appUpdateRequest);

    boolean deleteApp(Long id);

    Page<App> listApps(long current, long pageSize);

    // ==================== 用户 ====================

    /**
     * 用户创建应用
     *
     * @param appAddRequest 添加应用请求
     * @param request       请求对象
     * @return 新应用 id
     */
    long addAppFromUser(AppAddRequest appAddRequest, HttpServletRequest request);

    /**
     * 用户更新自己的应用（不可修改审核字段）
     *
     * @param appUpdateUserRequest 更新请求
     * @param request              请求对象
     * @return 是否成功
     */
    boolean updateAppFromUser(AppUpdateUserRequest appUpdateUserRequest, HttpServletRequest request);

    /**
     * 用户删除自己的应用
     *
     * @param id      应用 id
     * @param request 请求对象
     * @return 是否成功
     */
    boolean deleteAppFromUser(Long id, HttpServletRequest request);

    /**
     * 分页查询已通过审核的应用（公开）
     *
     * @param current  当前页
     * @param pageSize 每页大小
     * @return 应用分页
     */
    Page<App> listAppsPublic(long current, long pageSize);

    /**
     * 分页查询当前用户自己的应用
     *
     * @param current  当前页
     * @param pageSize 每页大小
     * @param request  请求对象
     * @return 应用分页
     */
    Page<App> listMyApps(long current, long pageSize, HttpServletRequest request);
}
