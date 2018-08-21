package net.vrgsoft.videcrop.cropview.window.handle;

import android.graphics.Rect;

import net.vrgsoft.videcrop.cropview.util.AspectRatioUtil;
import net.vrgsoft.videcrop.cropview.window.edge.Edge;
import net.vrgsoft.videcrop.cropview.window.edge.EdgePair;

abstract class HandleHelper {
    private static final float UNFIXED_ASPECT_RATIO_CONSTANT = 1.0F;
    private Edge mHorizontalEdge;
    private Edge mVerticalEdge;
    private EdgePair mActiveEdges;

    HandleHelper(Edge horizontalEdge, Edge verticalEdge) {
        this.mHorizontalEdge = horizontalEdge;
        this.mVerticalEdge = verticalEdge;
        this.mActiveEdges = new EdgePair(this.mHorizontalEdge, this.mVerticalEdge);
    }

    void updateCropWindow(float x, float y, Rect imageRect, float snapRadius) {
        EdgePair activeEdges = this.getActiveEdges();
        Edge primaryEdge = activeEdges.primary;
        Edge secondaryEdge = activeEdges.secondary;
        if (primaryEdge != null) {
            primaryEdge.adjustCoordinate(x, y, imageRect, snapRadius, 1.0F);
        }

        if (secondaryEdge != null) {
            secondaryEdge.adjustCoordinate(x, y, imageRect, snapRadius, 1.0F);
        }

    }

    abstract void updateCropWindow(float var1, float var2, float var3, Rect var4, float var5);

    EdgePair getActiveEdges() {
        return this.mActiveEdges;
    }

    EdgePair getActiveEdges(float x, float y, float targetAspectRatio) {
        float potentialAspectRatio = this.getAspectRatio(x, y);
        if (potentialAspectRatio > targetAspectRatio) {
            this.mActiveEdges.primary = this.mVerticalEdge;
            this.mActiveEdges.secondary = this.mHorizontalEdge;
        } else {
            this.mActiveEdges.primary = this.mHorizontalEdge;
            this.mActiveEdges.secondary = this.mVerticalEdge;
        }

        return this.mActiveEdges;
    }

    private float getAspectRatio(float x, float y) {
        float left = this.mVerticalEdge == Edge.LEFT ? x : Edge.LEFT.getCoordinate();
        float top = this.mHorizontalEdge == Edge.TOP ? y : Edge.TOP.getCoordinate();
        float right = this.mVerticalEdge == Edge.RIGHT ? x : Edge.RIGHT.getCoordinate();
        float bottom = this.mHorizontalEdge == Edge.BOTTOM ? y : Edge.BOTTOM.getCoordinate();
        float aspectRatio = AspectRatioUtil.calculateAspectRatio(left, top, right, bottom);
        return aspectRatio;
    }
}

