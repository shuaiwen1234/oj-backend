package com.wen.oj.judge;

import com.wen.oj.judge.strategy.DefaultJudgeStrategy;
import com.wen.oj.judge.strategy.JavaJudgeStrategy;
import com.wen.oj.judge.strategy.JudgeContext;
import com.wen.oj.judge.strategy.JudgeStrategy;
import com.wen.oj.model.dto.questionsubmit.JudgeInfo;
import com.wen.oj.model.enums.QuestionSubmitLanguageEnum;
import org.springframework.stereotype.Service;

/**
 * 这个类用来根据传入的judgeContext里面的编程语言来选择一个判题策略(默认或者java)来执行doJudge
 * 使用了策略模式
 */
@Service
public class JudgeManager {

    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        String language = judgeContext.getQuestionSubmit().getLanguage();
        if(language.equals(QuestionSubmitLanguageEnum.JAVA.getValue())){
            judgeStrategy = new JavaJudgeStrategy();
        }

        return  judgeStrategy.doJudge(judgeContext);
    }
}
