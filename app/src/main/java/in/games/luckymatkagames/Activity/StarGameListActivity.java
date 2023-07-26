package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.luckymatkagames.Adapter.GameAdapter;
import in.games.luckymatkagames.Adapter.StarlineAdapter;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.ApiGameModel;
import in.games.luckymatkagames.Model.GameModel;
import in.games.luckymatkagames.Model.StarlineModel;
import in.games.luckymatkagames.R;

import in.games.luckymatkagames.databinding.ActivityStarGameListBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomVolleyJsonArrayRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.RecyclerTouchListener;

import static in.games.luckymatkagames.Config.BaseUrl.URL_MATKA_GAMES;

public class StarGameListActivity extends AppCompatActivity {
    ActivityStarGameListBinding binding ;
    ArrayList<GameModel> game_list;
    ArrayList<ApiGameModel> tempList;
    Activity ctx = StarGameListActivity.this;
    GameAdapter gameAdapter ;
    LoadingBar loadingBar;
    Common common ;
    String type="";

    ArrayList<StarlineModel> sList;
    StarlineAdapter starlineAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding =ActivityStarGameListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle(""+getIntent().getStringExtra("matkaName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar = new LoadingBar(ctx);
        common = new Common(ctx);
        sList = new ArrayList<>();
        tempList = new ArrayList<>();
        game_list = new ArrayList<>();

        type = getIntent().getStringExtra("type");
        if (ConnectivityReceiver.isConnected()) {
                common.getUserStatus(this);

            if (type.equalsIgnoreCase("matka")) {

                //getGames();

            } else if (type.equalsIgnoreCase("starline")) {

                getStarlineGames();
                getClick();
            }
        }else {
            common.showToast("No Internet Connection");

        }


        binding.gameSingleDigit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DigitPanaActivity.class);
                i.putExtra("game_id", "2");
                i.putExtra("game_name", "Single Digits");
                i.putExtra("m_id", getIntent().getStringExtra("m_id"));
                i.putExtra("matkaName", getIntent().getStringExtra("matkaName"));
                i.putExtra("start_time", getIntent().getStringExtra("start_time"));
                i.putExtra("end_time", getIntent().getStringExtra("end_time"));

                startActivity(i);
            }
        });

        binding.gameSinglePanna.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DigitPanaActivity.class);
                i.putExtra("game_id", "7");
                i.putExtra("game_name", "Single Pana");
                i.putExtra("m_id", getIntent().getStringExtra("m_id"));
                i.putExtra("matkaName", getIntent().getStringExtra("matkaName"));
                i.putExtra("start_time", getIntent().getStringExtra("start_time"));
                i.putExtra("end_time", getIntent().getStringExtra("end_time"));

                startActivity(i);
            }
        });
        binding.gameDoublePanna.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DigitPanaActivity.class);
                i.putExtra("game_id", "8");
                i.putExtra("game_name", "Double Pana");
                i.putExtra("m_id", getIntent().getStringExtra("m_id"));
                i.putExtra("matkaName", getIntent().getStringExtra("matkaName"));
                i.putExtra("start_time", getIntent().getStringExtra("start_time"));
                i.putExtra("end_time", getIntent().getStringExtra("end_time"));

                startActivity(i);
            }
        });
        binding.gameTriplePanna.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DigitPanaActivity.class);
                i.putExtra("game_id", "9");
                i.putExtra("game_name", "Triple Pana");
                i.putExtra("m_id", getIntent().getStringExtra("m_id"));
                i.putExtra("matkaName", getIntent().getStringExtra("matkaName"));
                i.putExtra("start_time", getIntent().getStringExtra("start_time"));
                i.putExtra("end_time", getIntent().getStringExtra("end_time"));

                startActivity(i);
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

    private void getStarlineGames() {
        sList.clear ( );
        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );
        CustomVolleyJsonArrayRequest arrayRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, URL_MATKA_GAMES, params, new Response.Listener<JSONArray> ( ) {
            @Override
            public void onResponse(JSONArray response) {
                loadingBar.dismiss ( );
                Log.e ("Starline_games", "onResponse: "+response);
                try {
//                        JSONArray array = response.getJSONArray(0);
//                        Log.e("dfghj", String.valueOf(array.length()));
                    for (int i=0; i<response.length();i++)
                    {
                        StarlineModel selectGameModel = new StarlineModel();
                        JSONObject object = response.getJSONObject(i);
                        selectGameModel.setGame_id(object.getString("game_id"));
                        selectGameModel.setGame_name(object.getString("game_name"));
                        selectGameModel.setName(object.getString("name"));
                        selectGameModel.setPoints(object.getString("points"));
                        selectGameModel.setGame_logo(object.getString("game_logo"));
                        selectGameModel.setIs_close(object.getString("is_close"));
                        selectGameModel.setIs_starline_disable(object.getString("starline_disable"));
                        selectGameModel.setIs_deleted(object.getString("is_deleted"));
                        // selectGameModel.setFatafat_name(object.getString("fatafat_name"));
                        String game_id = object.getString("game_id");
                        if (game_id.equals("2")){
                            selectGameModel.setImg( R.drawable.game_single_digit_img);
                            sList.add(selectGameModel);
                        }
                        else if (game_id.equals("7")){
                            selectGameModel.setImg( R.drawable.game_single_panna_img);
                            sList.add(selectGameModel);
                        }else if (game_id.equals("8")){
                            selectGameModel.setImg( R.drawable.game_double_panna_img);
                            sList.add(selectGameModel);
                        }else if (game_id.equals("9")){
                            selectGameModel.setImg( R.drawable.game_triple_panna_img);
                            sList.add(selectGameModel);
                        }
//                        sList.add(selectGameModel);
                    }
                    Log.e("xsdfghju", String.valueOf(sList.size()));
                    // if (sList.size()>0) {
                    starlineAdapter = new StarlineAdapter(ctx, sList);
                    binding.rvGames.setLayoutManager(new GridLayoutManager (ctx, 2));
                    binding.rvGames.setAdapter(starlineAdapter);
                    //}

                } catch (Exception ex) {

                    ex.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                common.showVolleyError (error);
                error.printStackTrace();

            }
        });
        common.increaseResponseTimeArray(arrayRequest);
        AppController.getInstance ( ).addToRequestQueue (arrayRequest);

    }
    private void getClick() {
        binding.rvGames.addOnItemTouchListener(new RecyclerTouchListener (ctx, binding.rvGames, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = null;
                StarlineModel model = sList.get(position);
                switch (model.getGame_name().toLowerCase())
                {
                    case "single_digit":
                    case "jodi_digits":
                    case "single_pana":
                    case "double_pana":
                    case "triple_pana":

                        i = new Intent(ctx, DigitPanaActivity.class);
                        break;
                    case "half_sangam":
                        i = new Intent(ctx, HalfSangamActivity.class);
                        break;
                    case"full_sangam":
                        i = new Intent(ctx, FullSangamActivity.class);
                        break;

                }
                if(i!=null) {

                    i.putExtra("game_id", model.getGame_id());
                    i.putExtra("game_name", model.getName());
                    i.putExtra("m_id", getIntent().getStringExtra("m_id"));
                    i.putExtra("matkaName", getIntent().getStringExtra("matkaName"));
                    i.putExtra("start_time", getIntent().getStringExtra("start_time"));
                    i.putExtra("end_time", getIntent().getStringExtra("end_time"));

                    startActivity(i);

                }
            }


            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }



}