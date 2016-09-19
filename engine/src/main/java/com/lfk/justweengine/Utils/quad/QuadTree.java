package com.lfk.justweengine.Utils.quad;

import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liufengkai on 16/9/18.
 */
public class QuadTree {
    private int MAX_OBJECTS = 10;

    private int MAX_LEVELS = 5;

    private int level;

    private List<RectF> objects;

    private RectF bounds;

    private QuadTree[] nodes;

    public QuadTree(int level, RectF bounds) {
        this.level = level;
        this.objects = new ArrayList<>();
        this.bounds = bounds;
        this.nodes = new QuadTree[4];
    }

    /**
     * clear all sub nodes
     */
    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    /**
     * split nodes
     */
    private void split() {
        // width & height
        int subWidth = (int) (bounds.width() / 2);
        int subHeight = (int) (bounds.height() / 2);
        // x & y
        int x = (int) bounds.left;
        int y = (int) bounds.top;
        // split to four nodes
        nodes[0] = new QuadTree(level + 1, new RectF(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new QuadTree(level + 1, new RectF(x, y, subWidth, subHeight));
        nodes[2] = new QuadTree(level + 1, new RectF(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new QuadTree(level + 1, new RectF(x + subWidth, y + subHeight, subWidth, subHeight));

    }

    /**
     * 获取rect 所在的 index
     *
     * @param rectF 传入对象所在的矩形
     * @return index 使用类别区分所在象限
     */
    private int getIndex(RectF rectF) {
        int index = -1;
        double verticalMidpoint = bounds.left + (bounds.width() / 2);
        double horizontalMidpoint = bounds.top + (bounds.height() / 2);

        // contain top
        boolean topQuadrant = rectF.top < horizontalMidpoint
                && rectF.top + rectF.height() < horizontalMidpoint;
        // contain bottom
        boolean bottomQuadrant = rectF.top > horizontalMidpoint;

        // contain left
        if (rectF.left < verticalMidpoint
                && rectF.left + rectF.width() < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
            // contain right
        } else if (rectF.left > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    /**
     * insert object to tree
     *
     * @param rectF object
     */
    public void insert(RectF rectF) {
        if (nodes[0] != null) {
            int index = getIndex(rectF);

            if (index != -1) {
                nodes[index].insert(rectF);

                return;
            }
        }

        objects.add(rectF);

        if (objects.size() > MAX_OBJECTS
                && level < MAX_OBJECTS) {
            // don't have subNodes
            // split node
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {

                int index = getIndex(objects.get(i));

                if (index != -1) {

                    nodes[index].insert(objects.remove(i));

                } else {

                    // don't in subNode save to parent node.
                    // eq: object on line
                    i++;

                }
            }
        }
    }

    /**
     * return all the object collision with the object
     *
     * @param returnObjects return list
     * @param rectF         object
     * @return list of collision
     */
    public List<List<RectF>> retrieve(List<List<RectF>> returnObjects, RectF rectF) {
        int index = getIndex(rectF);

        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, rectF);
        }

        returnObjects.add(objects);
        return returnObjects;
    }
}

