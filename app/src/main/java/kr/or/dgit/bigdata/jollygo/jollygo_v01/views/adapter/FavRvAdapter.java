package kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Favlink;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.BitmapOnlyAsyncTask;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgHtmlAsyncTask;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgWords;

/**
 * Created by NCG on 2017-05-23.
 */

public class FavRvAdapter extends RecyclerView.Adapter<FavRvAdapter.ViewHolder> {
    private static int lastPosition = -1;
    private Context context;
    private static FloatingActionButton floatingActionButton;
    private static int clickIndex;
    private List<Favlink> favlinkList;
    private BitmapOnlyAsyncTask boat;

    public FavRvAdapter(Context context, FloatingActionButton floatingActionButton, List<Favlink> favlinkList) {
        this.context = context;
        this.floatingActionButton = floatingActionButton;
        this.favlinkList =favlinkList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public ImageView ivCard;
        public TextView tvTest;
        public CardView cv;
        public FloatingActionButton fab; //플로팅버튼 받아오기
        public ImageView ivBrowser;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v;
            ivCard = (ImageView) v.findViewById(R.id.ivFavCard);
            tvTest = (TextView) v.findViewById(R.id.tvFavTitle);
            ivBrowser = (ImageView) v.findViewById(R.id.ivFavCard);
            fab = floatingActionButton;
            ivCard.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            ClipData clip = ClipData.newPlainText("","");
            fab.setImageResource(R.drawable.ic_delete);
            fab.setVisibility(View.VISIBLE);
            cv.startDrag(clip, new View.DragShadowBuilder(cv),null, 0);
            //View.DragShadowBuilder(cv) 수정요망

            clickIndex = getLayoutPosition();
             Log.e("뷰홀더 롱크릭 >>>>>>>>>>>> ",clickIndex+"번 인덱스 출력");
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
                .inflate(R.layout.item_fav_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {//이벤트 처리

       //이미지 URL 비트맵 전환용 ASYCKTASK 생성
        boat = new BitmapOnlyAsyncTask();
        boat.execute(favlinkList.get(position).getFimgurl());
        try {
            Bitmap resBitmap = boat.get();
            holder.ivCard.setImageBitmap(resBitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //ImgHtmlAsyncTask 이미지 받아오기
        holder.tvTest.setText(favlinkList.get(position).getFname());

        //브라우저로 바로 이동
        holder.ivBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(favlinkList.get(position).getFurl()));
                context.startActivity(intent);
            }
        });

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
        if (favlinkList == null){
            return 0;
        }else{
            return favlinkList.size();
        }

    }

//addItem
    public void addItem(Favlink infoData) {
        favlinkList.add(infoData);
        notifyItemInserted(favlinkList.size()-1);
        notifyDataSetChanged();
    }
//deleteItem

    public void deleteItem(int position) { // 즐겨찾기 삭제 기능
        try {
            favlinkList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context,position+"번 삭제",Toast.LENGTH_LONG).show(); // 테스트용 출력물
            //notifyDataSetChanged();

        } catch(IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

}
