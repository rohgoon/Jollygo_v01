package kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by NCG on 2017-05-26.
 */

public class ImgHtmlAsyncTask extends AsyncTask<String,String,Map<String,Bitmap>> {
    @Override
    protected Map<String,Bitmap> doInBackground(String... params) {
        //차후 DB 구축시 파싱 예외처리 요망
        String url = "https://ko.wikipedia.org/wiki/" +params[0];
        Map<String,Bitmap> resMap= new HashMap<>();
        String img="";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Element img1 = doc.select("img.thumbimage").first(); //기본 썸네일 thumbimage
            img = "https:"+img1.attr("src");
            Log.e("파싱 결과값 >>>>>>>>>>>>> ",img);
            //경우의 수 추가 요망

            Bitmap imgBitmap = mkBitmap(img);
            resMap.put(params[0],imgBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resMap;
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
