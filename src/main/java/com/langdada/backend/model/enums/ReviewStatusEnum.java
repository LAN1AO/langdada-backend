package com.langdada.backend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审核状态枚举
 */
@Getter
public enum ReviewStatusEnum {

    PENDING(0, "待审核"),
    APPROVED(1, "通过"),
    REJECTED(2, "拒绝");

    private final int value;
    private final String text;

    ReviewStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 根据 value 获取枚举
     */
    public static ReviewStatusEnum getEnumByValue(Integer value) {
        if (ObjUtil.isNull(value)) {
            return null;
        }
        for (ReviewStatusEnum e : ReviewStatusEnum.values()) {
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
        return Arrays.stream(values()).map(ReviewStatusEnum::getValue).collect(Collectors.toList());
    }
}
