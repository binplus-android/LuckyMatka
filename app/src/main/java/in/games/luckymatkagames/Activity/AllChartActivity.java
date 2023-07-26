package in.games.luckymatkagames.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.games.luckymatkagames.Adapter.AllChartsAdapter;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Config.BaseUrl;
import in.games.luckymatkagames.Model.MatkasObjects;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.RecyclerTouchListener;

public class AllChartActivity extends AppCompatActivity {
RecyclerView rec_charts;
LoadingBar loadingBar;
Common common;
ArrayList<MatkasObjects> matkaList;
AllChartsAdapter adapter;
TextView txt_back;
String page_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chart);
        common = new Common(this);
        loadingBar = new LoadingBar(this);
        page_type = getIntent().getStringExtra("page_type");
        matkaList = new ArrayList<>();
        txt_back = findViewById(R.id.txt_back);
        rec_charts = findViewById(R.id.rec_charts);
        rec_charts.setLayoutManager(new LinearLayoutManager(AllChartActivity.this));
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
            if (page_type.equalsIgnoreCase("matka")) {
                getMatkaData(BaseUrl.URL_Matka);
            } else {
                getMatkaData(BaseUrl.URL_StarLine);
            }
        }else {
            common.showToast("No Internet Connection");
        }

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rec_charts.addOnItemTouchListener(new RecyclerTouchListener(this, rec_charts, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent  = new Intent(AllChartActivity.this,ChartWebViewActivity.class);
                intent.putExtra("name",matkaList.get(position).getId());
                intent.putExtra("page_type",page_type);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void getMatkaData(String url)
    {
        loadingBar.show();

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("matka",String.valueOf(response));
                        matkaList.clear();
                        for(int i=0; i<response.length();i++)
                        {
                            try
                            {
                                JSONObject jsonObject=response.getJSONObject(i);
                                MatkasObjects matkasObjects=new MatkasObjects();
                                matkasObjects.setId(jsonObject.getString("id"));
                                matkasObjects.setName(jsonObject.getString("name"));
                                matkaList.add(matkasObjects);

                            }
                            catch (Exception ex)
                            {
                                loadingBar.dismiss();
                                Toast.makeText(AllChartActivity.this,"Error :"+ex.getMessage(),Toast.LENGTH_LONG).show();

                                return;
                            }
                        }

                        rec_charts.setLayoutManager(new LinearLayoutManager(AllChartActivity.this));
                        adapter=new AllChartsAdapter(AllChartActivity.this,matkaList);
                        rec_charts.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        loadingBar.dismiss();


                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingBar.dismiss();
                        String msg=common.VolleyErrorMessage(error);
                        if(!msg.isEmpty())
                        {
                            common.showToast(""+msg);
                        }
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(AllChartActivity.this);
        requestQueue.add(jsonArrayRequest);
    }


}