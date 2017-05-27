package kr.or.dgit.bigdata.jollygo.jollygo_v01.searchmanage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

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
    @Override
    protected List<SearchResult> doInBackground(String... params) {
        String addr = "www.google.co.kr/search?q=allintext:";
        for (String str: params) {
            addr += "+"+str;
        }
        addr +="&hl=ko&noj=1&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiQkuKvicTTAhUBlZQKHXlUC7wQ_AUICigB&biw=853&bih=974";
        List<JSONArray> jsonList = new ArrayList<>();
        List<SearchResult> srList = new ArrayList<>();
        Document doc = null;
        try {
            //json 추출해서 리스트에 삽입
            doc = Jsoup.connect(addr).get();
            Elements jsons = doc.select("div.rg_meta"); //기본 썸네일 thumbimage 작동됨
            for (Element e: jsons) {
                String jsonStrFirst = e.html();//json 가져옴. 앞뒤로 따옴표 있으니 정리 필요
                //유니코드 정리 필요 Gson 확인요망
                /*Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String noEscapeStr = gson.toJson(queryCodeList).toString();*/

                String jsonRes = "["+jsonStrFirst.substring(1,jsonStrFirst.length()-2)+"]"; //따옴표 정리
                JSONArray jarr = new JSONArray(jsonRes);
                jsonList.add(jarr); //JSONArray 형태로 리스트 삽입
            }
            //json 분류 및 정리
            for (JSONArray jarr: jsonList) {
                // \u003d(=), \u0026(&) 등의 유니코드들을 교체시켜야함
            }

            //비트맵 처리
            String img = "https:";
            Log.e("파싱 결과값 >>>>>>>>>>>>> ",img);
            //경우의 수 추가 요망
            //Bitmap imgBitmap = mkBitmap(img);
            Bitmap imgBitmap;
            if (img.equals("")){
                imgBitmap = null;
            }else{
                imgBitmap = mkBitmap(img);
            }
            //SearchResult 생성

            // List<SearchResult>에 삽입


        } catch (Exception e) {
            e.printStackTrace();
        }

        return srList;
    }

    /*@Override
    protected Map<String,Bitmap> doInBackground(String... params) {
        //차후 DB 구축시 파싱 예외처리 요망
        String url = "https://ko.wikipedia.org/wiki/" +params[0];
        Map<String,Bitmap> resMap= new HashMap<>();
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
            resMap.put(params[0],imgBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resMap;
    }*/
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
