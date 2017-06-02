package kr.or.dgit.bigdata.jollygo.jollygo_v01.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
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
        final TextView tvTitle = (TextView) v;
        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                int searchCount = getAdapter().getItemCount();
                if (tvTitle.getText().toString().equals("")){
                    return;
                }else{
                    if (searchCount>1) {
                        tvTitle.setText(searchCount + " items were ready");
                    }else if (searchCount == 1){
                        tvTitle.setText(searchCount + " item was ready");
                    }else if (searchCount == 0 ){
                        tvTitle.setText("What a lot of chefs in the world");
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int searchCount = getAdapter().getItemCount();
                if (tvTitle.getText().toString().equals("")){
                    return;
                }else{
                    if (searchCount>1) {
                        tvTitle.setText(searchCount + " items were ready");
                    }else if (searchCount == 1){
                        tvTitle.setText(searchCount + " item was ready");
                    }else if (searchCount == 0){
                        tvTitle.setText("What a lot of chefs in the world");
                    }
                }
            }
        });
    }
}
