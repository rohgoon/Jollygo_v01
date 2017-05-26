package kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgHtmlAsyncTask;

/**
 * Created by NCG on 2017-05-23.
 */

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
    private List<String> mDataset; //재료 검색어 리스트
    private static int lastPosition = -1;
    private Context context;
    private static FloatingActionButton floatingActionButton;
    private static int clickIndex;


    public RvAdapter(Context context, FloatingActionButton floatingActionButton) {
        this.context = context;
        this.floatingActionButton = floatingActionButton;
        mDataset = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public ImageView ivCard;
        public TextView tvTest;
        public  CardView cv;
       // public RelativeLayout rl;
        public FloatingActionButton fab; //플로팅버튼 받아오기

        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v;
            ivCard = (ImageView) v.findViewById(R.id.ivCard);
            tvTest = (TextView) v.findViewById(R.id.tvTest);
            //rl = (RelativeLayout) v.findViewById(R.id.card_layout);
            fab = floatingActionButton;
            ivCard.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            ClipData clip = ClipData.newPlainText("","");
            v.startDrag(clip, new View.DragShadowBuilder(v),null, 0);
            clickIndex = getLayoutPosition();
             Log.e("뷰홀더 롱크릭 >>>>>>>>>>>> ",clickIndex+"번 인덱스 출력");
            fab.setImageResource(R.drawable.ic_delete);
            //프롤팅버튼에 드랍 이벤트 주기

            return true;
        }


    }//ViewHolder

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
    public void onBindViewHolder(ViewHolder holder, int position) {//이벤트 처리
        setAnimationFadeIn(holder.cv, position);

        //ImgHtmlAsyncTask 이미지 받아오기
        ImgHtmlAsyncTask iha = new ImgHtmlAsyncTask();
        iha.execute(mDataset.get(position));
        Map<String,Bitmap> imgMap = new HashMap<>();
        try {
            imgMap= iha.get();
            Bitmap resBitmap =imgMap.get(mDataset.get(position));
            if (resBitmap == null){
                holder.ivCard.setImageResource(R.drawable.jg_icon);//default image
            }else {
                holder.ivCard.setImageBitmap(resBitmap);
            }
            iha.isCancelled();
        } catch (Exception e) {
            e.printStackTrace();
            holder.ivCard.setImageResource(R.drawable.jg_icon);//default image
        }



        holder.tvTest.setText(mDataset.get(position));

        //프롤팅버튼에 드랍 이벤트 주기

        holder.fab.setOnDragListener(fabDragListener);//드래그 리스너 구현
    }
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
                    // 삭제
                    deleteItem(clickIndex);

                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    fb.setImageResource(R.drawable.ic_action_name);

                    return true;
                default:

                    return true;
            }
        }
    };

    @Override
    public int getItemCount() {
        if (mDataset == null){
            return 0;
        }else{
            return mDataset.size();
        }

    }

//addItem
    public void addItem(String infoData) {
        mDataset.add(infoData);
        notifyItemInserted(mDataset.size()-1);
        notifyDataSetChanged();
    }
//deleteItem

    public void deleteItem(int position) {
        try {
            mDataset.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context,position+"번 삭제",Toast.LENGTH_LONG).show();
            //notifyDataSetChanged();
        } catch(IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }
    private void setAnimationFadeIn(View viewToAnimate, int position){

        if (position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public List<String> getmDataset() {
        return mDataset;
    }
}
