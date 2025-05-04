package com.hang.hoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.hang.hoj.model.dto.topicsubmit.JudgeInfo;
import com.hang.hoj.model.entity.Post;
import com.hang.hoj.model.entity.TopicSubmit;
import com.hang.hoj.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Author: HSolaire
 * Date: 2025/4/29 23:37
 */
@Data
public class TopicSubmitVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目id
     */
    private Long topicId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 判题代码
     */
    private String code;

    /**
     * 使用语言
     */
    private String language;

    /**
     * 判题状态（0 - 待答题，1 - 判题中， 2 - 成功， 3 - 失败）
     */
    private Integer status;

    /**
     * 判题信息（json 对象）
     */
    private JudgeInfo judgeInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * User
     */
    private UserVO user;

    /**
     * Topic
     */
    private TopicVO topic;

    /**
     * 包装类转对象
     *
     * @param topicSubmitVO
     * @return
     */
    public static TopicSubmit voToObj(TopicSubmitVO topicSubmitVO) {
        if (topicSubmitVO == null) {
            return null;
        }
        TopicSubmit topicSubmit = new TopicSubmit();
        BeanUtils.copyProperties(topicSubmitVO, topicSubmit);
        if(topicSubmitVO.getJudgeInfo() != null) {
            topicSubmit.setJudgeInfo(JSONUtil.toJsonStr(topicSubmitVO.getJudgeInfo()));
        }

        return topicSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param topicSubmit
     * @return
     */
    public static TopicSubmitVO objToVo(TopicSubmit topicSubmit) {
        if (topicSubmit == null) {
            return null;
        }
        TopicSubmitVO topicSubmitVO = new TopicSubmitVO();
        BeanUtils.copyProperties(topicSubmit, topicSubmitVO);
        topicSubmitVO.setJudgeInfo(JSONUtil.toBean(topicSubmit.getJudgeInfo(), JudgeInfo.class));
        return topicSubmitVO;
    }

}
