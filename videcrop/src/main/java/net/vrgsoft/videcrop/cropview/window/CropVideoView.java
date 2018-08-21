package net.vrgsoft.videcrop.cropview.window;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import net.vrgsoft.videcrop.R;
import net.vrgsoft.videcrop.cropview.window.edge.Edge;

public class CropVideoView extends FrameLayout {
    private PlayerView mPlayerView;
    private CropView mCropView;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoRotationDegrees;
    private int mGuidelines = 1;
    private boolean mFixAspectRatio = false;
    private int mAspectRatioX = 1;
    private int mAspectRatioY = 1;

    public CropVideoView(Context context) {
        super(context);
        init(context);
    }

    public CropVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CropVideoView, 0, 0);

        try {
            mGuidelines = ta.getInteger(R.styleable.CropVideoView_guidelines, 1);
            mFixAspectRatio = ta.getBoolean(R.styleable.CropVideoView_fixAspectRatio, false);
            mAspectRatioX = ta.getInteger(R.styleable.CropVideoView_aspectRatioX, 1);
            mAspectRatioY = ta.getInteger(R.styleable.CropVideoView_aspectRatioY, 1);
        } finally {
            ta.recycle();
        }

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_crop, this, true);
        mPlayerView = v.findViewById(R.id.playerView);
        mCropView = v.findViewById(R.id.cropView);
        mCropView.setInitialAttributeValues(mGuidelines, mFixAspectRatio, mAspectRatioX, mAspectRatioY);
    }

    protected void onSizeChanged(int newWidth, int newHeight, int oldw, int oldh) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (mVideoRotationDegrees == 90 || mVideoRotationDegrees == 270) {
            if (mVideoWidth >= mVideoHeight) {
                lp.width = (int) (newHeight * (1.0f * mVideoHeight / mVideoWidth));
                lp.height = newHeight;
            } else {
                lp.width = newWidth;
                lp.height = (int) (newWidth * (1.0f * mVideoWidth / mVideoHeight));
            }
        } else {
            if (mVideoWidth >= mVideoHeight) {
                lp.width = newWidth;
                lp.height = (int) (newWidth * (1.0f * mVideoHeight / mVideoWidth));
            } else {
                lp.width = (int) (newHeight * (1.0f * mVideoWidth / mVideoHeight));
                lp.height = newHeight;
            }
        }

        setLayoutParams(lp);
        Rect rect = new Rect(0, 0, lp.width, lp.height);
        mCropView.setBitmapRect(rect);
        mCropView.resetCropOverlayView();
    }

    public void setPlayer(SimpleExoPlayer player) {
        mPlayerView.setPlayer(player);
        mCropView.resetCropOverlayView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPlayerView.setPlayer(null);
    }

    public Rect getCropRect() {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        Rect result = new Rect();

        if (mVideoRotationDegrees == 90 || mVideoRotationDegrees == 270) {
            if (mVideoRotationDegrees == 90) {
                result.left = mVideoWidth - (int) (bottom * mVideoWidth / getHeight());
                result.right = mVideoWidth - (int) (top * mVideoWidth / getHeight());
                result.top = (int) (left * mVideoHeight / getWidth());
                result.bottom = (int) (right * mVideoHeight / getWidth());
            } else {
                result.left = (int) (top * mVideoWidth / getHeight());
                result.right = (int) (bottom * mVideoWidth / getHeight());
                result.top = mVideoHeight - (int) (right * mVideoHeight / getWidth());
                result.bottom = mVideoHeight - (int) (left * mVideoHeight / getWidth());
            }
            int realRight = result.right;
            result.right = result.bottom - result.top;
            result.bottom = realRight - result.left;
        } else {
            result.left = (int) (left * mVideoWidth / getWidth());
            result.right = (int) (right * mVideoWidth / getWidth());
            result.top = (int) (top * mVideoHeight / getHeight());
            result.bottom = (int) (bottom * mVideoHeight / getHeight());

            result.right = result.right - result.left;
            result.bottom = result.bottom - result.top;
        }

        return result;
    }

    public void setFixedAspectRatio(boolean fixAspectRatio) {
        mCropView.setFixedAspectRatio(fixAspectRatio);
    }

    public void setAspectRatio(int aspectRatioX, int aspectRatioY) {
        mAspectRatioX = aspectRatioX;
        mAspectRatioY = aspectRatioY;
        mCropView.setAspectRatioX(this.mAspectRatioX);
        mCropView.setAspectRatioY(this.mAspectRatioY);
    }

    public void initBounds(int videoWidth, int videoHeight, int rotationDegrees) {
        mVideoWidth = videoWidth;
        mVideoHeight = videoHeight;
        mVideoRotationDegrees = rotationDegrees;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//
//    }
}
