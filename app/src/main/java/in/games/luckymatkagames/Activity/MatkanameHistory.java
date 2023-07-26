package in.games.luckymatkagames.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.luckymatkagames.Adapter.MatkaHistoryAdpater;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.MatkasObjects;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.RecyclerTouchListener;

import static in.games.luckymatkagames.Config.BaseUrl.URL_Matka;
import static in.games.luckymatkagames.Config.BaseUrl.URL_StarLine;

public class MatkanameHistory extends AppCompatActivity {
    RecyclerView rec_mname;
    LoadingBar loadingBar;
    Common common;
    MatkaHistoryAdpater matkaAdpater ;
    ArrayList<MatkasObjects> matkaList ;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_matkaname_history);
        initview();
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
        }else {
            common.showToast("No Internet Connection");
        }
        rec_mname.setLayoutManager (new LinearLayoutManager (MatkanameHistory .this));
        if (type.equalsIgnoreCase("matka"))
        {
            getSupportActionBar().setTitle("Matka Chart");
            getMatkaData();
        }else if (type.equalsIgnoreCase("starline"))
        {
            getSupportActionBar().setTitle("Starline Chart");
            getStarlineData();
        }

        rec_mname.addOnItemTouchListener(new RecyclerTouchListener(MatkanameHistory.this, rec_mname, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent (MatkanameHistory.this,ChartActivity.class);
                intent.putExtra( "matka_id",matkaList.get(position).getId ());
                intent.putExtra("type",type);
                startActivity (intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void initview() {
        type = getIntent().getStringExtra("type");
        rec_mname=findViewById (R.id.rec_mname);
        loadingBar=new LoadingBar (MatkanameHistory.this);
        common=new Common (MatkanameHistory.this);
        matkaList=new ArrayList<> (  );
    }

    public void getMatkaData()
    {
        loadingBar.show();
        HashMap<String,String> params = new HashMap<> (  );
        common.postRequest(URL_Matka, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                loadingBar.dismiss();
                try
                {  JSONArray response=new JSONArray(res);

                    for(int i=0; i<response.length();i++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);

                        Log.e ("higfd", "onResponse: "+jsonObject );
                        MatkasObjects matkasObjects=new MatkasObjects();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setName(jsonObject.getString("name"));

                        matkaList.add(matkasObjects);
                        matkaAdpater = new MatkaHistoryAdpater (MatkanameHistory.this, matkaList);
                        matkaAdpater.notifyDataSetChanged();
                        rec_mname.setAdapter(matkaAdpater);
                    }

                } catch (JSONException e) {
                    loadingBar.dismiss ();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }


    public void getStarlineData()
    {

        loadingBar.show();

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_StarLine, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("starlineRespo",response.toString());

                for(int i=0; i<response.length();i++)
                {
                    try
                    {
                        JSONObject jsonObject=response.getJSONObject(i);

                        MatkasObjects matkasObjects=new MatkasObjects();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setName(jsonObject.getString("s_game_time"));
                        matkaList.add(matkasObjects);
                        loadingBar.dismiss();
                        Log.e("sdfgh", String.valueOf(matkaList.size()));
                      if(matkaList.size()>0)
                      {
                          matkaAdpater = new MatkaHistoryAdpater (MatkanameHistory.this, matkaList);
                          matkaAdpater.notifyDataSetChanged();
                          rec_mname.setAdapter(matkaAdpater);
                      }

                    }
                    catch (Exception ex)
                    {
                        loadingBar.dismiss();
                        ex.printStackTrace();
                    }
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingBar.dismiss();
                        common.showVolleyError(error);
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(MatkanameHistory.this);
        requestQueue.add(jsonArrayRequest);
    }

}