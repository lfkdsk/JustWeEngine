package com.lfk.justweengine.Engine;

import com.lfk.justweengine.Drawable.Sprite.BaseSub;

import java.util.HashMap;

/**
 * 对象池组
 * Created by liufengkai on 16/4/25.
 */
public class ObjectPoolGroup {
    // ObjectPoolGroup
    private final HashMap<String, ObjectPool<?>> freeObjectsHashMap;
    // default factory method
    private final ObjectPool.publicObjectFactory factory;
    // default max size
    private final int maxSize;
    /**
     * 初始化对象池
     *
     * @param factory 这里的工厂方法是BaseSub的默认生成方法,
     *                如果你所需要的方法不存在则默认使用
     * @param maxSize 对象池的默认选项
     */
    public ObjectPoolGroup(ObjectPool.publicObjectFactory factory, int maxSize) {
        this.maxSize = maxSize;
        this.factory = factory;
        this.freeObjectsHashMap = new HashMap<>();
    }

    /**
     * 注册工厂方法
     *
     * @param objectType 类
     * @param factory    工厂方法(注意泛型与上文一致)
     * @param maxSize    最大数量
     * @param <T>        泛型
     */
    public <T> ObjectPool<T> registerFactory(Class<T> objectType,
                                             ObjectPool.publicObjectFactory<T> factory,
                                             int maxSize) {
        ObjectPool<T> objectPool = ObjectPool.newInstance(objectType, factory, maxSize);
        this.freeObjectsHashMap.put(objectType.getName(), objectPool);
        return objectPool;
    }

    public <T> ObjectPool<T> registerFactory(Class<T> objectType,
                                             ObjectPool.publicObjectFactory<T> factory) {
        ObjectPool<T> objectPool = ObjectPool.newInstance(objectType, factory, maxSize);
        this.freeObjectsHashMap.put(objectType.getName(), objectPool);
        return objectPool;
    }

    /**
     * 注销工厂方法
     *
     * @param objectType 类
     */
    public void unregisterFactory(Class objectType) {
        this.freeObjectsHashMap.remove(objectType.getName());
    }

    /**
     * 使用类型名进行生成调用
     *
     * @param type BaseSub类型
     * @return 新建对象
     */
    public <T> T newObject(Class<T> type) {
        T object;
        if (freeObjectsHashMap.size() == 0) {
            if (freeObjectsHashMap.containsKey(type.getName())) {
                object = (getObjectPool(type)).newObject();
            } else {
                object = (getPublicFactory(type)).createObject();
            }
        } else
            object = (getObjectPool(type)).remove();
        return object;
    }

    /**
     * 获取对象池
     *
     * @param clazz 类
     * @param <T>   泛型
     * @return 对应的对象池
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectPool<T> getObjectPool(Class<T> clazz) {
        return (ObjectPool<T>) freeObjectsHashMap.get(clazz.getName());
    }

    @SuppressWarnings("unchecked")
    public <T> ObjectPool<T> getObjectPool(String baseSub) {
        return (ObjectPool<T>) freeObjectsHashMap.get(baseSub);
    }

    @SuppressWarnings("unchecked")
    private <T> ObjectPool.publicObjectFactory<T> getPublicFactory(Class<T> clazz) {
        return (ObjectPool.publicObjectFactory<T>) factory;
    }

    /**
     * baseSub 和 类有相同的名字
     *
     * @param baseSub 物体
     * @param <T>     泛型
     */
    public <T extends BaseSub> void free(T baseSub) {
        getObjectPool(baseSub.s_name).free(baseSub);
    }

    public void clear() {
        freeObjectsHashMap.clear();
    }
}
