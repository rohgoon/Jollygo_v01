package kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rohgoon on 2017-05-25.
 */

public class ImgWords{

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

