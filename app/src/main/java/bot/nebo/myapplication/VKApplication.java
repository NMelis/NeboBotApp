package bot.nebo.myapplication;

import android.app.Application;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

import org.litepal.LitePal;

import bot.nebo.myapplication.helper.Helper;
import io.fabric.sdk.android.Fabric;

public class VKApplication extends Application {
    public static final String urlStorageMds = "http://nebo-bot.s3-website.eu-west-3.amazonaws.com/";
    public static final String urlVKGroup = "https://vk.com/neboskrebot";
    public static final String urlTGChannel = "https://t.me/neboskrebot";
    public static final String urlSupport = "https://vk.com/topic-139649001_39187935";

    public static final String urlYandexMoney= "https://money.yandex.ru/to/410018158432823";
    public static final String urlWebMoney= "R591665965981";
    public static final String urlQiwi= "+79139248443";

    @Override
    public void onCreate() {
        super.onCreate();
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        LitePal.initialize(this);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Helper.log("Open app");
    }
}
