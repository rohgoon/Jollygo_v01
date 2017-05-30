package kr.or.dgit.bigdata.jollygo.jollygo_v01.searchmanage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NCG on 2017-05-26.
 */

public class HtmlJsonAsyncTask extends AsyncTask<String,String,List<SearchResult>> {
    private int cutIndex = 0;
    @Override
    protected List<SearchResult> doInBackground(String... params) {
        String addr = "https://www.google.co.kr/search?q=allintext:+레시피";
        for (String str: params) {
            addr += "+"+str;
        }
        addr +="+이웃추가&hl=ko&noj=1&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiQkuKvicTTAhUBlZQKHXlUC7wQ_AUICigB&biw=853&bih=974";
        Log.e("인풋 스트링값 >>>>>>>>>>>>> ", addr);
        List<SearchResult> srList = new ArrayList<>(); // 결과 리스트
        Document doc = null;
        try {
            //json 추출해서 리스트에 삽입
            doc = Jsoup.connect(addr).get();
            Elements jsons = doc.select("div.rg_meta"); //기본 썸네일 thumbimage 작동됨
            for (Element e: jsons) { //중복 검사후 리스트로 삽입해야 함.
                SearchResult sr = new SearchResult(); //SearchResult 생성

                String jsonRes = e.html();//json 가져옴. 그냥 긁어도 기능함.

                //String jsonRes = "["+jsonStrFirst.substring(1,jsonStrFirst.length()-2)+"]"; //따옴표 정리. 확인 요망
                Log.e("파싱 스트링값 >>>>>>>>>>>>> ", jsonRes);
                //유니코드 정리 필요 Gson 확인요망
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();

                sr = gson.fromJson(jsonRes,SearchResult.class); // ou : 이미지 pt : 제목 ru : 링크주소
                //비트맵 처리
                Log.e("이미지 결과값 >>>>>>>>>>>>> ", sr.getOu());
                //중복 제거 처리
                boolean checkTf = false;
                if (cutIndex > 0) {
                    for (int i = 0; i <srList.size();i++){
                        if (sr.getRid().equals(srList.get(i).getRid())) {
                            checkTf =false;
                            break;
                        }else {
                            checkTf =true;
                        }
                    }
                }else{
                   checkTf = true;
                }
                if (checkTf){
                    Bitmap imgBitmap;
                    if (sr.getOu().equals("")) {
                        imgBitmap = null;
                    } else {
                        imgBitmap = mkBitmap(sr.getOu());
                    }
                    sr.setBitmap(imgBitmap);
                    srList.add(sr); // List<SearchResult>에 삽입
                    cutIndex++;
                }

                if (cutIndex >19){
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return srList;
    }
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
}
