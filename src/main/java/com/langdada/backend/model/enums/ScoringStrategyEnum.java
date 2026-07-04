package com.langdada.backend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评分策略枚举
 */
@Getter
public enum ScoringStrategyEnum {

    CUSTOM(0, "自定义"),
    AI(1, "AI");

    private final int value;
    private final String text;

    ScoringStrategyEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 根据 value 获取枚举
     */
    public static ScoringStrategyEnum getEnumByValue(Integer value) {
        if (ObjUtil.isNull(value)) {
            return null;
        }
        for (ScoringStrategyEnum e : ScoringStrategyEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }

    /**
     * 获取值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(ScoringStrategyEnum::getValue).collect(Collectors.toList());
    }
}
