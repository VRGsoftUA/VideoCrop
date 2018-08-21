package net.vrgsoft.videcrop.cropview.window.handle;

import android.graphics.Rect;

import net.vrgsoft.videcrop.cropview.util.AspectRatioUtil;
import net.vrgsoft.videcrop.cropview.window.edge.Edge;

class HorizontalHandleHelper extends HandleHelper {
    private Edge mEdge;

    HorizontalHandleHelper(Edge edge) {
        super(edge, null);
        this.mEdge = edge;
    }

    void updateCropWindow(float x, float y, float targetAspectRatio, Rect imageRect, float snapRadius) {
        this.mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        float targetWidth = AspectRatioUtil.calculateWidth(top, bottom, targetAspectRatio);
        float currentWidth = right - left;
        float difference = targetWidth - currentWidth;
        float halfDifference = difference / 2.0F;
        left -= halfDifference;
        right += halfDifference;
        Edge.LEFT.setCoordinate(left);
        Edge.RIGHT.setCoordinate(right);
        float offset;
        if (Edge.LEFT.isOutsideMargin(imageRect, snapRadius) && !this.mEdge.isNewRectangleOutOfBounds(Edge.LEFT, imageRect, targetAspectRatio)) {
            offset = Edge.LEFT.snapToRect(imageRect);
            Edge.RIGHT.offset(-offset);
            this.mEdge.adjustCoordinate(targetAspectRatio);
        }

        if (Edge.RIGHT.isOutsideMargin(imageRect, snapRadius) && !this.mEdge.isNewRectangleOutOfBounds(Edge.RIGHT, imageRect, targetAspectRatio)) {
            offset = Edge.RIGHT.snapToRect(imageRect);
            Edge.LEFT.offset(-offset);
            this.mEdge.adjustCoordinate(targetAspectRatio);
        }

    }
}

