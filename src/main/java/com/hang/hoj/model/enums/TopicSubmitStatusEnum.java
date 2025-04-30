package com.hang.hoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: HSolaire
 * Date: 2025/4/30 11:21
 */
public enum TopicSubmitStatusEnum {

    // 0 - 待答题，1 - 判题中， 2 - 成功， 3 - 失败
    WAITING(0, "等待中"),
    RUNNING(1, "判题中"),
    SUCCESS(2, "成功"),
    FAILURE(3, "失败");



    private final Integer code;
    private final String text;

    TopicSubmitStatusEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }


    public Integer getCode() {
        return code;
    }

    public String getText() {
        return text;
    }


    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.text).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static TopicSubmitStatusEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (TopicSubmitStatusEnum anEnum : TopicSubmitStatusEnum.values()) {
            if (anEnum.text.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
