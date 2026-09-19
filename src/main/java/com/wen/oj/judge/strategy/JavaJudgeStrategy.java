package com.wen.oj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.wen.oj.judge.codesandbox.impl.CodeSandBoxProxy;
import com.wen.oj.model.dto.question.JudgeCase;
import com.wen.oj.model.dto.question.JudgeConfig;
import com.wen.oj.model.dto.questionsubmit.JudgeInfo;
import com.wen.oj.model.entity.Question;
import com.wen.oj.model.enums.JudgeInfoMessageEnum;

import javax.annotation.Resource;
import java.util.List;

/**
 * java程序的判题策略 这里具体是将耗费的执行时间减少了300ms
 */
public class JavaJudgeStrategy implements JudgeStrategy {

    @Resource
    private CodeSandBoxProxy codeSandBoxProxy;


    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<String> inPut = judgeContext.getInPut();
        List<String> outPut = judgeContext.getOutPut();
        Question question = judgeContext.getQuestion();
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();


        //4. 根据沙箱的执行结果 设置题目的判题状态的信息
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        judgeInfoResponse.setTime(judgeInfo.getTime());
        judgeInfoResponse.setMemory(judgeInfo.getMemory());
        judgeInfoResponse.setStackSize(judgeInfo.getStackSize());




        //4.1 先判断沙箱执行结果的输出数量和预期输出数量是否相对
        if(outPut.size()!=inPut.size()){
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
            return judgeInfoResponse;
        }

        //4.2 依次判断每一项输出和预期输出是否相等
        for(int i=0;i<outPut.size();i++){
            if(!outPut.get(i).equals(judgeCaseList.get(i).getOutPut())){
                judgeInfoResponse.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
                return judgeInfoResponse;
            }
        }

        //4.3 判断是否符合题目限制要求
        JudgeConfig expectedJudgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        if(judgeConfig != null){
            if(judgeInfo.getTime()-300>expectedJudgeConfig.getTimeLimit()){
                judgeInfoResponse.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getValue());
                return judgeInfoResponse;
            }
            if(judgeInfo.getMemory()>expectedJudgeConfig.getMemoryLimit()){
                judgeInfoResponse.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getValue());
                return judgeInfoResponse;
            }
            if(judgeInfo.getStackSize()>expectedJudgeConfig.getStackLimit()){
                judgeInfoResponse.setMessage(JudgeInfoMessageEnum.STACK_LIMIT_EXCEEDED.getValue());
                return judgeInfoResponse;
            }
        }


        return  judgeInfoResponse;
    }
}
