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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgHtmlAsyncTask;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgWords;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.searchmanage.HtmlJsonAsyncTask;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.searchmanage.SearchResult;

/**
 * Created by NCG on 2017-05-23.
 */

public class ListRvAdapter extends RecyclerView.Adapter<ListRvAdapter.ViewHolder> {
    private Context context;
    private static int clickIndex;
    private List<SearchResult> srList; //파싱 결과가 들어감
    private HtmlJsonAsyncTask hjat;

    public ListRvAdapter(Context context, ImgWords imgWords) {
        this.context = context;
        srList = new ArrayList<>();
        int size = imgWords.getmDataset().size();
        String[] iwCsr = new String[size];
        for (int i=0;i<size;i++){
            iwCsr[i] = imgWords.getmDataset().get(i);
        }
        hjat = new HtmlJsonAsyncTask();
        hjat.execute(iwCsr);
        try {
            srList= hjat.get();//파싱결과 받음
            hjat.isCancelled();
        } catch (Exception e) {
            e.printStackTrace();
        };
        Log.e("리스트 사이즈>>>>>",srList.size()+"");
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivCard;
        public TextView tvTest;
        public CardView cv;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v;
            ivCard = (ImageView) v.findViewById(R.id.ivListCard);
            tvTest = (TextView) v.findViewById(R.id.tvListTitle);
        }
    }//ViewHolder


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_list, parent, false);//해당 레이아웃 결정
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//이벤트 처리
        Bitmap resBitmap=srList.get(position).getBitmap();
        if (resBitmap == null){
            holder.ivCard.setImageResource(R.drawable.jg_icon);//default image
        }else {
            holder.ivCard.setImageBitmap(resBitmap);
        }
        holder.tvTest.setText(srList.get(position).getPt());
        if (getItemCount()==(position-1)){
            hjat.isCancelled();
        }

    }

    @Override
    public int getItemCount() {
        if (srList == null){
            return 0;
        }else{
            return srList.size();
        }
    }
}
