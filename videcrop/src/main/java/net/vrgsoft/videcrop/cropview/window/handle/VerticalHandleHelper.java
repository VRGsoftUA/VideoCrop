package net.vrgsoft.videcrop.cropview.window.handle;

import android.graphics.Rect;

import net.vrgsoft.videcrop.cropview.util.AspectRatioUtil;
import net.vrgsoft.videcrop.cropview.window.edge.Edge;

class VerticalHandleHelper extends HandleHelper {
    private Edge mEdge;

    VerticalHandleHelper(Edge edge) {
        super(null, edge);
        this.mEdge = edge;
    }

    void updateCropWindow(float x, float y, float targetAspectRatio, Rect imageRect, float snapRadius) {
        this.mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        float targetHeight = AspectRatioUtil.calculateHeight(left, right, targetAspectRatio);
        float currentHeight = bottom - top;
        float difference = targetHeight - currentHeight;
        float halfDifference = difference / 2.0F;
        top -= halfDifference;
        bottom += halfDifference;
        Edge.TOP.setCoordinate(top);
        Edge.BOTTOM.setCoordinate(bottom);
        float offset;
        if (Edge.TOP.isOutsideMargin(imageRect, snapRadius) && !this.mEdge.isNewRectangleOutOfBounds(Edge.TOP, imageRect, targetAspectRatio)) {
            offset = Edge.TOP.snapToRect(imageRect);
            Edge.BOTTOM.offset(-offset);
            this.mEdge.adjustCoordinate(targetAspectRatio);
        }

        if (Edge.BOTTOM.isOutsideMargin(imageRect, snapRadius) && !this.mEdge.isNewRectangleOutOfBounds(Edge.BOTTOM, imageRect, targetAspectRatio)) {
            offset = Edge.BOTTOM.snapToRect(imageRect);
            Edge.TOP.offset(-offset);
            this.mEdge.adjustCoordinate(targetAspectRatio);
        }

    }
}

