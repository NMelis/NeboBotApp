package bot.nebo.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import bot.nebo.myapplication.helper.Helper;
import br.tiagohm.markdownview.MarkdownView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.fabric.sdk.android.Fabric;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.NewVersionAppInterface;

public class NewVersionAppActivity extends AppCompatActivity {
    private int clickCountBack = 0;
    private boolean pressVolumeUp = false;
    TextView textView10;
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
        if (!MainActivity.isDev) Sentry.init("https://822abac97e054180a1229c9f2d589926@sentry.io/1810160", new AndroidSentryClientFactory(this));
        markdownView = findViewById(R.id.markdown_view);
        buttonTg = findViewById(R.id.btnTg);
        buttonYa = findViewById(R.id.btnYa);
        textView10 = findViewById(R.id.textView10);
        buttonMega = findViewById(R.id.btnMega);
        Helper.log("Open page New version app");
        final Activity activity = (Activity) this;
        markdownView.loadMarkdownFromUrl(MainActivity.BASE_CDN_URL + MainActivity.VERSION_APP + "/textNew.md");
        try {
            Helper.RequestCore(true).getLastNewVersion(MainActivity.VERSION_APP, new NewVersionAppInterface() {
                @Override
                public void onResponse(final HashMap<String, Object> hashMap) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            required = (boolean) hashMap.get("required");
                            float versionInLast = (float) hashMap.get("version");
                            if (versionInLast == MainActivity.VERSION_APP){
                                textView10.setText("Это обновление у вас уже установлено");
                            }
                            if (!required) {
                                Crouton.makeText(activity, "Это обновление необезательное, можете не устанавливать его сейчас", Style.INFO).show();
                                Crouton.makeText(activity, "Нажмите назад (стрелочку) чтобы продолжить использование приложение без обновление", Style.INFO).show();
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
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            String canBack = getIntent().getExtras().getString("canBack");
            if (canBack != null){
                super.onBackPressed();
                return super.onKeyLongPress(keyCode, event);
            }
        }


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
            Crouton.makeText(this, "Это обновление обязательнен к установке", Style.ALERT).show();
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
