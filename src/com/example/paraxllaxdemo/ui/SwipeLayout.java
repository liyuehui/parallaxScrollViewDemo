package com.example.paraxllaxdemo.ui;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SwipeLayout extends FrameLayout {

	protected static final String TAG = SwipeLayout.class.getSimpleName();

	private static final int START_SLIDE = 0;
	public static final int SLIDING = 1;
	private static final int STOP_SLIDE = 2;

	private static final int Y_NOT_SLIDE = 3;
	private static final int Y_SLIDE_OK = 4;
	private LinearLayout ll_left;
	private LinearLayout ll_right;

	private int mLeftWidth;
	private int mRightWidth;
	private int mHeight;

	public int curState = STOP_SLIDE;
	// private int curStateY = Y_SLIDE_OK;

	// private ViewDragHelper viewDragHelper;
	private ScrollView svscrollView;

	public SwipeLayout(Context context) {
		this(context, null);
	}

	public SwipeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		//// viewDragHelper = ViewDragHelper.create(this, new
		//// ViewDragHelper.Callback() {
		//
		// @Override
		// public boolean tryCaptureView(View child, int pointerId) {
		// return true;
		// }
		//
		// @Override
		// public int clampViewPositionHorizontal(View child, int left, int dx)
		//// {
		//
		// if(child == ll_left){
		// if(left > 0)return 0;
		// }else if(left < -mRightWidth){
		// return -mRightWidth;
		// }
		//
		// return left;
		// }
		//
		//
		// });
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// return viewDragHelper.shouldInterceptTouchEvent(ev);
	// }

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		ll_left = (LinearLayout) getChildAt(0);
		ll_right = (LinearLayout) getChildAt(1);

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mLeftWidth = ll_left.getMeasuredWidth();
				mRightWidth = ll_right.getMeasuredWidth();
				mHeight = ll_right.getMeasuredHeight();
				Log.e(TAG, TAG + " mLeftWidth:" + mLeftWidth + " mRightWidth:" + mRightWidth);

			}

		});
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		ll_left.layout(0, 0, mLeftWidth, mHeight);
		ll_right.layout(mLeftWidth, 0, mLeftWidth + mRightWidth, mHeight);

		svscrollView = (ScrollView) this.getParent().getParent().getParent();
	}

	float downX = 0;
	float downY = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// viewDragHelper.processTouchEvent(event);

		float deltaX = 0;
		float deltaY = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getRawX();
			downY = event.getRawY();
			curState = START_SLIDE;
			
			Log.e(TAG, TAG + " downX:" + downX + " downY:" + downY);
			break;
		case MotionEvent.ACTION_MOVE:

			deltaX = event.getRawX() - downX;
			deltaY = event.getRawY() - downY;
			if (Math.abs(deltaX) != 0) {
				curState = SLIDING;
				int left = ll_left.getLeft();
				left += deltaX;
				Log.e(TAG, TAG + " deltaX:" + deltaX + " left:" + left);

				if (left < 0) {
					ll_left.layout(left, 0, left + mLeftWidth, mHeight);
					ll_right.offsetLeftAndRight((int) deltaX);
				}
				downX = event.getRawX();
				downY = event.getRawY();
			} else {
				curState = STOP_SLIDE;
				canY();
			}

			Log.e(TAG, TAG + " deltaX:" + deltaX + " delTaY:" + deltaY + " curState:" + curState);
			// deltaX:"+deltaX);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			Log.e(TAG, TAG + " MotionEvent.ACTION_UP " +" ll_left.getLeft() :"
		+ll_left.getLeft() +" -mRightWidth / 2:"+-mRightWidth / 2);
			if (ll_left.getLeft() < -mRightWidth / 2) {
				
				ll_left.layout(-mRightWidth, 0, mLeftWidth - mRightWidth, mHeight);
				ll_right.layout(mLeftWidth - mRightWidth, 0, 0, mHeight);
				Log.e(TAG, TAG + " ll_left open");
			} else {
				ll_left.layout(0, 0, mLeftWidth, mHeight);
				ll_right.layout(mLeftWidth, 0, mLeftWidth + mRightWidth, mHeight);
				Log.e(TAG, TAG + " ll_left close");
			}
			

			curState = STOP_SLIDE;
			canY();
			downX = 0;
			downY = 0;
			break;
		
//			curState = STOP_SLIDE;
//			canY();
//			Log.e(TAG, TAG + " ACTION_CANCEL");
//			break;
		

		}

		if (curState == SLIDING) {
			doNotY();
			return true;
		} else if (curState == START_SLIDE) {
			doNotY();
			return true;
		}

		return false;
	}

	private void doNotY() {
		svscrollView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
	}
	
	private void canY(){
		svscrollView.setOnTouchListener(null);
	}
	
    /*<com.example.paraxllaxdemo.ui.SwipeLayout
        android:id = "@+id/swipeLayout"
        android:layout_width="match_parent"
        android:layout_height="250dp">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#f00" >
        </LinearLayout>
         <LinearLayout
            android:layout_width="350dp"
            android:layout_height="match_parent"
            android:background="#0ff" >
        </LinearLayout>
    </com.example.paraxllaxdemo.ui.SwipeLayout>*/

}
