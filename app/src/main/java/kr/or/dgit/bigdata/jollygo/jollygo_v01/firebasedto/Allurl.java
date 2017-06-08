package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

/**
 * Created by rohgoon on 2017-06-08.
 */

public class Allurl {
    private int auno;
    private String auurl;
    private String auimgurl;
    private String auname;
    private int aucount;

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
}
