package bot.nebo.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.GetOntInfoListener;
import ru.nebolife.bot.core.listeners.NewYearStoreListener;

public class NewYearStoreActivity extends AppCompatActivity {
    TextView snows;
    TextView p1;
    TextView p2;
    TextView p3;
    TextView p4;
    TextView p5;
    private boolean isFinishWork;
    private ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_year_store);
        scrollView = findViewById(R.id.nyScrollViewLogs);
        snows = findViewById(R.id.snows);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        p5 = findViewById(R.id.p5);
        this.collectInfo();
    }

    private void collectInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AddAccountActivity.botClient.go("/ny");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snows.setText(Integer.toString(AddAccountActivity.botClient.profile.newYearStoreProfile.Snows));
                            p1.setText(Integer.toString(AddAccountActivity.botClient.profile.newYearStoreProfile.Product1ChristmasDecorations));
                            p2.setText(Integer.toString(AddAccountActivity.botClient.profile.newYearStoreProfile.Product2StuffedToys));
                            p3.setText(Integer.toString(AddAccountActivity.botClient.profile.newYearStoreProfile.Product3Gifts));
                            p4.setText(Integer.toString(AddAccountActivity.botClient.profile.newYearStoreProfile.Product4Masks));
                            p5.setText(Integer.toString(AddAccountActivity.botClient.profile.newYearStoreProfile.Product5Fireworks));
                        }
                    });
                } catch (StopBotException e) {
                    addLog("Бот был приостановлен");
                }
            }
        }).start();
    }

    public void doIt(View view) {
    }


    private void addLog(final String text){
        final Activity activity = (Activity) this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = new TextView(getBaseContext());
                textView.setText(text);
                textView.setTextColor(Color.BLUE);
                scrollView.addView(textView, 0);
                if (!isFinishWork) {
                    Crouton.makeText(activity, "Работа в новогодней лавочке закончена", Style.INFO).show();
                    isFinishWork = false;
                }
            }
        });
    }
}
