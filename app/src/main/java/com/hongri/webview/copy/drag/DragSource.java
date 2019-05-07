package com.hongri.webview.copy.drag;

import android.view.View;

/**
 * Interface defining an object where drag operations originate.
 *
 */
public interface DragSource {

    /**
     * This method is called to determine if the DragSource has something to drag.
     * 
     * @return True if there is something to drag
     */

    boolean allowDrag();

    /**
     * This method is used to tell the DragSource which drag controller it is working with.
     * 
     * @param dragger DragController
     */

    void setDragController(DragController dragger);

    /**
     * This method is called on the completion of the drag operation so the DragSource knows 
     * whether it succeeded or failed.
     * 
     * @param target View - the view that accepted the dragged object
     * @param success boolean - true means that the object was dropped successfully
     */

    void onDropCompleted(View target, boolean success);
}
