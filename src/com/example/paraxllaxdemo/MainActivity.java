package com.example.paraxllaxdemo;

import com.example.paraxllaxdemo.ui.ParallaxScrollView;
import com.example.paraxllaxdemo.ui.ParallaxScrollView.OnScrollViewSizeChangeListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected static final String TAG = MainActivity.class.getSimpleName();

	// 可滚动的ScrollViw
	private ParallaxScrollView parallaxScrollView;
	// 头部ActionBar
	private RelativeLayout rl_header;
	private Drawable draw_header_default_back;
	//
	private LinearLayout ll_inside_header;
	private ImageView iv;
	private Bitmap back_bitmap;
	private TextView tv_title;
	private ViewPager viewPager;

	private HorizontalScrollView horizontalScrollView;
	private TextView tv1, tv2, tv3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		parallaxScrollView = (ParallaxScrollView) findViewById(R.id.in_swipelayout_view)
				.findViewById(R.id.parallaxScrollView);
		rl_header = (RelativeLayout) findViewById(R.id.rl_header);
		draw_header_default_back = rl_header.getBackground();
		ll_inside_header = (LinearLayout) findViewById(R.id.ll_inside_header);
		iv = (ImageView) findViewById(R.id.iv);
		tv_title = (TextView) findViewById(R.id.tv_title);
		back_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		viewPager = (ViewPager) findViewById(R.id.in_swipelayout_view).findViewById(R.id.viewPager);

		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public int getCount() {
				return 10;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				View view = (View) object;
				container.removeView(view);
			}

			@Override
			public Object instantiateItem(ViewGroup container, final int position) {
				ImageView view = new ImageView(MainActivity.this);
				view.setImageBitmap(BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.timg));
				
				TextView tv = new TextView(MainActivity.this);
				tv.setText("hehe");
				container.addView(tv);
				return tv;
			}
		});

		WindowManager wm = getWindowManager();
		final int width = wm.getDefaultDisplay().getWidth();
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv1.setWidth(width / 2);
		tv2.setWidth(width / 2);
		tv3.setWidth(width / 2);

		parallaxScrollView.setListener(new OnScrollViewSizeChangeListener() {

			@Override
			public void onScrollViewSizeChange(int t, int totalHeight) {

				if (t == 0) {
					rl_header.setBackgroundDrawable(draw_header_default_back);
				} else {
					rl_header.setBackgroundDrawable(new ColorDrawable(0xff34C15F));
				}

				float alpha = (totalHeight - t) * 0xff / totalHeight;
				if (alpha > 0) {
					ll_inside_header.setAlpha(alpha);
					BitmapDrawable bd = new BitmapDrawable(back_bitmap);
					bd.setAlpha((int) alpha);
					iv.setBackgroundDrawable(bd);
					tv_title.setTextColor(Color.argb((int) alpha, 0, 0, 0));
				}
				Log.e(TAG, TAG + " alpha:" + alpha);
			}
		});

		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		horizontalScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.e(TAG, TAG + " MotionEvent.ACTION_DOWN:");
					break;
				case MotionEvent.ACTION_MOVE:
//					Log.e(TAG, TAG + " MotionEvent.ACTION_MOVE:");
					break;
				case MotionEvent.ACTION_UP:
					Log.e(TAG, TAG + " MotionEvent.ACTION_UP:");
					int[] r = new int[4];
					horizontalScrollView.getLocationInWindow(r);
					int left = horizontalScrollView.getScrollX();
					int w = width / 2;
//					for(int i = 0; i < r.length; i++){
//						Log.e(TAG, TAG+" left:"+left +" w/2:"+ w/2);
//					}
					
					
					if(left < w/2 ){
						Log.e(TAG, TAG+" left:"+left +" w/2:"+ w/2 +" left < w/2");
						horizontalScrollView.smoothScrollTo(0, r[1]);
						return true;
					}else if(left < w){
						Log.e(TAG, TAG+" left:"+left +" w:"+ w +" left < w");
						horizontalScrollView.smoothScrollTo(w, r[1]);
						return true;
					}
					

					break;
				case MotionEvent.ACTION_CANCEL:
					Log.e(TAG, TAG + " MotionEvent.ACTION_CANCEL:");
					break;
				}
				return false;
			}

		});
	}

}
