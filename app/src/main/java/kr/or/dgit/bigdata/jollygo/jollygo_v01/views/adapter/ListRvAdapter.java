package kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.WebActivity;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Allurl;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Allword;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Favlink;
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
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private boolean check = false;
    private int auNum;
    private Allurl auExist;
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
        public RelativeLayout rl;
        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v;
            ivCard = (ImageView) v.findViewById(R.id.ivListCard);
            tvTest = (TextView) v.findViewById(R.id.tvListTitle);
            rl = (RelativeLayout) v.findViewById(R.id.list_card_layout);
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
    public void onBindViewHolder(ViewHolder holder, final int position){//이벤트 처리 // ou : 이미지 pt : 제목 ru : 링크주소 rid : 중복검사용 고유번호
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
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //주소 중복 확인
                DatabaseReference aud = databaseReference.child("allurl");
                aud.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            check =false;
                            //int auno, String auurl, String auimgurl, String auname, int aucount
                            //Allurl auCheck = d.getValue(Allurl.class);
                            Allurl auCheck = new Allurl(
                                    Integer.parseInt(d.child("auno").getValue().toString()),
                                    d.child("auurl").getValue().toString(),
                                    d.child("auimgurl").getValue().toString(),
                                    d.child("auname").getValue().toString(),
                                    Integer.parseInt(d.child("aucount").getValue().toString())
                            );
                            if (auCheck == null){
                                break;
                            }
                            if (auCheck.getAuurl().equals(srList.get(position).getRu())){
                                check =true;
                                auExist = auCheck;
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //주소 없을시
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
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });//addListenerForSingleValueEvent
                if (check == false){
                    Allurl allurl = new Allurl(auNum,srList.get(position).getRu(),
                            srList.get(position).getOu(),srList.get(position).getPt(),0);
                    databaseReference.child("allurl").push().setValue(allurl);
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
                //주소(original) 수집

                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("url",srList.get(position).getRu());
                intent.putExtra("imgurl",srList.get(position).getOu());
                intent.putExtra("blogname",srList.get(position).getPt());
                intent.putExtra("byfav",false);
                context.startActivity(intent);
            }
        });
    }

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
}
