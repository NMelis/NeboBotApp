package bot.nebo.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import bot.nebo.myapplication.helper.Helper;
import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.CollectRevenueListener;
import ru.nebolife.bot.core.listeners.DeliveryListener;
import ru.nebolife.bot.core.listeners.RevenueBuildListener;

public class ManagerWorkActivity extends AppCompatActivity {
    private Button btnManagerStart;
    private RadioGroup radioGroup;
    private CheckBox checkBoxLoad;
    private CheckBox checkBoxDelivery;
    private CheckBox checkBoxBuy;
    private LinearLayout logLayout;
    private ScrollView scrollViewLog;
    private boolean isRunManager = false;
    private boolean cP = false;
    private boolean dP = false;
    private boolean bP = false;
    private boolean stopBot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_work);
        Fabric.with(this, new Crashlytics());
        radioGroup = findViewById(R.id.radioGroupLvlBuyProduct);
        checkBoxLoad = findViewById(R.id.checkBoxLoad);
        checkBoxDelivery = findViewById(R.id.checkBoxDelivery);
        checkBoxBuy = findViewById(R.id.checkBoxBuy);
        btnManagerStart = findViewById(R.id.btnStartManager);
        logLayout = findViewById(R.id.logLayout);
        scrollViewLog = findViewById(R.id.scrollViewLogs);
        AddAccountActivity.botClient.unStop();
    }

    public void startManager(View view){
        AddAccountActivity.botClient.unStop();
        btnManagerStart.setEnabled(false);
        isRunManager = true;
        btnManagerStart.setText("Ждемс....");

        Helper.log("Start manager");
        addLog("Менеджер начал работу");
        int checked = radioGroup.getCheckedRadioButtonId();
        final String c;
        switch (checked){
            case R.id.middle:
                c = "B";
                break;
            case R.id.high:
                c = "C";
                break;
            default:
                c = "A";
                break;

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AddAccountActivity.botClient.runManager(new RevenueBuildListener() {
                        @Override
                        public void onFinish() {
                            cP = true;
                            addLog("Сборка выручки закончена");
                        }

                        @Override
                        public void onRevenue(String floor) {
                            addLog(floor);
                        }
                    }, new DeliveryListener() {
                        @Override
                        public void onDelivery(String floor) {
                            addLog(floor);
                        }

                        @Override
                        public void onFinish() {
                            dP = true;
                            addLog("Доставка товаров закончена");
                        }
                    }, new CollectRevenueListener() {
                        @Override
                        public void onBuy(String floor, String price) {
                            addLog(floor + "  " + price);
                        }

                        @Override
                        public void onFinish() {
                            bP = true;
                            addLog("Закупка товаров закончена");
                        }
                    }, c, checkBoxLoad.isChecked(), checkBoxDelivery.isChecked(), checkBoxBuy.isChecked());
                } catch (StopBotException e) {
                    cP = dP = bP = isRunManager = true;
                    addLog("Бот был приостановлен");
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
                logLayout.addView(textView, 0);
                if (cP && dP && bP && isRunManager) {
                    isRunManager = false;
                    btnManagerStart.setEnabled(true);
                    btnManagerStart.setText("Начать");
                    addLog("Менеджер закончил работу");
                    cP = false;
                    dP = false;
                    bP = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!isRunManager)
            super.onBackPressed();
        else {
            if (stopBot) {
                AddAccountActivity.botClient.stop();
                cP = dP = bP = isRunManager = false;
                addLog("");
                Toast.makeText(this, "Бот был остановлен", Toast.LENGTH_LONG).show();
                stopBot = false;
                return;
            }
            Toast.makeText(this, "Нажмите еще раз чтобы остановить бота", Toast.LENGTH_LONG).show();
            stopBot = true;
        }

    }
}
