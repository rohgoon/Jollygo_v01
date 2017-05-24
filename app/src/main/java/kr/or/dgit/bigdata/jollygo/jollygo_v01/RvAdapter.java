package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by NCG on 2017-05-23.
 */

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
    private List<String> mDataset;
    private static int lastPosition = -1;
    private Context context;
    private int longClickDuration = 3000;
    private boolean isLongPress = false;



    public RvAdapter(List<String> myDataset, Context mContext) {
        mDataset = myDataset;
        context = mContext;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCard;
        TextView tvTest;
        CardView cv;
        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v;
            ivCard = (ImageView) v.findViewById(R.id.ivCard);
            tvTest = (TextView) v.findViewById(R.id.tvTest);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {//이벤트 처리


        setAnimationFadeIn(holder.cv, position);
        holder.ivCard.setImageResource(R.drawable.jg_icon);
        holder.tvTest.setText(mDataset.get(position));



       holder.ivCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mDataset.remove(position);
                    notifyDataSetChanged();
                    return true;
                }
       });


    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset == null){
            return 0;
        }else{
            return mDataset.size();
        }

    }
    private void setAnimationFadeIn(View viewToAnimate, int position){

        if (position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
   /* private void setAnimationFadeOut(View viewToAnimate, int position){

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
            animation.setDuration(2000);
            viewToAnimate.startAnimation(animation);

            lastPosition = position;
        }
    }*/

}
