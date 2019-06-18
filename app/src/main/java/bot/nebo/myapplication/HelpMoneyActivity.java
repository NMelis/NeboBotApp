package bot.nebo.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import bot.nebo.myapplication.helper.Helper;
import io.fabric.sdk.android.Fabric;

public class HelpMoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_money);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        Helper.log("Open Help Project ->");

    }

    public void shareLink(View view){
        Helper.log("Open Help Project -> Try shared Project");
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareBody = "Бот для Небоскребов (nebo.mobi)";
        String shareSub = "Бот android приложение для игры Небоскребы (nebo.mobi): " +
                                "Подробнее https://blog.nebolife.ru/2019/02/nebo-bot-android.html" +
                                " " +
                                "Группа в вк: https://vk.com/club139649001";
        myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
        myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
        startActivity(Intent.createChooser(myIntent, "Рассказать друзьям"));
    }

    public void helpMoney(View view) {
        Intent intent = new Intent(getBaseContext(), MoneySponsorListActivity.class);
        startActivity(intent);
    }
}
