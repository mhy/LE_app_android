package com.mhy.lefinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class TabStripView extends SurfaceView {
	
	private Paint mRect;
	private float x1;
	private float x2;
		 
	public TabStripView(Context context, AttributeSet attrs, int defStyle) {
		 super(context, attrs, defStyle);
		 init();
	}
	 
	public TabStripView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 init();
	}
	
	public TabStripView(Context context) {
		super(context);
		 init();
	}
	
	public void setX1(float value){
		x1 = value;
	}
	
	public void setX2(float value){
		x2 = value;
	}

	private void init(){
		mRect = new Paint();
		x1 = 0;
		x2 = 0;
		mRect.setAntiAlias(true);
		mRect.setStyle(Style.FILL);
		mRect.setColor(Color.argb(255, 227, 0, 0));
		setWillNotDraw(false);
	}
	
	public void setStrip(float lineLeft, float lineRight){
		this.x1 = lineLeft;
		this.x2 = lineRight;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(x1, 0, x2, 10, mRect);

	}
	
	

}
