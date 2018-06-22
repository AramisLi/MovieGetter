package com.aramis.library.aramis;

/**
 * AramisEvaluator
 * Created by Aramis on 2017/3/29.
 */

public class AramisEvaluator {

    public int evaluate(float fraction, int startColor, int endColor) {
        int startA = (startColor >> 24) & 0xff;
        int startR = (startColor >> 16) & 0xff;
        int startG = (startColor >> 8) & 0xff;
        int startB = startColor & 0xff;

        int endA = (endColor >> 24) & 0xff;
        int endR = (endColor >> 16) & 0xff;
        int endG = (endColor >> 8) & 0xff;
        int endB = endColor & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

    public int evaluateA(float fraction, int color) {
        int endA = (color >> 24) & 0xff;
        int endR = (color >> 16) & 0xff;
        int endG = (color >> 8) & 0xff;
        int endB = color & 0xff;
        return (((int) (fraction * endA)) << 24) |(endR << 16) | (endG << 8) | endB;
    }
}
