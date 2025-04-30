package com.hang.hoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.hoj.common.ErrorCode;
import com.hang.hoj.exception.BusinessException;
import com.hang.hoj.model.dto.topicsubmit.TopicSubmitAddRequest;
import com.hang.hoj.model.entity.Topic;
import com.hang.hoj.model.entity.TopicSubmit;
import com.hang.hoj.model.entity.TopicSubmit;
import com.hang.hoj.model.entity.User;
import com.hang.hoj.model.enums.TopicSubmitLanguageEnum;
import com.hang.hoj.model.enums.TopicSubmitStatusEnum;
import com.hang.hoj.service.TopicService;
import com.hang.hoj.service.TopicSubmitService;
import com.hang.hoj.service.TopicSubmitService;
import com.hang.hoj.mapper.TopicSubmitMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author hsola
 * @description 针对表【topic_submit(题目提交)】的数据库操作Service实现
 * @createDate 2025-04-29 23:07:29
 */
@Service
public class TopicSubmitServiceImpl extends ServiceImpl<TopicSubmitMapper, TopicSubmit>
        implements TopicSubmitService {

    @Resource
    private TopicService topicService;

    /**
     * 题目提交
     *
     * @param topicSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doTopicSubmit(TopicSubmitAddRequest topicSubmitAddRequest, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Topic topic = topicService.getById(topicSubmitAddRequest.getTopicId());
        if (topic == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        TopicSubmitLanguageEnum languageEnum = TopicSubmitLanguageEnum.getEnumByValue(topicSubmitAddRequest.getLanguage());

        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        // 是否已题目提交
        long userId = loginUser.getId();
        // 每个用户串行题目提交 (todo 防止用户重复提交，判题中，不允许提交 重要，限流或加锁，只允许同时添加一条)
        // 锁必须要包裹住事务方法
//        TopicSubmitService topicSubmitService = (TopicSubmitService) AopContext.currentProxy();
//        synchronized (String.valueOf(userId).intern()) {
//            return topicSubmitService.doTopicSubmitInner(userId, topicId);
//        }
        TopicSubmit topicSubmit = new TopicSubmit();
        topicSubmit.setUserId(userId);
        topicSubmit.setCode(topicSubmitAddRequest.getCode());
        topicSubmit.setLanguage(topicSubmitAddRequest.getLanguage());
        topicSubmit.setStatus(TopicSubmitStatusEnum.WAITING.getCode());
        // 判题沙箱 xxx

        topicSubmit.setJudgeInfo("{}");
        boolean save = this.save(topicSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        // todo: 这里可以获取id，save完回填吗？
        return topicSubmit.getTopicId();
    }

}




