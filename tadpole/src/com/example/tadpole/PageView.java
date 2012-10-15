package com.example.tadpole;

import org.tadpole.util.TadpoleLog;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;

public class PageView extends ViewGroup {

	public static final String tag = "ViewGroupTst";

	private Scroller mScroller;

	private TextView child = null;

	private int mPagingTouchSlop;

	public PageView(Context context) {
		super(context);

		this.setBackgroundColor(Color.YELLOW);
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		child = new TextView(context);
		child.setText("1111111111111");
		child.setBackgroundColor(Color.RED);
		child.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT));
		this.addView(child);

		Button tstButton = new Button(context);
		tstButton.setText("�������");

		this.addView(tstButton);
		mScroller = new Scroller(context, new DecelerateInterpolator());
		
		final ViewConfiguration conf = ViewConfiguration.get(context);
		// getScaledPagingTouchSlop() only available in API Level 8
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// TadpoleLog.d(tag,
		// "onMeasure widthMeasureSpec:%d, heightMeasureSpec%d",
		// widthMeasureSpec, heightMeasureSpec);
		// this.setMeasuredDimension(400, 1000);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		TadpoleLog.d(tag, "onSizeChanged");
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		TadpoleLog.d(tag, "onLayout");
		int childCount = this.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = this.getChildAt(i);
			setupView(child, i);
		}

	}

	private void setupView(View child, int position) {
		if (child == null) {
			return;
		}

		LayoutParams lp = child.getLayoutParams();
		if (lp == null) {
			lp = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
		}

		// Measure the view
		final int childWidthSpec = getChildMeasureSpec(
				MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
				0, lp.width);
		final int childHeightSpec = getChildMeasureSpec(
				MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY),
				0, lp.height);
		child.measure(childWidthSpec, childHeightSpec);

		// Layout the view
		final int childLeft = 0;
		child.layout(childLeft, 0, childLeft + child.getMeasuredWidth(),
				child.getMeasuredHeight());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		TadpoleLog.d(tag, "onInterceptTouchEvent");
		 /*
         * Shortcut the most recurring case: the user is in the dragging state
         * and he is moving his finger. We want to intercept this motion.
         */
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_MOVE && mIsBeingDragged) {
            return true;
        }

        final int x = (int) ev.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartMotionX = x;
                /*
                 * If currently scrolling and user touches the screen, initiate
                 * drag; otherwise don't. mScroller.isFinished should be false
                 * when being flinged.
                 */
                mIsBeingDragged = !mScroller.isFinished();
                if (mIsBeingDragged) {
                    mScroller.forceFinished(true);
//                    mHandler.removeCallbacks(mScrollerRunnable);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                /*
                 * mIsBeingDragged == false, otherwise the shortcut would have
                 * caught it. Check whether the user has moved far enough from
                 * his original down touch.
                 */

                final int xDiff = (int) Math.abs(x - mStartMotionX);
                if (xDiff > mPagingTouchSlop) {
                    mIsBeingDragged = true;
                    performStartTracking(x);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /*
                 * Release the drag
                 */
                mIsBeingDragged = false;
                break;
        }

        /*
         * Motion events are only intercepted during dragging mode.
         */
        return mIsBeingDragged;
	}

	
	private void performStartTracking(int startMotionX) {
//        if (mOnPageChangeListener != null) {
//            mOnPageChangeListener.onStartTracking(this);
//        }
//        mStartMotionX = startMotionX;
//        mStartOffsetX = mOffsetX;
    }
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
		}
	}

	// 开始按下的坐标
	private int mStartMotionX = 0;

	// 视图偏移
	private int mOffsetX = 0;

	private int mStartOffsetX;

	private boolean mIsBeingDragged = false;

	public int getOffsetForPage() {
		WindowManager wm = (WindowManager) this.getContext().getSystemService(
				Service.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	public boolean onTouchEvent(MotionEvent ev) {
		TadpoleLog.d(tag, "onTouchEvent");
		TadpoleLog.d(tag, "onTouchEvent mIsBeingDragged = %b", mIsBeingDragged);

		int action = ev.getAction();
		final int x = (int) ev.getX();
		final int y = (int) ev.getY();

		TadpoleLog.d(tag, "x = %d & y = %d", x, y);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			performStartTracking(x);
			break;
		case MotionEvent.ACTION_MOVE:

			// Scroll to follow the motion event
			final int newOffset = mStartOffsetX - (mStartMotionX - x);

			TadpoleLog.d(tag, "mStartMotionX = %d, newOffset = %d",
					mStartMotionX, newOffset);

			// || newOffset < getOffsetForPage(mPageCount - 1)
			if (newOffset > 0 || newOffset < getOffsetForPage()) {
				mStartOffsetX = mOffsetX;
				mStartMotionX = x;
				setChildOffset(newOffset);
			} else {
				setChildOffset(newOffset);
			}
			break;
		case MotionEvent.ACTION_UP:
			mIsBeingDragged = false;
			break;

		case MotionEvent.ACTION_CANCEL:
			mIsBeingDragged = false;
			break;
		default:
			break;
		}

		invalidate();

		return true;
	}

	public void setChildOffset(int offset) {
		child.offsetLeftAndRight(offset);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		TadpoleLog.d(tag, "onSaveInstanceState");
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		TadpoleLog.d(tag, "onRestoreInstanceState");
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
	}

	static class SavedState extends BaseSavedState {

		int currentPage;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPage = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(currentPage);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}
