package kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rohgoon on 2017-05-25.
 */

public class ImgWords {
    private List<String> mDataset;
    private Map<String,Bitmap> resultImgMap;

    public ImgWords() {
        mDataset = new ArrayList<>();
        resultImgMap = new HashMap<>();
    }

    public ImgWords(List<String> mDataset) {
        this.mDataset = mDataset;
    }
    public List<String> getmDataset() {
        return mDataset;
    }

    public void setmDataset(List<String> mDataset) {
        this.mDataset = mDataset;
    }

    public Map<String, Bitmap> getResultImgMap() {
        return resultImgMap;
    }

    public void setResultImgMap(Map<String, Bitmap> resultImgMap) {
        this.resultImgMap = resultImgMap;
    }
}
