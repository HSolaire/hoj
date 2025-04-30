package com.hang.hoj.model.dto.topicsubmit;

import lombok.Data;

/**
 * Author: HSolaire
 * Date: 2025/4/29 23:28
 */
@Data
public class JudgeInfo {

    /**
     * 消耗时间（ms）
     */
    private Long time;

    /**
     * 消耗内存（kb）
     */
    private Long memory;

    /**
     * 程序执行信息
     */
    private String message;


}
