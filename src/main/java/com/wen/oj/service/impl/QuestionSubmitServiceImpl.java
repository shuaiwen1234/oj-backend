package com.wen.oj.service.impl;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wen.oj.common.ErrorCode;
import com.wen.oj.exception.BusinessException;
import com.wen.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.wen.oj.model.entity.Question;
import com.wen.oj.model.entity.QuestionSubmit;
import com.wen.oj.model.entity.User;
import com.wen.oj.model.enums.QuestionSubmitLanguageEnum;
import com.wen.oj.service.QuestionService;
import com.wen.oj.service.QuestionSubmitService;
import com.wen.oj.mapper.QuestionSubmitMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author a1472
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2026-09-16 22:39:43
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return 返回插入的记录(题解)的id
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionSubmitAddRequest.getQuestionId());
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //获取用户id
        long userId = loginUser.getId();

        QuestionSubmit questionSubmit = new QuestionSubmit();
        List<String> languages = QuestionSubmitLanguageEnum.getValues();
        if(!languages.contains(questionSubmit.getLanguage())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"暂时不支持该编程语言");
        }

        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());

        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setStatus(0);
        questionSubmit.setQuestionId(questionSubmitAddRequest.getQuestionId());
        questionSubmit.setUserId(userId);
        questionSubmit.setCreateTime(new Date());
        questionSubmit.setUpdateTime(new Date());

        boolean isSuccess = this.save(questionSubmit);
        if(!isSuccess){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }

        return questionSubmit.getId();
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionSubmitInner(long userId, long questionId) {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        QueryWrapper<QuestionSubmit> thumbQueryWrapper = new QueryWrapper<>(questionSubmit);
        QuestionSubmit oldQuestionSubmit = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已提交题目
        if (oldQuestionSubmit != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 题目提交数 - 1
                result = questionService.update()
                        .eq("id", questionId)
                        .gt("thumbNum", 0)
                        .setSql("thumbNum = thumbNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未提交题目
            result = this.save(questionSubmit);
            if (result) {
                // 题目提交数 + 1
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("thumbNum = thumbNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }


}




