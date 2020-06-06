package com.youngsoft.cookconverter.ui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.youngsoft.cookconverter.R;

public class WrapContentHeightViewPager extends ViewPager {

    public WrapContentHeightViewPager(@NonNull Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.i("WCHVP","onMeasure called");
        int height = 0;
        int tabHeight = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
            if(child.getId() == R.id.tl_baking_input || child.getId() == R.id.tl_baking_output) {
                //Log.i("WCHVP","i= " + i + " is TabLayout");
                tabHeight=h;
            }
            //Log.i("WCHVP","i=" + i + " of " + getChildCount() + " h=" + h + " of " + height + " child " + child);
        }

        if (height != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height+tabHeight, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
