package com.hang.hoj.judge.sandbox.impl;

import com.hang.hoj.judge.model.ExecuteCodeRequest;
import com.hang.hoj.judge.model.ExecuteCodeResponse;
import com.hang.hoj.judge.sandbox.CodeSandbox;
import org.springframework.stereotype.Service;

@Service
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
