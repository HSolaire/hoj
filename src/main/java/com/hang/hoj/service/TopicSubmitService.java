package com.hang.hoj.service;

import com.hang.hoj.model.dto.topicsubmit.TopicSubmitAddRequest;
import com.hang.hoj.model.entity.TopicSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.hoj.model.entity.User;

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

}
