package com.mpos.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DoubleRelative extends RelativeLayout {

	GestureDetector gestureDetector;

	public DoubleRelative(Context context) {
		super(context);

		gestureDetector = new GestureDetector(context, new GestureListener());
		// TODO Auto-generated constructor stub
	}

	public DoubleRelative(Context context, AttributeSet attrs) {
		super(context, attrs);
		// creating new gesture detector
		gestureDetector = new GestureDetector(context, new GestureListener());
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return gestureDetector.onTouchEvent(e);
	}

	private class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		// event when double tap occurs
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			float x = e.getX();
			float y = e.getY();

			Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
			
			// Just Add any events that wants to be displayed.

			return true;
		}
	}

}
