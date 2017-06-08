package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

/**
 * Created by rohgoon on 2017-06-08.
 */

public class Favlink {
    private int fno;
    private String furl;
    private String fimgurl;
    private String uid;
    private String fname;
    private int fcount;

    public Favlink() {
    }

    public Favlink(int fno, String furl, String fimgurl, String uid, String fname, int fcount) {
        this.fno = fno;
        this.furl = furl;
        this.fimgurl = fimgurl;
        this.uid = uid;
        this.fname = fname;
        this.fcount = fcount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFimgurl() {
        return fimgurl;
    }

    public void setFimgurl(String fimgurl) {
        this.fimgurl = fimgurl;
    }

    public int getFno() {
        return fno;
    }

    public void setFno(int fno) {
        this.fno = fno;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    @Override
    public String toString() {
        return "fno:" + fno +
                "\nfurl:" + furl +
                "\nfimgurl:" + fimgurl +
                "\nuid:" + uid +
                "\nfname:" + fname +
                "\nfcount:" + fcount;
    }

}
