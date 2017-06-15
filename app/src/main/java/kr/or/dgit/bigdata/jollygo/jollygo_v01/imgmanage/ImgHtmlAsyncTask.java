package kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Allword;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Favlink;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Uword;

/**
 * Created by NCG on 2017-05-26.
 */

public class ImgHtmlAsyncTask extends AsyncTask<String,Integer,Map<String,Bitmap>> {//재료명 여기서 수집

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Map<String,Bitmap> resMap;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String,Bitmap> doInBackground(final String... params) {
        //차후 DB 구축시 파싱 예외처리 요망
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        resMap = new HashMap<>();
        //있는지 검색하고 예외처리

        Query awd = databaseReference.child("allword").orderByChild("awname").equalTo(params[0]);
        awd.addListenerForSingleValueEvent(new ValueEventListener() {//하나만 받아옴
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()<1){
                    resMap = wordNotExists(params[0], resMap);
                }else{//검색하는 단어 allword로 던짐
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        //int awno, String awname, String awimgurl, int awcount
                        Allword aw = new Allword(
                                Integer.parseInt(d.child("awno").getValue().toString()),
                                d.child("awname").getValue().toString(),
                                d.child("awimgurl").getValue().toString(),
                                Integer.parseInt(d.child("awcount").getValue().toString())
                        );
                        //트랜잭션 처리로 allword, uword 카운트 올리기
                        //uword에 등록되지 않은 단어의 경우와 등록된 단어의 경우가 있음
                        checkUword(aw);
                        //allword용 트랜잭션도 가동시키기
                        clickStack(d.getRef(),1);
                        resMap = wordExists(params[0], aw.getAwimgurl(), resMap);
                    }//foreach
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return resMap;
    }
    private void checkUword(final Allword aw){
        Query uwQuery = databaseReference.child("uword").orderByChild("uid").equalTo(currentUser.getUid());
        uwQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()<1){
                    //uword 바로 새로 삽입
                    Uword uw = new Uword(0,currentUser.getUid(),aw.getAwno(),0);
                    databaseReference.child("uword").push().setValue(uw);
                }else {
                    int uwCheckNum =0;
                    List<Uword> uwordList = new ArrayList<Uword>();
                    for (DataSnapshot d:dataSnapshot.getChildren()) {
                        //int uwno, String uid, int awno, int uwcount
                        Uword uw0 = new Uword(
                                Integer.parseInt(d.child("uwno").getValue().toString()),
                                d.child("uid").getValue().toString(),
                                Integer.parseInt(d.child("awno").getValue().toString()),
                                Integer.parseInt(d.child("uwcount").getValue().toString())
                        );
                        uwordList.add(uw0);
                        if (uw0.getAwno() == aw.getAwno()){
                            uwCheckNum++;//검색해서 같은 단어가 하나라도 있으면 증가
                            clickStack(d.getRef(),2);
                        }
                    }//foreach
                    if (uwCheckNum <1){
                        Uword uwForNum = uwordList.get(uwordList.size()-1);
                        Uword uw = new Uword(uwForNum.getUwno()+1,currentUser.getUid(),aw.getAwno(),0);
                        databaseReference.child("uword").push().setValue(uw);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    };

    private Map<String, Bitmap> wordNotExists(String param, Map<String, Bitmap> resMap) {
        String url = "https://ko.wikipedia.org/wiki/" +param;
        String img="";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Element img1 = doc.select("img.thumbimage").first(); //기본 썸네일 thumbimage 작동됨
            Element img2 = doc.select("li.gallerybox img").first(); //예비1 썸네일 thumbimage 작동됨
            Element img3 = doc.select("table.infobox a img").first();//예비2 썸네일 thumbimage
            if(img1!=null&&img2!=null){
                img = "https:"+img2.attr("src"); // img2에서 src 빼내기
                Log.e("파싱 출처2 >>>>>>>>>>>>> ",img);
            }else if(img1 != null&&img2==null){
                img = "https:"+img1.attr("src"); // img1에서 src 빼내기
                Log.e("파싱 출처1 >>>>>>>>>>>>> ",img);
            }else if(img1==null&&img2 !=null){
                img = "https:"+img2.attr("src"); // img2에서 src 빼내기
                Log.e("파싱 출처2 >>>>>>>>>>>>> ",img);
            }else if(img1==null&&img2==null&&img3!=null){
                img = "https:"+img3.attr("src"); // img3에서 src 빼내기
                Log.e("파싱 출처3 >>>>>>>>>>>>> ",img);
            }
            //img = "https:"+img1.attr("src");
            Log.e("파싱 결과값 >>>>>>>>>>>>> ",img);
            //경우의 수 추가 요망
            //Bitmap imgBitmap = mkBitmap(img);
            Bitmap imgBitmap;
            if (img.equals("")){
                imgBitmap = null;
            }else{
                imgBitmap = mkBitmap(img);
            }
            resMap.put(param,imgBitmap);

            //단어푸쉬
            wordsSetValue(param,img);//(String name, String imgurl,int awno, int uwno) allword와 uword에 추가
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resMap;
    }//wordNotExists

    private Map<String, Bitmap> wordExists(String param, String img,Map<String, Bitmap> resMap) {//트랜잭션 통해 카운트를 올림 + resMap.put(param,imgBitmap);
        Bitmap bitmap = mkBitmap(img);
        resMap.put(param,bitmap);
        return resMap;
    }//wordExists


    private Bitmap mkBitmap(String s) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(s);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true); connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            if(connection!=null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Map<String, Bitmap> stringBitmapMap) {
        super.onPostExecute(stringBitmapMap);
    }

    private void wordsSetValue(final String name, final String imgurl){//단어 푸쉬하는곳
        //awno,uwno 갱신값 넣기

        Query awQuery = databaseReference.child("allword").orderByKey().limitToLast(1);
        awQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    //int awno, String awname, String awimgurl, int awcount
                    Allword awLast = new Allword(
                            Integer.parseInt(d.child("awno").getValue().toString()),
                            d.child("awname").getValue().toString(),
                            d.child("awimgurl").getValue().toString(),
                            Integer.parseInt(d.child("awcount").getValue().toString())
                    );


                    final int awno = awLast.getAwno()+1;
                    Allword aw = new Allword(awno,name,imgurl,0);
                    Query uwQuery = databaseReference.child("uword").orderByChild("uid").equalTo(currentUser.getUid());
                    uwQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int uwno = (int) dataSnapshot.getChildrenCount();
                            Uword uw = new Uword(uwno,currentUser.getUid(),awno,0);
                            databaseReference.child("uword").push().setValue(uw);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    databaseReference.child("allword").push().setValue(aw);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void clickStack(DatabaseReference ref, final int ckeckClass) {


        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (ckeckClass ==1){//allword용 트랜잭션
                    Allword aw = mutableData.getValue(Allword.class);
                    if (aw == null){
                        return Transaction.success(mutableData);
                    }
                    aw.setAwcount(aw.getAwcount()+1);
                    mutableData.setValue(aw);
                }else if(ckeckClass ==2){//uword용 트랜잭션
                    Uword uw = mutableData.getValue(Uword.class);
                    if (uw == null){
                        return Transaction.success(mutableData);
                    }
                    uw.setUwcount(uw.getUwcount()+1);
                    mutableData.setValue(uw);

                }else{
                    return Transaction.success(mutableData);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d("트랜잭션 완료", "postTransaction:onComplete:" + databaseError);
            }
        });
    }
}
