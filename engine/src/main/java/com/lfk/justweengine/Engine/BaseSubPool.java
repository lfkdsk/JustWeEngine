package com.lfk.justweengine.Engine;

import com.lfk.justweengine.Drawable.Sprite.BaseSub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * BaseSub对象池
 * Created by liufengkai on 16/4/25.
 */
public class BaseSubPool<B extends BaseSub> {
    protected final List<B> freeObjects;

    private final baseSubPoolObjectFactory<B> factory;

    private final int maxSize;

    private HashMap<String, baseSubPoolObjectFactory<B>> factoryTable;

    public interface baseSubPoolObjectFactory<B> {
        B createObject();
    }

    /**
     * 初始化对象池
     *
     * @param factory 这里的工厂方法是BaseSub的默认生成方法,
     *                如果你所需要的方法不存在则默认使用
     * @param maxSize 对象池的默认选项
     */
    public BaseSubPool(baseSubPoolObjectFactory<B> factory, int maxSize) {
        this.maxSize = maxSize;
        this.factory = factory;
        this.freeObjects = new ArrayList<>();
        this.factoryTable = new HashMap<>();
    }

    /**
     * 注册BaseSub工厂类
     *
     * @param type    BaseSub具体类型
     * @param factory 生成工厂类
     */
    public void registerBaseSubFactory(String type, baseSubPoolObjectFactory<B> factory) {
        this.factoryTable.put(type, factory);
    }

    /**
     * 注销一种生成类型
     *
     * @param type BaseSub具体类型
     */
    public void unregisterBaseSubFactory(String type) {
        this.factoryTable.remove(type);
    }

    /**
     * 使用类型名进行生成调用
     *
     * @param type BaseSub类型
     * @return 新建对象
     */
    public B newObject(String type) {
        B object;
        if (freeObjects.size() == 0) {
            if (factoryTable.containsKey(type)) {
                object = (factoryTable.get(type)).createObject();
            } else {
                object = factory.createObject();
            }
        } else
            object = freeObjects.remove(freeObjects.size() - 1);
        return object;
    }

    /**
     * 释放对象
     *
     * @param object
     */
    public void free(B object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }

    /**
     * 完全释放
     */
    public void clear() {
        freeObjects.clear();
    }


}
