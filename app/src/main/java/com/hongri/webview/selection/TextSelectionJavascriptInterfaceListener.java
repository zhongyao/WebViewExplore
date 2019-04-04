package com.hongri.webview.selection;

/**
 * @author hongri
 */
public interface TextSelectionJavascriptInterfaceListener {

	/**
	 * Informs the listener that there was a javascript error.
	 * @param error
	 */
	void JSError(String error);
	
	
	/**
	 * The user has started dragging the selection handles.
	 */
	void StartSelectionMode();
	
	/**
	 * The user has stopped dragging the selection handles.
	 */
	void EndSelectionMode();
	
	/**
	 * Tells the listener to show the context menu for the given range and selected text.
	 * The bounds parameter contains a json string representing the selection bounds in the form 
	 * { 'left': leftPoint, 'top': topPoint, 'right': rightPoint, 'bottom': bottomPoint }
	 * @param range
	 * @param text
	 * @param handleBounds
	 * @param menuBounds
	 */
	void SelectionChanged(String range, String text, String handleBounds, String menuBounds);
	
	/**
	 * Sends the content width to the listener.  
	 * Necessary because Android web views don't allow you to get the content width.
	 * @param contentWidth
	 */
	void SetContentWidth(float contentWidth);
}
