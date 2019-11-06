package bot.nebo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import br.tiagohm.markdownview.MarkdownView;
import io.fabric.sdk.android.Fabric;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

public class MenuActivity extends AppCompatActivity {
    MarkdownView markdownviewNew;
    TextView coin;
    TextView gold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        if (!MainActivity.isDev) Sentry.init("https://822abac97e054180a1229c9f2d589926@sentry.io/1810160", new AndroidSentryClientFactory(this));

        gold = findViewById(R.id.gold);
        coin = findViewById(R.id.coin);
        markdownviewNew = (MarkdownView) findViewById(R.id.markdownviewNew);
        markdownviewNew.loadMarkdownFromUrl(VKApplication.urlStorageMds + "lastNew.md");
        coin.setText(AddAccountActivity.botClient.profile.coin);
        gold.setText(AddAccountActivity.botClient.profile.gold);

    }

    public void btnAllNews(View view){
        Intent intent = new Intent(this, NewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void btnManager(View v){
        Intent intent = new Intent(this, ManagerWorkActivity.class);
        startActivity(intent);
    }

    public void startBtnLifter(View view) {
        Intent intent = new Intent(this, LifterActivity.class);
        startActivity(intent);
    }

    public void onInviter(View view) {
        Intent intent = new Intent(this, InviterActivity.class);
        startActivity(intent);
    }

    public void btnMyQuestions(View view) {
        Intent intent = new Intent(this, MyQuestionsActivity.class);
        startActivity(intent);
    }

    public void startBtnHotel(View view) {
        Intent intent = new Intent(this, HotelActivity.class);
        startActivity(intent);
    }

}
