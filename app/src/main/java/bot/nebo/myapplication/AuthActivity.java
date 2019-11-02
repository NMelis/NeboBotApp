package bot.nebo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;

import org.litepal.LitePal;

import java.util.HashMap;

import bot.nebo.myapplication.helper.Helper;
import bot.nebo.myapplication.models.User;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.CheckInstance;
import ru.nebolife.bot.core.listeners.NewVersionAppInterface;


public class AuthActivity extends AppCompatActivity {
    static User user;
    EditText myName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_auth);
        myName = findViewById(R.id.myName);

        Helper.log("Login to app");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AuthActivity.user = LitePal.findFirst(User.class);
        System.out.println("user" + user);
        checkNewVersionApp();

        if (this.checkInternet()){
            Crouton.makeText(this, "Нету доступ к интернету", Style.INFO).show();
            return;
        }
        if (user != null){
            if (user.getBaned()) {baned(); return;}
            start(user.getVkId());
        }
    }

    public void onAuth(View view){
        if (this.checkInternet()){
            Crouton.makeText(this, "Нету доступ к интернету", Style.INFO).show();
            return;
        }

        if (myName.getText().toString().equals("")){
            Crouton.makeText(this, "Введите хотябы кличку собаки вашего соседа из университете", Style.ALERT).show();
            return;
        }
        if (AuthActivity.user == null) {
            AuthActivity.user = new User();
            AuthActivity.user.setVkId("123123123123");
            AuthActivity.user.setVkFirstName(myName.getText().toString());
            AuthActivity.user.setVkLastName("41241");
            AuthActivity.user.save();
        }
        Crouton.makeText(this, "Привет, " + user.getVkFirstName() + "!", Style.INFO).show();

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return !activeNetwork.isConnectedOrConnecting();
    }

    private void start(String value){
        try {
            Helper.RequestCore(false).isUserVkBaned(value, new CheckInstance() {
                @Override
                public void onResponse(boolean b) {
                    if (b){
                        baned();
                    }else {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (StopBotException e) {
            e.printStackTrace();
        }
    }
    private void checkNewVersionApp(){
        try {
            Helper.RequestCore(true).getLastNewVersion(MainActivity.VERSION_APP, new NewVersionAppInterface() {
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

    private void baned(){
        Intent intent = new Intent(getBaseContext(), BanedActivity.class);
        startActivity(intent);
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
