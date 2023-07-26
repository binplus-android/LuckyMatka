package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.luckymatkagames.Adapter.GameRateAdapter;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.GameRateModel;
import in.games.luckymatkagames.databinding.ActivityGameListBinding;
import in.games.luckymatkagames.databinding.ActivityGameRatesBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.LoadingBar;

import static in.games.luckymatkagames.Config.BaseUrl.URL_NOTICE;

public class GameRateActivity extends AppCompatActivity {
    ActivityGameRatesBinding binding ;
    ArrayList<GameRateModel> ratelist;
    Activity ctx = GameRateActivity.this;
    GameRateAdapter gameRateAdapter;
    LoadingBar loadingBar ;
    Common common ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameRatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();

    }

    void initViews()
    {
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        ratelist = new ArrayList<>();
        binding.rvRates.setLayoutManager(new LinearLayoutManager(ctx));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Game Rates");
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
            getRates();
        } else {
            common.showToast("No Internet Connection");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void getRates() {
        loadingBar.show();
        ratelist.clear();
        HashMap<String, String> params = new HashMap<String, String>();

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_NOTICE, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("URL_NOTICE",response.toString());
                    String status=response.getString("status");
                    if(status.equals("success")) {

                        JSONArray array=response.getJSONArray("data");

                        for (int i=0; i<array.length();i++) {

                            GameRateModel gameRateModel=new GameRateModel();
                            JSONObject object=array.getJSONObject(i);
                            gameRateModel.setId(object.getString("id"));
                            gameRateModel.setName(object.getString("name"));
                            gameRateModel.setRate_range(object.getString("rate_range"));
                            gameRateModel.setRate(object.getString("rate"));
                            String type= object.getString("type");
                            gameRateModel.setType(type);
                            if(type.equals("0")) {
                                ratelist.add(gameRateModel);
                            }
                        }
                        if(ratelist.size()>0) {
                            gameRateAdapter = new GameRateAdapter(ratelist, ctx);
                            binding.rvRates.setAdapter(gameRateAdapter);
                            gameRateAdapter.notifyDataSetChanged();
                        }
                    } else {
                        common.showToast("Something went wrong");
                    }
                    loadingBar.dismiss();
                } catch (Exception ex) {
                    loadingBar.dismiss();
                    common.showToast(""+ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.VolleyErrorMessage(error);
            }
        });
        common.increaseResponseTime(customJsonRequest);
        AppController.getInstance().addToRequestQueue(customJsonRequest);
    }
}
