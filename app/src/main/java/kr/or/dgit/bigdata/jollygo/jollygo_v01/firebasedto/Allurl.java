package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rohgoon on 2017-06-08.
 */
@IgnoreExtraProperties
public class Allurl {
    private int auno;
    private String auurl;
    private String auimgurl;
    private String auname;
    private int aucount;
    public Map<String, Boolean> aucstars = new HashMap<>();

    public Allurl() {
    }

    public Allurl(int auno, String auurl, String auimgurl, String auname, int aucount) {
        this.auno = auno;
        this.auurl = auurl;
        this.auimgurl = auimgurl;
        this.auname = auname;
        this.aucount = aucount;
    }

    public String getAuimgurl() {
        return auimgurl;
    }

    public void setAuimgurl(String auimgurl) {
        this.auimgurl = auimgurl;
    }

    public int getAuno() {
        return auno;
    }

    public void setAuno(int auno) {
        this.auno = auno;
    }

    public String getAuurl() {
        return auurl;
    }

    public void setAuurl(String auurl) {
        this.auurl = auurl;
    }

    public String getAuname() {
        return auname;
    }

    public void setAuname(String auname) {
        this.auname = auname;
    }

    public int getAucount() {
        return aucount;
    }

    public void setAucount(int aucount) {
        this.aucount = aucount;
    }

    @Override
    public String toString() {
        return "auno=" + auno +
                "\nauurl:" + auurl +
                "\nauimgurl:" + auimgurl +
                "\nauname:" + auname +
                "\naucount:" + aucount;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("auno", auno);
        result.put("auurl", auurl);
        result.put("auimgurl", auimgurl);
        result.put("auname", auname);
        result.put("aucount", aucount);
        result.put("aucstars", aucstars);

        return result;
    }
}
