package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

/**
 * Created by rohgoon on 2017-06-08.
 */

public class uword {
    private int uwno;
    private String idToken;
    private int awno;
    private int uwcount;

    public int getUwno() {
        return uwno;
    }

    public void setUwno(int uwno) {
        this.uwno = uwno;
    }

    public String getidToken() {
        return idToken;
    }

    public void setidToken(String idToken) {
        this.idToken = idToken;
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
                "\nidToken:" + idToken +
                "\nawno:" + awno +
                "\nuwcount:" + uwcount;
    }
}
