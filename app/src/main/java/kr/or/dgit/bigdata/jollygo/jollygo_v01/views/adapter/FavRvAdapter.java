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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.WebActivity;
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
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    public FavRvAdapter(Context context, FloatingActionButton floatingActionButton, List<Favlink> favlinkList, DatabaseReference databaseReference, FirebaseUser currentUser) {
        this.context = context;
        this.floatingActionButton = floatingActionButton;
        this.favlinkList = favlinkList;
        this.databaseReference = databaseReference;
        this.currentUser = currentUser;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public ImageView ivCard;
        public TextView tvTest;
        public CardView cv;
        public FloatingActionButton fab; //플로팅버튼 받아오기
        public ImageView ivBrowser;
        public RelativeLayout rl;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v;
            ivCard = (ImageView) v.findViewById(R.id.ivFavCard);
            tvTest = (TextView) v.findViewById(R.id.tvFavTitle);
            ivBrowser = (ImageView) v.findViewById(R.id.ivBrowser);
            rl = (RelativeLayout) v.findViewById(R.id.list_fav_layout);
            fab = floatingActionButton;
            ivCard.setOnLongClickListener(this);
            tvTest.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            ClipData clip = ClipData.newPlainText("", "");
            fab.setImageResource(R.drawable.ic_delete);
            fab.setVisibility(View.VISIBLE);
            cv.startDrag(clip, new View.DragShadowBuilder(cv), null, 0);
            //View.DragShadowBuilder(cv) 수정요망

            clickIndex = getLayoutPosition();
            Log.e("뷰홀더 롱크릭 >>>>>>>>>>>> ", clickIndex + "번 인덱스 출력");
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

        //클릭시 웹뷰로 이동

        holder.tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goWebView(position);
            }
        });
        holder.ivCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goWebView(position);
            }
        });

        //프롤팅버튼에 드랍 이벤트 주기
        holder.fab.setOnDragListener(fabDragListener);//드래그 리스너 구현
        boat.isCancelled();
        //Toast.makeText(context,"사이즈는 "+favlinkList.size(),Toast.LENGTH_SHORT).show();//사이즈 자체가 16임->수정 완료
    }//onBindViewHolder

    View.OnDragListener fabDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            FloatingActionButton fb = null;
            if (v instanceof FloatingActionButton) {
                fb = (FloatingActionButton) v;
            }
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    //파이어베이스에서도 삭제
                    long fno = favlinkList.get(clickIndex).getFno();
                    Log.e("받아온 fno>>", fno + "");
                    //쿼리로 던지고 데이터스냅샷의 getRef로 받아와 삭제해야 한다.
                    Query fld = databaseReference.child("favlink").orderByChild("fno").equalTo(fno); //fno 기준으로 삭제함으로 fno를 특정화 시키는게 중요
                    fld.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                d.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    // 삭제
                    deleteItem(clickIndex);
                    fb.setVisibility(View.GONE);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    fb.setVisibility(View.GONE);

                    return true;
                default:

                    return true;
            }
        }
    };

    @Override
    public int getItemCount() {
        if (favlinkList == null) {
            return 0;
        } else {
            return favlinkList.size();
        }

    }

    //addItem
    public void addItem(Favlink infoData) {
        favlinkList.add(infoData);
        notifyItemInserted(favlinkList.size() - 1);
        notifyDataSetChanged();
    }
//deleteItem

    public void deleteItem(int position) { // 즐겨찾기 삭제 기능
        try {
            favlinkList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, position + "번 삭제", Toast.LENGTH_LONG).show(); // 테스트용 출력물
            //notifyDataSetChanged();

        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public static int getClickIndex() {
        return clickIndex;
    }

    private void goWebView(int position) {
        Query flq = databaseReference.child("favlink").orderByChild("fno").equalTo(favlinkList.get(position).getFno());

        flq.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    Favlink fl = new Favlink(Integer.parseInt(d.child("fno").getValue().toString()),
                            d.child("furl").getValue().toString(),
                            d.child("fimgurl").getValue().toString(),
                            d.child("uid").getValue().toString(),
                            d.child("fname").getValue().toString(),
                            Integer.parseInt(d.child("fcount").getValue().toString())
                            );
                    //(int fno, String furl, String fimgurl, String uid, String fname, int fcount) {
                    //Toast.makeText(context,fl.toString(),Toast.LENGTH_SHORT).show();
                    clickStack(d.getRef());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", favlinkList.get(position).getFurl());
        intent.putExtra("imgurl", favlinkList.get(position).getFimgurl());
        intent.putExtra("blogname", favlinkList.get(position).getFname());
        intent.putExtra("byfav", true);
        context.startActivity(intent);

    }

    private void clickStack(DatabaseReference ref) {
        ref.runTransaction(new Transaction.Handler() {//실행안된
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Favlink fl = mutableData.getValue(Favlink.class);
                Log.e("즐찾 트랜잭션:",fl.toString());
                //Toast.makeText(context,fl.toString(),Toast.LENGTH_SHORT).show();
                if (fl == null){
                    return Transaction.success(mutableData);
                }
                fl.setFcount(fl.getFcount()+1);
                mutableData.setValue(fl);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d("트랜잭션 완료", "postTransaction:onComplete:" + databaseError);
            }
        });
    }

}