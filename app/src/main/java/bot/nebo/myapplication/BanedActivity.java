package bot.nebo.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import bot.nebo.myapplication.helper.Helper;
import io.fabric.sdk.android.Fabric;

public class BanedActivity extends AppCompatActivity {
    private int clickCountBack = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baned);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        Helper.log("Open baned page");
    }

    public void OpenVkGroupChat(View view){
        String url = "https://vk.com/im?sel=-139649001";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onBackPressed() {
        clickCountBack++;
        if (clickCountBack >= 15){
            Helper.log("Use hack to on back with baned page");
            AuthActivity.user.setBaned(false);
            AuthActivity.user.save();
            Intent intent = new Intent(getBaseContext(), AuthActivity.class);
            startActivity(intent);
        }
    }
}
