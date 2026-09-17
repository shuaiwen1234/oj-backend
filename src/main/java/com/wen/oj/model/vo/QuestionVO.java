package com.wen.oj.model.vo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wen.oj.model.dto.question.JudgeConfig;
import com.wen.oj.model.entity.Post;
import com.wen.oj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * 题目VO
 * @TableName question
 */
@Data
public class QuestionVO {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建题目的用户的信息
     */
    private UserVO userVO;

    private static final long serialVersionUID = 1L;

    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        //这里是因为数据库存的是json字符串(String类型) 但是应该返回给前端的是List<String>
        //questionVO 里的tags字段的类型是List<String> 所以这里copyProperties不会设置他 需要自己设置
        if(StrUtil.isNotBlank(question.getTags())) {
            //JSONUtil.toList(question.getTags(), String.class) 是将指定的json字符串转换为List<String>
            //注：这里的json需要符合格式 第一个参数是指定的字符串 第二个参数是集合里元素的类型
            questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        }

        //同上 将json字符串转为bean
        String judgeConfig = question.getJudgeConfig();
        if(StrUtil.isNotBlank(judgeConfig)) {
            questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfig, JudgeConfig.class));
        }
        return questionVO;
    }

    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        //这里是因为数据库存的是json字符串(String类型) 但是应该返回给前端的是List<String>
        //questionVO 里的tags字段的类型是List<String> 所以这里copyProperties不会设置他 需要自己设置
        if(questionVO.getTags() != null) {
            //将集合转为字符串类型
            // 用 JSONUtil.toJsonStr，而不是 List 自带的 toString()
            question.setTags(JSONUtil.toJsonStr(questionVO.getTags()));
        }

        //同上 将bean对象转为json字符串
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        if(judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        return question;
    }

}