package com.langdada.backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.langdada.backend.exception.ErrorCode;
import com.langdada.backend.exception.ThrowUtils;
import com.langdada.backend.mapper.ScoringResultMapper;
import com.langdada.backend.model.dto.ScoringResultAddRequest;
import com.langdada.backend.model.dto.ScoringResultUpdateRequest;
import com.langdada.backend.model.entity.App;
import com.langdada.backend.model.entity.ScoringResult;
import com.langdada.backend.model.entity.User;
import com.langdada.backend.service.IAppService;
import com.langdada.backend.service.IScoringResultService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.langdada.backend.model.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 评分结果 服务实现类
 * </p>
 *
 * @author langxiao
 * @since 2026-07-04
 */
@Service
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult> implements IScoringResultService {

    @Resource
    private IAppService appService;

    // ==================== 管理员 ====================

    @Override
    public long addScoringResult(ScoringResultAddRequest scoringResultAddRequest) {
        ThrowUtils.throwIf(StrUtil.isBlank(scoringResultAddRequest.getResultName()), ErrorCode.PARAMS_ERROR, "结果名称不能为空");
        ThrowUtils.throwIf(scoringResultAddRequest.getAppId() == null, ErrorCode.PARAMS_ERROR, "应用 id 不能为空");
        ScoringResult scoringResult = new ScoringResult();
        scoringResult.setResultName(scoringResultAddRequest.getResultName());
        scoringResult.setResultDesc(scoringResultAddRequest.getResultDesc());
        scoringResult.setResultPicture(scoringResultAddRequest.getResultPicture());
        scoringResult.setResultProp(scoringResultAddRequest.getResultProp());
        scoringResult.setResultScoreRange(scoringResultAddRequest.getResultScoreRange());
        scoringResult.setAppId(scoringResultAddRequest.getAppId());
        scoringResult.setUserId(scoringResultAddRequest.getUserId());
        boolean saveResult = this.save(scoringResult);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "添加评分结果失败，数据库错误");
        return scoringResult.getId();
    }

    @Override
    public boolean updateScoringResult(ScoringResultUpdateRequest scoringResultUpdateRequest) {
        Long id = scoringResultUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "评分结果 id 不合法");
        ScoringResult existResult = this.getById(id);
        ThrowUtils.throwIf(existResult == null, ErrorCode.NOT_FOUND_ERROR, "评分结果不存在");
        LambdaUpdateWrapper<ScoringResult> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ScoringResult::getId, id);
        if (StrUtil.isNotBlank(scoringResultUpdateRequest.getResultName())) {
            updateWrapper.set(ScoringResult::getResultName, scoringResultUpdateRequest.getResultName());
        }
        if (StrUtil.isNotBlank(scoringResultUpdateRequest.getResultDesc())) {
            updateWrapper.set(ScoringResult::getResultDesc, scoringResultUpdateRequest.getResultDesc());
        }
        if (StrUtil.isNotBlank(scoringResultUpdateRequest.getResultPicture())) {
            updateWrapper.set(ScoringResult::getResultPicture, scoringResultUpdateRequest.getResultPicture());
        }
        if (StrUtil.isNotBlank(scoringResultUpdateRequest.getResultProp())) {
            updateWrapper.set(ScoringResult::getResultProp, scoringResultUpdateRequest.getResultProp());
        }
        if (scoringResultUpdateRequest.getResultScoreRange() != null) {
            updateWrapper.set(ScoringResult::getResultScoreRange, scoringResultUpdateRequest.getResultScoreRange());
        }
        return this.update(updateWrapper);
    }

    @Override
    public boolean deleteScoringResult(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "评分结果 id 不合法");
        ScoringResult existResult = this.getById(id);
        ThrowUtils.throwIf(existResult == null, ErrorCode.NOT_FOUND_ERROR, "评分结果不存在");
        return this.removeById(id);
    }

    @Override
    public Page<ScoringResult> listScoringResults(long current, long pageSize) {
        return this.page(new Page<>(current, pageSize));
    }

    // ==================== 用户 ====================

    @Override
    public long addScoringResultFromUser(ScoringResultAddRequest scoringResultAddRequest, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        Long appId = scoringResultAddRequest.getAppId();
        ThrowUtils.throwIf(StrUtil.isBlank(scoringResultAddRequest.getResultName()), ErrorCode.PARAMS_ERROR, "结果名称不能为空");
        ThrowUtils.throwIf(appId == null, ErrorCode.PARAMS_ERROR, "应用 id 不能为空");
        // 验证应用存在且属于当前用户
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        ThrowUtils.throwIf(!app.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "只能为自己的应用添加评分结果");
        ScoringResult scoringResult = new ScoringResult();
        scoringResult.setResultName(scoringResultAddRequest.getResultName());
        scoringResult.setResultDesc(scoringResultAddRequest.getResultDesc());
        scoringResult.setResultPicture(scoringResultAddRequest.getResultPicture());
        scoringResult.setResultProp(scoringResultAddRequest.getResultProp());
        scoringResult.setResultScoreRange(scoringResultAddRequest.getResultScoreRange());
        scoringResult.setAppId(appId);
        scoringResult.setUserId(loginUser.getId());
        boolean saveResult = this.save(scoringResult);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "添加评分结果失败，数据库错误");
        return scoringResult.getId();
    }

    @Override
    public boolean updateScoringResultFromUser(ScoringResultUpdateRequest scoringResultUpdateRequest, HttpServletRequest request) {
        Long id = scoringResultUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "评分结果 id 不合法");
        User loginUser = getLoginUser(request);
        ScoringResult existResult = this.getById(id);
        ThrowUtils.throwIf(existResult == null, ErrorCode.NOT_FOUND_ERROR, "评分结果不存在");
        ThrowUtils.throwIf(!existResult.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "无权修改该评分结果");
        LambdaUpdateWrapper<ScoringResult> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ScoringResult::getId, id);
        if (StrUtil.isNotBlank(scoringResultUpdateRequest.getResultName())) {
            updateWrapper.set(ScoringResult::getResultName, scoringResultUpdateRequest.getResultName());
        }
        if (StrUtil.isNotBlank(scoringResultUpdateRequest.getResultDesc())) {
            updateWrapper.set(ScoringResult::getResultDesc, scoringResultUpdateRequest.getResultDesc());
        }
        if (StrUtil.isNotBlank(scoringResultUpdateRequest.getResultPicture())) {
            updateWrapper.set(ScoringResult::getResultPicture, scoringResultUpdateRequest.getResultPicture());
        }
        if (StrUtil.isNotBlank(scoringResultUpdateRequest.getResultProp())) {
            updateWrapper.set(ScoringResult::getResultProp, scoringResultUpdateRequest.getResultProp());
        }
        if (scoringResultUpdateRequest.getResultScoreRange() != null) {
            updateWrapper.set(ScoringResult::getResultScoreRange, scoringResultUpdateRequest.getResultScoreRange());
        }
        return this.update(updateWrapper);
    }

    @Override
    public boolean deleteScoringResultFromUser(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "评分结果 id 不合法");
        User loginUser = getLoginUser(request);
        ScoringResult existResult = this.getById(id);
        ThrowUtils.throwIf(existResult == null, ErrorCode.NOT_FOUND_ERROR, "评分结果不存在");
        ThrowUtils.throwIf(!existResult.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "无权删除该评分结果");
        return this.removeById(id);
    }

    @Override
    public Page<ScoringResult> listScoringResultsByApp(Long appId, long current, long pageSize) {
        ThrowUtils.throwIf(appId == null, ErrorCode.PARAMS_ERROR, "应用 id 不能为空");
        LambdaQueryWrapper<ScoringResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScoringResult::getAppId, appId);
        queryWrapper.orderByAsc(ScoringResult::getResultScoreRange);
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    @Override
    public Page<ScoringResult> listMyScoringResults(long current, long pageSize, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        LambdaQueryWrapper<ScoringResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScoringResult::getUserId, loginUser.getId());
        queryWrapper.orderByDesc(ScoringResult::getCreateTime);
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    private User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        ThrowUtils.throwIf(currentUser == null || currentUser.getId() == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }
}
