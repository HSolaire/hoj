package com.hang.hoj.service;

import com.hang.hoj.judge.CodeSandboxFactory;
import com.hang.hoj.judge.model.ExecuteCodeRequest;
import com.hang.hoj.judge.model.ExecuteCodeResponse;
import com.hang.hoj.judge.sandbox.CodeSandbox;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

/**
 * Author: HSolaire
 * Date: 2025/5/17 23:33
 */
@SpringBootTest
public class JudgeServiceTest {

    @Autowired
    List<CodeSandbox> codeSandboxList;


    @Value("${codesandbox.type:example}")
    private String type;

    @Test
    void testList() {
        codeSandboxList.forEach(s -> s.executeCode(null));
    }


    @Test
    void testCodeSandbox() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        ExecuteCodeRequest codeRequest = ExecuteCodeRequest.builder()
                .code("1+1")
                .language("java")
                .judgeCaseList(Collections.emptyList())
                .build();
        ExecuteCodeResponse codeResponse = codeSandbox.executeCode(codeRequest);
    }


}
