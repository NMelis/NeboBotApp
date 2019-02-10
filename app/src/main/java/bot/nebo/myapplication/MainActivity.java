package bot.nebo.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.litepal.LitePal;

import java.util.HashMap;

import bot.nebo.myapplication.models.User;
import bot.nebo.myapplication.models.UserAccount;
import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.NewVersionAppInterface;

public class MainActivity extends Activity {
    public static final float VERSION_APP = (float) 0.7;
    public static final boolean isDev = true;
    static User user;
    Button btnLoadAccounts;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics());
        btnLoadAccounts = findViewById(R.id.btnLoadAccount);
        TextView textViewVersionApp = findViewById(R.id.textViewVersionApp);
        textViewVersionApp.setText("Версия приложение: "+ VERSION_APP);
        UserAccount userAccount = LitePal.findFirst(UserAccount.class);
        if (userAccount == null) btnLoadAccounts.setEnabled(false);


        user = LitePal.findFirst(User.class);
        if (!this.checkInternet()){
            Toast.makeText(this, "Нету доступ к интеренету", Toast.LENGTH_LONG).show();
        }

        Bundle extra = getIntent().getExtras();
        if (extra == null) checkNewVersionApp();


    }

    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnectedOrConnecting();
    }

    public void loadAccount(View v){
        UserAccount userAccount = LitePal.findFirst(UserAccount.class);
        if (userAccount == null) {
            Toast.makeText(this, "У вас нету сохраненных аккаунтов", Toast.LENGTH_SHORT).show();
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
            new RequestCore("").getLastNewVersion(VERSION_APP, new NewVersionAppInterface() {
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
}
