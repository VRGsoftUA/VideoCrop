package net.vrgsoft.videcrop.cropview.util;

import android.graphics.Rect;

public final class AspectRatioUtil {
    private AspectRatioUtil() {
    }

    public static float calculateAspectRatio(float left, float top, float right, float bottom) {
        float width = right - left;
        float height = bottom - top;
        return width / height;
    }

    public static float calculateAspectRatio(Rect rect) {
        return (float)rect.width() / (float)rect.height();
    }

    public static float calculateLeft(float top, float right, float bottom, float targetAspectRatio) {
        float height = bottom - top;
        return right - targetAspectRatio * height;
    }

    public static float calculateTop(float left, float right, float bottom, float targetAspectRatio) {
        float width = right - left;
        return bottom - width / targetAspectRatio;
    }

    public static float calculateRight(float left, float top, float bottom, float targetAspectRatio) {
        float height = bottom - top;
        return targetAspectRatio * height + left;
    }

    public static float calculateBottom(float left, float top, float right, float targetAspectRatio) {
        float width = right - left;
        return width / targetAspectRatio + top;
    }

    public static float calculateWidth(float top, float bottom, float targetAspectRatio) {
        float height = bottom - top;
        return targetAspectRatio * height;
    }

    public static float calculateHeight(float left, float right, float targetAspectRatio) {
        float width = right - left;
        return width / targetAspectRatio;
    }
}
