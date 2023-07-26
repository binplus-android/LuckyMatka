package in.games.luckymatkagames.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import in.games.luckymatkagames.Adapter.StarRateAdapter;
import in.games.luckymatkagames.Adapter.StarlineGamesAdapter;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.GameRateModel;
import in.games.luckymatkagames.Model.RateModel;
import in.games.luckymatkagames.Model.StarlineGamesModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;

import static android.app.PendingIntent.getActivity;
import static in.games.luckymatkagames.Config.BaseUrl.URL_NOTICE;
import static in.games.luckymatkagames.Config.BaseUrl.URL_StarLine;

public class StarlineActivity extends AppCompatActivity {

    SwipeRefreshLayout swipe;
    RecyclerView rec_starline;
    ArrayList<StarlineGamesModel> sList;
    LoadingBar loadingBar;
    ArrayList<RateModel> list,slist;
    ArrayList<GameRateModel>rlist;
    StarlineGamesAdapter starlineGameAdapter;
    StarRateAdapter adapter;
    Common common;
    TextView tv_starhistory,tv_chart,tv_winhistory,btn_starlinRate;
    RecyclerView rec_rate;
    Context context = StarlineActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_starline);
        common = new Common(context);
        loadingBar = new LoadingBar(StarlineActivity.this);
        sList = new ArrayList<>();
        rlist=new ArrayList<> (  );
        list = new ArrayList<>();
        swipe = findViewById(R.id.swipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Starline Games");
        rec_starline = findViewById(R.id.rec_starline);
        tv_chart = findViewById(R.id.tv_chart);
        tv_starhistory = findViewById(R.id.tv_starhistory);
        tv_winhistory=findViewById (R.id.tv_winhistory);
        btn_starlinRate=findViewById (R.id.btn_starlinRate);
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
            getmatkaRate();
            getStarlineGames();
        }else {
            common.showToast("No Internet Connection");
        }
        tv_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (StarlineActivity.this,ChartActivity.class);
                intent.putExtra("type","starline");
                startActivity (intent);
            }
        });

        tv_starhistory.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i=new Intent ( StarlineActivity.this,HistryActivity.class);
                i.putExtra("type","star");
                startActivity (i);
            }
        });
        tv_winhistory.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i=new Intent ( StarlineActivity.this,StarWinHistory.class);
                startActivity (i);
            }
        });
        btn_starlinRate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
             showStarlineGameRate();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    getmatkaRate();
                    getStarlineGames();
                    swipe.setRefreshing(false);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void getStarlineGames()
    {
        sList.clear ();
        loadingBar.show();

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_StarLine, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("starline_response",response.toString());

                for(int i=0; i<response.length();i++)
                {
                    try
                    {
                        JSONObject jsonObject=response.getJSONObject(i);

                        StarlineGamesModel matkasObjects=new StarlineGamesModel ();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
                        matkasObjects.setS_game_number(jsonObject.getString("s_game_number"));
                        matkasObjects.setS_game_end_time(jsonObject.getString("s_game_end_time"));

                        sList.add(matkasObjects);
                    }
                    catch (Exception ex)
                    {
                        loadingBar.dismiss();

                        return;
                    }
                }
                loadingBar.dismiss();
                rec_starline.setLayoutManager(new LinearLayoutManager (StarlineActivity.this));
                rec_starline.hasFixedSize();
                starlineGameAdapter = new StarlineGamesAdapter(context,sList);
                starlineGameAdapter.notifyDataSetChanged();
                rec_starline.setAdapter(starlineGameAdapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingBar.dismiss();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    private void getmatkaRate() {
        loadingBar.show();
        sList.clear ();
        list.clear ();
        list=new ArrayList<>();
        slist=new ArrayList<>();
        rlist.clear ();
        String tag_json_obj = "json_notice_req";
        HashMap<String, String> params = new HashMap<String, String> ();
        common.postRequest (URL_NOTICE, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e ("TAG", "onResponse: "+response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject (String.valueOf (response));
                    jsonObject.getString ("status");
                    if(jsonObject.getString ("status").equals ("success")) {
                        JSONArray array=jsonObject.getJSONArray("data");
                        Log.e ("vfdshredhedh", "onResponse: "+response);
                        for (int i=0; i<array.length();i++)
                        {
                            RateModel gameRateModel=new RateModel();
                            GameRateModel model=new GameRateModel();
                            JSONObject object=array.getJSONObject(i);
                            gameRateModel.setId(object.getString("id"));
                            gameRateModel.setName(object.getString("name"));
                            gameRateModel.setRate_range(object.getString("rate_range"));
                            gameRateModel.setRate(object.getString("rate"));
                            String type= object.getString("type");
                            gameRateModel.setType(type);


                            if(type.equals("0"))
                            {
                                list.add(gameRateModel);
                            }
                            else
                            {
                                slist.add(gameRateModel);

                            }
                        }
                        Log.e ("array.length()", "onResponse: "+array.length());
                        for (int is=0; is<array.length ();is++)
                        {
                            Log.e ("arrayccc ", "onResponse: "+array.length());
                            GameRateModel model=new GameRateModel();
                            JSONObject object=array.getJSONObject(is);
                            model.setId(object.getString("id"));
                            model.setName(object.getString("name"));
                            model.setRate_range(object.getString("rate_range"));
                            model.setRate(object.getString("rate"));
                            String type= object.getString("type");
                            model.setType(type);

                            if(type.equals("0")) {

                            } else {
                                rlist.add (model);
                                Log.e ("ncjs ", "onResponse: "+rlist.size ());
                            }
                        }
//                        StarRateAdapter adapter=new StarRateAdapter (rlist,StarlineActivity.this);
//                        rec_rate.setAdapter (adapter);
                    }
                    loadingBar.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.showVolleyError(error);
            }
        });
    }

    void showStarlineGameRate(){
        Dialog dialog ;
        dialog=new Dialog(StarlineActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_starline_rate);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        rec_rate =  dialog.findViewById (R.id.rec_rate);
        rec_rate.setLayoutManager (new LinearLayoutManager (StarlineActivity.this));
        Log.e("asdfg", String.valueOf(rlist.size()));
        if (rlist.size()>0) {
            adapter = new StarRateAdapter(rlist, StarlineActivity.this);
            rec_rate.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        dialog.setCanceledOnTouchOutside (true);
    }

}