package in.games.luckymatkagames.Activity;

import static in.games.luckymatkagames.Config.BaseUrl.URL_DpMotor;
import static in.games.luckymatkagames.Config.BaseUrl.URL_SPMotor;
import static in.games.luckymatkagames.Fragment.HomeFragment.finalMatkaID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.games.luckymatkagames.Adapter.TableRecyclerAdapter;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Interfaces.OnGetMatka;
import in.games.luckymatkagames.Interfaces.OnGetWallet;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.Model.MatkasObjects;
import in.games.luckymatkagames.Model.TableModel;
import in.games.luckymatkagames.Model.WalletObjects;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityDigitPanaBinding;
import in.games.luckymatkagames.databinding.ActivitySpMotorBinding;
import in.games.luckymatkagames.databinding.ActivitySplashBinding;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;

public class SpMotorActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySpMotorBinding binding ;
    Activity ctx = SpMotorActivity.this;
    Session_management session_management;
    LoadingBar loadingBar;
    Common common;
    TextView tv_no;
    int betType =9 ,min_bid_amt =0;
    String type="",matka_name ="" ,game_name="",game_id="",m_id="",end_time="",start_time="",bet_type="";
    List<TableModel> list;
    List<String> digitList;
    TableRecyclerAdapter tableAdaper;
    MatkasObjects item;
    BroadcastReceiver mMessageReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);


        binding = ActivitySpMotorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
//        binding.btnAdd.setText(Html.fromHtml("<font>"+ "<big>"+ "+" +"</big>"+"</font>"
//                + "ADD"));
//        binding.btnAdd.setText(Html.fromHtml(getResources().getString(R.string.Proceed)));
        binding.rbClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    binding.rbOpen.setChecked(false);
                    bet_type ="close";
                }
            }
        });
        binding.rbOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    binding.rbClose.setChecked(false);
                    if (Integer.parseInt(m_id)>finalMatkaID)
                    {
                        bet_type="close";
                    }else {
                        bet_type = "open";
                    }
                }
            }
        });

    }

    private void initViews() {

        list = new ArrayList<> ();
        digitList = new ArrayList<>();
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);

        session_management = new Session_management(ctx);
        matka_name = getIntent().getStringExtra("matkaName");
        game_id=getIntent().getStringExtra("game_id");
        game_name=getIntent().getStringExtra("game_name");
        m_id=getIntent().getStringExtra("m_id");
        end_time = getIntent().getStringExtra("end_time");
        start_time= getIntent().getStringExtra("start_time");


        if(game_name.equalsIgnoreCase ("SP")) {
            binding.tvDp.setText ("SP Motor");
            binding.tvTitle.setText("SP Motor");
        } else {
            binding.tvDp.setText ("DP Motor");
            binding.tvTitle.setText("DP Motor");
        }

        if (common.getTimeDifference(start_time) > 0) {
            type="Open";
        } else {
            // tv_type.setText("Close");
            if (common.getTimeDifference(end_time) > 0) {
                type="Close";
            } else{
                type="Open";
            }

        }
//        if (Integer.parseInt(m_id)>20)
//        {
//            binding.radiogroup.setVisibility(View.GONE);
//            binding.relType.setVisibility (View.GONE);
//        }


        binding.ivBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.btnReset.setOnClickListener(this);
        binding.tvDate.setText(common.getCurrDateDay());
        binding.listView.setLayoutManager(new LinearLayoutManager (ctx));
        tableAdaper=new TableRecyclerAdapter(list, ctx);

        binding.listView.setAdapter(tableAdaper);
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                min_bid_amt = Integer.parseInt(model.getMin_bet_amt());
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent (this, MainActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_submit:
                common.setBidsDialog(list,m_id,common.getCurrDate(),game_id,
                        binding.tvWallet.getText().toString(),matka_name,loadingBar,binding.btnSubmit,start_time,end_time);
                break;

            case R.id.btn_add:
                validate();
                break;

            case R.id.btn_reset:
                binding.etDp.setText("");
                binding.etPoints.setText("");
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

   public void validate() {
        String points = binding.etPoints.getText ( ).toString ( );
        String digit = binding.etDp.getText ( ).toString ( );

        if (bet_type.isEmpty ( )) {
            common.showToast ("Select Bet Type");

        } else {
            if (digit.isEmpty ( )) {
                binding.etDp.setError ("Enter Motor");
                binding.etDp.requestFocus();
            } else if (binding.etDp.getText ().toString ().length ()<4) {
                binding.etDp.setError("Please enter 4 digit");
                binding.etDp.requestFocus();
                return;
            } else {
                if (points.isEmpty ( )) {
                    binding.etPoints.setError ("Enter Points");
                } else if (Integer.parseInt (points) < min_bid_amt) {
                    common.showToast ("Minimum bid amount is " + min_bid_amt);
                } else {
                    String inputData =digit;
                    String p=points;
                    if (inputData.equals("false")) {
                        common.showToast ( "Wrong input");
                    } else {
                        if (game_name.equalsIgnoreCase ("SP")) {
                            getDataSet (inputData, p, bet_type, URL_SPMotor);
                        } else {
                            getDataSet (inputData, p, bet_type, URL_DpMotor);
                        }
                    }
                }
            }
        }
    }


    private void getDataSet(final String inputData, final String point, final String typ , final String url) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("arr", inputData);
        common.postRequest(url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("sp_dp_motor_response",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray as = jsonObject.getJSONArray("data");
                        final String data= String.valueOf(as);
                        if (data.equals("[]")) {
                            binding.etDp.setError("Enter valid digits");
                        }else {
                            for (int i = 0; i <= as.length() - 1; i++) {
                                String p = as.getString(i);
                                list.add(new TableModel(p, point, typ));

                                if(list.size ()==0) {
                                    binding.btnSubmit.setVisibility (View.GONE);
                                } else {
                                    binding.btnSubmit.setVisibility (View.VISIBLE);
                                }

                                tableAdaper.notifyDataSetChanged();
                                binding.etDp.setText("");
                                binding.etPoints.setText("");
                            }
                        }
                        loadingBar.dismiss();
                    } else {
                        loadingBar.dismiss();
                        common.showToast ( "Something went wrong");
                    }

                } catch (Exception ex) {
                    common.showToast("Error :" + ex.getMessage());
                    loadingBar.dismiss();
                    return;
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

    @Override
    protected void onStart() {
        super.onStart ( );

        common.getMatkaAndWallet(m_id, new OnGetMatka () {
            @Override
            public void onGetMatka(MatkasObjects model) {
                String sTime= common.getStartEndTime(model)[0];
                String eTime= common.getStartEndTime(model)[1];
                betType=common.getBetType(common.getASandC(sTime,eTime));

                if (betType==1) {
                    binding.rbClose.setChecked(true);
                    binding.rbOpen.setEnabled(false);
                } else if (betType== 0) {
                    binding.rbOpen.setChecked(true);
                    binding.rbClose.setEnabled(false);
                } else {
                    binding.rbOpen.setChecked(true);
                }
            }
        });

        common.getWalletAmount(new OnGetWallet () {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                binding.tvWallet.setText(walletModel.getWallet_points());
            }
        });
    }
}









