package com.hang.hoj.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.hang.hoj.annotation.AuthCheck;
import com.hang.hoj.common.BaseResponse;
import com.hang.hoj.common.DeleteRequest;
import com.hang.hoj.common.ErrorCode;
import com.hang.hoj.common.ResultUtils;
import com.hang.hoj.constant.UserConstant;
import com.hang.hoj.exception.BusinessException;
import com.hang.hoj.exception.ThrowUtils;
import com.hang.hoj.model.dto.topic.*;
import com.hang.hoj.model.entity.Topic;
import com.hang.hoj.model.entity.User;
import com.hang.hoj.model.vo.TopicVO;
import com.hang.hoj.service.TopicService;
import com.hang.hoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 题目接口
 */
@RestController
@RequestMapping("/topic")
@Slf4j
public class TopicController {

    @Resource
    private TopicService topicService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param topicAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTopic(@RequestBody TopicAddRequest topicAddRequest, HttpServletRequest request) {
        if (topicAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicAddRequest, topic);
        List<String> tags = topicAddRequest.getTags();
        if (tags != null) {
            topic.setTags(JSONUtil.toJsonStr(tags));
        }

        JudgeConfig judgeConfig = topicAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            topic.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }

        List<JudgeCase> judgeCase = topicAddRequest.getJudgeCase();
        if (judgeCase != null) {
            topic.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
        }

        topicService.validTopic(topic, true);
        User loginUser = userService.getLoginUser(request);
        topic.setUserId(loginUser.getId());
        boolean result = topicService.save(topic);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newTopicId = topic.getId();
        return ResultUtils.success(newTopicId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTopic(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Topic oldTopic = topicService.getById(id);
        ThrowUtils.throwIf(oldTopic == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTopic.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = topicService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param topicUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateTopic(@RequestBody TopicUpdateRequest topicUpdateRequest) {
        if (topicUpdateRequest == null || topicUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicUpdateRequest, topic);
        List<String> tags = topicUpdateRequest.getTags();
        if (tags != null) {
            topic.setTags(JSONUtil.toJsonStr(tags));
        }

        JudgeConfig judgeConfig = topicUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            topic.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }

        List<JudgeCase> judgeCase = topicUpdateRequest.getJudgeCase();
        if (judgeCase != null) {
            topic.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
        }

        // 参数校验
        topicService.validTopic(topic, false);
        long id = topicUpdateRequest.getId();
        // 判断是否存在
        Topic oldTopic = topicService.getById(id);
        ThrowUtils.throwIf(oldTopic == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = topicService.updateById(topic);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取 (仅本人或管理员)
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Topic> getTopicById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topic topic = topicService.getById(id);
        if (topic == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (!Objects.equals(topic.getUserId(), loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(topic);
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<TopicVO> getTopicVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topic topic = topicService.getById(id);
        if (topic == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(topicService.getTopicVO(topic, request));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param topicQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Topic>> listTopicByPage(@RequestBody TopicQueryRequest topicQueryRequest) {
        long current = topicQueryRequest.getCurrent();
        long size = topicQueryRequest.getPageSize();
        Page<Topic> topicPage = topicService.page(new Page<>(current, size),
                topicService.getQueryWrapper(topicQueryRequest));
        return ResultUtils.success(topicPage);
    }

    /**
     * 分页获取列表（封装类 公共）
     *
     * @param topicQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<TopicVO>> listTopicVOByPage(@RequestBody TopicQueryRequest topicQueryRequest,
                                                         HttpServletRequest request) {
        long current = topicQueryRequest.getCurrent();
        long size = topicQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Topic> topicPage = topicService.page(new Page<>(current, size),
                topicService.getQueryWrapper(topicQueryRequest));
        return ResultUtils.success(topicService.getTopicVOPage(topicPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表 （本人已创建）
     *
     * @param topicQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<TopicVO>> listMyTopicVOByPage(@RequestBody TopicQueryRequest topicQueryRequest,
                                                           HttpServletRequest request) {
        if (topicQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        topicQueryRequest.setUserId(loginUser.getId());
        long current = topicQueryRequest.getCurrent();
        long size = topicQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Topic> topicPage = topicService.page(new Page<>(current, size),
                topicService.getQueryWrapper(topicQueryRequest));
        return ResultUtils.success(topicService.getTopicVOPage(topicPage, request));
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param topicEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editTopic(@RequestBody TopicEditRequest topicEditRequest, HttpServletRequest request) {
        if (topicEditRequest == null || topicEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicEditRequest, topic);
        List<String> tags = topicEditRequest.getTags();
        if (tags != null) {
            topic.setTags(JSONUtil.toJsonStr(tags));
        }
        // 参数校验
        topicService.validTopic(topic, false);
        User loginUser = userService.getLoginUser(request);
        long id = topicEditRequest.getId();
        // 判断是否存在
        Topic oldTopic = topicService.getById(id);
        ThrowUtils.throwIf(oldTopic == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldTopic.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = topicService.updateById(topic);
        return ResultUtils.success(result);
    }

    /**
     * 普通用户
     */

    /**
     * 管理员
     */

}
