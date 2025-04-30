package com.hang.hoj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 题目提交
 * @TableName topic_submit
 */
@TableName(value ="topic_submit")
@Data
public class TopicSubmit {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 题目id
     */
    private Long topicId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 判题代码
     */
    private String code;

    /**
     * 使用语言
     */
    private String language;

    /**
     * 判题状态（0 - 待答题，1 - 判题中， 2 - 成功， 3 - 失败）
     */
    private Integer status;

    /**
     * 判题信息（json 对象）
     */
    private String judgeInfo;

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
