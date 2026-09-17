package com.wen.oj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wen.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.wen.oj.model.entity.QuestionSubmit;
import com.wen.oj.model.entity.User;

/**
* @author a1472
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2026-09-16 22:39:43
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 提交
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 题目提交（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionSubmitInner(long userId, long questionId);
}
