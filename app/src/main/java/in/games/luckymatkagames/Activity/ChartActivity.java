package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomVolleyJsonArrayRequest;
import in.games.luckymatkagames.utils.LoadingBar;

import static in.games.luckymatkagames.Config.BaseUrl.URL_INDEX;

public class ChartActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    Common module;
    SwipeRefreshLayout swipe;
    String app_link ="",chart_link="",matka_id="",type="";
    in.games.luckymatkagames.databinding.ActivityChartBinding binding ;
    LoadingBar loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        //setContentView (R.layout.activity_chart);
        binding = in.games.luckymatkagames.databinding.ActivityChartBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chart");
        initview();
        if (ConnectivityReceiver.isConnected()) {
            module.getUserStatus(this);
        }else {
            module.showToast("No Internet Connection");
        }


    }

    private void initview() {

        matka_id = getIntent().getStringExtra("matka_id");
        type = getIntent().getStringExtra("type");

        loadingBar = new LoadingBar(ChartActivity.this);
        webView=findViewById (R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        module=new Common (ChartActivity.this);

        if (type.equalsIgnoreCase("matka")) {
            getHistory();
        }
        else if (type.equalsIgnoreCase("starline"))
        {
            module.getConfigData(new OnConfigData() {
                @Override
                public void onGetConfigData(ConfigModel model) {
                    webView.loadUrl (model.getStarline_chart_link());
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                    webView.setWebViewClient(new myWebViewClient ());
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void getHistory() {
        loadingBar.show();

        String json_tag="json_splash_request";
        HashMap<String,String> params=new HashMap<String, String>();
        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET,URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("starlinehistory",""+response.toString());
                try
                {
                    JSONArray array = new JSONArray(response.toString());
                    JSONObject dataObj=array.getJSONObject(0);
                    loadingBar.dismiss();
                    chart_link = dataObj.getString("chart_link");
//                  webView.loadUrl (app_link);
                    Log.e("Chart",chart_link+matka_id);
                    webView.loadUrl (chart_link+matka_id);

                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                    webView.setWebViewClient(new myWebViewClient ());

                } catch (Exception ex) {

                    loadingBar.dismiss();
                    ex.printStackTrace();
                    Toast.makeText(ChartActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                error.printStackTrace();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    Toast.makeText (ChartActivity.this, ""+msg, Toast.LENGTH_SHORT).show ( );
                }
            }
        });
        module.increaseResponseTimeArray(customVolleyJsonArrayRequest);
        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_tag);

    }



    private class myWebViewClient extends WebViewClient {



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
            view.loadUrl(Url);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);

        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }


    }
}