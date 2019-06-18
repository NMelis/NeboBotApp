package bot.nebo.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import bot.nebo.myapplication.helper.Helper;
import io.fabric.sdk.android.Fabric;

public class MoneySponsorListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_sponsor_list);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        Helper.log("Open Help Money page");
    }

    public void Yandex(View view){
        Helper.log("Open Help -> Yandex");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(VKApplication.urlYandexMoney));
        startActivity(browserIntent);
    }

    public void YandexNumber(View view){
        Helper.log("Open Help -> YandexNumber");
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Скопирован Yandex номер: ", VKApplication.urlWebMoney);
        clipboard.setPrimaryClip(clip);
    }

    public void WebMoney(View view){
        Helper.log("Open Help -> WebMoney");
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Скопирован WebMoney номер, wmr:", VKApplication.urlWebMoney);
        clipboard.setPrimaryClip(clip);
    }

    public void Qiwi(View view){
        Helper.log("Open Help -> Qiwi");
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Скопирован Qiwi номер телефон:", VKApplication.urlQiwi);
        clipboard.setPrimaryClip(clip);
    }
}
