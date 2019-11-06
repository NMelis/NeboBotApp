package bot.nebo.myapplication;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import org.litepal.LitePal;

import bot.nebo.myapplication.helper.Helper;
import io.fabric.sdk.android.Fabric;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

public class VKApplication extends Application {
    public static final String urlStorageMds = "http://dfcfx0pfka9xy.cloudfront.net/";
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
        if (!MainActivity.isDev) Sentry.init("https://822abac97e054180a1229c9f2d589926@sentry.io/1810160", new AndroidSentryClientFactory(this));
        LitePal.initialize(this);
        Helper.log("Open app");
    }
}
