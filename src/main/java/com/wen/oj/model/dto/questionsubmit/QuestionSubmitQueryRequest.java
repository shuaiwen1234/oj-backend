package com.wen.oj.model.dto.questionsubmit;

import com.wen.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 编程语言
     */
    private String language;

    private static final long serialVersionUID = 1L;
}