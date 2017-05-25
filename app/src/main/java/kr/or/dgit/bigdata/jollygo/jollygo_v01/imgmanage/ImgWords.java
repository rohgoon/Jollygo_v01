package kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rohgoon on 2017-05-25.
 */

public class ImgWords {
    private List<String> mDataset;
    private List<String> mImgset; //이미지 url 리스트
    private static Map<String,String> imgMatchingRes = new HashMap<>();

    public ImgWords(List<String> mDataset, List<String> mImgset) {
        this.mDataset = mDataset;
        this.mImgset = mImgset;
    }

    public List<String> getmDataset() {
        return mDataset;
    }

    public void setmDataset(List<String> mDataset) {
        this.mDataset = mDataset;
    }

    public List<String> getmImgset() {
        return mImgset;
    }

    public void setmImgset(List<String> mImgset) {
        this.mImgset = mImgset;
    }

    public static Map<String, String> getImgMatchingRes() {
        return imgMatchingRes;
    }

    public static void setImgMatchingRes(Map<String, String> imgMatchingRes) {
        ImgWords.imgMatchingRes = imgMatchingRes;
    }
}
