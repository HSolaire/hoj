package com.hang.hoj.model.dto.topic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hang.hoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TopicQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目标题
     */
    private String title;


    /**
     * 题目难度
     */
    private Integer level;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目标签
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 题目创建者id
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}
