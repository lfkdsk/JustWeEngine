package com.lfk.justweengine.Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象回收池
 * 和自动的对象回收池可以选择使用
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/17.
 */
public class ObjectPool<T> {
    public String poolName = null;

    public ObjectPool(publicObjectFactory<T> factory, int maxSize) {
        this.freeObjects = new ArrayList<>(maxSize);
        this.factory = factory;
        this.maxSize = maxSize;
    }

    public interface publicObjectFactory<T> {
        T createObject();
    }

    protected final List<T> freeObjects;

    protected final publicObjectFactory<T> factory;

    private final int maxSize;

    /**
     * 新建对象
     *
     * @return object
     */
    public T newObject() {
        T object;
        if (freeObjects.size() == 0) {
            object = factory.createObject();
        } else
            object = freeObjects.remove(freeObjects.size() - 1);
        return object;
    }

    /**
     * 释放对象
     *
     * @param object
     */
    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }

    public T remove() {
        return freeObjects.remove(freeObjects.size() - 1);
    }

    public int size() {
        return freeObjects.size();
    }

    /**
     * 完全释放
     */
    public void clear() {
        freeObjects.clear();
    }

    public static <T> ObjectPool<T> newInstance(Class<T> clazz, publicObjectFactory<T> factory, int maxSize) {
        ObjectPool<T> pool;
        pool = new ObjectPool<>(factory, maxSize);
        pool.poolName = clazz.getName();
        return pool;
    }
}
