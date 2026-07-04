package com.langdada.backend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 应用类型枚举
 */
@Getter
public enum AppTypeEnum {

    SCORE(0, "得分类"),
    TEST(1, "测评类");

    private final int value;
    private final String text;

    AppTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 根据 value 获取枚举
     */
    public static AppTypeEnum getEnumByValue(Integer value) {
        if (ObjUtil.isNull(value)) {
            return null;
        }
        for (AppTypeEnum e : AppTypeEnum.values()) {
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
        return Arrays.stream(values()).map(AppTypeEnum::getValue).collect(Collectors.toList());
    }
}
