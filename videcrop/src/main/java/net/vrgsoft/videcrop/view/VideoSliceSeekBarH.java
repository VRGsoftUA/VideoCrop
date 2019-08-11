package net.vrgsoft.videcrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import net.vrgsoft.videcrop.R;

public class VideoSliceSeekBarH extends AppCompatImageView {
    private static final int SELECT_THUMB_NON = 0;
    private static final int SELECT_THUMB_LEFT = 1;
    private static final int SELECT_THUMB_RIGHT = 2;
    private boolean blocked;
    private boolean isVideoStatusDisplay;
    private long maxValue = 100;
    private Paint paint = new Paint();
    private Paint paintThumb = new Paint();
    private int progressBottom;
    private int progressColor = getResources().getColor(android.R.color.black);
    private int progressHalfHeight = 3;
    private int progressMinDiff = 15;
    private int progressMinDiffPixels;
    private int progressTop;
    private SeekBarChangeListener scl;
    private int secondaryProgressColor = getResources().getColor(R.color.txt_color);
    private int selectedThumb;
    private Bitmap thumbCurrentVideoPosition = BitmapFactory.decodeResource(getResources(), R.drawable.ic_thumb_3);
    private int thumbCurrentVideoPositionHalfWidth;
    private int thumbCurrentVideoPositionX;
    private int thumbCurrentVideoPositionY;
    private int thumbPadding = getResources().getDimensionPixelOffset(R.dimen.default_margin);
    private Bitmap thumbSlice = BitmapFactory.decodeResource(getResources(), R.drawable.ic_thumb_3);
    private int thumbSliceHalfWidth;
    private long thumbSliceLeftValue;
    private int thumbSliceLeftX;
    private long thumbSliceRightValue;
    private int thumbSliceRightX;
    private int thumbSliceY;
    private Bitmap thumbSlicer = BitmapFactory.decodeResource(getResources(), R.drawable.ic_thumb_3);

    public interface SeekBarChangeListener {
        void seekBarValueChanged(long leftThumb, long rightThumb);
    }

    public VideoSliceSeekBarH(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VideoSliceSeekBarH(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoSliceSeekBarH(Context context) {
        super(context);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        if (this.thumbSlice.getHeight() > getHeight()) {
            getLayoutParams().height = this.thumbSlice.getHeight();
        }
        this.thumbSliceY = (getHeight() / 2) - (this.thumbSlice.getHeight() / 2);
        this.thumbCurrentVideoPositionY = (getHeight() / 2) - (this.thumbCurrentVideoPosition.getHeight() / 2);
        this.thumbSliceHalfWidth = this.thumbSlice.getWidth() / 2;
        this.thumbCurrentVideoPositionHalfWidth = this.thumbCurrentVideoPosition.getWidth() / 2;
        if (this.thumbSliceLeftX == 0 || this.thumbSliceRightX == 0) {
            this.thumbSliceLeftX = this.thumbPadding;
            this.thumbSliceRightX = getWidth() - this.thumbPadding;
        }
        this.progressMinDiffPixels = calculateCorrds(this.progressMinDiff) - (this.thumbPadding * 2);
        this.progressTop = (getHeight() / 2) - this.progressHalfHeight;
        this.progressBottom = (getHeight() / 2) + this.progressHalfHeight;
        invalidate();
    }

    public void setSeekBarChangeListener(SeekBarChangeListener scl) {
        this.scl = scl;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColor(this.progressColor);
        canvas.drawRect(new Rect(this.thumbPadding, this.progressTop, this.thumbSliceLeftX, this.progressBottom), this.paint);
        canvas.drawRect(new Rect(this.thumbSliceRightX, this.progressTop, getWidth() - this.thumbPadding, this.progressBottom), this.paint);
        this.paint.setColor(this.secondaryProgressColor);
        canvas.drawRect(new Rect(this.thumbSliceLeftX, this.progressTop, this.thumbSliceRightX, this.progressBottom), this.paint);
        if (!this.blocked) {
            canvas.drawBitmap(this.thumbSlice, (float) (this.thumbSliceLeftX - this.thumbSliceHalfWidth), (float) this.thumbSliceY, this.paintThumb);
            canvas.drawBitmap(this.thumbSlicer, (float) (this.thumbSliceRightX - this.thumbSliceHalfWidth), (float) this.thumbSliceY, this.paintThumb);
        }
        if (this.isVideoStatusDisplay) {
            canvas.drawBitmap(this.thumbCurrentVideoPosition, (float) (this.thumbCurrentVideoPositionX - this.thumbCurrentVideoPositionHalfWidth), (float) this.thumbCurrentVideoPositionY, this.paintThumb);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.blocked) {
            int mx = (int) event.getX();
            switch (event.getAction()) {
                case 0:
                    if ((mx < this.thumbSliceLeftX - this.thumbSliceHalfWidth || mx > this.thumbSliceLeftX + this.thumbSliceHalfWidth) && mx >= this.thumbSliceLeftX - this.thumbSliceHalfWidth) {
                        if ((mx < this.thumbSliceRightX - this.thumbSliceHalfWidth || mx > this.thumbSliceRightX + this.thumbSliceHalfWidth) && mx <= this.thumbSliceRightX + this.thumbSliceHalfWidth) {
                            if ((mx - this.thumbSliceLeftX) + this.thumbSliceHalfWidth >= (this.thumbSliceRightX - this.thumbSliceHalfWidth) - mx) {
                                if ((mx - this.thumbSliceLeftX) + this.thumbSliceHalfWidth > (this.thumbSliceRightX - this.thumbSliceHalfWidth) - mx) {
                                    this.selectedThumb = 2;
                                    break;
                                }
                            }
                            this.selectedThumb = 1;
                            break;
                        }
                        this.selectedThumb = 2;
                        break;
                    }
                    this.selectedThumb = 1;
                    break;
                case 1:
                    this.selectedThumb = 0;
                    break;
                case 2:
                    if ((mx <= (this.thumbSliceLeftX + this.thumbSliceHalfWidth) + 0 && this.selectedThumb == 2) || (mx >= (this.thumbSliceRightX - this.thumbSliceHalfWidth) + 0 && this.selectedThumb == 1)) {
                        this.selectedThumb = 0;
                    }
                    if (this.selectedThumb != 1) {
                        if (this.selectedThumb == 2) {
                            this.thumbSliceRightX = mx;
                            break;
                        }
                    }
                    this.thumbSliceLeftX = mx;
                    break;
            }
            notifySeekBarValueChanged();
        }
        return true;
    }

    private void notifySeekBarValueChanged() {
        if (this.thumbSliceLeftX < this.thumbPadding) {
            this.thumbSliceLeftX = this.thumbPadding;
        }
        if (this.thumbSliceRightX < this.thumbPadding) {
            this.thumbSliceRightX = this.thumbPadding;
        }
        if (this.thumbSliceLeftX > getWidth() - this.thumbPadding) {
            this.thumbSliceLeftX = getWidth() - this.thumbPadding;
        }
        if (this.thumbSliceRightX > getWidth() - this.thumbPadding) {
            this.thumbSliceRightX = getWidth() - this.thumbPadding;
        }
        invalidate();
        if (this.scl != null) {
            calculateThumbValue();
            this.scl.seekBarValueChanged(this.thumbSliceLeftValue, this.thumbSliceRightValue);
        }
    }

    private void calculateThumbValue() {
        this.thumbSliceLeftValue = (this.maxValue * (this.thumbSliceLeftX - this.thumbPadding)) / (getWidth() - (this.thumbPadding * 2));
        this.thumbSliceRightValue = (this.maxValue * (this.thumbSliceRightX - this.thumbPadding)) / (getWidth() - (this.thumbPadding * 2));
    }

    private int calculateCorrds(long progress) {
        return ((int) (((((double) getWidth()) - (2.0d * ((double) this.thumbPadding))) / ((double) this.maxValue)) * ((double) progress))) + this.thumbPadding;
    }

    public void setLeftProgress(long progress) {
        if (progress < this.thumbSliceRightValue - this.progressMinDiff) {
            this.thumbSliceLeftX = calculateCorrds(progress);
        }
        notifySeekBarValueChanged();
    }

    public void setRightProgress(long progress) {
        if (progress > this.thumbSliceLeftValue + this.progressMinDiff) {
            this.thumbSliceRightX = calculateCorrds(progress);
        }
        notifySeekBarValueChanged();
    }

    public int getSelectedThumb() {
        return this.selectedThumb;
    }

    public long getLeftProgress() {
        return this.thumbSliceLeftValue;
    }

    public long getRightProgress() {
        return this.thumbSliceRightValue;
    }

    public void setProgress(int leftProgress, int rightProgress) {
        if (rightProgress - leftProgress > this.progressMinDiff) {
            this.thumbSliceLeftX = calculateCorrds(leftProgress);
            this.thumbSliceRightX = calculateCorrds(rightProgress);
        }
        notifySeekBarValueChanged();
    }

    public void videoPlayingProgress(long progress) {
        this.isVideoStatusDisplay = true;
        this.thumbCurrentVideoPositionX = calculateCorrds(progress);
        invalidate();
    }

    public void removeVideoStatusThumb() {
        this.isVideoStatusDisplay = false;
        invalidate();
    }

    public void setSliceBlocked(boolean isBLock) {
        this.blocked = isBLock;
        invalidate();
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public void setProgressMinDiff(int progressMinDiff) {
        this.progressMinDiff = progressMinDiff;
        this.progressMinDiffPixels = calculateCorrds(progressMinDiff);
    }
}
