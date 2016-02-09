package com.lfk.justweengine.Utils.script;

/**
 * Created by liufengkai on 16/2/8.
 */
public interface KeyCode {

    // 用于注释
    // single annotate
    String FLAG_S_ANNO_TAG = "//";

    String FLAG_INCULDE_TAG = "INCLUDE";

    // 条件分支

    String FLAG_IF_TAG = "IF";

    String FLAG_ELSE_TAG = "ELSE";

    String FLAG_ELSE_IF_TAG = "ELSEIF";

    String FLAG_END_IF_TAG = "ENDIF";

    String FLAG_EQUAL_TAG = "==";

    String FLAG_UN_EQUAL_TAG = "!=";

    String FLAG_BIGGER_EQUAL_TAG = ">=";

    String FLAG_SMALLER_EQUAL_TAG = "<=";

    String FLAG_SMALLER_TAG = "<";

    String FLAG_BIGGER_TAG = ">";

    String FLAG_SET_TAG = "=";
    // 运行

    String FLAG_RUN_TAG = "RUN";

    // 开始

    String FLAG_BEGIN_TAG = "BEGIN";

    // 结束

    String FLAG_END_TAG = "END";

    // 主函数

    String FLAG_MAIN_TAG = "MAIN";
}
