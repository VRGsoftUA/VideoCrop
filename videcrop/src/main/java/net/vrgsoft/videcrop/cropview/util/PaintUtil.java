package net.vrgsoft.videcrop.cropview.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

public final class PaintUtil {
    private static final int DEFAULT_CORNER_COLOR = -1;
    private static final String SEMI_TRANSPARENT = "#AAFFFFFF";
    private static final String DEFAULT_BACKGROUND_COLOR_ID = "#B0000000";
    private static final float DEFAULT_LINE_THICKNESS_DP = 3.0F;
    private static final float DEFAULT_CORNER_THICKNESS_DP = 5.0F;
    private static final float DEFAULT_GUIDELINE_THICKNESS_PX = 1.0F;

    private PaintUtil() {
    }

    public static Paint newBorderPaint(Context context) {
        float lineThicknessPx = TypedValue.applyDimension(1, 3.0F, context.getResources().getDisplayMetrics());
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.parseColor("#AAFFFFFF"));
        borderPaint.setStrokeWidth(lineThicknessPx);
        borderPaint.setStyle(Paint.Style.STROKE);
        return borderPaint;
    }

    public static Paint newGuidelinePaint() {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#AAFFFFFF"));
        paint.setStrokeWidth(1.0F);
        return paint;
    }

    public static Paint newBackgroundPaint(Context context) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#B0000000"));
        return paint;
    }

    public static Paint newCornerPaint(Context context) {
        float lineThicknessPx = TypedValue.applyDimension(1, 5.0F, context.getResources().getDisplayMetrics());
        Paint cornerPaint = new Paint();
        cornerPaint.setColor(-1);
        cornerPaint.setStrokeWidth(lineThicknessPx);
        cornerPaint.setStyle(Paint.Style.STROKE);
        return cornerPaint;
    }

    public static float getCornerThickness() {
        return 5.0F;
    }

    public static float getLineThickness() {
        return 3.0F;
    }
}
