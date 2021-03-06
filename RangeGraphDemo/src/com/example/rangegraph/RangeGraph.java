package com.example.rangegraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Draw a Range Graph to show if the current value is in range or not
 * Example AsciiArt:<pre>
 *       | T |
 *       | T |
 * 140 | M |
 *        | M |
 * 120 | B |
 *        | B |
 * </pre>
 * The region T will be red if the value is too high, blank otherwise
 * The region M will be red if the value is out of range, green otherwise
 * The region B will be red if the value is too low, green otherwise
 * @author Ian Darwin
 *
 */
public class RangeGraph extends View {

	private static final String TAG = "RangeGraph";
	
	// State
	private int mMin, mMax;
	private int mValue;
	
	// Graphic
	private int mFontSize = 16;
	private Paint mPaint;
    
	// Layout
	private int mWidth = 50;
	private int mHeight = 200;

	public RangeGraph(Context context) {
		super(context);
		commonSetup();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public RangeGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		// XXX deal with AttributedSet here
		commonSetup();
	}
	
	private void commonSetup() {
		// for now
		mMin = 140;
		mMax = 160;
		mValue = 132;
		
		// for now
		mWidth = 200;
		mHeight = 300;

		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		// Scale the desired text size to match screen density
        mPaint.setTextSize(mFontSize * getResources().getDisplayMetrics().density);
		mPaint.setStrokeWidth(2f);
        setPadding(5, 5, 5, 5);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mWidth, mHeight);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// In case you forget, Android drawing co-ordinates:
		// mPaint.setColor(Color.MAGENTA);
		// canvas.drawLine(120, 200, 160, 240, mPaint);
		// Draws a line that leads downward to the right.
		
		// Label it
		int oneThirdHeight = getPaddingTop() + (2*mHeight/3);
        int twoThirdsHeight = getPaddingTop() + (mHeight/3);
        
        canvas.drawText(Integer.toString(mMax), 
        		getPaddingLeft(), twoThirdsHeight, mPaint);
		canvas.drawText(Integer.toString(mMin), 
        		getPaddingLeft(), oneThirdHeight, mPaint);
        
        // Draw the uprights, at 1/2 and 2/3 of the width
        int top = getPaddingTop();
        int bot = mHeight - getPaddingBottom();
        int leftSide = mWidth/2;
        int rightSide = (int) (mWidth*0.66f);
        canvas.drawLine(leftSide, top, leftSide, bot, mPaint);
		canvas.drawLine(rightSide, top, rightSide, bot, mPaint);
        
		// Now draw the bar graph.
		// The distance from min to max fills the middle third of the graph
		int oneThirdValue = mMax - mMin;
		int valueRange = oneThirdValue * 3;
		int valueAtBottom = mMin - oneThirdValue;
		int visibleValue = Math.max(0, mValue - valueAtBottom);
		int barHeight = Math.min(mHeight, (int) (mHeight * (1f * visibleValue / valueRange)));
		mPaint.setColor(isInRange() ? Color.GREEN : Color.RED);
		Log.d(TAG,
			String.format("drawRect(%d %d %d %d)",
					leftSide, mHeight - barHeight, rightSide, bot));
		canvas.drawRect(leftSide, mHeight - barHeight, rightSide, bot, mPaint);
	}

	public boolean isInRange() {
		return mValue >= mMin && mValue <= mMax;
	}
	
	// Simple accessors
	
	public int getMin() {
		return mMin;
	}
	public void setMin(int min) {
		this.mMin = min;
	}
	public int getMax() {
		return mMax;
	}
	public void setMax(int max) {
		this.mMax = max;
	}
	public int getValue() {
		return mValue;
	}
	public void setValue(int value) {
		this.mValue = value;
	}
	
}
