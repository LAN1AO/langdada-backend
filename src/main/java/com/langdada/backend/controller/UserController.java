package com.langdada.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.langdada.backend.annotion.AuthCheck;
import com.langdada.backend.common.BaseResponse;
import com.langdada.backend.common.DeleteRequest;
import com.langdada.backend.common.PageRequest;
import com.langdada.backend.common.ResultUtils;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.model.dto.*;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.model.vo.LoginUserVO;
import com.langdada.backend.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.langdada.backend.model.constant.UserConstant.ADMIN_ROLE;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @ApiOperation("用户注册（公开）")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @ApiOperation("用户登录（公开）")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    @ApiOperation("获取当前登录用户（用户）")
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    @ApiOperation("用户注销（用户）")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    // ==================== 管理员用户管理 ====================

    @ApiOperation("添加用户（管理员）")
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        long id = userService.addUser(userAddRequest);
        return ResultUtils.success(id);
    }

    @ApiOperation("获取用户信息（管理员）")
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<LoginUserVO> getUserById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    @ApiOperation("分页查询用户列表（管理员）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Page<LoginUserVO>> listUsers(@RequestBody PageRequest pageRequest) {
        ThrowUtils.throwIf(pageRequest == null, ErrorCode.PARAMS_ERROR);
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<LoginUserVO> voPage = userService.listUsers(current, pageSize);
        return ResultUtils.success(voPage);
    }

    @ApiOperation("更新用户信息（管理员）")
    @PutMapping("/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.updateUser(userUpdateRequest);
        return ResultUtils.success(result);
    }

    @ApiOperation("删除用户（管理员）")
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.deleteUser(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    // ==================== 用户自管理 ====================

    @ApiOperation("更新自身信息（用户）")
    @PutMapping("/update/self")
    public BaseResponse<Boolean> updateSelf(@RequestBody UserUpdateSelfRequest userUpdateSelfRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userUpdateSelfRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.updateSelf(userUpdateSelfRequest, request);
        return ResultUtils.success(result);
    }

}
