package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by NCG on 2017-05-23.
 */

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
    private List<String> mDataset;
    private static int lastPosition = -1;
    private Context context;
    private static FloatingActionButton floatingActionButton;
    private static int clickIndex;

    public RvAdapter(List<String> myDataset, Context mContext) {
        mDataset = myDataset;
        context = mContext;

    }

    public RvAdapter(List<String> mDataset, Context context, FloatingActionButton floatingActionButton) {
        this.mDataset = mDataset;
        this.context = context;
        this.floatingActionButton = floatingActionButton;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCard;
        TextView tvTest;
        CardView cv;
        RelativeLayout rl;
        FloatingActionButton fab; //플로팅버튼 받아오기

        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v;
            ivCard = (ImageView) v.findViewById(R.id.ivCard);
            tvTest = (TextView) v.findViewById(R.id.tvTest);
            rl = (RelativeLayout) v.findViewById(R.id.card_layout);
            fab = floatingActionButton;
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {//이벤트 처리

        setAnimationFadeIn(holder.cv, position);
        holder.ivCard.setImageResource(R.drawable.jg_icon);
        holder.tvTest.setText(mDataset.get(position));


       holder.ivCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipData clip = ClipData.newPlainText("","");
                    v.startDrag(clip, new View.DragShadowBuilder(v),null, 0);
                    Toast.makeText(context,position+"번 클릭",Toast.LENGTH_SHORT).show();
                    clickIndex = position;
                    holder.fab.setImageResource(R.drawable.ic_delete);
                    return true;
                   /* mDataset.remove(position);

                    notifyDataSetChanged();
                    return true;*/
                }
       });
        //프롤팅버튼에 드랍 이벤트 주기
        View.OnDragListener fabDragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                FloatingActionButton fb = null;
                if(v instanceof FloatingActionButton){
                    fb= (FloatingActionButton) v;
                }
                switch (event.getAction()){
                    case DragEvent.ACTION_DROP:
                        fb.setImageResource(R.drawable.ic_action_name);
                        mDataset.remove(clickIndex);
                        notifyItemRemoved(clickIndex);
                        Toast.makeText(context,clickIndex+"번 삭제",Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                       // holder.cv.setVisibility(View.VISIBLE);
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        fb.setImageResource(R.drawable.ic_action_name);
                       // holder.cv.setVisibility(View.VISIBLE);
                        return true;
                    default:
                       // holder.cv.setVisibility(View.VISIBLE);
                        return true;
                }
            }
        };
        holder.fab.setOnDragListener(fabDragListener);//드래그 리스너 구현


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
