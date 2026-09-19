package com.wen.oj.judge.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.wen.oj.common.ErrorCode;
import com.wen.oj.exception.BusinessException;
import com.wen.oj.judge.JudgeManager;
import com.wen.oj.judge.codesandbox.impl.CodeSandBoxProxy;
import com.wen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.wen.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.wen.oj.judge.service.JudgeService;
import com.wen.oj.judge.strategy.DefaultJudgeStrategy;
import com.wen.oj.judge.strategy.JudgeContext;
import com.wen.oj.judge.strategy.JudgeStrategy;
import com.wen.oj.model.dto.question.JudgeCase;
import com.wen.oj.model.dto.question.JudgeConfig;
import com.wen.oj.model.dto.questionsubmit.JudgeInfo;
import com.wen.oj.model.entity.Question;
import com.wen.oj.model.entity.QuestionSubmit;
import com.wen.oj.model.enums.JudgeInfoMessageEnum;
import com.wen.oj.model.enums.QuestionSubmitStatusEnum;
import com.wen.oj.model.vo.QuestionSubmitVO;
import com.wen.oj.service.QuestionService;
import com.wen.oj.service.QuestionSubmitService;
import org.eclipse.parsson.JsonUtil;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private QuestionSubmitService questionSubmitService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CodeSandBoxProxy  codeSandBoxProxy;
    @Autowired
    private JudgeManager  judgeManager;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        //1. 根据传入的题目提交记录id 获取对应的题目 提交信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if(questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if(question == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目信息不存在");
        }

        //2. 判断这个题目提交信息的状态 如果不是待判题就不用判题了
        if(QuestionSubmitStatusEnum.WAITING.getValue()!=questionSubmit.getStatus()){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目正在判题或已判题结束，请勿重复操作");
        }

        //2.1 修改题目提交信息为判题中
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());

        //2.2 更新判题状态
        boolean b = questionSubmitService.updateById(questionSubmitUpdate);
        if(!b){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交状态更新失败 请稍后再试");
        }


        //3. 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inPut = judgeCaseList.stream().map(JudgeCase::getInPut).collect(Collectors.toList());

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(questionSubmit.getCode())
                .input(inPut)
                .language(questionSubmit.getLanguage())
                .build();

        //3.1 调用沙箱 获取到执行结果
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(executeCodeRequest);
        //3.2 对结果做出判断
        JudgeContext judgeContext = JudgeContext.builder()
                .inPut(inPut)
                .outPut(executeCodeResponse.getOutput())
                .question(question)
                .questionSubmit(questionSubmit)
                .judgeInfo(executeCodeResponse.getJudgeInfo())
                .judgeCaseList(judgeCaseList).build();

        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        //4 修改题目提交信息为判题结束
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));

        //4.1 更新判题状态
         b = questionSubmitService.updateById(questionSubmitUpdate);
        if(!b){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交状态更新失败 请稍后再试");
        }

        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResult;

    }
}
