package com.lfk.justweengine.Utils.script;

/**
 * Function 函数
 *
 * @author liufengkai
 *         Created by liufengkai on 16/2/8.
 */
public class Function {
    // 函数开始位置
    private long startIndex;
    // 函数结束位置
    private long endIndex;
    
    public Function(long startIndex, long endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }
}
