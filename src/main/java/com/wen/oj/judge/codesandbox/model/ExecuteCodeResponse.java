package com.wen.oj.judge.codesandbox.model;

import com.wen.oj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeResponse {

    /**
     * 输出用例
     */
    private List<String> output;

    /**
     * 执行信息(不一定是程序的执行信息)
     */
    private String message;

    /**
     * 执行的状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
