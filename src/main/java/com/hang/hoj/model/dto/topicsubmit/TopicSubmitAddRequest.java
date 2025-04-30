package com.hang.hoj.model.dto.topicsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: HSolaire
 * Date: 2025/4/30 10:59
 */
@Data
public class TopicSubmitAddRequest implements Serializable {

    /**
     * 题目id
     */
    private Long topicId;

    /**
     * 判题代码
     */
    private String code;

    /**
     * 使用语言
     */
    private String language;

}
