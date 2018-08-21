package net.vrgsoft.videcrop.cropview.util;

import android.content.Context;
import android.util.Pair;
import android.util.TypedValue;

import net.vrgsoft.videcrop.cropview.window.CropView;
import net.vrgsoft.videcrop.cropview.window.handle.Handle;

public final class HandleUtil {
    private static final int TARGET_RADIUS_DP = 24;

    private HandleUtil() {
    }

    public static float getTargetRadius(Context context) {
        float targetRadius = TypedValue.applyDimension(1, 24.0F, context.getResources().getDisplayMetrics());
        return targetRadius;
    }

    public static Handle getPressedHandle(float x, float y, float left, float top, float right, float bottom, float targetRadius) {
        Handle pressedHandle = null;
        if (isInCornerTargetZone(x, y, left, top, targetRadius)) {
            pressedHandle = Handle.TOP_LEFT;
        } else if (isInCornerTargetZone(x, y, right, top, targetRadius)) {
            pressedHandle = Handle.TOP_RIGHT;
        } else if (isInCornerTargetZone(x, y, left, bottom, targetRadius)) {
            pressedHandle = Handle.BOTTOM_LEFT;
        } else if (isInCornerTargetZone(x, y, right, bottom, targetRadius)) {
            pressedHandle = Handle.BOTTOM_RIGHT;
        } else if (isInCenterTargetZone(x, y, left, top, right, bottom) && focusCenter()) {
            pressedHandle = Handle.CENTER;
        } else if (isInHorizontalTargetZone(x, y, left, right, top, targetRadius)) {
            pressedHandle = Handle.TOP;
        } else if (isInHorizontalTargetZone(x, y, left, right, bottom, targetRadius)) {
            pressedHandle = Handle.BOTTOM;
        } else if (isInVerticalTargetZone(x, y, left, top, bottom, targetRadius)) {
            pressedHandle = Handle.LEFT;
        } else if (isInVerticalTargetZone(x, y, right, top, bottom, targetRadius)) {
            pressedHandle = Handle.RIGHT;
        } else if (isInCenterTargetZone(x, y, left, top, right, bottom) && !focusCenter()) {
            pressedHandle = Handle.CENTER;
        }

        return pressedHandle;
    }

    public static Pair<Float, Float> getOffset(Handle handle, float x, float y, float left, float top, float right, float bottom) {
        if (handle == null) {
            return null;
        } else {
            float touchOffsetX = 0.0F;
            float touchOffsetY = 0.0F;
            switch(handle) {
                case TOP_LEFT:
                    touchOffsetX = left - x;
                    touchOffsetY = top - y;
                    break;
                case TOP_RIGHT:
                    touchOffsetX = right - x;
                    touchOffsetY = top - y;
                    break;
                case BOTTOM_LEFT:
                    touchOffsetX = left - x;
                    touchOffsetY = bottom - y;
                    break;
                case BOTTOM_RIGHT:
                    touchOffsetX = right - x;
                    touchOffsetY = bottom - y;
                    break;
                case LEFT:
                    touchOffsetX = left - x;
                    touchOffsetY = 0.0F;
                    break;
                case TOP:
                    touchOffsetX = 0.0F;
                    touchOffsetY = top - y;
                    break;
                case RIGHT:
                    touchOffsetX = right - x;
                    touchOffsetY = 0.0F;
                    break;
                case BOTTOM:
                    touchOffsetX = 0.0F;
                    touchOffsetY = bottom - y;
                    break;
                case CENTER:
                    float centerX = (right + left) / 2.0F;
                    float centerY = (top + bottom) / 2.0F;
                    touchOffsetX = centerX - x;
                    touchOffsetY = centerY - y;
            }

            Pair<Float, Float> result = new Pair(touchOffsetX, touchOffsetY);
            return result;
        }
    }

    private static boolean isInCornerTargetZone(float x, float y, float handleX, float handleY, float targetRadius) {
        return Math.abs(x - handleX) <= targetRadius && Math.abs(y - handleY) <= targetRadius;
    }

    private static boolean isInHorizontalTargetZone(float x, float y, float handleXStart, float handleXEnd, float handleY, float targetRadius) {
        return x > handleXStart && x < handleXEnd && Math.abs(y - handleY) <= targetRadius;
    }

    private static boolean isInVerticalTargetZone(float x, float y, float handleX, float handleYStart, float handleYEnd, float targetRadius) {
        return Math.abs(x - handleX) <= targetRadius && y > handleYStart && y < handleYEnd;
    }

    private static boolean isInCenterTargetZone(float x, float y, float left, float top, float right, float bottom) {
        return x > left && x < right && y > top && y < bottom;
    }

    private static boolean focusCenter() {
        return !CropView.showGuidelines();
    }
}
