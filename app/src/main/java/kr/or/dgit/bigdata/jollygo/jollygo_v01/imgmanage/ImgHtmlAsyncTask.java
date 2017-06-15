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
import com.google.firebase.database.Query;
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
    private Map<String,Bitmap> resMap= new HashMap<>();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String,Bitmap> doInBackground(final String... params) {
        //차후 DB 구축시 파싱 예외처리 요망
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //있는지 검색하고 예외처리

        Query awd = databaseReference.child("allword").orderByChild("awname").equalTo(params[0]);
        awd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()<1){
                    resMap = wordNotExists(params[0], resMap);
                }else{
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        //int awno, String awname, String awimgurl, int awcount
                        Allword aw = new Allword(
                                Integer.parseInt(d.child("awno").getValue().toString()),
                                d.child("awname").getValue().toString(),
                                d.child("awimgurl").getValue().toString(),
                                Integer.parseInt(d.child("awcount").getValue().toString())
                        );
                        resMap = wordExists(params[0], aw.getAwimgurl(), resMap);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return resMap;
    }

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

            wordsSetValue(param,img,0,0);//(String name, String imgurl,int awno, int uwno) allword와 uword에 추가
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

    private void wordsSetValue(String name, String imgurl,int awno, int uwno){
        Allword aw = new Allword(awno,name,imgurl,0);
        Uword uw = new Uword(uwno,currentUser.getUid(),awno,0);
        databaseReference.child("allword").push().setValue(aw);
        databaseReference.child("uword").push().setValue(uw);
    }


}
