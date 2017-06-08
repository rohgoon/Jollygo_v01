package kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto;

/**
 * Created by rohgoon on 2017-06-08.
 */

public class user {
    private String idToken;
    private String uid;
    private String uname;

    public String getidToken() {
        return idToken;
    }

    public void setidToken(String idToken) {
        this.idToken = idToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "idToken:" + idToken +
                "\nuid:" + uid +
                "\nuname:" + uname;
    }
}
