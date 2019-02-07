package bot.nebo.myapplication.models;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {

    private String vkFirstName;
    private String vkLastName;
    private Boolean isBaned;
    private Boolean isTester;
    private String vkId;

    public Boolean getBaned() {
        return isBaned;
    }

    public void setBaned(Boolean baned) {
        isBaned = baned;
    }

    public Boolean getTester() {
        return isTester;
    }

    public void setTester(Boolean tester) {
        isTester = tester;
    }


    public String getVkFirstName() {
        return vkFirstName;
    }

    public void setVkFirstName(String vkFirstName) {
        this.vkFirstName = vkFirstName;
    }

    public String getVkLastName() {
        return vkLastName;
    }

    public void setVkLastName(String vkLastName) {
        this.vkLastName = vkLastName;
    }

    public String  getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public String getVkEmail() {
        return vkEmail;
    }

    public void setVkEmail(String vkEmail) {
        this.vkEmail = vkEmail;
    }


    @Column(unique = true, defaultValue = "None")
    private String vkEmail;

}