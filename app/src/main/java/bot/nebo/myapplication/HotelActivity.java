package bot.nebo.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.GetOntInfoListener;

public class HotelActivity extends AppCompatActivity {
    TextView totalPlaceInHotel;
    TextView totalFreePlaceOnHotel;
    TextView totalHumanOnHotel;
    TextView humansWithPlus;
    TextView humansWithMinus;
    TextView humansNineLvlWithMinus;
    TextView humansNineLvlWithPlus;
    TextView musorPeople;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        totalPlaceInHotel = findViewById(R.id.totalPlaceOnHotel);
        totalFreePlaceOnHotel = findViewById(R.id.totalFreePlaceHotel);
        totalHumanOnHotel = findViewById(R.id.totalHumanOnHotel);
        humansWithPlus = findViewById(R.id.humansWithPlus);
        humansWithMinus = findViewById(R.id.humansWithMinus);
        humansNineLvlWithMinus = findViewById(R.id.humansNineLvlWithMinus);
        humansNineLvlWithPlus = findViewById(R.id.humansNineLvlWithPlus);
//        musorPeople = findViewById(R.id.musorPeople);

        if (AddAccountActivity.botClient == null) back();
        getData();
    }

    private void getData(){
        new Thread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                try {
                    AddAccountActivity.botClient.go("/home");
                    AddAccountActivity.botClient.goHotel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            totalPlaceInHotel.setText(Integer.toString(AddAccountActivity.botClient.profile.hotel.totalCountHuman));
                            totalFreePlaceOnHotel.setText(Integer.toString(AddAccountActivity.botClient.profile.hotel.totalFreePlace));
                            totalHumanOnHotel.setText(Integer.toString(AddAccountActivity.botClient.profile.hotel.totalCountHuman-AddAccountActivity.botClient.profile.hotel.totalFreePlace));
                            humansWithPlus.setText(Integer.toString(AddAccountActivity.botClient.profile.hotel.humansWithPlus));
                            humansWithMinus.setText(Integer.toString(AddAccountActivity.botClient.profile.hotel.humansWithMinus));
                            humansNineLvlWithMinus.setText(Integer.toString(AddAccountActivity.botClient.profile.hotel.humansNineLvlWithMinus));
                            humansNineLvlWithPlus.setText(Integer.toString(AddAccountActivity.botClient.profile.hotel.humansNineLvlWithPlus));
//                            musorPeople.setText(Integer.toString(AddAccountActivity.botClient.profile.hotel.totalFreePlace-(AddAccountActivity.botClient.profile.hotel.humansNineLvlWithMinus-AddAccountActivity.botClient.profile.hotel.humansWithMinus)));
                        }
                    });
                } catch (StopBotException e) {
                }
            }
        }).start();
    }

    private void back(){
        Intent intent = new Intent(getBaseContext(), ListAccountActivity.class);
        startActivity(intent);
    }

    public void btnHumanInHotelEvict(View v) {
        Intent intent = new Intent(getBaseContext(), HotelEvictActivity.class);
        startActivity(intent);
    }

}
