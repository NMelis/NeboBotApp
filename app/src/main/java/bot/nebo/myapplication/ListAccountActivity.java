package bot.nebo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import bot.nebo.myapplication.models.UserAccount;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.fabric.sdk.android.Fabric;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

public class ListAccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String login;
    Button deleteSelectedAccount;
    Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_account);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        if (!MainActivity.isDev) Sentry.init("https://822abac97e054180a1229c9f2d589926@sentry.io/1810160", new AndroidSentryClientFactory(this));

        deleteSelectedAccount = findViewById(R.id.btnDeleteAccount);
        dropdown = findViewById(R.id.dropdownAccounts);
        loadAccountToSpinner();
    }

    private void loadAccountToSpinner(){
        List<UserAccount> allAccounts = LitePal.findAll(UserAccount.class);
        List<String> accounts = new ArrayList<>();
        for (UserAccount account: allAccounts){
            accounts.add(account.getNikName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, accounts.toArray(new String[0]));
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
    }

    public void inputSelectAccount (View v){
        Log.i("sd", login.toString());
        if (login == null){
            Crouton.makeText(ListAccountActivity.this, "Вы не выбрали аккаунт", Style.ALERT).show();
            return;
        }
        Intent intent = new Intent(ListAccountActivity.this, AddAccountActivity.class);
        intent.putExtra("LOGIN", login);
        startActivity(intent);
    }

    public void deleteSelectedAccount(View v){
        if (login == null){
            Crouton.makeText(ListAccountActivity.this, "Вы не выбрали аккаунт", Style.ALERT).show();
            return;
        }
        UserAccount account = LitePal.where("nikName = ?", login).find(UserAccount.class).get(0);
        account.delete();
        Crouton.makeText(this, "Аккаунт с логином \""+login+"\" удален с списка",Style.CONFIRM).show();
        loadAccountToSpinner();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        login = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        login = "";
    }
}
