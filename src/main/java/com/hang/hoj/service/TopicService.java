package com.hang.hoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.hoj.model.dto.topic.TopicQueryRequest;
import com.hang.hoj.model.entity.Topic;
import com.hang.hoj.model.entity.Topic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.hoj.model.vo.TopicVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author hsola
* @description 针对表【topic(题目)】的数据库操作Service
* @createDate 2025-04-29 23:06:33
*/
public interface TopicService extends IService<Topic> {

    /**
     * 校验
     *
     * @param topic
     * @param add
     */
    void validTopic(Topic topic, boolean add);

    /**
     * 获取查询条件
     *
     * @param topicQueryRequest
     * @return
     */
    QueryWrapper<Topic> getQueryWrapper(TopicQueryRequest topicQueryRequest);

    /**
     * 获取题目封装
     *
     * @param topic
     * @param request
     * @return
     */
    TopicVO getTopicVO(Topic topic, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param topicPage
     * @param request
     * @return
     */
    Page<TopicVO> getTopicVOPage(Page<Topic> topicPage, HttpServletRequest request);


}
