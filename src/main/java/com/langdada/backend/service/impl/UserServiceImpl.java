package com.langdada.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.langdada.backend.exception.BusinessException;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.mapper.UserMapper;
import com.langdada.backend.model.dto.UserAddRequest;
import com.langdada.backend.model.dto.UserUpdateRequest;
import com.langdada.backend.model.dto.UserUpdateSelfRequest;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.model.enums.UserRoleEnum;
import com.langdada.backend.model.vo.LoginUserVO;
import com.langdada.backend.service.IUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.langdada.backend.model.constant.UserConstant.*;


/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword, checkPassword), ErrorCode.PARAMS_ERROR, "参数为空");
        ThrowUtils.throwIf(userAccount.length() < minAccountLen, ErrorCode.PARAMS_ERROR, "用户账号过短");
        ThrowUtils.throwIf(userPassword.length() < minPasswordLen, ErrorCode.PARAMS_ERROR, "用户密码过短");
        ThrowUtils.throwIf(!userPassword.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        // 2. 检查是否重复
        long count = this.count(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount));
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "账号重复");
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword), ErrorCode.PARAMS_ERROR, "参数为空");
        ThrowUtils.throwIf(userAccount.length() < minAccountLen, ErrorCode.PARAMS_ERROR, "账号错误");
        ThrowUtils.throwIf(userPassword.length() < minPasswordLen, ErrorCode.PARAMS_ERROR, "密码错误");
        // 2. 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        User user = this.getOne(queryWrapper);
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        // 3. 验证密码 (BCrypt)
        ThrowUtils.throwIf(!matchesPassword(userPassword, user.getUserPassword()), ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        // 4. 检查是否被封禁
        ThrowUtils.throwIf(UserRoleEnum.BAN.getValue().equals(user.getUserRole()), ErrorCode.NO_AUTH_ERROR, "用户已被禁用");
        // 5. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 6. 获得脱敏后的用户信息
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        ThrowUtils.throwIf(currentUser == null || currentUser.getId() == null, ErrorCode.NOT_LOGIN_ERROR);
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        ThrowUtils.throwIf(currentUser == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    @Override
    public String getEncryptPassword(String userPassword) {
        return PASSWORD_ENCODER.encode(userPassword);
    }

    /**
     * 验证密码是否匹配
     */
    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    // ==================== 管理员用户管理 ====================

    @Override
    public long addUser(UserAddRequest userAddRequest) {
        String userAccount = userAddRequest.getUserAccount();
        String userPassword = userAddRequest.getUserPassword();
        String userName = userAddRequest.getUserName();
        String userRole = userAddRequest.getUserRole();
        // 校验
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword), ErrorCode.PARAMS_ERROR, "账号或密码为空");
        ThrowUtils.throwIf(userAccount.length() < minAccountLen, ErrorCode.PARAMS_ERROR, "用户账号过短");
        ThrowUtils.throwIf(userPassword.length() < minPasswordLen, ErrorCode.PARAMS_ERROR, "用户密码过短");
        long count = this.count(new QueryWrapper<User>().eq("userAccount", userAccount));
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "账号重复");
        // 校验角色是否合法
        UserRoleEnum roleEnum = UserRoleEnum.getEnumByValue(userRole);
        ThrowUtils.throwIf(ObjUtil.isNull(roleEnum), ErrorCode.PARAMS_ERROR, "用户角色不合法");
        // 插入用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(getEncryptPassword(userPassword));
        user.setUserName(StrUtil.isNotBlank(userName) ? userName : "无名");
        user.setUserAvatar(userAddRequest.getUserAvatar());
        user.setUserProfile(userAddRequest.getUserProfile());
        user.setUserRole(userRole);
        boolean saveResult = this.save(user);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "添加用户失败，数据库错误");
        return user.getId();
    }

    @Override
    public boolean updateUser(UserUpdateRequest userUpdateRequest) {
        Long id = userUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "用户 id 不合法");
        // 检查用户是否存在
        User existUser = this.getById(id);
        ThrowUtils.throwIf(existUser == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        // 校验角色
        String userRole = userUpdateRequest.getUserRole();
        if (StrUtil.isNotBlank(userRole)) {
            UserRoleEnum roleEnum = UserRoleEnum.getEnumByValue(userRole);
            ThrowUtils.throwIf(ObjUtil.isNull(roleEnum), ErrorCode.PARAMS_ERROR, "用户角色不合法");
        }
        // 只更新非空字段
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, id);
        if (StrUtil.isNotBlank(userUpdateRequest.getUserName())) {
            updateWrapper.set(User::getUserName, userUpdateRequest.getUserName());
        }
        if (StrUtil.isNotBlank(userUpdateRequest.getUserAvatar())) {
            updateWrapper.set(User::getUserAvatar, userUpdateRequest.getUserAvatar());
        }
        if (StrUtil.isNotBlank(userUpdateRequest.getUserProfile())) {
            updateWrapper.set(User::getUserProfile, userUpdateRequest.getUserProfile());
        }
        if (StrUtil.isNotBlank(userRole)) {
            updateWrapper.set(User::getUserRole, userRole);
        }
        return this.update(updateWrapper);
    }

    @Override
    public boolean deleteUser(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "用户 id 不合法");
        User existUser = this.getById(id);
        ThrowUtils.throwIf(existUser == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        return this.removeById(id);
    }

    @Override
    public Page<LoginUserVO> listUsers(long current, long pageSize) {
        Page<User> page = this.page(new Page<>(current, pageSize));
        List<LoginUserVO> voList = page.getRecords().stream()
                .map(this::getLoginUserVO)
                .collect(Collectors.toList());
        Page<LoginUserVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public boolean updateSelf(UserUpdateSelfRequest userUpdateSelfRequest, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, loginUser.getId());
        if (StrUtil.isNotBlank(userUpdateSelfRequest.getUserName())) {
            updateWrapper.set(User::getUserName, userUpdateSelfRequest.getUserName());
        }
        if (StrUtil.isNotBlank(userUpdateSelfRequest.getUserAvatar())) {
            updateWrapper.set(User::getUserAvatar, userUpdateSelfRequest.getUserAvatar());
        }
        if (StrUtil.isNotBlank(userUpdateSelfRequest.getUserProfile())) {
            updateWrapper.set(User::getUserProfile, userUpdateSelfRequest.getUserProfile());
        }
        return this.update(updateWrapper);
    }

}
