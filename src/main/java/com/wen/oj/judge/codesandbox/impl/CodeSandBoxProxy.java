package com.wen.oj.judge.codesandbox.impl;

import com.wen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.wen.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.wen.oj.judge.codesandbox.CodeSandBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("codeSandBoxProxy")
public class CodeSandBoxProxy implements CodeSandBox {

    private final CodeSandBox codeSandBox;

    @Autowired
    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest){
        log.info("代码沙箱请求信息 executeCodeRequest为: {}", executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱返回信息 executeCodeResponse为: {}", executeCodeResponse.toString());
        return executeCodeResponse;
    }

}
