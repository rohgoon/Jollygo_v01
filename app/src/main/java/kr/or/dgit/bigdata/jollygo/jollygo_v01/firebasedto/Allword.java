package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

/**
 * Created by rohgoon on 2017-06-08.
 */

public class Allword {
    private int awno;
    private String awname;
    private String awimgurl;
    private int awcount;

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
}
