package in.games.luckymatkagames.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Config.BaseUrl;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomVolleyJsonArrayRequest;
import in.games.luckymatkagames.utils.LoadingBar;

public class ChartWebViewActivity extends AppCompatActivity {
WebView mWebview;
Common common;
LoadingBar loadingBar;
ProgressBar pbar;
TextView txt_back,tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_web_view);
        loadingBar = new LoadingBar(this);
        common = new Common(this);
        mWebview = findViewById(R.id.webView);
        pbar = findViewById(R.id.pbar);
        tv_title = findViewById(R.id.tv_title);
        txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent().getStringExtra("page_type").equals("live_result")) {
            tv_title.setText("Live Results");
        }else {
            tv_title.setText("Chart");
        }
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
            mWebview.clearCache(true);
            mWebview.getSettings().setLoadsImagesAutomatically(true);
            mWebview.getSettings().setJavaScriptEnabled(true);
            mWebview.getSettings().setDomStorageEnabled(true);
            mWebview.getSettings().setAppCacheEnabled(false);
//            mWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebview.setWebViewClient(new myWebViewClient());
            if (Build.VERSION.SDK_INT >= 21) {
                mWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            //FOR WEBPAGE SLOW UI
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            getChartData();
        } else {
            common.showToast("No Internet Connection");
        }
    }

    public void getChartData() {

        String json_tag="json_splash_request";
        HashMap<String,String> params=new HashMap<String, String>();
        CustomVolleyJsonArrayRequest customJsonRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET, BaseUrl.URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                Log.e("asdasd",""+response.toString());
                try
                {
                    JSONObject dataObj=response.getJSONObject(0);
                    if (getIntent().getStringExtra("page_type").equals("matka")) {
                        mWebview.loadUrl(dataObj.getString("chart_link") + getIntent().getStringExtra("name"));
                    }else  if (getIntent().getStringExtra("page_type").equals("live_result")) {
                        mWebview.loadUrl(dataObj.getString("live_result"));

                    } else {
                        mWebview.loadUrl(dataObj.getString("starline_chart_link") + getIntent().getStringExtra("name"));
                    }
                    Log.e("asdfgt",dataObj.getString("chart_url")+getIntent().getStringExtra("name"));
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(""+msg);
                }
            }
        });
        common.increaseResponseTimeArray(customJsonRequest);
        AppController.getInstance().addToRequestQueue(customJsonRequest,json_tag);


    }
    private class myWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
            // Toast.makeText (context, "fgh"+Url, Toast.LENGTH_SHORT).show ( );
            view.loadUrl(Url);
//            txt.setText(Url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(pbar.getVisibility()== View.GONE){
                pbar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            clearCookies(ChartWebViewActivity.this);
            pbar.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("C.TAG", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.d("C.TAG", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }
}