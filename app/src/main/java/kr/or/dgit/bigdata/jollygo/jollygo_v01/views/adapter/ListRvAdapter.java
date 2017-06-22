package kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.SearchActivity;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.WebActivity;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Allurl;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Allword;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Favlink;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.BitmapOnlyAsyncTask;
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
    private BitmapOnlyAsyncTask boat;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private boolean check = false;
    private int auNum;
    private Allurl auExist;
    private List<Bitmap> bitmapList;
    private String[] bmCsr;
    private ProgressBar progressBar;
    public ListRvAdapter(Context context, ImgWords imgWords, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
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

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            hjat.isCancelled();
        }
        Log.e("리스트 결과 사이즈>>>>>",srList.size()+"");
        //핸들러로 던지기 참치
        Message msg = new Message();
        msg.what =1;

        Handler mh = new mHandler();
        mh.sendMessageDelayed(msg,600);

       /* int bitmapSize = srList.size();
        bmCsr = new String[bitmapSize];
        for (int i=0;i<bitmapSize;i++){
            bmCsr[i] = srList.get(i).getOu();
        }
        boat = new BitmapOnlyAsyncTask();
        boat.execute(bmCsr);
        try {
            bitmapList = boat.get();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            boat.isCancelled();
        }
        for (int i =0; i<srList.size();i++){
            srList.get(i).setBitmap(bitmapList.get(i));
        }*/

    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivCard;
        public TextView tvTest;
        public CardView cv;
        public RelativeLayout rl;
        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v;
            ivCard = (ImageView) v.findViewById(R.id.ivListCard);
            tvTest = (TextView) v.findViewById(R.id.tvListTitle);
            rl = (RelativeLayout) v.findViewById(R.id.list_card_layout);
        }
    }//ViewHolder 참치


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
    public void onBindViewHolder(ViewHolder holder, final int position){//이벤트 처리 // ou : 이미지 pt : 제목 ru : 링크주소 rid : 중복검사용 고유번호
        Bitmap resBitmap=srList.get(position).getBitmap();
        if (resBitmap == null){
            holder.ivCard.setImageResource(R.drawable.imgloading1);//default image
        }else {
            holder.ivCard.setImageBitmap(resBitmap);
        }
        holder.tvTest.setText(srList.get(position).getPt());

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //주소 중복 확인
                Query aud = databaseReference.child("allurl").orderByChild("auurl").equalTo(srList.get(position).getRu());
                aud.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("주소중복체크",dataSnapshot.getChildrenCount()+"");
                        if (dataSnapshot.getChildrenCount() <1){
                            check = false;
                            existCheck(check,position);
                        }else {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                //int auno, String auurl, String auimgurl, String auname, int aucount
                                //Allurl auCheck = d.getValue(Allurl.class);
                                Allurl auCheck = new Allurl(
                                        Integer.parseInt(d.child("auno").getValue().toString()),
                                        d.child("auurl").getValue().toString(),
                                        d.child("auimgurl").getValue().toString(),
                                        d.child("auname").getValue().toString(),
                                        Integer.parseInt(d.child("aucount").getValue().toString())
                                );
                                if (auCheck == null) {
                                    check = false;
                                    existCheck(check,position);
                                    break;
                                }
                                check = true;
                                auExist = auCheck;
                                existCheck(check,position);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //주소(original) 수집
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("url",srList.get(position).getRu());
                intent.putExtra("imgurl",srList.get(position).getOu());
                intent.putExtra("blogname",srList.get(position).getPt());
                intent.putExtra("byfav",false);
                context.startActivity(intent);
            }
        });
    }//onBindViewHolder

    @Override
    public int getItemCount() {
        if (srList == null){
            return 0;
        }else{
            return srList.size();
        }
    }
    private void clickStack(DatabaseReference ref) {
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Allurl au = mutableData.getValue(Allurl.class);
                if (au == null){
                    return Transaction.success(mutableData);
                }
                au.setAucount(au.getAucount()+1);
                mutableData.setValue(au);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d("트랜잭션 완료", "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    private void existCheck(boolean tf, final int position){
        if (tf == false){
            DatabaseReference aud = databaseReference.child("aud");
            aud.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Allurl auCheck = new Allurl(
                                Integer.parseInt(d.child("auno").getValue().toString()),
                                d.child("auurl").getValue().toString(),
                                d.child("auimgurl").getValue().toString(),
                                d.child("auname").getValue().toString(),
                                Integer.parseInt(d.child("aucount").getValue().toString())
                        );
                        auNum = auCheck.getAuno();
                        auNum++;
                        Allurl allurl = new Allurl(auNum,srList.get(position).getRu(),
                                srList.get(position).getOu(),srList.get(position).getPt(),0);
                        databaseReference.child("allurl").push().setValue(allurl);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else {//트랜잭션 통해 카운팅 수 올리기
            Query auForCount = databaseReference.child("allurl").orderByChild("auno").equalTo(auExist.getAuno());
            auForCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Allurl auCheck = new Allurl(
                                Integer.parseInt(d.child("auno").getValue().toString()),
                                d.child("auurl").getValue().toString(),
                                d.child("auimgurl").getValue().toString(),
                                d.child("auname").getValue().toString(),
                                Integer.parseInt(d.child("aucount").getValue().toString())
                        );
                        clickStack(d.getRef());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private class mHandler extends Handler{ // 리사이클링 뷰 갱신하기 전에 프로그래스바 보이기 위한 핸들러
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1){
                int bitmapSize = srList.size();
                bmCsr = new String[bitmapSize];
                for (int i=0;i<bitmapSize;i++){
                    bmCsr[i] = srList.get(i).getOu();
                }
                boat = new BitmapOnlyAsyncTask();
                boat.execute(bmCsr);
                try {
                    bitmapList = boat.get();

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    boat.isCancelled();
                }
                for (int i =0; i<srList.size();i++){
                    srList.get(i).setBitmap(bitmapList.get(i));
                }
                notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                removeMessages(msg.what);//
            }
        }
    }
}
