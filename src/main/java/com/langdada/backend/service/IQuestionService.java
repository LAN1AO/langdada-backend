package com.langdada.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.langdada.backend.model.dto.QuestionAddRequest;
import com.langdada.backend.model.dto.QuestionUpdateRequest;
import com.langdada.backend.model.entity.Question;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 题目 服务类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
public interface IQuestionService extends IService<Question> {

    // ==================== 管理员 ====================

    long addQuestion(QuestionAddRequest questionAddRequest);

    boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest);

    boolean deleteQuestion(Long id);

    Page<Question> listQuestions(long current, long pageSize);

    // ==================== 用户 ====================

    /**
     * 用户为自己应用添加题目
     *
     * @param questionAddRequest 添加题目请求
     * @param request            请求对象
     * @return 新题目 id
     */
    long addQuestionFromUser(QuestionAddRequest questionAddRequest, HttpServletRequest request);

    /**
     * 用户更新自己的题目
     *
     * @param questionUpdateRequest 更新题目请求
     * @param request               请求对象
     * @return 是否成功
     */
    boolean updateQuestionFromUser(QuestionUpdateRequest questionUpdateRequest, HttpServletRequest request);

    /**
     * 用户删除自己的题目
     *
     * @param id      题目 id
     * @param request 请求对象
     * @return 是否成功
     */
    boolean deleteQuestionFromUser(Long id, HttpServletRequest request);

    /**
     * 按应用 ID 分页查询题目
     *
     * @param appId    应用 id
     * @param current  当前页
     * @param pageSize 每页大小
     * @return 题目分页
     */
    Page<Question> listQuestionsByApp(Long appId, long current, long pageSize);

    /**
     * 分页查询当前用户自己的题目
     *
     * @param current  当前页
     * @param pageSize 每页大小
     * @param request  请求对象
     * @return 题目分页
     */
    Page<Question> listMyQuestions(long current, long pageSize, HttpServletRequest request);
}
