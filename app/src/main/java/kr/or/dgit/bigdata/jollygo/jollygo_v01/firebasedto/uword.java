package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

/**
 * Created by rohgoon on 2017-06-08.
 */

public class uword {
    private int uwno;
    private int uno;
    private int awno;
    private int uwcount;

    public int getUwno() {
        return uwno;
    }

    public void setUwno(int uwno) {
        this.uwno = uwno;
    }

    public int getUno() {
        return uno;
    }

    public void setUno(int uno) {
        this.uno = uno;
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
                "\nuno:" + uno +
                "\nawno:" + awno +
                "\nuwcount:" + uwcount;
    }
}
