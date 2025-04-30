package com.hang.hoj.controller;

import com.hang.hoj.common.BaseResponse;
import com.hang.hoj.common.ErrorCode;
import com.hang.hoj.common.ResultUtils;
import com.hang.hoj.exception.BusinessException;
import com.hang.hoj.model.dto.topicsubmit.TopicSubmitAddRequest;
import com.hang.hoj.model.entity.User;
import com.hang.hoj.service.TopicSubmitService;
import com.hang.hoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 点赞 / 取消点赞
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
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long result = topicSubmitService.doTopicSubmit(topicSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

}
