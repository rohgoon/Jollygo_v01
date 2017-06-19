package kr.or.dgit.bigdata.jollygo.jollygo_v01.customviews;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgWords;

/**
 * Created by rohgoon on 2017-05-29.
 */

public class GridRecyclerView extends RecyclerView {
    private ImgWords imgWords;
    private Context context;

    public GridRecyclerView(Context context) {
        super(context);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void gridChangeListener(View v){
        final FloatingActionButton fab = (FloatingActionButton) v;
        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),Color.rgb(255,150,0), Color.rgb(228,0,110),Color.rgb(5,50,250), Color.rgb(21,156,3),  Color.rgb(255,150,0));
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if(getAdapter().getClass().getName().equals("kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter.RvAdapter")) {
                    int searchCount = getAdapter().getItemCount();

                    if (searchCount > 0) {
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                int color = (int) animator.getAnimatedValue();
                                fab.setBackgroundTintList(ColorStateList.valueOf(color));
                            }
                        });
                        colorAnimation.setRepeatCount(Animation.INFINITE);
                        colorAnimation.setDuration(2000);
                        colorAnimation.start();
                    } else {
                        colorAnimation.cancel();
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 150, 0)));
                    }
                }else{
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 150, 0)));
                }

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                colorAnimation.cancel();
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,150,0)));

            }
        });
    }
}
