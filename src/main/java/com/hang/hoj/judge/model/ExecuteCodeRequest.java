package com.hang.hoj.judge.model;

import com.hang.hoj.model.dto.topic.JudgeCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: HSolaire
 * Date: 2025/5/17 21:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeRequest {

    private String code;

    private String language;

    private List<JudgeCase> judgeCaseList;

}
