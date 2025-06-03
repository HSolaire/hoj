package com.hang.hoj.model.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: HSolaire
 * Date: 2025/4/29 23:28
 * <p>
 * 题目用例
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;

}
