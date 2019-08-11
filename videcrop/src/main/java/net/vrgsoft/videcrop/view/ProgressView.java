package net.vrgsoft.videcrop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import net.vrgsoft.videcrop.R;

public class ProgressView extends View {
    private Paint mPaint;
    private int mCurrentProgress;
    private RectF mRectF;
    private float mPadding;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(getContext().getResources().getDimension(R.dimen.progress_width));
        mPadding = getContext().getResources().getDimension(R.dimen.padding);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRectF = new RectF(mPadding, mPadding, getWidth() - mPadding, getHeight() - mPadding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(getWidth() * 1.0f / 2, getHeight() * 1.0f  / 2, getWidth() * 1.0f / 2 - mPadding, mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawArc(mRectF, 270, mCurrentProgress * 1.0f / 100 * 360, false, mPaint);
    }

    public void setProgress(int progress) {
        mCurrentProgress = progress;
        invalidate();
    }
}
