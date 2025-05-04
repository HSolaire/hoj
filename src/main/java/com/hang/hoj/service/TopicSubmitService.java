package com.hang.hoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.hoj.model.dto.topic.TopicQueryRequest;
import com.hang.hoj.model.dto.topicsubmit.TopicSubmitAddRequest;
import com.hang.hoj.model.dto.topicsubmit.TopicSubmitQueryRequest;
import com.hang.hoj.model.entity.Topic;
import com.hang.hoj.model.entity.TopicSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.hoj.model.entity.User;
import com.hang.hoj.model.vo.TopicSubmitVO;
import com.hang.hoj.model.vo.TopicVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author hsola
* @description 针对表【topic_submit(题目提交)】的数据库操作Service
* @createDate 2025-04-29 23:07:29
*/
public interface TopicSubmitService extends IService<TopicSubmit> {

    /**
     * 点赞
     *
     * @param topicSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doTopicSubmit(TopicSubmitAddRequest topicSubmitAddRequest, User loginUser);


    /**
     * 获取查询条件
     *
     * @param topicQueryRequest
     * @return
     */
    QueryWrapper<TopicSubmit> getQueryWrapper(TopicSubmitQueryRequest topicQueryRequest);

    /**
     * 获取题目（当前用户）
     *
     * @param topicSubmit
     * @param request
     * @return
     */
    TopicSubmitVO getTopicSubmitVO(TopicSubmit topicSubmit, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param topicSubmitPage
     * @param request
     * @return
     */
    Page<TopicSubmitVO> getTopicSubmitVOPage(Page<TopicSubmit> topicSubmitPage, HttpServletRequest request);

}
