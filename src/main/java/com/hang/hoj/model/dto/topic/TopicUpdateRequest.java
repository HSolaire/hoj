package com.hang.hoj.model.dto.topic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求
 *
 */
@Data
public class TopicUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目描述
     */
    private String described;

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
     * 判题配置（json 对象）
     */
    private String judgeConfig;

    /**
     * 判题用例（json 对象）
     */
    private String judgeCase;


    private static final long serialVersionUID = 1L;
}
