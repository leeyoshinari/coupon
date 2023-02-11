package com.example.coupon.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout {
    public CustomLinearLayout(Context context) {
        this(context, null);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getViewWidth(View view) {
        int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        return view.getMeasuredWidth();
    }

    private int getViewHeight(View view) {
        int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        return view.getMeasuredHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int count = getChildCount();
        int minHeight = 999;
        int measureHeight = 0;
        int lastRight = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                int childLong = lastRight + layoutParams.getMarginStart() + getViewWidth(child) + layoutParams.getMarginEnd();
                if (childLong > getMeasuredWidth()) {
                    measureHeight = measureHeight + minHeight + getPaddingBottom();
                    lastRight = layoutParams.getMarginStart() + child.getMeasuredWidth() + layoutParams.getMarginEnd();
                    int compareHeight = layoutParams.topMargin + child.getMeasuredHeight() + layoutParams.bottomMargin;
                    minHeight = Math.min(minHeight, compareHeight);
                } else {
                    int compareHeight = layoutParams.topMargin + child.getMeasuredHeight() + layoutParams.bottomMargin;
                    minHeight = Math.min(minHeight, compareHeight);
                    lastRight = lastRight + layoutParams.getMarginStart() + child.getMeasuredWidth() + layoutParams.getMarginEnd();
                }
            }
        }
        if (measureHeight != 0) {
            measureHeight += minHeight + getPaddingBottom() + getPaddingTop();
        } else {
            measureHeight += minHeight + getPaddingBottom() + getPaddingTop();
        }
        setMeasuredDimension(getMeasuredWidth(), measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int lastRight = 0;
        int lastBottom = getPaddingTop();
        int left, top, right, bottom;
        int tempHeight = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                int childLong = lastRight + layoutParams.getMarginStart() + getViewWidth(child) + layoutParams.getMarginEnd();
                if (childLong > getMeasuredWidth()) {
                    lastBottom = lastBottom + tempHeight + getPaddingBottom();
                    tempHeight = 0;
                    left = layoutParams.getMarginStart();
                    top = lastBottom + layoutParams.topMargin;
                    right = layoutParams.getMarginStart() + child.getMeasuredWidth();
                    bottom = lastBottom + layoutParams.topMargin + child.getMeasuredHeight();
                    lastRight = layoutParams.getMarginStart() + child.getMeasuredWidth() + layoutParams.getMarginEnd();

                    int compareHeight = layoutParams.topMargin + child.getMeasuredHeight() + layoutParams.bottomMargin;
                    tempHeight = Math.max(compareHeight, tempHeight);
                } else {
                    left = lastRight + layoutParams.getMarginStart();
                    top = lastBottom + layoutParams.topMargin;
                    right = lastRight + layoutParams.getMarginStart() + child.getMeasuredWidth();
                    bottom = lastBottom + layoutParams.topMargin + child.getMeasuredHeight();
                    lastRight = lastRight + layoutParams.getMarginStart() + child.getMeasuredWidth() + layoutParams.getMarginEnd();

                    int compareHeight = layoutParams.topMargin + child.getMeasuredHeight() + layoutParams.bottomMargin;
                    tempHeight = Math.max(compareHeight, tempHeight);
                }
                child.layout(left, top, right, bottom);
            }
        }
    }
}
