package com.hang.hoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.hoj.common.ErrorCode;
import com.hang.hoj.constant.CommonConstant;
import com.hang.hoj.constant.UserConstant;
import com.hang.hoj.exception.BusinessException;
import com.hang.hoj.model.dto.topicsubmit.TopicSubmitAddRequest;
import com.hang.hoj.model.dto.topicsubmit.TopicSubmitQueryRequest;
import com.hang.hoj.model.entity.*;
import com.hang.hoj.model.entity.TopicSubmit;
import com.hang.hoj.model.enums.TopicSubmitLanguageEnum;
import com.hang.hoj.model.enums.TopicSubmitStatusEnum;
import com.hang.hoj.model.enums.UserRoleEnum;
import com.hang.hoj.model.vo.TopicSubmitVO;
import com.hang.hoj.model.vo.TopicVO;
import com.hang.hoj.model.vo.UserVO;
import com.hang.hoj.service.TopicService;
import com.hang.hoj.service.TopicSubmitService;
import com.hang.hoj.mapper.TopicSubmitMapper;
import com.hang.hoj.service.UserService;
import com.hang.hoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Resource
    private UserService userService;

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
        topicSubmit.setTopicId(topic.getId());
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

    // 重要：通用查询条件，不带上
    @Override
    public QueryWrapper<TopicSubmit> getQueryWrapper(TopicSubmitQueryRequest topicSubmitQueryRequest) {
        QueryWrapper<TopicSubmit> queryWrapper = new QueryWrapper<>();
        if (topicSubmitQueryRequest == null) {
            return queryWrapper;
        }

        Long topicId = topicSubmitQueryRequest.getTopicId();
        Integer status = topicSubmitQueryRequest.getStatus();
        String language = topicSubmitQueryRequest.getLanguage();
        Long userId = topicSubmitQueryRequest.getUserId();
        String sortField = topicSubmitQueryRequest.getSortField();
        String sortOrder = topicSubmitQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(topicId), "topicId", topicId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    // 重要，传topicSubmit而不是topicSubmitRequest是MybatisPlus直接有getById的方法
    @Override
    public TopicSubmitVO getTopicSubmitVO(TopicSubmit topicSubmit, HttpServletRequest request) {
        TopicSubmitVO topicSubmitVO = TopicSubmitVO.objToVo(topicSubmit);
        User loginUser = userService.getLoginUser(request);
        Long userId = topicSubmit.getUserId();

        // 脱敏：只有管理员或用户本人才能看到写的代码
        if (loginUser.getId() != userId && userService.isAdmin(request)) {
            topicSubmitVO.setCode(null);
        }

        // 1. 关联查询用户信息
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }

        UserVO userVO = userService.getUserVO(user);
        topicSubmitVO.setUser(userVO);

        // 2. 关联题目信息
        Long topicId = topicSubmit.getTopicId();
        Topic topic = null;
        if (topicId != null && topicId > 0) {
            topic = topicService.getById(topicId);
        }
        TopicVO topicVO = TopicVO.objToVo(topic);
        topicSubmitVO.setTopic(topicVO);
        return topicSubmitVO;
    }

    @Override
    public Page<TopicSubmitVO> getTopicSubmitVOPage(Page<TopicSubmit> topicSubmitPage, HttpServletRequest request) {
        List<TopicSubmit> topicSubmitList = topicSubmitPage.getRecords();
        Page<TopicSubmitVO> topicSubmitVOPage = new Page<>(topicSubmitPage.getCurrent(), topicSubmitPage.getSize(), topicSubmitPage.getTotal());
        if (CollUtil.isEmpty(topicSubmitList)) {
            return topicSubmitVOPage;
        }
        // 1. 关联查询用户信息 (可能会有多个用户吗 ？？？)
        Set<Long> userIdSet = topicSubmitList.stream().map(TopicSubmit::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<TopicSubmitVO> topicSubmitVOList = topicSubmitList.stream().map(topicSubmit -> {
            TopicSubmitVO topicSubmitVO = TopicSubmitVO.objToVo(topicSubmit);
            Long userId = topicSubmit.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            topicSubmitVO.setUser(userService.getUserVO(user));
            topicSubmitVO.setCode(null);
            return topicSubmitVO;
        }).collect(Collectors.toList());
        topicSubmitVOPage.setRecords(topicSubmitVOList);
        return topicSubmitVOPage;
    }
}




