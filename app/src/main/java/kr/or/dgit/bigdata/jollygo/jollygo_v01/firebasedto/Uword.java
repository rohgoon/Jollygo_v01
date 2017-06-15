package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rohgoon on 2017-06-08.
 */
@IgnoreExtraProperties
public class Uword {
    private int uwno;
    private String uid;
    private String uwname;
    private int uwcount;
    public Map<String, Boolean> uwcstars = new HashMap<>();

    public Uword() {
    }

    public Uword(int uwno, String uid, String uwname, int uwcount) {
        this.uwno = uwno;
        this.uid = uid;
        this.uwname = uwname;
        this.uwcount = uwcount;
    }

    public int getUwno() {
        return uwno;
    }

    public void setUwno(int uwno) {
        this.uwno = uwno;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUwname() {
        return uwname;
    }

    public void setUwname(String uwname) {
        this.uwname = uwname;
    }

    public int getUwcount() {
        return uwcount;
    }

    public void setUwcount(int uwcount) {
        this.uwcount = uwcount;
    }

    @Override
    public String toString() {
        return "uwno:" + uwno +
                "\nuid:" + uid +
                "\nuwname:" + uwname +
                "\nuwcount:" + uwcount;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uwno", uwno);
        result.put("uid", uid);
        result.put("uwname", uwname);
        result.put("uwcount", uwcount);
        result.put("uwcstars", uwcstars);

        return result;
    }
}
