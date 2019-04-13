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
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.litepal.LitePal;

import java.util.HashMap;

import bot.nebo.myapplication.helper.Helper;
import bot.nebo.myapplication.models.User;
import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.CheckInstance;
import ru.nebolife.bot.core.listeners.NewVersionAppInterface;


public class AuthActivity extends AppCompatActivity {
    static User user;
    String[] scope = new String[]{VKScope.EMAIL};
    Button authVkBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        Helper.log("Login to app");
        setContentView(R.layout.activity_auth);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        authVkBtn = (Button) findViewById(R.id.authVkBtn);
        AuthActivity.user = LitePal.findFirst(User.class);
        System.out.println("user" + user);
        checkNewVersionApp();

        if (this.checkInternet()){
            Toast.makeText(this, "Нету доступ к интернету", Toast.LENGTH_LONG).show();
            return;
        }
        if (user != null){
            if (user.getBaned()) {baned(); return;}
            start(user.getVkId());
        }
    }

    public void onAuth(View view){
        if (this.checkInternet()){
            Toast.makeText(this, "Нету доступ к интернету", Toast.LENGTH_LONG).show();
            return;
        }
        final VKAccessToken token = VKAccessToken.currentToken();
        if (token == null || token.isExpired()) { VKSdk.login(this, scope);}
        if (AuthActivity.user == null) {
            if (VKSdk.isLoggedIn()) {
                authVkBtn.setEnabled(false);
                Toast.makeText(this, "Секундочку...", Toast.LENGTH_SHORT).show();
                VKApi.users().get().executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKApiUser user = (VKApiUser) ((VKList) response.parsedModel).get(0);
                        Toast.makeText(getBaseContext(), user.first_name, Toast.LENGTH_LONG).show();
                        if (AuthActivity.user == null) {
                            AuthActivity.user = new User();
                            if (token.email != null) {
                                AuthActivity.user.setVkEmail(token.email);
                            }
                            AuthActivity.user.setVkId(String.valueOf(user.getId()));
                            AuthActivity.user.setVkFirstName(user.first_name);
                            AuthActivity.user.setVkLastName(user.last_name);
                            AuthActivity.user.save();
                        }
                        start(String.valueOf(user.getId()));

                    }
                });
            }

        }
        else {
            Toast.makeText(this, user.getVkFirstName(), Toast.LENGTH_LONG).show();
            start(user.getVkId());
        }
    }

    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return !activeNetwork.isConnectedOrConnecting();
    }

    private void start(String value){
        try {
            new RequestCore("").isUserVkBaned(value, new CheckInstance() {
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
            new RequestCore("").getLastNewVersion(MainActivity.VERSION_APP, new NewVersionAppInterface() {
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
