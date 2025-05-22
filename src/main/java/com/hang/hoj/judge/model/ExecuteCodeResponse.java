package com.hang.hoj.judge.model;

import com.hang.hoj.model.dto.topicsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: HSolaire
 * Date: 2025/5/17 21:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeResponse {

    private Long topicSubmitId;

    private String message;

    private JudgeInfo judgeInfo;

    private Long status;

}
