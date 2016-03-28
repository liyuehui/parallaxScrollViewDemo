package com.example.paraxllaxdemo.ui;

import com.example.paraxllaxdemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class ParallaxScrollView extends ScrollView {

	private static final String TAg = ParallaxScrollView.class.getSimpleName();

	private RelativeLayout ll_header;
	private int mHeaderHeight;
	private int touchSlop;

	public ParallaxScrollView(Context context) {
		super(context);
	}

	public ParallaxScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		ll_header = (RelativeLayout) findViewById(R.id.rl_first_part);
		touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mHeaderHeight = ll_header.getMeasuredHeight();
				Log.e(TAg, TAg + " mHeaderHeight:" + mHeaderHeight);

			}
		});
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

//		if (t > touchSlop *2 ) {
			ll_header.setTranslationY(t / 2);
			if(listener != null){
				listener.onScrollViewSizeChange(t,mHeaderHeight);
			}
//			Log.e(TAg, TAg + " onScrollChanged t:" + t + " size:" + size + " touchSlop:" + touchSlop);
//		}
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

		if (deltaY < 0 && isTouchEvent && Math.abs(deltaY) > touchSlop) {
			int h = ll_header.getMeasuredHeight() + Math.abs(deltaY / 3);
			if (h > mHeaderHeight + 500)
				h = mHeaderHeight + 500;
			ll_header.getLayoutParams().height = h;
			ll_header.requestLayout();
			Log.e(TAg, " overScrollBy deltaYt:" + deltaY + " isTouchEvent:" + isTouchEvent);

		}

		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX,
				maxOverScrollY, isTouchEvent);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:

			if (ll_header.getMeasuredHeight() > mHeaderHeight) {
				ResetAnimation ani = new ResetAnimation();
				startAnimation(ani);
			}

			break;

		}
		return super.onTouchEvent(ev);
	}

	
	public interface OnScrollViewSizeChangeListener{
		public void onScrollViewSizeChange(int t,int totalHeight);
	}
	
	
	private OnScrollViewSizeChangeListener listener;
	
	public void setListener(OnScrollViewSizeChangeListener listener){
		this.listener = listener;
	}
	
	/**
	 * 
	 * @author LYH
	 *
	 */
	class ResetAnimation extends Animation {

		public ResetAnimation() {
			setDuration(500);
			setInterpolator(new OvershootInterpolator());
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);

			int h = (int) (ll_header.getMeasuredHeight()
					+ interpolatedTime * (mHeaderHeight - ll_header.getMeasuredHeight()));
			ll_header.getLayoutParams().height = h;
			ll_header.requestLayout();
		}

	}
}
