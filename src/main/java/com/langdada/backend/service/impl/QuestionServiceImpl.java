package com.langdada.backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.mapper.QuestionMapper;
import com.langdada.backend.model.dto.QuestionAddRequest;
import com.langdada.backend.model.dto.QuestionUpdateRequest;
import com.langdada.backend.model.entity.App;
import com.langdada.backend.model.entity.Question;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.service.IAppService;
import com.langdada.backend.service.IQuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.langdada.backend.model.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 题目 服务实现类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Resource
    private IAppService appService;

    // ==================== 管理员 ====================

    @Override
    public long addQuestion(QuestionAddRequest questionAddRequest) {
        ThrowUtils.throwIf(StrUtil.isBlank(questionAddRequest.getQuestionContent()), ErrorCode.PARAMS_ERROR, "题目内容不能为空");
        ThrowUtils.throwIf(questionAddRequest.getAppId() == null, ErrorCode.PARAMS_ERROR, "应用 id 不能为空");
        Question question = new Question();
        question.setQuestionContent(questionAddRequest.getQuestionContent());
        question.setAppId(questionAddRequest.getAppId());
        question.setUserId(questionAddRequest.getUserId());
        boolean saveResult = this.save(question);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "添加题目失败，数据库错误");
        return question.getId();
    }

    @Override
    public boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest) {
        Long id = questionUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "题目 id 不合法");
        Question existQuestion = this.getById(id);
        ThrowUtils.throwIf(existQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        ThrowUtils.throwIf(StrUtil.isBlank(questionUpdateRequest.getQuestionContent()), ErrorCode.PARAMS_ERROR, "题目内容不能为空");
        existQuestion.setQuestionContent(questionUpdateRequest.getQuestionContent());
        return this.updateById(existQuestion);
    }

    @Override
    public boolean deleteQuestion(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "题目 id 不合法");
        Question existQuestion = this.getById(id);
        ThrowUtils.throwIf(existQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        return this.removeById(id);
    }

    @Override
    public Page<Question> listQuestions(long current, long pageSize) {
        return this.page(new Page<>(current, pageSize));
    }

    // ==================== 用户 ====================

    @Override
    public long addQuestionFromUser(QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        Long appId = questionAddRequest.getAppId();
        ThrowUtils.throwIf(StrUtil.isBlank(questionAddRequest.getQuestionContent()), ErrorCode.PARAMS_ERROR, "题目内容不能为空");
        ThrowUtils.throwIf(appId == null, ErrorCode.PARAMS_ERROR, "应用 id 不能为空");
        // 验证应用存在且属于当前用户
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        ThrowUtils.throwIf(!app.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "只能为自己的应用添加题目");
        Question question = new Question();
        question.setQuestionContent(questionAddRequest.getQuestionContent());
        question.setAppId(appId);
        question.setUserId(loginUser.getId());
        boolean saveResult = this.save(question);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "添加题目失败，数据库错误");
        return question.getId();
    }

    @Override
    public boolean updateQuestionFromUser(QuestionUpdateRequest questionUpdateRequest, HttpServletRequest request) {
        Long id = questionUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "题目 id 不合法");
        User loginUser = getLoginUser(request);
        Question existQuestion = this.getById(id);
        ThrowUtils.throwIf(existQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        ThrowUtils.throwIf(!existQuestion.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "无权修改该题目");
        ThrowUtils.throwIf(StrUtil.isBlank(questionUpdateRequest.getQuestionContent()), ErrorCode.PARAMS_ERROR, "题目内容不能为空");
        existQuestion.setQuestionContent(questionUpdateRequest.getQuestionContent());
        return this.updateById(existQuestion);
    }

    @Override
    public boolean deleteQuestionFromUser(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "题目 id 不合法");
        User loginUser = getLoginUser(request);
        Question existQuestion = this.getById(id);
        ThrowUtils.throwIf(existQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        ThrowUtils.throwIf(!existQuestion.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "无权删除该题目");
        return this.removeById(id);
    }

    @Override
    public Page<Question> listQuestionsByApp(Long appId, long current, long pageSize) {
        ThrowUtils.throwIf(appId == null, ErrorCode.PARAMS_ERROR, "应用 id 不能为空");
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getAppId, appId);
        queryWrapper.orderByAsc(Question::getCreateTime);
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    @Override
    public Page<Question> listMyQuestions(long current, long pageSize, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getUserId, loginUser.getId());
        queryWrapper.orderByDesc(Question::getCreateTime);
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    private User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        ThrowUtils.throwIf(currentUser == null || currentUser.getId() == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }
}
