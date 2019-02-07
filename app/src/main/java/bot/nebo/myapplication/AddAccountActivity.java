package bot.nebo.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import bot.nebo.myapplication.helper.Helper;
import bot.nebo.myapplication.models.User;
import bot.nebo.myapplication.models.UserAccount;
import io.fabric.sdk.android.Fabric;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.core.sites.NeboMobi;
import ru.nebolife.bot.core.core.sites.PumpitRu;
import ru.nebolife.bot.core.listeners.CheckInstance;

public class AddAccountActivity extends AppCompatActivity {
    Spinner dropdown;
    String[] siteItems = new String[]{"nebo.mobi", "pumpit.ru"};
    String seletedSite;
    EditText editNickName;
    EditText editPassword;
    UserAccount userAccount;
    Button btnLogin;
    ProgressBar progressBarLogin;
    ArrayAdapter<String> adapter;
    public static User user;
    static RequestCore botClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Fabric.with(this, new Crashlytics());
        setTitle("Вход");
        user = LitePal.findFirst(User.class);
        dropdown = findViewById(R.id.listSite);
        editNickName = findViewById(R.id.loginInput);
        editPassword = findViewById(R.id.passwordInput);
        btnLogin = findViewById(R.id.btnLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.INVISIBLE);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, siteItems);
        dropdown.setAdapter(adapter);


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                seletedSite = siteItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                seletedSite = siteItems[0];
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            loadUserSelectedAccount(extras.getString("LOGIN"));
        }

    }
    public void login(View view) {
        if (editNickName.getText().toString().isEmpty()) {
            editNickName.setError("Вы не указали Логин");
            return;
        }else if (editPassword.getText().toString().isEmpty()) {
            editPassword.setError("Вы не указали Пароль");
            return;
        }
        progressBarLogin.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        List<UserAccount> _user = LitePal.where("nikName = ?", editNickName.getText().toString()).find(UserAccount.class);
        if (_user.isEmpty()) userAccount = new UserAccount();
        else {
            userAccount = _user.get(0);
        }

        userAccount.setNikName(editNickName.getText().toString());
        userAccount.setPassword(editPassword.getText().toString());
        userAccount.setSite(seletedSite);
        userAccount.setVerify(false);
        userAccount.save();

        new LoginTask().execute(seletedSite, editNickName.getText().toString(),
                editPassword.getText().toString());
    }

    private class LoginTask extends AsyncTask<Object, String, Boolean> {
        Boolean isLogin = false;
        String site;
        String login;
        String password;
        @Override
        protected Boolean doInBackground(Object... objects) {
            site = (String) objects[0];
            login = (String) objects[1];
            password = (String) objects[2];
            if (site.equals("nebo.mobi")) {
                try {
                    AddAccountActivity.botClient = new NeboMobi(login, password).login();
                } catch (IOException e) {
                    Crashlytics.logException(e);
                    AddAccountActivity.botClient = null;
                }
            }
            else if (site.equals("pumpit.ru")) AddAccountActivity.botClient = new PumpitRu(login, password).login();
            this.isLogin = AddAccountActivity.botClient != null;

            return isLogin;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (this.isLogin) {
                new RequestCore("").isUserBaned(login, new CheckInstance() {
                    @Override
                    public void onResponse(boolean b) {
                        if (b){
                            AuthActivity.user.setBaned(true);
                            AuthActivity.user.save();
                            Intent intent = new Intent(getBaseContext(), BanedActivity.class);
                            startActivity(intent);
                        }else {
                            userAccount.setVerify(true);
                            userAccount.save();
                            Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                System.out.println("Successful authorization");
                Helper.logWithParametrs("Login", "success", userAccount.getNikName(), "site", userAccount.getSite());
            }else {
                Toast.makeText(getBaseContext(), "Неправильный пароль или логин", Toast.LENGTH_SHORT).show();
                Helper.logWithParametrs("Login", "failed", userAccount.getNikName(), "site", userAccount.getSite());
            }
            progressBarLogin.setVisibility(View.INVISIBLE);
            btnLogin.setEnabled(true);
            super.onPostExecute(aBoolean);
        }
    }
    public void loadUserSelectedAccount(String userLogin){
        userAccount = LitePal.where("nikName = ?", userLogin).find(UserAccount.class).get(0);
        editNickName.setText(userAccount.getNikName());
        editPassword.setText(userAccount.getPassword());
        int spinnerPosition = adapter.getPosition(userAccount.getSite());
        dropdown.setSelection(spinnerPosition);
    }

}
