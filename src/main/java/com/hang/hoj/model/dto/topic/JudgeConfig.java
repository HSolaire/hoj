package com.hang.hoj.model.dto.topic;

import lombok.Data;

/**
 * Author: HSolaire
 * Date: 2025/4/29 23:29
 * 判题配置
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制（ms）
     */
    private Long timeLimit;

    /**
     * 内存显示（kb）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（kb）
     */
    private Long stackLimit;

}
