package com.lfk.justweengine.Anim;

/**
 * 透明度动画
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/28.
 */
public class AlphaAnimation extends BaseAnim {
    private int maxAlpha;
    private int minAlpha;
    private int changeAlpha;

    public AlphaAnimation(int changeAlpha) {
        this(255, 0, changeAlpha);
    }

    public AlphaAnimation(int maxAlpha, int minAlpha, int changeAlpha) {
        super();
        this.maxAlpha = maxAlpha;
        this.minAlpha = minAlpha;
        this.changeAlpha = changeAlpha;
        this.animType = AnimType.ALPHA;
        this.animating = true;
    }

    @Override
    public int adjustAlpha(int ori) {
        int modified = ori;
        modified += changeAlpha;
        if (modified < minAlpha) {
            modified = minAlpha;
            animating = false;
        }
        if (modified > maxAlpha) {
            modified = maxAlpha;
            animating = false;
        }
        return modified;
    }
}
