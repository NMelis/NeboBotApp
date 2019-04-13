package bot.nebo.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import bot.nebo.myapplication.helper.Helper;
import br.tiagohm.markdownview.MarkdownView;
import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.NewVersionAppInterface;

public class NewVersionAppActivity extends AppCompatActivity {
    private int clickCountBack = 0;
    private boolean pressVolumeUp = false;
    TextView updateTextWithVersion;
    TextView desc;
    Button buttonTg;
    Button buttonYa;
    Button buttonMega;
    String tgLink;
    String yaLink;
    String megaLink;
    String gDriveLink;
    MarkdownView markdownView;
    boolean required = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_version_app);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        markdownView = findViewById(R.id.markdown_view);
        buttonTg = findViewById(R.id.btnTg);
        buttonYa = findViewById(R.id.btnYa);
        buttonMega = findViewById(R.id.btnMega);
        Helper.log("Open page New version app");
        markdownView.loadMarkdownFromUrl("https://s3.eu-west-3.amazonaws.com/nebo-bot/textNew.md");
        try {
            new RequestCore("").getLastNewVersion(MainActivity.VERSION_APP, new NewVersionAppInterface() {
                @Override
                public void onResponse(final HashMap<String, Object> hashMap) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            required = (boolean) hashMap.get("required");
                            if (!required) {
                                Toast.makeText(getApplicationContext(), "Это обновление необезательное, можете не устанавливать его сейчас", Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "Нажмите назад (стрелочку) чтобы продолжить использование приложение без обновление", Toast.LENGTH_LONG).show();
                            }
                            JSONObject links = (JSONObject) hashMap.get("links");
                            setTitle((String) hashMap.get("desc"));
                            try {
                                tgLink = links.getString("tg");
                                megaLink = links.getString("mega");
                                gDriveLink = links.getString("gD");
                                yaLink = links.getString("ya");
                            } catch (JSONException e) {
                                Crashlytics.logException(e);
                            }

                        }
                    });
                }
            });
        } catch (StopBotException e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (clickCountBack == 5) {
                pressVolumeUp = true;
                Go();
            }
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) clickCountBack++;
        if (keyCode == KeyEvent.KEYCODE_BACK){
            System.out.println(required);
            if (!required){
                super.onBackPressed();
                return super.onKeyLongPress(keyCode, event);
            }
            Toast.makeText(this, "Это обновление обязательнен к установке", Toast.LENGTH_LONG).show();
            return false;

        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onBackPressed() {
    }

    public void Go() {
        if (clickCountBack == 5 && pressVolumeUp){
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("TO_NEED_CHECK", "yes");
            startActivity(intent);
        }

        System.out.println(clickCountBack);
        System.out.println(pressVolumeUp);
    }

    public void btnTg(View view) {
        Helper.logWithParametr("Open page New version app", "click", "Telegram");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tgLink));
        startActivity(browserIntent);
    }

    public void btnYa(View view) {
        Helper.logWithParametr("Open page New version app", "click", "Yandex");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(yaLink));
        startActivity(browserIntent);
    }

    public void btnMega(View view) {
        Helper.logWithParametr("Open page New version app", "click", "Mega");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(megaLink));
        startActivity(browserIntent);
    }

}
