package manu.apps.prochama.adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import manu.apps.prochama.enumarators.SwipeDirections;

public class SwipeViewPagerAdapter extends ViewPager {

    private float initialXValue;
    private SwipeDirections directions;

    public SwipeViewPagerAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.directions = SwipeDirections.all;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if(this.directions == SwipeDirections.all) return true;

        if(directions == SwipeDirections.none )//disable any swipe
            return false;

        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && directions == SwipeDirections.right ) {
                    // swipe from left to right detected
                    return false;
                }else if (diffX < 0 && directions == SwipeDirections.left ) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirections direction) {
        this.directions = direction;
    }
}
