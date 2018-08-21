package net.vrgsoft.videcrop.cropview.window;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import net.vrgsoft.videcrop.cropview.util.AspectRatioUtil;
import net.vrgsoft.videcrop.cropview.util.HandleUtil;
import net.vrgsoft.videcrop.cropview.util.PaintUtil;
import net.vrgsoft.videcrop.cropview.window.edge.Edge;
import net.vrgsoft.videcrop.cropview.window.handle.Handle;

public class CropView extends View {
    private static final int SNAP_RADIUS_DP = 6;
    private static final float DEFAULT_SHOW_GUIDELINES_LIMIT = 100.0F;
    private static final float DEFAULT_CORNER_THICKNESS_DP = PaintUtil.getCornerThickness();
    private static final float DEFAULT_LINE_THICKNESS_DP = PaintUtil.getLineThickness();
    private static final float DEFAULT_CORNER_OFFSET_DP;
    private static final float DEFAULT_CORNER_EXTENSION_DP;
    private static final float DEFAULT_CORNER_LENGTH_DP = 20.0F;
    private static final int GUIDELINES_OFF = 0;
    private static final int GUIDELINES_ON_TOUCH = 1;
    private static final int GUIDELINES_ON = 2;
    private Paint mBorderPaint;
    private Paint mGuidelinePaint;
    private Paint mCornerPaint;
    private Paint mBackgroundPaint;
    private Rect mBitmapRect;
    private float mHandleRadius;
    private float mSnapRadius;
    private Pair<Float, Float> mTouchOffset;
    private Handle mPressedHandle;
    private boolean mFixAspectRatio = false;
    private int mAspectRatioX = 1;
    private int mAspectRatioY = 1;
    private float mTargetAspectRatio;
    private int mGuidelines;
    private boolean initializedCropWindow;
    private float mCornerExtension;
    private float mCornerOffset;
    private float mCornerLength;

    public CropView(Context context) {
        super(context);
        mTargetAspectRatio = (float)mAspectRatioX / (float)mAspectRatioY;
        initializedCropWindow = false;
        init(context);
    }

    public CropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTargetAspectRatio = (float)mAspectRatioX / (float)mAspectRatioY;
        initializedCropWindow = false;
        init(context);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        initCropWindow(mBitmapRect);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas, mBitmapRect);
        if (showGuidelines()) {
            if (mGuidelines == 2) {
                drawRuleOfThirdsGuidelines(canvas);
            } else if (mGuidelines == 1) {
                if (mPressedHandle != null) {
                    drawRuleOfThirdsGuidelines(canvas);
                }
            } else if (mGuidelines == 0) {
            }
        }

        canvas.drawRect(Edge.LEFT.getCoordinate(), Edge.TOP.getCoordinate(), Edge.RIGHT.getCoordinate(), Edge.BOTTOM.getCoordinate(), mBorderPaint);
        drawCorners(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        } else {
            switch(event.getAction()) {
                case 0:
                    onActionDown(event.getX(), event.getY());
                    return true;
                case 1:
                case 3:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    onActionUp();
                    return true;
                case 2:
                    onActionMove(event.getX(), event.getY());
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                default:
                    return false;
            }
        }
    }

    public void setBitmapRect(Rect bitmapRect) {
        mBitmapRect = bitmapRect;
        initCropWindow(mBitmapRect);
    }

    public void resetCropOverlayView() {
        if (initializedCropWindow) {
            initCropWindow(mBitmapRect);
            invalidate();
        }

    }

    public void setGuidelines(int guidelines) {
        if (guidelines >= 0 && guidelines <= 2) {
            mGuidelines = guidelines;
            if (initializedCropWindow) {
                initCropWindow(mBitmapRect);
                invalidate();
            }

        } else {
            throw new IllegalArgumentException("Guideline value must be set between 0 and 2. See documentation.");
        }
    }

    public void setFixedAspectRatio(boolean fixAspectRatio) {
        mFixAspectRatio = fixAspectRatio;
        if (initializedCropWindow) {
            initCropWindow(mBitmapRect);
            invalidate();
        }
    }

    public void setAspectRatioX(int aspectRatioX) {
        if (aspectRatioX <= 0) {
            throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
        } else {
            mAspectRatioX = aspectRatioX;
            mTargetAspectRatio = (float)mAspectRatioX / (float)mAspectRatioY;
            if (initializedCropWindow) {
                initCropWindow(mBitmapRect);
                invalidate();
            }

        }
    }

    public void setAspectRatioY(int aspectRatioY) {
        if (aspectRatioY <= 0) {
            throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
        } else {
            mAspectRatioY = aspectRatioY;
            mTargetAspectRatio = (float)mAspectRatioX / (float)mAspectRatioY;
            if (initializedCropWindow) {
                initCropWindow(mBitmapRect);
                invalidate();
            }

        }
    }

    public void setInitialAttributeValues(int guidelines, boolean fixAspectRatio, int aspectRatioX, int aspectRatioY) {
        if (guidelines >= 0 && guidelines <= 2) {
            mGuidelines = guidelines;
            mFixAspectRatio = fixAspectRatio;
            if (aspectRatioX <= 0) {
                throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
            } else {
                mAspectRatioX = aspectRatioX;
                mTargetAspectRatio = (float)mAspectRatioX / (float)mAspectRatioY;
                if (aspectRatioY <= 0) {
                    throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
                } else {
                    mAspectRatioY = aspectRatioY;
                    mTargetAspectRatio = (float)mAspectRatioX / (float)mAspectRatioY;
                }
            }
        } else {
            throw new IllegalArgumentException("Guideline value must be set between 0 and 2. See documentation.");
        }
    }

    private void init(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mHandleRadius = HandleUtil.getTargetRadius(context);
        mSnapRadius = TypedValue.applyDimension(1, 6.0F, displayMetrics);
        mBorderPaint = PaintUtil.newBorderPaint(context);
        mGuidelinePaint = PaintUtil.newGuidelinePaint();
        mBackgroundPaint = PaintUtil.newBackgroundPaint(context);
        mCornerPaint = PaintUtil.newCornerPaint(context);
        mCornerOffset = TypedValue.applyDimension(1, DEFAULT_CORNER_OFFSET_DP, displayMetrics);
        mCornerExtension = TypedValue.applyDimension(1, DEFAULT_CORNER_EXTENSION_DP, displayMetrics);
        mCornerLength = TypedValue.applyDimension(1, 20.0F, displayMetrics);
        mGuidelines = 1;
    }

    private void initCropWindow(Rect bitmapRect) {
        if (!initializedCropWindow) {
            initializedCropWindow = true;
        }

        float centerX;
        float cropWidth;
        if (mFixAspectRatio) {
            float halfCropWidth;
            if (AspectRatioUtil.calculateAspectRatio(bitmapRect) > mTargetAspectRatio) {
                Edge.TOP.setCoordinate((float)bitmapRect.top);
                Edge.BOTTOM.setCoordinate((float)bitmapRect.bottom);
                centerX = (float)getWidth() / 2.0F;
                cropWidth = Math.max(40.0F, AspectRatioUtil.calculateWidth(Edge.TOP.getCoordinate(), Edge.BOTTOM.getCoordinate(), mTargetAspectRatio));
                if (cropWidth == 40.0F) {
                    mTargetAspectRatio = 40.0F / (Edge.BOTTOM.getCoordinate() - Edge.TOP.getCoordinate());
                }

                halfCropWidth = cropWidth / 2.0F;
                Edge.LEFT.setCoordinate(centerX - halfCropWidth);
                Edge.RIGHT.setCoordinate(centerX + halfCropWidth);
            } else {
                Edge.LEFT.setCoordinate((float)bitmapRect.left);
                Edge.RIGHT.setCoordinate((float)bitmapRect.right);
                centerX = (float)getHeight() / 2.0F;
                cropWidth = Math.max(40.0F, AspectRatioUtil.calculateHeight(Edge.LEFT.getCoordinate(), Edge.RIGHT.getCoordinate(), mTargetAspectRatio));
                if (cropWidth == 40.0F) {
                    mTargetAspectRatio = (Edge.RIGHT.getCoordinate() - Edge.LEFT.getCoordinate()) / 40.0F;
                }

                halfCropWidth = cropWidth / 2.0F;
                Edge.TOP.setCoordinate(centerX - halfCropWidth);
                Edge.BOTTOM.setCoordinate(centerX + halfCropWidth);
            }
        } else {
            centerX = 0.1F * (float)bitmapRect.width();
            cropWidth = 0.1F * (float)bitmapRect.height();
            Edge.LEFT.setCoordinate((float)bitmapRect.left + centerX);
            Edge.TOP.setCoordinate((float)bitmapRect.top + cropWidth);
            Edge.RIGHT.setCoordinate((float)bitmapRect.right - centerX);
            Edge.BOTTOM.setCoordinate((float)bitmapRect.bottom - cropWidth);
        }

    }

    public static boolean showGuidelines() {
        return Math.abs(Edge.LEFT.getCoordinate() - Edge.RIGHT.getCoordinate()) >= 100.0F && Math.abs(Edge.TOP.getCoordinate() - Edge.BOTTOM.getCoordinate()) >= 100.0F;
    }

    private void drawRuleOfThirdsGuidelines(Canvas canvas) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        float oneThirdCropWidth = Edge.getWidth() / 3.0F;
        float x1 = left + oneThirdCropWidth;
        canvas.drawLine(x1, top, x1, bottom, mGuidelinePaint);
        float x2 = right - oneThirdCropWidth;
        canvas.drawLine(x2, top, x2, bottom, mGuidelinePaint);
        float oneThirdCropHeight = Edge.getHeight() / 3.0F;
        float y1 = top + oneThirdCropHeight;
        canvas.drawLine(left, y1, right, y1, mGuidelinePaint);
        float y2 = bottom - oneThirdCropHeight;
        canvas.drawLine(left, y2, right, y2, mGuidelinePaint);
    }

    private void drawBackground(Canvas canvas, Rect bitmapRect) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        canvas.drawRect((float)bitmapRect.left, (float)bitmapRect.top, (float)bitmapRect.right, top, mBackgroundPaint);
        canvas.drawRect((float)bitmapRect.left, bottom, (float)bitmapRect.right, (float)bitmapRect.bottom, mBackgroundPaint);
        canvas.drawRect((float)bitmapRect.left, top, left, bottom, mBackgroundPaint);
        canvas.drawRect(right, top, (float)bitmapRect.right, bottom, mBackgroundPaint);
    }

    private void drawCorners(Canvas canvas) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        canvas.drawLine(left - mCornerOffset, top - mCornerExtension, left - mCornerOffset, top + mCornerLength, mCornerPaint);
        canvas.drawLine(left, top - mCornerOffset, left + mCornerLength, top - mCornerOffset, mCornerPaint);
        canvas.drawLine(right + mCornerOffset, top - mCornerExtension, right + mCornerOffset, top + mCornerLength, mCornerPaint);
        canvas.drawLine(right, top - mCornerOffset, right - mCornerLength, top - mCornerOffset, mCornerPaint);
        canvas.drawLine(left - mCornerOffset, bottom + mCornerExtension, left - mCornerOffset, bottom - mCornerLength, mCornerPaint);
        canvas.drawLine(left, bottom + mCornerOffset, left + mCornerLength, bottom + mCornerOffset, mCornerPaint);
        canvas.drawLine(right + mCornerOffset, bottom + mCornerExtension, right + mCornerOffset, bottom - mCornerLength, mCornerPaint);
        canvas.drawLine(right, bottom + mCornerOffset, right - mCornerLength, bottom + mCornerOffset, mCornerPaint);
    }

    private void onActionDown(float x, float y) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        mPressedHandle = HandleUtil.getPressedHandle(x, y, left, top, right, bottom, mHandleRadius);
        if (mPressedHandle != null) {
            mTouchOffset = HandleUtil.getOffset(mPressedHandle, x, y, left, top, right, bottom);
            invalidate();
        }
    }

    private void onActionUp() {
        if (mPressedHandle != null) {
            mPressedHandle = null;
            invalidate();
        }
    }

    private void onActionMove(float x, float y) {
        if (mPressedHandle != null) {
            x += mTouchOffset.first;
            y += mTouchOffset.second;
            if (mFixAspectRatio) {
                mPressedHandle.updateCropWindow(x, y, mTargetAspectRatio, mBitmapRect, mSnapRadius);
            } else {
                mPressedHandle.updateCropWindow(x, y, mBitmapRect, mSnapRadius);
            }
            invalidate();
        }
    }

    static {
        DEFAULT_CORNER_OFFSET_DP = DEFAULT_CORNER_THICKNESS_DP / 2.0F - DEFAULT_LINE_THICKNESS_DP / 2.0F;
        DEFAULT_CORNER_EXTENSION_DP = DEFAULT_CORNER_THICKNESS_DP / 2.0F + DEFAULT_CORNER_OFFSET_DP;
    }
}
