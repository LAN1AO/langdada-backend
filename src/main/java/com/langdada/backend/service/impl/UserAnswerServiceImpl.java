package com.langdada.backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.mapper.UserAnswerMapper;
import com.langdada.backend.model.dto.UserAnswerAddRequest;
import com.langdada.backend.model.dto.UserAnswerUpdateRequest;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.model.entity.UserAnswer;
import com.langdada.backend.service.IUserAnswerService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.langdada.backend.model.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 用户答题记录 服务实现类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
@Service
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer> implements IUserAnswerService {

    // ==================== 管理员 ====================

    @Override
    public long addUserAnswer(UserAnswerAddRequest userAnswerAddRequest) {
        ThrowUtils.throwIf(userAnswerAddRequest.getAppId() == null, ErrorCode.PARAMS_ERROR, "应用 id 不能为空");
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(userAnswerAddRequest.getAppId());
        userAnswer.setAppType(userAnswerAddRequest.getAppType());
        userAnswer.setScoringStrategy(userAnswerAddRequest.getScoringStrategy());
        userAnswer.setChoices(userAnswerAddRequest.getChoices());
        userAnswer.setResultId(userAnswerAddRequest.getResultId());
        userAnswer.setResultName(userAnswerAddRequest.getResultName());
        userAnswer.setResultDesc(userAnswerAddRequest.getResultDesc());
        userAnswer.setResultPicture(userAnswerAddRequest.getResultPicture());
        userAnswer.setResultScore(userAnswerAddRequest.getResultScore());
        userAnswer.setUserId(userAnswerAddRequest.getUserId());
        boolean saveResult = this.save(userAnswer);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "添加答题记录失败，数据库错误");
        return userAnswer.getId();
    }

    @Override
    public boolean updateUserAnswer(UserAnswerUpdateRequest userAnswerUpdateRequest) {
        Long id = userAnswerUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "答题记录 id 不合法");
        UserAnswer existAnswer = this.getById(id);
        ThrowUtils.throwIf(existAnswer == null, ErrorCode.NOT_FOUND_ERROR, "答题记录不存在");
        LambdaUpdateWrapper<UserAnswer> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserAnswer::getId, id);
        if (StrUtil.isNotBlank(userAnswerUpdateRequest.getChoices())) {
            updateWrapper.set(UserAnswer::getChoices, userAnswerUpdateRequest.getChoices());
        }
        if (userAnswerUpdateRequest.getResultId() != null) {
            updateWrapper.set(UserAnswer::getResultId, userAnswerUpdateRequest.getResultId());
        }
        if (StrUtil.isNotBlank(userAnswerUpdateRequest.getResultName())) {
            updateWrapper.set(UserAnswer::getResultName, userAnswerUpdateRequest.getResultName());
        }
        if (StrUtil.isNotBlank(userAnswerUpdateRequest.getResultDesc())) {
            updateWrapper.set(UserAnswer::getResultDesc, userAnswerUpdateRequest.getResultDesc());
        }
        if (StrUtil.isNotBlank(userAnswerUpdateRequest.getResultPicture())) {
            updateWrapper.set(UserAnswer::getResultPicture, userAnswerUpdateRequest.getResultPicture());
        }
        if (userAnswerUpdateRequest.getResultScore() != null) {
            updateWrapper.set(UserAnswer::getResultScore, userAnswerUpdateRequest.getResultScore());
        }
        return this.update(updateWrapper);
    }

    @Override
    public boolean deleteUserAnswer(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "答题记录 id 不合法");
        UserAnswer existAnswer = this.getById(id);
        ThrowUtils.throwIf(existAnswer == null, ErrorCode.NOT_FOUND_ERROR, "答题记录不存在");
        return this.removeById(id);
    }

    @Override
    public Page<UserAnswer> listUserAnswers(long current, long pageSize) {
        return this.page(new Page<>(current, pageSize));
    }

    // ==================== 用户 ====================

    @Override
    public long addUserAnswerFromUser(UserAnswerAddRequest userAnswerAddRequest, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        ThrowUtils.throwIf(userAnswerAddRequest.getAppId() == null, ErrorCode.PARAMS_ERROR, "应用 id 不能为空");
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(userAnswerAddRequest.getAppId());
        userAnswer.setAppType(userAnswerAddRequest.getAppType());
        userAnswer.setScoringStrategy(userAnswerAddRequest.getScoringStrategy());
        userAnswer.setChoices(userAnswerAddRequest.getChoices());
        userAnswer.setResultId(userAnswerAddRequest.getResultId());
        userAnswer.setResultName(userAnswerAddRequest.getResultName());
        userAnswer.setResultDesc(userAnswerAddRequest.getResultDesc());
        userAnswer.setResultPicture(userAnswerAddRequest.getResultPicture());
        userAnswer.setResultScore(userAnswerAddRequest.getResultScore());
        userAnswer.setUserId(loginUser.getId());
        boolean saveResult = this.save(userAnswer);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "提交答题记录失败，数据库错误");
        return userAnswer.getId();
    }

    @Override
    public boolean deleteUserAnswerFromUser(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "答题记录 id 不合法");
        User loginUser = getLoginUser(request);
        UserAnswer existAnswer = this.getById(id);
        ThrowUtils.throwIf(existAnswer == null, ErrorCode.NOT_FOUND_ERROR, "答题记录不存在");
        ThrowUtils.throwIf(!existAnswer.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "无权删除该答题记录");
        return this.removeById(id);
    }

    @Override
    public Page<UserAnswer> listMyAnswers(long current, long pageSize, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        LambdaQueryWrapper<UserAnswer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAnswer::getUserId, loginUser.getId());
        queryWrapper.orderByDesc(UserAnswer::getCreateTime);
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    private User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        ThrowUtils.throwIf(currentUser == null || currentUser.getId() == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }
}
