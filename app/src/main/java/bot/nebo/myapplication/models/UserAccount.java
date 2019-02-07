package bot.nebo.myapplication.models;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class UserAccount extends LitePalSupport {

    private String nikName;
    private String password;
    private String site;
    @Column(defaultValue = "false")
    private boolean isVerify;

    public String getNikName() {
        return nikName;
    }

    public void setNikName(String nikName) {
        this.nikName = nikName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }

}