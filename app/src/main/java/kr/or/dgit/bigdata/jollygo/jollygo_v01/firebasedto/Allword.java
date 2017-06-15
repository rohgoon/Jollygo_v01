package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rohgoon on 2017-06-08.
 */
@IgnoreExtraProperties
public class Allword {
    private int awno;
    private String awname;
    private String awimgurl;
    private int awcount;
    public Map<String, Boolean> awcstars = new HashMap<>();

    public Allword() {
    }

    public Allword(int awno, String awname, String awimgurl, int awcount) {
        this.awno = awno;
        this.awname = awname;
        this.awimgurl = awimgurl;
        this.awcount = awcount;
    }

    public int getAwno() {
        return awno;
    }

    public void setAwno(int awno) {
        this.awno = awno;
    }

    public String getAwname() {
        return awname;
    }

    public void setAwname(String awname) {
        this.awname = awname;
    }

    public String getAwimgurl() {
        return awimgurl;
    }

    public void setAwimgurl(String awimgurl) {
        this.awimgurl = awimgurl;
    }

    public int getAwcount() {
        return awcount;
    }

    public void setAwcount(int awcount) {
        this.awcount = awcount;
    }

    @Override
    public String toString() {
        return "awno:" + awno +
                "\nawname:" + awname +
                "\nawimgurl:" + awimgurl +
                "\nawcount:" + awcount;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("awno", awno);
        result.put("awname", awname);
        result.put("awimgurl", awimgurl);
        result.put("awcount", awcount);
        result.put("awcstars", awcstars);

        return result;
    }
}
