package bot.nebo.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.Utils;
import io.fabric.sdk.android.Fabric;

public class MenuActivity extends AppCompatActivity {
    MarkdownView markdownviewNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (!MainActivity.isDev) Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Crashlytics());
        markdownviewNew = (MarkdownView) findViewById(R.id.markdownviewNew);
        new LoadLastNew().execute();
    }

    public void btnAllNews(View view){
        Intent intent = new Intent(this, NewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void btnManager(View v){
        Intent intent = new Intent(this, ManagerWorkActivity.class);
        startActivity(intent);
    }

    public void startBtnLifter(View view) {
        Intent intent = new Intent(this, LifterActivity.class);
        startActivity(intent);
    }

    public void onInviter(View view) {
        Intent intent = new Intent(this, InviterActivity.class);
        startActivity(intent);
    }

    class LoadLastNew extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            URLConnection connection = null;
            InputStream is = null;
            String var5 = "#Вестей нету(";
            try {
                connection = (new URL(VKApplication.urlStorageMds + "lastNew.md")).openConnection();
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                try {
                    var5 = Utils.getStringFromInputStream(is = connection.getInputStream());
                }catch (FileNotFoundException ignored){
                }finally {
                    onProgressUpdate(new String[] {var5});
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            markdownviewNew.loadMarkdownFromUrl(values[0].toString());
        }

        @Override
        protected void onPreExecute() {
            //Setup precondition to execute some task
        }
    }
}
