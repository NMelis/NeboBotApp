package bot.nebo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import com.crashlytics.android.Crashlytics;

import java.util.concurrent.TimeUnit;

import br.tiagohm.markdownview.MarkdownView;
import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.LiftListener;

public class MenuActivity extends AppCompatActivity {
    MarkdownView markdownviewNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        markdownviewNew = (MarkdownView) findViewById(R.id.markdownviewNew);
        markdownviewNew.loadMarkdownFromUrl(VKApplication.urlStorageMds + "lastNew.md");

        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class).setInitialDelay(1, TimeUnit.SECONDS).build();
        WorkManager.getInstance().enqueue(myWorkRequest);

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

    public static class MyWorker extends Worker {

        static final String TAG = "workmng";

        @NonNull
        @Override
        public WorkerResult doWork() {
            Log.d(TAG, "doWork: start");
            final int[] countUp = {0};
            try {
                AddAccountActivity.botClient.lift(new LiftListener() {
                    @Override
                    public void onUp() {
                        Log.d(TAG,"doWork: Поднимаем чела");
                    }

                    @Override
                    public void onTip(String s, String s1) {
                        String typeAward;
                        if (s.equals("gold")) typeAward = "One Доллар";
                        else typeAward = "Монеты";
                        Log.d(TAG,"doWork: Чел дал на чаевые " + s1 + " " + typeAward);
                        countUp[0]++;
                    }

                    @Override
                    public void onFinish() {
                        if (countUp[0] == 0) Log.d(TAG,"Лифт пустой");
                        else Log.d(TAG,"doWork: Лифтер закончил работу, он поднял " + countUp[0] + " челов");
                        countUp[0] = 0;
                    }
                });
            } catch (StopBotException e) {
                Log.d(TAG,"doWork: Бот был приостановлен");
            }
            Log.d(TAG, "doWork: end");

            return WorkerResult.SUCCESS;
        }
    }

}
