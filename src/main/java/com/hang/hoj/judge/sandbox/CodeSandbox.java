package com.hang.hoj.judge.sandbox;

import com.hang.hoj.judge.model.ExecuteCodeRequest;
import com.hang.hoj.judge.model.ExecuteCodeResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest request) throws IOException, InterruptedException;

}
