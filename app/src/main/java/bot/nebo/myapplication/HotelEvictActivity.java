package bot.nebo.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.HotelListener;
import ru.nebolife.bot.core.models.Human;

public class HotelEvictActivity extends AppCompatActivity {
    private LinearLayout linearLayoutEvictScrollViewLogs;
    Button IdbntEvictAll;
    Button idbntEvictAllMinus;
    Button idbntEvictAllFree;
    Button idbntEvictAllDown9Lvl;
    Button idbntEvictAllDown9LvlMinus;
    private boolean isFinishWork = false;
    private boolean stopBot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_evict);
        linearLayoutEvictScrollViewLogs = findViewById(R.id.linearLayoutEvictScrollViewLogs);
        IdbntEvictAll = findViewById(R.id.IdbntEvictAll);
        idbntEvictAllMinus = findViewById(R.id.idbntEvictAllMinus);
        idbntEvictAllFree = findViewById(R.id.idbntEvictAllFree);
        idbntEvictAllDown9Lvl = findViewById(R.id.idbntEvictAllDown9Lvl);
        idbntEvictAllDown9LvlMinus = findViewById(R.id.idbntEvictAllDown9LvlMinus);
    }

    private void addLog(final String text) {
        final Activity activity = (Activity) this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = new TextView(getBaseContext());
                textView.setText(text);
                textView.setTextColor(Color.BLUE);
                linearLayoutEvictScrollViewLogs.addView(textView, 0);
            }
        });
    }

    public void humanInHotelEvict(final HashMap<String, ArrayList<String>> humanListForEvict) {
        blockAllBtn(false);
        isFinishWork = true;
        AddAccountActivity.botClient.unStop();
        addLog("Поехали...");
        final Activity activity = (Activity) this;
        new Thread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                try {
                    AddAccountActivity.botClient.go("/home");
                    AddAccountActivity.botClient.goHotel();

                    for (final Map.Entry<String, ArrayList<String>> human : humanListForEvict.entrySet()) {
                        AddAccountActivity.botClient.humanInHotelEvict(human.getKey(), new HotelListener() {
                            @Override
                            public void onError() {
                                addLog("Error");
                            }

                            @Override
                            public void onFinish() {
                                addLog(human.getValue().get(0) + " ["+ human.getValue().get(1) +"] - выселен");
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            blockAllBtn(true);
                            addLog("Задача выполнена");
                            isFinishWork = false;
                        }
                    });
                } catch (StopBotException e) {
                    isFinishWork = false;
                    Crouton.makeText(activity, "Бот остоновлен", Style.INFO).show();
                }
            }
        }).start();
    }

    public void bntEvictAll(View view) {
        HashMap<String, ArrayList<String>> humanList = new HashMap<>();
        for (final Human human : AddAccountActivity.botClient.profile.hotel.humans) {
            humanList.put(human.link, new ArrayList<>(Arrays.asList(human.name, human.job)));
        }
        humanInHotelEvict(humanList);

    }

    public void bntEvictAllFree(View view) {
        HashMap<String, ArrayList<String>> humanList = new HashMap<>();
        for (final Human human : AddAccountActivity.botClient.profile.hotel.humans) {
            if (human.need == Human.Need.Free) {
                humanList.put(human.link, new ArrayList<>(Arrays.asList(human.name, human.job)));
            }
        }
        humanInHotelEvict(humanList);
    }

    public void bntEvictAllMinus(View view) {
        HashMap<String, ArrayList<String>> humanList = new HashMap<>();
        for (final Human human : AddAccountActivity.botClient.profile.hotel.humans) {
            if (human.need == Human.Need.Minus) {
                humanList.put(human.link, new ArrayList<>(Arrays.asList(human.name, human.job)));
            }
        }
        humanInHotelEvict(humanList);
    }

    public void bntEvictAllDown9Lvl(View view) {
        HashMap<String, ArrayList<String>> humanList = new HashMap<>();
        for (final Human human : AddAccountActivity.botClient.profile.hotel.humans) {
            if (human.lvl < 9) {
                humanList.put(human.link, new ArrayList<>(Arrays.asList(human.name, human.job)));
            }
        }
        humanInHotelEvict(humanList);
    }

    public void bntEvictAllDown9LvlMinus(View view) {
        HashMap<String, ArrayList<String>> humanList = new HashMap<>();
        for (final Human human : AddAccountActivity.botClient.profile.hotel.humans) {
            if (human.lvl < 9 || Human.Need.Minus == human.need) {
                humanList.put(human.link, new ArrayList<>(Arrays.asList(human.name, human.job)));
            }
        }
        humanInHotelEvict(humanList);
    }

    public void blockAllBtn(boolean flag) {
        IdbntEvictAll.setEnabled(flag);
        idbntEvictAllMinus.setEnabled(flag);
        idbntEvictAllFree.setEnabled(flag);
        idbntEvictAllDown9Lvl.setEnabled(flag);
        idbntEvictAllDown9LvlMinus.setEnabled(flag);
    }

    @Override
    public void onBackPressed() {
        if (!isFinishWork)
            super.onBackPressed();
        else {
            if (stopBot) {
                AddAccountActivity.botClient.stop();
                blockAllBtn(true);
                Crouton.makeText(this, "Бот был остановлен", Style.ALERT).show();
                stopBot = false;
                isFinishWork = false;
                return;
            }
            Crouton.makeText(this, "Нажмите еще раз чтобы остановить бота", Style.INFO).show();
            stopBot = true;
        }
    }
}