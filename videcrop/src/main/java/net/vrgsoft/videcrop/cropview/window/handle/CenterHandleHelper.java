package net.vrgsoft.videcrop.cropview.window.handle;

import android.graphics.Rect;

import net.vrgsoft.videcrop.cropview.window.edge.Edge;

class CenterHandleHelper extends HandleHelper {
    CenterHandleHelper() {
        super(null, null);
    }

    void updateCropWindow(float x, float y, Rect imageRect, float snapRadius) {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        float currentCenterX = (left + right) / 2.0F;
        float currentCenterY = (top + bottom) / 2.0F;
        float offsetX = x - currentCenterX;
        float offsetY = y - currentCenterY;
        Edge.LEFT.offset(offsetX);
        Edge.TOP.offset(offsetY);
        Edge.RIGHT.offset(offsetX);
        Edge.BOTTOM.offset(offsetY);
        float offset;
        if (Edge.LEFT.isOutsideMargin(imageRect, snapRadius)) {
            offset = Edge.LEFT.snapToRect(imageRect);
            Edge.RIGHT.offset(offset);
        } else if (Edge.RIGHT.isOutsideMargin(imageRect, snapRadius)) {
            offset = Edge.RIGHT.snapToRect(imageRect);
            Edge.LEFT.offset(offset);
        }

        if (Edge.TOP.isOutsideMargin(imageRect, snapRadius)) {
            offset = Edge.TOP.snapToRect(imageRect);
            Edge.BOTTOM.offset(offset);
        } else if (Edge.BOTTOM.isOutsideMargin(imageRect, snapRadius)) {
            offset = Edge.BOTTOM.snapToRect(imageRect);
            Edge.TOP.offset(offset);
        }

    }

    void updateCropWindow(float x, float y, float targetAspectRatio, Rect imageRect, float snapRadius) {
        this.updateCropWindow(x, y, imageRect, snapRadius);
    }
}

