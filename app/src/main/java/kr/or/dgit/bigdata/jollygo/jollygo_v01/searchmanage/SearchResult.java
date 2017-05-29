package kr.or.dgit.bigdata.jollygo.jollygo_v01.searchmanage;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by rohgoon on 2017-05-27.
 */

public class SearchResult {// ou : 이미지 pt : 제목 ru : 링크주소
    private String ou;
    private String pt;
    private String ru;
    private Bitmap bitmap;

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "ou:" + ou +
                "\npt:" + pt +
                "\nru:" + ru;
    }
}
