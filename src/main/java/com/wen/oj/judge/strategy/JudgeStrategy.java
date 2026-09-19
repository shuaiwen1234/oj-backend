package com.wen.oj.judge.strategy;

import com.wen.oj.model.dto.questionsubmit.JudgeInfo;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
