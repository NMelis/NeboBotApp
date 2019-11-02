package bot.nebo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import br.tiagohm.markdownview.MarkdownView;
import io.fabric.sdk.android.Fabric;

public class MenuActivity extends AppCompatActivity {
    MarkdownView markdownviewNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        markdownviewNew = (MarkdownView) findViewById(R.id.markdownviewNew);
        markdownviewNew.loadMarkdownFromUrl(VKApplication.urlStorageMds + "lastNew.md");

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
