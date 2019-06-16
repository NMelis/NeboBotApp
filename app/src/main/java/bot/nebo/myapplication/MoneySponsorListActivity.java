package bot.nebo.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MoneySponsorListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_sponsor_list);
    }

    public void Yandex(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(VKApplication.urlYandexMoney));
        startActivity(browserIntent);
    }

    public void YandexNumber(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Скопирован Yandex номер: ", VKApplication.urlWebMoney);
        clipboard.setPrimaryClip(clip);
    }

    public void WebMoney(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Скопирован WebMoney номер, wmr:", VKApplication.urlWebMoney);
        clipboard.setPrimaryClip(clip);
    }

    public void Qiwi(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Скопирован Qiwi номер телефон:", VKApplication.urlQiwi);
        clipboard.setPrimaryClip(clip);
    }
}
