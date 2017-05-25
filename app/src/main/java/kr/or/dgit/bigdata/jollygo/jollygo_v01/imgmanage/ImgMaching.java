package kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;

/**
 * Created by rohgoon on 2017-05-25.
 */

public class ImgMaching { //검색어와 이미지를 매칭. 이미지URL과 검색어를 묶어서 던져 줌. AsyncTaskd 상속
    private static int cn =0;
    private int defaultImg = R.drawable.jg_icon;
    private String img = defaultImg+"";
    public void getWikiRes(String word){//쓰레드 필요함
        parseByTread("https://ko.wikipedia.org/wiki/" + word);
        for (;;) {
            if (cn == 1) {
                Log.e("파싱 결과값 >>>>>>>>>>>>> ", img);
                img = "https:" + img;
                break;
            }
        }
    }

    private void parseByTread(final String url) {
        final List<String> resList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
               // final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(url).get();
                    Element img1 = doc.select("img.thumbimage").first(); //기본 썸네일 thumbimage
                    img = img1.attr("src");
                    Log.i("파싱 결과값 >>>>>>>>>>>>> ",img1.attr("src"));
                    cn =1;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    };

    public String getImg() {
        return img;
    }
/* Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();

        }
    };*/

     /*  try {
         new Thread() {

                     *//*String url = "https://ko.wikipedia.org/wiki/"+md; //한국어 주소
                    Document doc = Jsoup.connect(url).get();
                    Element img1 = doc.select("img.thumbimage").first(); //기본 썸네일 thumbimage
                    Element img2 = doc.select("li.gallerybox img").first(); //기본 썸네일 thumbimage
                    //hadler로 전송
                    String attr1 = img1.attr("src");
                    Bundle bun = new Bundle();
                    bun.putString("img1", attr1);
                    Message msg = handler.obtainMessage();
                    msg.setData(bun);
                    handler.sendMessage(msg);
                    //경우의수 처리
                if(img1.hasText()&&img2.hasText()){
                        res = img2.attr("src"); // img2에서 src 빼내기
                    }else if(img1.hasText()&&img2.hasText()==false){
                        res = img1.attr("src"); // img1에서 src 빼내기
                    }else if(img1.hasText()==false&&img2.hasText()){
                        res = img2.attr("src"); // img2에서 src 빼내기
                    }else if(img1.hasText()==false&&img2.hasText()==false){
                        Element img3 = doc.select("table.infobox a img").first();
                        if (img3.hasText()) {
                            res = img3.attr("src"); // img3에서 src 빼내기
                        }
                    }
                    imgMatchingRes.put(md,res); // 결과를 map으로 넣어줌
                    mImgset.add(res);*//*
        }.start();
    } catch (Exception e) {
        e.printStackTrace();
    }*/
}
