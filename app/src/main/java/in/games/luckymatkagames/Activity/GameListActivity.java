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
import in.games.luckymatkagames.Interfaces.OnGetMatka;
import in.games.luckymatkagames.Model.ApiGameModel;
import in.games.luckymatkagames.Model.GameModel;
import in.games.luckymatkagames.Model.MatkasObjects;
import in.games.luckymatkagames.Model.StarlineModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityGameListBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomVolleyJsonArrayRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.RecyclerTouchListener;

import static in.games.luckymatkagames.Config.BaseUrl.URL_MATKA_GAMES;

public class GameListActivity extends AppCompatActivity {
    ActivityGameListBinding binding ;
    ArrayList<GameModel>game_list;
    ArrayList<ApiGameModel> tempList,tempListempty;
    Activity ctx = GameListActivity.this;
    GameAdapter gameAdapter ;
    LoadingBar loadingBar;
    Common common ;
    String type="";
    ArrayList<StarlineModel> sList;
    StarlineAdapter starlineAdapter;
    String openTimeOut="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle(""+getIntent().getStringExtra("matkaName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar = new LoadingBar(ctx);
        common = new Common(ctx);
        sList = new ArrayList<>();
        tempListempty = new ArrayList<>();
        tempList = new ArrayList<>();
        game_list = new ArrayList<>();
        type = getIntent().getStringExtra("type");
        Log.e("dfgh",getIntent().getStringExtra("m_id"));
        loadingBar.show();
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);

            common.getMatkaAndWallet(getIntent().getStringExtra("m_id"), new OnGetMatka() {
                @Override
                public void onGetMatka(MatkasObjects model) {
                    loadingBar.dismiss();
                    String sTime = common.getStartEndTime(model)[0];
                    String eTime = common.getStartEndTime(model)[1];
                    Log.e("cfvgbhujmk", sTime + "++" + eTime);
                    int betType = common.getBetType(common.getASandC(sTime, eTime));
                    openTimeOut = String.valueOf(betType);
                    if (type.equalsIgnoreCase("matka")) {
                        getGames();
                    } else if (type.equalsIgnoreCase("starline")) {
                        getStarlineGames();
                        getClick();
                    }
                }
            });
        }else {
            common.showToast("No Internet Connection");
        }

        binding.rvGames.addOnItemTouchListener(new RecyclerTouchListener(ctx, binding.rvGames, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = null;
                ApiGameModel model = tempListempty.get(position);
                if(model.getGame_id ().equalsIgnoreCase ("-1"))
                {

                }
                if(model.getIs_visible ()==false)
                {}
                else {
                    switch (model.getGame_name().toLowerCase())
                    {
                        case "single_digit":
                            i = new Intent(ctx, DigitPanaActivity.class);
                            i.putExtra("game_name", "Single Digits");
                            break;
                        case "jodi_digits":
                            i = new Intent(ctx, DigitPanaActivity.class);
                            i.putExtra("game_name", "Jodi Digits");
                            break;
                        case "single_pana":
                            i = new Intent(ctx, DigitPanaActivity.class);
                            i.putExtra("game_name", "Single Pana");
                            break;
                        case "double_pana":
                            i = new Intent(ctx, DigitPanaActivity.class);
                            i.putExtra("game_name", "Double Pana");
                            break;
                        case "triple_pana":
                            i = new Intent(ctx, DigitPanaActivity.class);
                            i.putExtra("game_name", "Triple Pana");
                            break;
                        case "half_sangam":
                            i = new Intent(ctx, HalfSangamActivity.class);
                            i.putExtra("game_name", "Half Sangam Digits");
                            break;
                        case"full_sangam":
                            i = new Intent(ctx, FullSangamActivity.class);
                            i.putExtra("game_name", "Full Sangam Digits");
                            break;
                        case "sp_motor":
                            i = new Intent(ctx, SpMotorActivity.class);
                            i.putExtra("game_name", "SP");
                            break;
                        case "dp_motor":
                            i = new Intent(ctx, SpMotorActivity.class);
                            i.putExtra("game_name", "DP");
                            break;
                        case "group_jodi":
                            i = new Intent(ctx, DigitPanaActivity.class);
                            i.putExtra("game_name", "Group Jodi");
                            break;
                        case "panel_group":
                            i = new Intent(ctx, DigitPanaActivity.class);
                            i.putExtra("game_name", "Panel Group");
                            break;
                    }

                    if(i!=null) {
                        i.putExtra("game_id", model.getGame_id());
                        i.putExtra("m_id", getIntent().getStringExtra("m_id"));
                        i.putExtra("matkaName", getIntent().getStringExtra("matkaName"));
                        i.putExtra("start_time", getIntent().getStringExtra("start_time"));
                        i.putExtra("end_time", getIntent().getStringExtra("end_time"));

                        startActivity(i);
                    }
                }}


            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }


    public void getGames() {
        tempList.clear ( );
        tempListempty.clear ();
        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );
        common.postRequestString(URL_MATKA_GAMES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                Log.e("getGames_response", "onResponse: "+res );
                loadingBar.dismiss ( );
                try
                {    tempListempty.add (new ApiGameModel ( "-1","","",
                        "","","",R.drawable.logo ));

                    JSONArray response = new JSONArray(res);
                    for (int i=0; i<response.length();i++)
                    {
                        ApiGameModel selectGameModel = new ApiGameModel ();
                        JSONObject object = response.getJSONObject(i);
                        selectGameModel.setGame_id(object.getString("game_id"));
                        selectGameModel.setGame_name(object.getString("game_name"));
                        selectGameModel.setName(object.getString("name"));
                        selectGameModel.setPoints(object.getString("points"));
                        selectGameModel.setIs_close(object.getString("is_close"));
                        selectGameModel.setIs_deleted(object.getString("is_deleted"));
                        String game_id = object.getString("game_id");
                        if (object.getString("is_deleted").equals("0")) {
                            if (game_id.equals("2")) {
                                selectGameModel.setGame_logo(R.drawable.game_single_digit_img);
                                // tempList.add(selectGameModel);
                            } else if (game_id.equals("3")) {
                                if (!openTimeOut.equals("1")) {
                                    selectGameModel.setGame_logo(R.drawable.game_jodi_digit_img);
                                    tempList.add(selectGameModel);
                                }
                                else{
                                    selectGameModel.setIs_visible (false);
                                    selectGameModel.setGame_logo(R.drawable.game_jodi_digit_img);
                                    tempList.add(selectGameModel);
                                }
                            } else if (game_id.equals("5")) {
                                selectGameModel.setGame_logo(R.drawable.game_group_panel);
                                //  tempList.add(selectGameModel);
                            } else if (game_id.equals("6")) {
                                if (!openTimeOut.equals("1")) {
                                    selectGameModel.setGame_logo(R.drawable.game_group_jodi);
                                    // tempList.add(selectGameModel);
                                }
                            } else if (game_id.equals("7")) {
                                selectGameModel.setGame_logo(R.drawable.game_single_panna_img);
                                tempList.add(selectGameModel);
                            } else if (game_id.equals("8")) {
                                selectGameModel.setGame_logo(R.drawable.game_double_panna_img);
                                tempList.add(selectGameModel);
                            } else if (game_id.equals("9")) {
                                selectGameModel.setGame_logo(R.drawable.game_triple_panna_img);
                                tempList.add(selectGameModel);
                            }else if (game_id.equals("10")) {
                                selectGameModel.setGame_logo(R.drawable.game_sp_motor);
                                //tempList.add(selectGameModel);
                            }else if (game_id.equals("11")) {
                                selectGameModel.setGame_logo(R.drawable.game_dp_motor);
                                // tempList.add(selectGameModel);
                            } else if (game_id.equals("12")) {
                                if (!openTimeOut.equals("1")) {
                                    selectGameModel.setGame_logo(R.drawable.game_half_sangam_img);
                                    tempList.add(selectGameModel);
                                }
                                else{
                                    selectGameModel.setIs_visible (false);
                                    selectGameModel.setGame_logo(R.drawable.game_half_sangam_img);
                                    tempList.add(selectGameModel);
                                }
                            } else if (game_id.equals("13")) {
                                if (!openTimeOut.equals("1")) {
                                    selectGameModel.setGame_logo(R.drawable.game_full_sangam_img);
                                    tempList.add(selectGameModel);
                                }
                                else{
                                    selectGameModel.setIs_visible (false);
                                    selectGameModel.setGame_logo(R.drawable.game_full_sangam_img);
                                    tempList.add(selectGameModel);
                                }
                            } else {
                                selectGameModel.setGame_logo(R.drawable.logo);
                                tempList.add(selectGameModel);
                            }
                        }
                    }


                    tempListempty.addAll (tempList);
                    Log.e("matkagame", String.valueOf(tempList.size()));

                    if (tempListempty.size()>0) {
                        gameAdapter = new GameAdapter(ctx,tempListempty);
                        binding.rvGames.setLayoutManager(new GridLayoutManager(ctx,2));
                        binding.rvGames.setAdapter(gameAdapter);
                    }

                }
                catch (Exception ex) {
                    loadingBar.dismiss ();
                    ex.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                error.printStackTrace ();
                common.showVolleyError (error);
            }
        });

    }

    private void getStarlineGames() {
        binding.gameJodi.setVisibility(View.GONE);
        binding.gameHalf.setVisibility(View.GONE);
        binding.gamefull.setVisibility(View.GONE);
        sList.clear ( );
        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );
        CustomVolleyJsonArrayRequest arrayRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, URL_MATKA_GAMES, params, new Response.Listener<JSONArray> ( ) {
            @Override
            public void onResponse(JSONArray response) {
                loadingBar.dismiss ( );
                Log.e ("Starline_games", "onResponse: "+response);
                try {
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
                        if (object.getString("is_starline").equals("1")) {
                            if (object.getString("starline_disable").equals("0")) {
                                if (game_id.equals("2")) {
                                    selectGameModel.setImg(R.drawable.game_single_digit_img);
                                    // sList.add(selectGameModel);
                                    binding.gameSingleDigit.setVisibility(View.VISIBLE);
                                } else if (game_id.equals("7")) {
                                    selectGameModel.setImg(R.drawable.game_single_panna_img);
                                    sList.add(selectGameModel);
                                    binding.gameSinglePanna.setVisibility(View.VISIBLE);
                                } else if (game_id.equals("8")) {
                                    selectGameModel.setImg(R.drawable.game_double_panna_img);
                                    sList.add(selectGameModel);
                                    binding.gameDoublePanna.setVisibility(View.VISIBLE);
                                } else if (game_id.equals("9")) {
                                    selectGameModel.setImg(R.drawable.game_triple_panna_img);
                                    sList.add(selectGameModel);
                                    binding.gameTriplePanna.setVisibility(View.VISIBLE);
                                }
//                        sList.add(selectGameModel);
                            }
                        }
                    }
                    Log.e("xsdfghju", String.valueOf(sList.size()));
                    // if (sList.size()>0) {
                    starlineAdapter = new StarlineAdapter(ctx, sList);
                    binding.rvGames.setLayoutManager(new GridLayoutManager(ctx, 2));
                    binding.rvGames.setAdapter(starlineAdapter);
                    //}

                } catch (Exception ex) {
                    loadingBar.dismiss ();
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
        binding.rvGames.addOnItemTouchListener(new RecyclerTouchListener(ctx, binding.rvGames, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = null;
                StarlineModel model = sList.get(position);
                if(model.getGame_id ().equalsIgnoreCase ("-1"))
                {}else {
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
                }}

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
}