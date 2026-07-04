package com.langdada.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.langdada.backend.model.dto.ScoringResultAddRequest;
import com.langdada.backend.model.dto.ScoringResultUpdateRequest;
import com.langdada.backend.model.entity.ScoringResult;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 评分结果 服务类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
public interface IScoringResultService extends IService<ScoringResult> {

    // ==================== 管理员 ====================

    long addScoringResult(ScoringResultAddRequest scoringResultAddRequest);

    boolean updateScoringResult(ScoringResultUpdateRequest scoringResultUpdateRequest);

    boolean deleteScoringResult(Long id);

    Page<ScoringResult> listScoringResults(long current, long pageSize);

    // ==================== 用户 ====================

    /**
     * 用户为自己应用添加评分结果
     *
     * @param scoringResultAddRequest 添加请求
     * @param request                 请求对象
     * @return 新评分结果 id
     */
    long addScoringResultFromUser(ScoringResultAddRequest scoringResultAddRequest, HttpServletRequest request);

    /**
     * 用户更新自己的评分结果
     *
     * @param scoringResultUpdateRequest 更新请求
     * @param request                    请求对象
     * @return 是否成功
     */
    boolean updateScoringResultFromUser(ScoringResultUpdateRequest scoringResultUpdateRequest, HttpServletRequest request);

    /**
     * 用户删除自己的评分结果
     *
     * @param id      评分结果 id
     * @param request 请求对象
     * @return 是否成功
     */
    boolean deleteScoringResultFromUser(Long id, HttpServletRequest request);

    /**
     * 按应用 ID 分页查询评分结果
     *
     * @param appId    应用 id
     * @param current  当前页
     * @param pageSize 每页大小
     * @return 评分结果分页
     */
    Page<ScoringResult> listScoringResultsByApp(Long appId, long current, long pageSize);

    /**
     * 分页查询当前用户自己的评分结果
     *
     * @param current  当前页
     * @param pageSize 每页大小
     * @param request  请求对象
     * @return 评分结果分页
     */
    Page<ScoringResult> listMyScoringResults(long current, long pageSize, HttpServletRequest request);
}
