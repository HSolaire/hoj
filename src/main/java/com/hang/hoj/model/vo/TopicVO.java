package com.hang.hoj.model.vo;

import cn.hutool.json.JSONUtil;

import java.util.Date;
import java.util.List;

import com.hang.hoj.model.dto.topic.JudgeConfig;
import com.hang.hoj.model.entity.Topic;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 题目VO
 */
@Data
public class TopicVO {
    /**
     * id
     */
    private Long id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目描述
     */
    private String described;

    /**
     * 题目难度
     */
    private Integer level;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目标签
     */
    private List<String> tags;


    /**
     * 题目创建者id
     */
    private String userId;

    /**
     * 提交总次数
     */
    private Integer totalCount;

    /**
     * 通过总次数
     */
    private Integer acceptedCount;

    /**
     * 点赞数
     */
    private Integer thumbCount;

    /**
     * 收藏数
     */
    private Integer favourCount;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 用户
     */
    private UserVO userVO;

    /**
     * 包装类转对象
     *
     * @param topicVO
     * @return
     */
    public static Topic voToObj(TopicVO topicVO) {
        if (topicVO == null) {
            return null;
        }
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicVO, topic);
        List<String> tagList = topicVO.getTags();
        if (tagList != null) {
            topic.setTags(JSONUtil.toJsonStr(tagList));
        }

        if (topic.getJudgeConfig() != null) {
            topic.setJudgeConfig(JSONUtil.toJsonStr(topic.getJudgeConfig()));
        }


        return topic;
    }

    /**
     * 对象转包装类
     *
     * @param topic
     * @return
     */
    public static TopicVO objToVo(Topic topic) {
        if (topic == null) {
            return null;
        }
        TopicVO topicVO = new TopicVO();
        BeanUtils.copyProperties(topic, topicVO);
        topicVO.setTags(JSONUtil.toList(topic.getTags(), String.class));
        topicVO.setJudgeConfig(JSONUtil.toBean(topic.getJudgeConfig(), JudgeConfig.class));

        return topicVO;
    }


    private static final long serialVersionUID = 1L;

}
