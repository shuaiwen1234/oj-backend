package com.wen.oj.judge.codesandbox.impl;

import com.wen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.wen.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.wen.oj.judge.codesandbox.CodeSandBox;
import com.wen.oj.model.dto.questionsubmit.JudgeInfo;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service("codeSandBox")
/**
 * 远程调用代码沙箱
 */
public class RemoteCodeSandBoxImpl implements CodeSandBox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        //TODO 模拟沙箱执行
        ExecuteCodeResponse response = ExecuteCodeResponse.builder()
                .output(Arrays.asList("3 2"))          // ← 必须给，判题策略要拿它逐项比对
                .message("测试用例")
                .judgeInfo(new JudgeInfo("测试信息", 100L, 200L, 300L))
                .build();
        return response;
    }
}
