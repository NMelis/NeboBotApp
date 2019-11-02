package bot.nebo.myapplication.helper;


import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import org.litepal.LitePal;

import bot.nebo.myapplication.AddAccountActivity;
import bot.nebo.myapplication.MainActivity;
import bot.nebo.myapplication.models.User;
import ru.nebolife.bot.core.core.RequestCore;

public class Helper {
    public static boolean checkRequestCore(Object requestCore){
        return requestCore == null;
    }


    private static String getUserName(){
        AddAccountActivity.user = LitePal.findFirst(User.class);
        String userName = "NoName";
        if (AddAccountActivity.user != null) userName = AddAccountActivity.user.getVkFirstName();
        if (userName == null) {userName = "Хрень";}
        return userName;
    }

    public static void log(String eventName){
        String userName = getUserName();
        if (userName.equals("Keanu") || userName.equals("Melis") || MainActivity.isDev) return;
        Answers.getInstance().logCustom(new CustomEvent(eventName)
                .putCustomAttribute("UserName", userName));
    }
    public static void logWithParametr(String eventName, String key, String value){
        String userName = getUserName();
        if (userName.equals("Keanu") || userName.equals("Melis") || MainActivity.isDev) return;
        Answers.getInstance().logCustom(new CustomEvent(eventName)
                .putCustomAttribute("UserName", userName)
                .putCustomAttribute(key, value));
    }

    public static void logWithParametrs(String eventName, String key, String value, String key2, String value2){
        String userName = getUserName();
        if (userName.equals("Keanu") || userName.equals("Melis") || MainActivity.isDev) return;
        Answers.getInstance().logCustom(new CustomEvent(eventName)
                .putCustomAttribute("UserName", userName)
                .putCustomAttribute(key, value)
                .putCustomAttribute(key2, value2));
    }

    public static RequestCore RequestCore(boolean withVersion){
        if (withVersion)
            return new RequestCore(MainActivity.BASE_CDN_URL + MainActivity.VERSION_APP);
        else
            return new RequestCore(MainActivity.BASE_CDN_URL);
    }
}
