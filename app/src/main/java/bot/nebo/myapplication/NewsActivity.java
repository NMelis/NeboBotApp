package bot.nebo.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.crashlytics.android.Crashlytics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import bot.nebo.myapplication.helper.Helper;
import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.Utils;
import io.fabric.sdk.android.Fabric;

public class NewsActivity extends AppCompatActivity {
    ScrollView scrollViewNews;
    LinearLayout linearLayout;
    ProgressBar progressBarLoadNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Fabric.with(this, new Crashlytics());
        scrollViewNews = findViewById(R.id.scrollViewNews);
        linearLayout = findViewById(R.id.newsLayout);
        progressBarLoadNews = findViewById(R.id.progressBarLoadNews);
        new LoadLastNews().execute();
        Helper.log("Open page all news");
    }

    class LoadLastNews extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            URLConnection connection = null;
            int i = 0;
            InputStream is = null;
            while (true) {
                try {
                    String url = VKApplication.urlStorageMds + "news-" + i + ".md";
                    connection = (new URL(url)).openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    String body = "";
                    try {
                        body = Utils.getStringFromInputStream(is = connection.getInputStream());
                    } catch (FileNotFoundException ignored) {
                        break;
                    } finally {
                        onProgressUpdate(new String[]{url, body});
                        i++;
                    }

                } catch (IOException e) {
                    break;
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException var14) {
                            Crashlytics.logException(var14);
                        }
                    }

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressBarLoadNews.setVisibility(View.INVISIBLE);
            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(final Object[] values) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(values[0]);
                    MarkdownView markdownView = new MarkdownView(getBaseContext());
                    markdownView.loadMarkdown(values[1].toString());
                    linearLayout.addView(markdownView, 0);
                }
            });
        }

    }
}
