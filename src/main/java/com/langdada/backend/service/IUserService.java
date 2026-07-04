package com.langdada.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.langdada.backend.model.dto.UserAddRequest;
import com.langdada.backend.model.dto.UserUpdateRequest;
import com.langdada.backend.model.dto.UserUpdateSelfRequest;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
public interface IUserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return 脱敏后的用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    // ==================== 管理员用户管理 ====================

    /**
     * 管理员添加用户
     *
     * @param userAddRequest 添加用户请求
     * @return 新用户 id
     */
    long addUser(UserAddRequest userAddRequest);

    /**
     * 管理员修改用户信息
     *
     * @param userUpdateRequest 更新用户请求
     * @return 是否成功
     */
    boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 管理员删除用户
     *
     * @param id 用户 id
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    /**
     * 管理员分页查询用户（脱敏）
     *
     * @param current  当前页
     * @param pageSize 每页大小
     * @return 脱敏用户分页
     */
    Page<LoginUserVO> listUsers(long current, long pageSize);

    String getEncryptPassword(String userPassword);

    /**
     * 用户更新自身信息
     *
     * @param userUpdateSelfRequest 更新请求
     * @param request              请求对象
     * @return 是否成功
     */
    boolean updateSelf(UserUpdateSelfRequest userUpdateSelfRequest, HttpServletRequest request);
}
