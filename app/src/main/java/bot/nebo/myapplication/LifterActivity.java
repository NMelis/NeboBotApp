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
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.core.works.Lift;
import ru.nebolife.bot.core.listeners.GetOntInfoListener;
import ru.nebolife.bot.core.listeners.LiftGetAllDollarsListener;
import ru.nebolife.bot.core.listeners.LiftListener;

public class LifterActivity extends AppCompatActivity {
    private LinearLayout liftLogLayout;
    private ScrollView scrollView;
    private boolean isFinishWork = false;
    private int countUp = 0;
    private Button btnStartLiter;
    private Button btnCallVisitors;
    private Button btnPayAllDollars;
    private Button btnRunLiftOnGold;
    private Button btnCallAndRunLift;
    Lift lift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifter);
        Fabric.with(this, new Crashlytics());
        btnPayAllDollars = findViewById(R.id.btnPayAllGold);
        btnCallVisitors = findViewById(R.id.btnCallVisitors);
        btnRunLiftOnGold = findViewById(R.id.btnRunLiftOnGold);
        btnCallAndRunLift = findViewById(R.id.btnCallAndRunLift);
        liftLogLayout = findViewById(R.id.liftLogLayout);
        scrollView = findViewById(R.id.liftScrollViewLogs);
        btnStartLiter = findViewById(R.id.btnStartLiter);
        lift = AddAccountActivity.botClient.Lift();

    }

    public void startLifter(View view) {
        if (AddAccountActivity.botClient == null) {back(); return;}
        Answers.getInstance().logCustom(new CustomEvent("Start lifter"));
        runWork(false);
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
                    runWork(true);
                    countUp = 0;
                }
            }
        });
    }

    private void back(){
        Intent intent = new Intent(getBaseContext(), ListAccountActivity.class);
        startActivity(intent);
    }


    private void runWork(boolean is){
        btnStartLiter.setEnabled(is);
        btnCallVisitors.setEnabled(is);
        btnPayAllDollars.setEnabled(is);
        btnRunLiftOnGold.setEnabled(is);
        btnCallAndRunLift.setEnabled(is);
    }

    @Override
    public void onBackPressed() {
        if (!isFinishWork)
            super.onBackPressed();
        else
            Toast.makeText(this, "Нельзя выйти пока выполняеться задача", Toast.LENGTH_SHORT).show();

    }

    public void onBtnCallAndRunLift(View view) {
        runWork(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lift.activateLift(new GetOntInfoListener(){
                    @Override
                    public void response(String s) {
                        addLog(s);
                        lift.processLiftAll(new GetOntInfoListener(){
                            @Override
                            public void response(String s) {
                                addLog(s);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public void onBtnRunLiftOnGold(View view) {
        runWork(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lift.processLiftAll(new GetOntInfoListener(){
                    @Override
                    public void response(String s) {
                        addLog(s);
                    }
                });
            }
        }).start();
    }

    public void onBtnCallVisitors(View view) {
        runWork(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lift.activateLift(new GetOntInfoListener(){
                    @Override
                    public void response(String s) {
                        addLog(s);
                    }
                });
            }
        }).start();
    }

    public void onBtnPayAllGold(View view) {
        runWork(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lift.payAllDollars(new LiftGetAllDollarsListener() {
                    @Override
                    public void response(String s) {
                        addLog(s);
                    }
                });
            }
        }).start();
    }
}
