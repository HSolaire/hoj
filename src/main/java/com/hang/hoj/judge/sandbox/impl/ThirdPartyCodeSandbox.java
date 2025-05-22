package com.hang.hoj.judge.sandbox.impl;

import com.hang.hoj.judge.model.ExecuteCodeRequest;
import com.hang.hoj.judge.model.ExecuteCodeResponse;
import com.hang.hoj.judge.sandbox.CodeSandbox;
import org.springframework.stereotype.Service;

/**
 * Author: HSolaire
 * Date: 2025/5/17 23:28
 */
@Service
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
