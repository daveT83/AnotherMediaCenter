package Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicInformation {
    private String userName;

    private String password;

    private boolean isPasswordRequired;
    private SimpleDateFormat dateFormat;
    private String backgroundPath;
    private String userPicturePath;
    private boolean isCurrentActive;

    public boolean getIsPasswordRequired() {
        return isPasswordRequired;
    }

    public void setPasswordRequired(boolean passwordRequired) {
        isPasswordRequired = passwordRequired;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getUserPicturePath() {
        return userPicturePath;
    }

    public void setUserPicturePath(String userPicturePath) {
        this.userPicturePath = userPicturePath;
    }

    public boolean getIsCurrentActive() {
        return isCurrentActive;
    }

    public void setIsCurrentActive(boolean currentActive) {
        isCurrentActive = currentActive;
    }

    public String getDate(){
        return dateFormat.format(new Date());
    }


}
