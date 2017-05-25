package kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rohgoon on 2017-05-25.
 */

public class ImgMaching { //검색어와 이미지를 매칭. 이미지URL과 검색어를 묶어서 던져 줌
    private List<String> mDataset;
    private static Map<String,String> imgMatchingRes = new HashMap<>();


    public List<String> getmDataset() {
        return mDataset;
    }

    public void setmDataset(List<String> mDataset) {
        this.mDataset = mDataset;
    }
}
