package kr.or.dgit.bigdata.jollygo.jollygo_v01.searchmanage;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by rohgoon on 2017-05-27.
 */

public class SearchResult {
    private String title;
    private String addr;
    private Bitmap bitmap;

    public SearchResult(String title, String addr, Bitmap bitmap) {
        this.title = title;
        this.addr = addr;
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public String getAddr() {
        return addr;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
