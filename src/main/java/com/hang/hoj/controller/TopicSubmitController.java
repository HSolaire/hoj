package com.hang.hoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.hoj.annotation.AuthCheck;
import com.hang.hoj.common.BaseResponse;
import com.hang.hoj.common.ErrorCode;
import com.hang.hoj.common.ResultUtils;
import com.hang.hoj.constant.UserConstant;
import com.hang.hoj.exception.BusinessException;
import com.hang.hoj.exception.ThrowUtils;
import com.hang.hoj.model.dto.post.PostQueryRequest;
import com.hang.hoj.model.dto.topicsubmit.TopicSubmitAddRequest;
import com.hang.hoj.model.dto.topicsubmit.TopicSubmitQueryRequest;
import com.hang.hoj.model.entity.Post;
import com.hang.hoj.model.entity.TopicSubmit;
import com.hang.hoj.model.entity.User;
import com.hang.hoj.model.enums.UserRoleEnum;
import com.hang.hoj.model.vo.PostVO;
import com.hang.hoj.model.vo.TopicSubmitVO;
import com.hang.hoj.service.TopicSubmitService;
import com.hang.hoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 */
@RestController
@RequestMapping("/topic_submit")
@Slf4j
public class TopicSubmitController {

    @Resource
    private TopicSubmitService topicSubmitService;

    @Resource
    private UserService userService;

    /**
     * 题目提交
     *
     * @param topicSubmitAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Long> doTopicSubmit(@RequestBody TopicSubmitAddRequest topicSubmitAddRequest,
            HttpServletRequest request) {
        if (topicSubmitAddRequest == null || topicSubmitAddRequest.getTopicId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        long result = topicSubmitService.doTopicSubmit(topicSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    // 先查询，再根据权限脱敏过滤

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.USER_LOGIN_STATE)
    public BaseResponse<TopicSubmitVO> getTopicSubmitVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TopicSubmit topicSubmit = topicSubmitService.getById(id);
        if (topicSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(topicSubmitService.getTopicSubmitVO(topicSubmit, request));
    }

    /**
     * 分页获取列表（封装类） 分页获取题目提交列表 （除了管理员，普通用户只能看到非答案、提交代码的等公开信息）
     *
     * @param topicSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<TopicSubmitVO>> listTopicSubmitVOByPage(@RequestBody TopicSubmitQueryRequest topicSubmitQueryRequest,
                                                       HttpServletRequest request) {
        long current = topicSubmitQueryRequest.getCurrent();
        long size = topicSubmitQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<TopicSubmit> topicSubmitPage = topicSubmitService.page(new Page<>(current, size),
                topicSubmitService.getQueryWrapper(topicSubmitQueryRequest));
        return ResultUtils.success(topicSubmitService.getTopicSubmitVOPage(topicSubmitPage, request));
    }

}
