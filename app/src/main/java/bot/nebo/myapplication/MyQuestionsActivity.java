package bot.nebo.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import bot.nebo.myapplication.helper.Helper;
import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.core.works.Lift;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.QuestsListener;

public class MyQuestionsActivity extends AppCompatActivity {
    private LinearLayout liftLogLayout;
    private ScrollView scrollView;
    private boolean isFinishWork = false;
    private Button btnStartWorkMyQuestions;
    Lift lift;
    private boolean stopBot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_questions);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        btnStartWorkMyQuestions = findViewById(R.id.btnStartWorkMyQuestions);

        liftLogLayout = findViewById(R.id.liftLogLayout);
        scrollView = findViewById(R.id.liftScrollViewLogs);
        lift = AddAccountActivity.botClient.Lift();
        if (AddAccountActivity.botClient == null) back();
        AddAccountActivity.botClient.unStop();

    }

    public void startWork(View view) {
        AddAccountActivity.botClient.unStop();
        Helper.log("Start my questions");
        runWork(false);
        isFinishWork = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AddAccountActivity.botClient.getQuestsAward(new QuestsListener() {
                        @Override
                        public void onCatch(String s, String s1, String s2) {
                            addLog("За задание: \""+s + "\" получен - Монет: " + s1 + " и Долларов: " + s2);
                        }

                        @Override
                        public void onFinish() {
                            isFinishWork = false;
                            addLog("Все награды за задание получены");
                        }
                    });
                } catch (StopBotException e) {
                    isFinishWork = false;
                    addLog("Бот был приостановлен");
                }
            }}).start();
    }

    private void addLog(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = new TextView(getBaseContext());
                textView.setText(text);
                textView.setTextColor(Color.BLUE);
                liftLogLayout.addView(textView, 0);
                if (!isFinishWork) {
                    Toast.makeText(getBaseContext(), "Бот закончил работу", Toast.LENGTH_SHORT).show();
                    btnStartWorkMyQuestions.setEnabled(true);
                    btnStartWorkMyQuestions.setText("Получить награды за задание");
                    isFinishWork = false;
                    runWork(true);
                }
            }
        });
    }

    private void back(){
        Intent intent = new Intent(getBaseContext(), ListAccountActivity.class);
        startActivity(intent);
    }


    private void runWork(boolean is){
        btnStartWorkMyQuestions.setEnabled(is);
    }

    @Override
    public void onBackPressed() {
        if (!isFinishWork)
            super.onBackPressed();
        else {
            if (stopBot) {
                AddAccountActivity.botClient.stop();
                runWork(true);
                Toast.makeText(this, "Бот был остановлен", Toast.LENGTH_LONG).show();
                stopBot = false;
                isFinishWork = false;
                return;
            }
            Toast.makeText(this, "Нажмите еще раз чтобы остановить бота", Toast.LENGTH_LONG).show();
            stopBot = true;
        }

    }

}
