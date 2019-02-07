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
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.listeners.LiftListener;

public class LifterActivity extends AppCompatActivity {
    private LinearLayout liftLogLayout;
    private ScrollView scrollView;
    private boolean isFinishWork = false;
    private Button btnStartLiter;
    private int countUp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifter);
        Fabric.with(this, new Crashlytics());
        liftLogLayout = findViewById(R.id.liftLogLayout);
        scrollView = findViewById(R.id.liftScrollViewLogs);
        btnStartLiter = findViewById(R.id.btnStartLiter);
    }

    public void startLifter(View view) {
        if (AddAccountActivity.botClient == null) {back(); return;}
        Answers.getInstance().logCustom(new CustomEvent("Start lifter"));
        btnStartLiter.setEnabled(false);
        btnStartLiter.setText("Стоять смирно....");
        isFinishWork = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                AddAccountActivity.botClient.lift(new LiftListener() {
                    @Override
                    public void onUp() {
                        addLog("Поднимаем чела");
                    }

                    @Override
                    public void onTip(String s, String s1) {
                        String typeAward;
                        if (s.equals("gold")) typeAward = "One Доллар";
                        else typeAward = "Монеты";
                        addLog("Чел дал на чаевые " + s1 + " " + typeAward);
                        countUp++;
                    }

                    @Override
                    public void onFinish() {
                        isFinishWork = false;
                        if (countUp == 0) addLog("Лифт пустой");
                        else addLog("Лифтер закончил работу, он поднял " + countUp + " челов");
                        countUp = 0;
                    }
                });
            }}).start();
    }

    private void addLog(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = new TextView(getBaseContext());
                textView.setText(text);
                textView.setTextColor(Color.BLUE);
                textView.setPadding(0,0,0,5);
                liftLogLayout.addView(textView);
                scrollView.fullScroll(View.FOCUS_DOWN);
                if (!isFinishWork) {
                    Toast.makeText(getBaseContext(), "Лифтер закончил работу", Toast.LENGTH_SHORT).show();
                    btnStartLiter.setEnabled(true);
                    btnStartLiter.setText("Начать");
                    isFinishWork = false;
                    countUp = 0;
                }
            }
        });
    }

    private void back(){
        Intent intent = new Intent(getBaseContext(), ListAccountActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (!isFinishWork)
            super.onBackPressed();
        else
            Toast.makeText(this, "Нельзя выйти пока выполняеться задача", Toast.LENGTH_SHORT).show();

    }
}
