package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

/**
 * Created by rohgoon on 2017-06-08.
 */

public class favlink {
    private int fno;
    private String furl;
    private String fimgurl;
    private int uno;
    private int fcount;

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

    public int getUno() {
        return uno;
    }

    public void setUno(int uno) {
        this.uno = uno;
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
                "\nuno:" + uno +
                "\nfcount:" + fcount;
    }

}
