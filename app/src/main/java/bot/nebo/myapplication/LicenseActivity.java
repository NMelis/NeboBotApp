package bot.nebo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import org.litepal.LitePal;
import bot.nebo.myapplication.models.User;


public class LicenseActivity extends AppCompatActivity {

    static User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        user = LitePal.findFirst(User.class);
    }

    public void acceptLic(View view) {
        user.setAcceptLicense(true);
        user.save();

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
}
