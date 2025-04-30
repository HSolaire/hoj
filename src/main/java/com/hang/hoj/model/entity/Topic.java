package com.hang.hoj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 题目
 * @TableName topic
 */
@TableName(value ="topic")
@Data
public class Topic {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
    private String tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 题目创建者id
     */
    private Long userId;

    /**
     * 提交总次数
     */
    private Integer totalCount;

    /**
     * 通过总次数
     */
    private Integer acceptedCount;

    /**
     * 点赞数
     */
    private Integer thumbCount;

    /**
     * 收藏数
     */
    private Integer favourCount;

    /**
     * 判题配置（json 对象）
     */
    private String judgeConfig;

    /**
     * 判题用例（json 对象）
     */
    private String judgeCase;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;
}
