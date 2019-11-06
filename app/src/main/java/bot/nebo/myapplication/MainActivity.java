package bot.nebo.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.litepal.LitePal;

import java.util.HashMap;

import bot.nebo.myapplication.helper.Helper;
import bot.nebo.myapplication.models.User;
import bot.nebo.myapplication.models.UserAccount;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.fabric.sdk.android.Fabric;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.BreadcrumbBuilder;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.NewVersionAppInterface;

public class MainActivity extends Activity {
    // TODO To need change version & isDev every build apk
    public static final float VERSION_APP = (float) 1.4;
    public static final boolean isDev = false;
    public static String BASE_CDN_URL = "http://dfcfx0pfka9xy.cloudfront.net/";
    static User user;
    Button btnLoadAccounts;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        if (!MainActivity.isDev) Sentry.init("https://822abac97e054180a1229c9f2d589926@sentry.io/1810160", new AndroidSentryClientFactory(this));
        btnLoadAccounts = findViewById(R.id.btnLoadAccount);
        TextView textViewVersionApp = findViewById(R.id.textViewVersionApp);
        textViewVersionApp.setText("Версия приложение: "+ VERSION_APP);
        UserAccount userAccount = LitePal.findFirst(UserAccount.class);
        if (userAccount == null) btnLoadAccounts.setEnabled(false);
        Helper.log("Main");


        user = LitePal.findFirst(User.class);

        Bundle extra = getIntent().getExtras();
        if (extra == null) checkNewVersionApp();

        if (!user.isAcceptLicense()){
            Intent intent = new Intent(getBaseContext(), LicenseActivity.class);
            startActivity(intent);
        }

        Crouton.makeText(this, "Привет, " + user.getVkFirstName(), Style.CONFIRM).show();

    }
    public void loadAccount(View v){
        UserAccount userAccount = LitePal.findFirst(UserAccount.class);
        if (userAccount == null) {
            Crouton.makeText(this, "У вас нету сохраненных аккаунтов", Style.ALERT).show();
        }
        Intent intent = new Intent(this, ListAccountActivity.class);
        startActivity(intent);

    }

    public void addAccount(View view) {
        Intent intent = new Intent(this, AddAccountActivity.class);
        startActivity(intent);

    }
    private void checkNewVersionApp(){
        try {
            Helper.RequestCore(true).getLastNewVersion(VERSION_APP, new NewVersionAppInterface() {
                @Override
                public void onResponse(HashMap<String, Object> hashMap) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getBaseContext(), NewVersionAppActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            });
        } catch (StopBotException e) {
            e.printStackTrace();
        }
    }

    public void onCannelTG(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(VKApplication.urlTGChannel));
        startActivity(browserIntent);
    }

    public void onGroupVK(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(VKApplication.urlVKGroup));
        startActivity(browserIntent);
    }

    public void onSupport(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(VKApplication.urlSupport));
        startActivity(browserIntent);
    }

    public void onNews(View view) {
        Intent intent = new Intent(getBaseContext(), NewsActivity.class);
        startActivity(intent);
    }

    public void onUpdates(View view) {
        Intent intent = new Intent(getBaseContext(), NewVersionAppActivity.class);
        intent.putExtra("canBack", "yes");
        startActivity(intent);
    }

    public void helpMoney(View view) {
        Intent intent = new Intent(getBaseContext(), HelpMoneyActivity.class);
        startActivity(intent);
    }
}
