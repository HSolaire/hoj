package com.hang.hoj.judge;

import com.hang.hoj.common.ErrorCode;
import com.hang.hoj.exception.BusinessException;
import com.hang.hoj.judge.sandbox.CodeSandbox;
import com.hang.hoj.judge.sandbox.impl.ExampleCodeSandbox;
import com.hang.hoj.judge.sandbox.impl.RemoteCodeSandbox;
import com.hang.hoj.judge.sandbox.impl.ThirdPartyCodeSandbox;

/**
 * Author: HSolaire
 * Date: 2025/5/17 21:18
 */
public class CodeSandboxFactory {

    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }


}
