package com.wen.oj.judge.service;

import com.wen.oj.model.entity.QuestionSubmit;
import com.wen.oj.model.vo.QuestionSubmitVO;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 进行判题
     * @param QuestionSubmitId 题解提交记录的id
     * @return
     */
    public QuestionSubmit doJudge(Long QuestionSubmitId);
}
