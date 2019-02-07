package bot.nebo.myapplication.helper;


import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import org.litepal.LitePal;

import bot.nebo.myapplication.AddAccountActivity;
import bot.nebo.myapplication.models.User;

public class Helper {

    public static boolean checkRequestCore(Object requestCore){
        return requestCore == null;
    }


    private static String getUserName(){
        AddAccountActivity.user = LitePal.findFirst(User.class);
        String userName = "NoName";
        if (AddAccountActivity.user != null) userName = AddAccountActivity.user.getVkFirstName();
        return userName;
    }

    public static void log(String eventName){
        Answers.getInstance().logCustom(new CustomEvent(eventName)
                .putCustomAttribute("UserName", getUserName()));
    }
    public static void logWithParametr(String eventName, String key, String value){
        Answers.getInstance().logCustom(new CustomEvent(eventName)
                .putCustomAttribute("UserName", getUserName())
                .putCustomAttribute(key, value));
    }

    public static void logWithParametrs(String eventName, String key, String value, String key2, String value2){
        Answers.getInstance().logCustom(new CustomEvent(eventName)
                .putCustomAttribute("UserName", getUserName())
                .putCustomAttribute(key, value)
                .putCustomAttribute(key2, value2));
    }
}
