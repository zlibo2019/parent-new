package com.weds.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {
    // 数据权限类型
    Type type() default Type.NONE;

    // 数据权限字段别名
    String alias() default "";

    // 是否启用
    boolean active() default true;

    public enum Type {
        NONE(0, ""), ORG(1, "#ORG#"), PLACE(2, "#PLACE#"), DEVICE(3, "#DEVICE#");

        private final int value;
        private final String slot;

        Type(int value, String slot) {
            this.value = value;
            this.slot = slot;
        }

        public int value() {
            return this.value;
        }

        public String slot() {
            return this.slot;
        }
    }
}
