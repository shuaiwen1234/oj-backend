package com.wen.oj.model.dto.questionsubmit;

import lombok.Data;

@Data
/**
 * 判题配置
 */
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 程序执行耗时(ms)
     */
    private Long time;

    /**
     * 程序占用内存大小(KB)
     */
    private Long memory;

    private static final long serialVersionUID = 1L;
}
