package com.wen.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wen.oj.model.entity.Question;
import com.wen.oj.service.QuestionService;
import com.wen.oj.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author a1472
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2026-09-16 22:33:46
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




