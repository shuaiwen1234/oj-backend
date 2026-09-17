package com.wen.oj.model.dto.question;

import lombok.Data;

@Data
/**
 * 判题用例
 */
public class JudgeCase {
    /**
     * 输入用例
     */
    private String inPut;
    /**
     * 输出用例
     */
    private String outPut;
}
