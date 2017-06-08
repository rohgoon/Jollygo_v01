package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

/**
 * Created by rohgoon on 2017-06-08.
 */

public class Uword {
    private int uwno;
    private String uid;
    private int awno;
    private int uwcount;

    public Uword() {
    }

    public Uword(int uwno, String uid, int awno, int uwcount) {
        this.uwno = uwno;
        this.uid = uid;
        this.awno = awno;
        this.uwcount = uwcount;
    }

    public int getUwno() {
        return uwno;
    }

    public void setUwno(int uwno) {
        this.uwno = uwno;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public int getAwno() {
        return awno;
    }

    public void setAwno(int awno) {
        this.awno = awno;
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
                "\nawno:" + awno +
                "\nuwcount:" + uwcount;
    }
}
