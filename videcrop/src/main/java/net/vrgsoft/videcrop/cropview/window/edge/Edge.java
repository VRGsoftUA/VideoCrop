package net.vrgsoft.videcrop.cropview.window.edge;

import android.graphics.Rect;
import android.view.View;

import net.vrgsoft.videcrop.cropview.util.AspectRatioUtil;

public enum Edge {
    LEFT,
    TOP,
    RIGHT,
    BOTTOM;

    public static final int MIN_CROP_LENGTH_PX = 40;
    private float mCoordinate;

    private Edge() {
    }

    public void setCoordinate(float coordinate) {
        this.mCoordinate = coordinate;
    }

    public void offset(float distance) {
        this.mCoordinate += distance;
    }

    public float getCoordinate() {
        return this.mCoordinate;
    }

    public void adjustCoordinate(float x, float y, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        switch(this) {
            case LEFT:
                this.mCoordinate = adjustLeft(x, imageRect, imageSnapRadius, aspectRatio);
                break;
            case TOP:
                this.mCoordinate = adjustTop(y, imageRect, imageSnapRadius, aspectRatio);
                break;
            case RIGHT:
                this.mCoordinate = adjustRight(x, imageRect, imageSnapRadius, aspectRatio);
                break;
            case BOTTOM:
                this.mCoordinate = adjustBottom(y, imageRect, imageSnapRadius, aspectRatio);
        }

    }

    public void adjustCoordinate(float aspectRatio) {
        float left = LEFT.getCoordinate();
        float top = TOP.getCoordinate();
        float right = RIGHT.getCoordinate();
        float bottom = BOTTOM.getCoordinate();
        switch(this) {
            case LEFT:
                this.mCoordinate = AspectRatioUtil.calculateLeft(top, right, bottom, aspectRatio);
                break;
            case TOP:
                this.mCoordinate = AspectRatioUtil.calculateTop(left, right, bottom, aspectRatio);
                break;
            case RIGHT:
                this.mCoordinate = AspectRatioUtil.calculateRight(left, top, bottom, aspectRatio);
                break;
            case BOTTOM:
                this.mCoordinate = AspectRatioUtil.calculateBottom(left, top, right, aspectRatio);
        }

    }

    public boolean isNewRectangleOutOfBounds(Edge edge, Rect imageRect, float aspectRatio) {
        float offset = edge.snapOffset(imageRect);
        float right;
        float left;
        float top;
        float bottom;
        switch(this) {
            case LEFT:
                if (edge.equals(TOP)) {
                    right = (float)imageRect.top;
                    left = BOTTOM.getCoordinate() - offset;
                    top = RIGHT.getCoordinate();
                    bottom = AspectRatioUtil.calculateLeft(right, top, left, aspectRatio);
                    return this.isOutOfBounds(right, bottom, left, top, imageRect);
                }

                if (edge.equals(BOTTOM)) {
                    right = (float)imageRect.bottom;
                    left = TOP.getCoordinate() - offset;
                    top = RIGHT.getCoordinate();
                    bottom = AspectRatioUtil.calculateLeft(left, top, right, aspectRatio);
                    return this.isOutOfBounds(left, bottom, right, top, imageRect);
                }
                break;
            case TOP:
                if (edge.equals(LEFT)) {
                    right = (float)imageRect.left;
                    left = RIGHT.getCoordinate() - offset;
                    top = BOTTOM.getCoordinate();
                    bottom = AspectRatioUtil.calculateTop(right, left, top, aspectRatio);
                    return this.isOutOfBounds(bottom, right, top, left, imageRect);
                }

                if (edge.equals(RIGHT)) {
                    right = (float)imageRect.right;
                    left = LEFT.getCoordinate() - offset;
                    top = BOTTOM.getCoordinate();
                    bottom = AspectRatioUtil.calculateTop(left, right, top, aspectRatio);
                    return this.isOutOfBounds(bottom, left, top, right, imageRect);
                }
                break;
            case RIGHT:
                if (edge.equals(TOP)) {
                    right = (float)imageRect.top;
                    left = BOTTOM.getCoordinate() - offset;
                    top = LEFT.getCoordinate();
                    bottom = AspectRatioUtil.calculateRight(top, right, left, aspectRatio);
                    return this.isOutOfBounds(right, top, left, bottom, imageRect);
                }

                if (edge.equals(BOTTOM)) {
                    right = (float)imageRect.bottom;
                    left = TOP.getCoordinate() - offset;
                    top = LEFT.getCoordinate();
                    bottom = AspectRatioUtil.calculateRight(top, left, right, aspectRatio);
                    return this.isOutOfBounds(left, top, right, bottom, imageRect);
                }
                break;
            case BOTTOM:
                if (edge.equals(LEFT)) {
                    right = (float)imageRect.left;
                    left = RIGHT.getCoordinate() - offset;
                    top = TOP.getCoordinate();
                    bottom = AspectRatioUtil.calculateBottom(right, top, left, aspectRatio);
                    return this.isOutOfBounds(top, right, bottom, left, imageRect);
                }

                if (edge.equals(RIGHT)) {
                    right = (float)imageRect.right;
                    left = LEFT.getCoordinate() - offset;
                    top = TOP.getCoordinate();
                    bottom = AspectRatioUtil.calculateBottom(left, top, right, aspectRatio);
                    return this.isOutOfBounds(top, left, bottom, right, imageRect);
                }
        }

        return true;
    }

    private boolean isOutOfBounds(float top, float left, float bottom, float right, Rect imageRect) {
        return top < (float)imageRect.top || left < (float)imageRect.left || bottom > (float)imageRect.bottom || right > (float)imageRect.right;
    }

    public float snapToRect(Rect imageRect) {
        float oldCoordinate = this.mCoordinate;
        switch(this) {
            case LEFT:
                this.mCoordinate = (float)imageRect.left;
                break;
            case TOP:
                this.mCoordinate = (float)imageRect.top;
                break;
            case RIGHT:
                this.mCoordinate = (float)imageRect.right;
                break;
            case BOTTOM:
                this.mCoordinate = (float)imageRect.bottom;
        }

        float offset = this.mCoordinate - oldCoordinate;
        return offset;
    }

    public float snapOffset(Rect imageRect) {
        float oldCoordinate = this.mCoordinate;
        float newCoordinate = oldCoordinate;
        switch(this) {
            case LEFT:
                newCoordinate = (float)imageRect.left;
                break;
            case TOP:
                newCoordinate = (float)imageRect.top;
                break;
            case RIGHT:
                newCoordinate = (float)imageRect.right;
                break;
            case BOTTOM:
                newCoordinate = (float)imageRect.bottom;
        }

        float offset = newCoordinate - oldCoordinate;
        return offset;
    }

    public void snapToView(View view) {
        switch(this) {
            case LEFT:
                this.mCoordinate = 0.0F;
                break;
            case TOP:
                this.mCoordinate = 0.0F;
                break;
            case RIGHT:
                this.mCoordinate = (float)view.getWidth();
                break;
            case BOTTOM:
                this.mCoordinate = (float)view.getHeight();
        }

    }

    public static float getWidth() {
        return RIGHT.getCoordinate() - LEFT.getCoordinate();
    }

    public static float getHeight() {
        return BOTTOM.getCoordinate() - TOP.getCoordinate();
    }

    public boolean isOutsideMargin(Rect rect, float margin) {
        boolean result = false;
        switch(this) {
            case LEFT:
                result = this.mCoordinate - (float)rect.left < margin;
                break;
            case TOP:
                result = this.mCoordinate - (float)rect.top < margin;
                break;
            case RIGHT:
                result = (float)rect.right - this.mCoordinate < margin;
                break;
            case BOTTOM:
                result = (float)rect.bottom - this.mCoordinate < margin;
        }

        return result;
    }

    public boolean isOutsideFrame(Rect rect) {
        double margin = 0.0D;
        boolean result = false;
        switch(this) {
            case LEFT:
                result = (double)(this.mCoordinate - (float)rect.left) < margin;
                break;
            case TOP:
                result = (double)(this.mCoordinate - (float)rect.top) < margin;
                break;
            case RIGHT:
                result = (double)((float)rect.right - this.mCoordinate) < margin;
                break;
            case BOTTOM:
                result = (double)((float)rect.bottom - this.mCoordinate) < margin;
        }

        return result;
    }

    private static float adjustLeft(float x, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        float resultX;
        if (x - (float)imageRect.left < imageSnapRadius) {
            resultX = (float)imageRect.left;
        } else {
            float resultXHoriz = Float.POSITIVE_INFINITY;
            float resultXVert = Float.POSITIVE_INFINITY;
            if (x >= RIGHT.getCoordinate() - 40.0F) {
                resultXHoriz = RIGHT.getCoordinate() - 40.0F;
            }

            if ((RIGHT.getCoordinate() - x) / aspectRatio <= 40.0F) {
                resultXVert = RIGHT.getCoordinate() - 40.0F * aspectRatio;
            }

            resultX = Math.min(x, Math.min(resultXHoriz, resultXVert));
        }

        return resultX;
    }

    private static float adjustRight(float x, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        float resultX;
        if ((float)imageRect.right - x < imageSnapRadius) {
            resultX = (float)imageRect.right;
        } else {
            float resultXHoriz = Float.NEGATIVE_INFINITY;
            float resultXVert = Float.NEGATIVE_INFINITY;
            if (x <= LEFT.getCoordinate() + 40.0F) {
                resultXHoriz = LEFT.getCoordinate() + 40.0F;
            }

            if ((x - LEFT.getCoordinate()) / aspectRatio <= 40.0F) {
                resultXVert = LEFT.getCoordinate() + 40.0F * aspectRatio;
            }

            resultX = Math.max(x, Math.max(resultXHoriz, resultXVert));
        }

        return resultX;
    }

    private static float adjustTop(float y, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        float resultY;
        if (y - (float)imageRect.top < imageSnapRadius) {
            resultY = (float)imageRect.top;
        } else {
            float resultYVert = Float.POSITIVE_INFINITY;
            float resultYHoriz = Float.POSITIVE_INFINITY;
            if (y >= BOTTOM.getCoordinate() - 40.0F) {
                resultYHoriz = BOTTOM.getCoordinate() - 40.0F;
            }

            if ((BOTTOM.getCoordinate() - y) * aspectRatio <= 40.0F) {
                resultYVert = BOTTOM.getCoordinate() - 40.0F / aspectRatio;
            }

            resultY = Math.min(y, Math.min(resultYHoriz, resultYVert));
        }

        return resultY;
    }

    private static float adjustBottom(float y, Rect imageRect, float imageSnapRadius, float aspectRatio) {
        float resultY;
        if ((float)imageRect.bottom - y < imageSnapRadius) {
            resultY = (float)imageRect.bottom;
        } else {
            float resultYVert = Float.NEGATIVE_INFINITY;
            float resultYHoriz = Float.NEGATIVE_INFINITY;
            if (y <= TOP.getCoordinate() + 40.0F) {
                resultYVert = TOP.getCoordinate() + 40.0F;
            }

            if ((y - TOP.getCoordinate()) * aspectRatio <= 40.0F) {
                resultYHoriz = TOP.getCoordinate() + 40.0F / aspectRatio;
            }

            resultY = Math.max(y, Math.max(resultYHoriz, resultYVert));
        }

        return resultY;
    }
}

