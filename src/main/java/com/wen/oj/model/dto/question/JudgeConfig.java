package com.wen.oj.model.dto.question;

import lombok.Data;

@Data
/**
 * 判题配置
 */
public class JudgeConfig {
    /**
     * 时间限制(ms)
     */
    private Long timeLimit;

    /**
     * 内存限制(KB)
     */
    private Long memoryLimit;

    /**
     * 栈空间限制(KB)
     */
    private Long stackLimit;
}
