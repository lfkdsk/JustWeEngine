package com.lfk.justweengine.Utils.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liufengkai on 16/3/18.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LabelName {
    String columnName() default "label";

    boolean generatedId() default false;

    boolean autoincrement() default false;

    enum Type {NULL, BLOB, INTEGER, REAL, TEXT}

    Type type() default Type.NULL;
}
