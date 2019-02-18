package bot.nebo.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import bot.nebo.myapplication.helper.Helper;
import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.core.works.City;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.GetOntInfoListener;

public class InviterActivity extends AppCompatActivity {
    int minDays = 0;
    int maxDays = 3000;
    int minLvl = 0;
    int maxLvl = 80;
    EditText editTextMinLvl;
    EditText editTextMaxLvl;
    EditText editTextMinDays;
    EditText editTextMaxDays;
    Button btnStartInvite;

    City city;

    private LinearLayout inviterLogLayout;
    private ScrollView inviterScrollViewLogs;
    private boolean isFinishWork = false;
    private int invite = 0;
    private boolean stopBot = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviter);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        editTextMinDays = findViewById(R.id.editTextMinDays);
        editTextMaxDays = findViewById(R.id.editTextMaxDays);
        editTextMinLvl = findViewById(R.id.editTextMinLvl);
        editTextMaxLvl = findViewById(R.id.editTextMaxLvl);
        inviterLogLayout = findViewById(R.id.inviterLogLayout);
        inviterScrollViewLogs = findViewById(R.id.inviterScrollViewLogs);
        btnStartInvite = findViewById(R.id.btnStartInvite);

        Answers.getInstance().logCustom(new CustomEvent("Open page invite"));
    }

    private void reloadEditTexts(){
        editTextMinLvl.setText(Integer.toString(minLvl));
        editTextMaxLvl.setText(Integer.toString(maxLvl));
        editTextMinDays.setText(Integer.toString(minDays));
        editTextMaxDays.setText(Integer.toString(maxDays));
    }

    private void getResultWithEditTexts(){
        minLvl = Integer.parseInt(editTextMinLvl.getText().toString());
        maxLvl = Integer.parseInt(editTextMaxLvl.getText().toString());
        minDays = Integer.parseInt(editTextMinDays.getText().toString());
        maxDays = Integer.parseInt(editTextMaxDays.getText().toString());
    }

    public void onAllUsers(View view) {
        minDays = 0;
        maxDays = 3000;
        minLvl = 0;
        maxLvl = 85;
        reloadEditTexts();
    }

    public void onMiddleUsers(View view) {
        minDays = 0;
        maxDays = 3000;
        minLvl = 30;
        maxLvl = 60;
        reloadEditTexts();
    }

    public void onHigthUsers(View view) {
        minDays = 0;
        maxDays = 3000;
        minLvl = 60;
        maxLvl = 80;
        reloadEditTexts();
    }

    public void onStartInvite(View v){
        getResultWithEditTexts();
        city = AddAccountActivity.botClient.City();
        AddAccountActivity.botClient.unStop();

        Helper.logWithParametrs("Start invite", "city", AddAccountActivity.botClient.profile.city.name,
                "cityLvl", String.valueOf(AddAccountActivity.botClient.profile.city.lvl));
        btnStartInvite.setEnabled(false);
        btnStartInvite.setText("Ждем");
        isFinishWork = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    city.runInvite(minLvl, maxLvl, minDays, maxDays, new GetOntInfoListener() {
                        @Override
                        public void response(String s) {
                            addLog(s);
                        }
                    });
                } catch (StopBotException e) {
                    isFinishWork = false;
                    addLog("Приглашалка остановлен");
                }
            }
        }).start();
    }

    private void addLog(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = new TextView(getBaseContext());
                textView.setText(text);
                textView.setTextColor(Color.BLUE);
                inviterLogLayout.addView(textView, 0);
                if (!isFinishWork) {
                    btnStartInvite.setEnabled(true);
                    btnStartInvite.setText("Начать");
                    isFinishWork = false;
                    invite = 0;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!isFinishWork)
            super.onBackPressed();
        else {
            if (stopBot) {
                AddAccountActivity.botClient.stop();
                Toast.makeText(this, "Бот был остановлен", Toast.LENGTH_LONG).show();
                stopBot = false;
                isFinishWork = false;
                btnStartInvite.setEnabled(true);
                btnStartInvite.setText("Начать");
                return;
            }
            Toast.makeText(this, "Нажмите еще раз чтобы остановить бота", Toast.LENGTH_LONG).show();
            stopBot = true;
        }

    }
}
