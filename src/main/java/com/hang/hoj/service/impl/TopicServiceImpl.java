package com.hang.hoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.hoj.common.ErrorCode;
import com.hang.hoj.constant.CommonConstant;
import com.hang.hoj.exception.BusinessException;
import com.hang.hoj.exception.ThrowUtils;
import com.hang.hoj.model.dto.topic.TopicQueryRequest;
import com.hang.hoj.model.entity.*;
import com.hang.hoj.model.vo.TopicVO;
import com.hang.hoj.model.vo.UserVO;
import com.hang.hoj.service.TopicService;
import com.hang.hoj.mapper.TopicMapper;
import com.hang.hoj.service.UserService;
import com.hang.hoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hsola
 * @description 针对表【topic(题目)】的数据库操作Service实现
 * @createDate 2025-04-29 23:06:33
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
        implements TopicService {

    private UserService userService;


    /**
     * TODO 重新写
     *
     * @param topic
     * @param add
     */
    @Override
    public void validTopic(Topic topic, boolean add) {
        String title = topic.getTitle();
        String content = topic.getContent();
        String tags = topic.getTags();
        String answer = topic.getAnswer();
        String judgeConfig = topic.getJudgeConfig();
        String judgeCase = topic.getJudgeCase();

/*        if (topic == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }*/
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }

        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }

        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }

        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }

    }

    /**
     * 获取查询包装类
     *
     * @param topicQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Topic> getQueryWrapper(TopicQueryRequest topicQueryRequest) {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        if (topicQueryRequest == null) {
            return queryWrapper;
        }

        Long id = topicQueryRequest.getId();
        String title = topicQueryRequest.getTitle();
        String content = topicQueryRequest.getContent();
        List<String> tags = topicQueryRequest.getTags();
        String answer = topicQueryRequest.getAnswer();
        Long userId = topicQueryRequest.getUserId();
        String sortField = topicQueryRequest.getSortField();
        String sortOrder = topicQueryRequest.getSortOrder();

        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);

        // 拼接查询条件
//        if (StringUtils.isNotBlank(searchText)) {
//            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
//        }

        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public TopicVO getTopicVO(Topic topic, HttpServletRequest request) {
        TopicVO topicVO = TopicVO.objToVo(topic);
        long topicId = topic.getId();
        // 1. 关联查询用户信息
        Long userId = topic.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        topicVO.setUserVO(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
//        User loginUser = userService.getLoginUserPermitNull(request);
//        if (loginUser != null) {
//            // 获取点赞
//            QueryWrapper<TopicThumb> topicThumbQueryWrapper = new QueryWrapper<>();
//            topicThumbQueryWrapper.in("topicId", topicId);
//            topicThumbQueryWrapper.eq("userId", loginUser.getId());
//            TopicThumb topicThumb = topicThumbMapper.selectOne(topicThumbQueryWrapper);
//            topicVO.setHasThumb(topicThumb != null);
//            // 获取收藏
//            QueryWrapper<TopicFavour> topicFavourQueryWrapper = new QueryWrapper<>();
//            topicFavourQueryWrapper.in("topicId", topicId);
//            topicFavourQueryWrapper.eq("userId", loginUser.getId());
//            TopicFavour topicFavour = topicFavourMapper.selectOne(topicFavourQueryWrapper);
//            topicVO.setHasFavour(topicFavour != null);
//        }
        return topicVO;
    }

    @Override
    public Page<TopicVO> getTopicVOPage(Page<Topic> topicPage, HttpServletRequest request) {
        List<Topic> topicList = topicPage.getRecords();
        Page<TopicVO> topicVOPage = new Page<>(topicPage.getCurrent(), topicPage.getSize(), topicPage.getTotal());
        if (CollUtil.isEmpty(topicList)) {
            return topicVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = topicList.stream().map(Topic::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
//        Map<Long, Boolean> topicIdHasThumbMap = new HashMap<>();
//        Map<Long, Boolean> topicIdHasFavourMap = new HashMap<>();
//        User loginUser = userService.getLoginUserPermitNull(request);
//        if (loginUser != null) {
//            Set<Long> topicIdSet = topicList.stream().map(Topic::getId).collect(Collectors.toSet());
//            loginUser = userService.getLoginUser(request);
//            // 获取点赞
//            QueryWrapper<TopicThumb> topicThumbQueryWrapper = new QueryWrapper<>();
//            topicThumbQueryWrapper.in("topicId", topicIdSet);
//            topicThumbQueryWrapper.eq("userId", loginUser.getId());
//            List<TopicThumb> topicTopicThumbList = topicThumbMapper.selectList(topicThumbQueryWrapper);
//            topicTopicThumbList.forEach(topicTopicThumb -> topicIdHasThumbMap.put(topicTopicThumb.getTopicId(), true));
//            // 获取收藏
//            QueryWrapper<TopicFavour> topicFavourQueryWrapper = new QueryWrapper<>();
//            topicFavourQueryWrapper.in("topicId", topicIdSet);
//            topicFavourQueryWrapper.eq("userId", loginUser.getId());
//            List<TopicFavour> topicFavourList = topicFavourMapper.selectList(topicFavourQueryWrapper);
//            topicFavourList.forEach(topicFavour -> topicIdHasFavourMap.put(topicFavour.getTopicId(), true));
//        }
        // 填充信息
        List<TopicVO> topicVOList = topicList.stream().map(topic -> {
            TopicVO topicVO = TopicVO.objToVo(topic);
            Long userId = topic.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            topicVO.setUserVO(userService.getUserVO(user));
//            topicVO.setHasThumb(topicIdHasThumbMap.getOrDefault(topic.getId(), false));
//            topicVO.setHasFavour(topicIdHasFavourMap.getOrDefault(topic.getId(), false));
            return topicVO;
        }).collect(Collectors.toList());
        topicVOPage.setRecords(topicVOList);
        return topicVOPage;
    }
}




