package com.langdada.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.langdada.backend.model.dto.UserAnswerAddRequest;
import com.langdada.backend.model.dto.UserAnswerUpdateRequest;
import com.langdada.backend.model.entity.UserAnswer;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户答题记录 服务类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
public interface IUserAnswerService extends IService<UserAnswer> {

    // ==================== 管理员 ====================

    long addUserAnswer(UserAnswerAddRequest userAnswerAddRequest);

    boolean updateUserAnswer(UserAnswerUpdateRequest userAnswerUpdateRequest);

    boolean deleteUserAnswer(Long id);

    Page<UserAnswer> listUserAnswers(long current, long pageSize);

    // ==================== 用户 ====================

    /**
     * 用户提交答题记录
     *
     * @param userAnswerAddRequest 答题请求
     * @param request              请求对象
     * @return 新答题记录 id
     */
    long addUserAnswerFromUser(UserAnswerAddRequest userAnswerAddRequest, HttpServletRequest request);

    /**
     * 用户删除自己的答题记录
     *
     * @param id      答题记录 id
     * @param request 请求对象
     * @return 是否成功
     */
    boolean deleteUserAnswerFromUser(Long id, HttpServletRequest request);

    /**
     * 分页查询当前用户自己的答题记录
     *
     * @param current  当前页
     * @param pageSize 每页大小
     * @param request  请求对象
     * @return 答题记录分页
     */
    Page<UserAnswer> listMyAnswers(long current, long pageSize, HttpServletRequest request);
}
