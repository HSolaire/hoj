package com.hang.hoj.model.dto.topicsubmit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.hoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: HSolaire
 * Date: 2025/5/1 11:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopicSubmitQueryRequest extends PageRequest {

    /**
     * 题目id
     */
    private Long topicId;

    /**
     * 提交状态 Accepted / / /
     */
    private Integer status;

    /**
     * 使用语言
     */
    private String language;

    /**
     * userId
     */
    private Long userId;


}
