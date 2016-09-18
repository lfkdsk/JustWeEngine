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

    private List objects;

    private RectF bounds;

    private QuadTree[] nodes;

    public QuadTree(int level, RectF bounds) {
        this.level = level;
        this.objects = new ArrayList();
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
}

