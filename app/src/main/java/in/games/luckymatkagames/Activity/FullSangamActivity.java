package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;
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

import in.games.luckymatkagames.databinding.ActivityFullSangamBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Fragment.HomeFragment.finalMatkaID;

public class FullSangamActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityFullSangamBinding binding;
    Activity ctx = FullSangamActivity.this;
    Session_management session_management;
    LoadingBar loadingBar;
    Common common;
    String matka_name ="" ,game_name="",game_id="",m_id="",end_time="",start_time="";
    String bet_type ="close";
    int betType =9 ,wallet_amout,min_bid_amt=0;
    List<TableModel> list;
    TableRecyclerAdapter tableAdaper;
    BroadcastReceiver mMessageReceiver;
    private final String[] singlePaana={"137","128","146","236","245","290","380","470","489","560","678","579",
            "119","155","227","335","344","399","588","669","777","100","129","138","147","156","237","246",
            "345","390","480","570","589","679","110","228","255","336","499","660","778","200","444",
            "120","139","148","157","238","247","256","346","490","580","670","689","779","788","300","111",
            "130","149","158","167","239","248","257","347","356","590","680","789","699","770","400","888",
            "140","159","168","230","249","258","267","348","357","456","690","780","113","122","177","339",
            "366","447","799","889","500","555",
            "123","150","169","178","240","259","268","349","358","367","457","790","114","277","330","448",
            "466","556","880","899","600","222",
            "124","160","179","250","269","278","340","359","368","458","467","890","115","133","188","223","377",
            "449","557","566","700","999",
            "125","134","170","189","260","279","350","369","378","459","468","567","116","224","233","288","440",
            "477","558","666", "126","135","180","235","270","289","360","379","450","469","478",
            "568","117","144","199","225","388","559","577","667","900","333",
            "127","136","145","190","234","280","370","389","460","479","569","578","118","226","244","299","334","488",
            "668","677","000","550",
            "688",
            "166","229","337","355","445","599","112","220","266",
            "338","446","455",
            "800","990"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullSangamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
        }else {
            common.showToast("No Internet Connection");
        }
    }

    void initViews() {
        list = new ArrayList<>();
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        matka_name = getIntent().getStringExtra("matkaName");
        game_id=getIntent().getStringExtra("game_id");
        game_name=getIntent().getStringExtra("game_name");
        m_id=getIntent().getStringExtra("m_id");
        end_time = getIntent().getStringExtra("end_time");
        start_time= getIntent().getStringExtra("start_time");
        binding.tvTitle.setText(game_name);
        binding.etDigit.setHint ("Enter open pana");
        binding.etPana.setHint ("Enter close pana");
        binding.etPoints.setHint ("Enter point");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,singlePaana);
        binding.etDigit.setAdapter(adapter);
        binding.etDigit.setThreshold(1);
        binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(3) } );
        binding.etPana.setFilters( new InputFilter[] { new InputFilter.LengthFilter(3) } );
        binding.etPana.setAdapter(adapter);
        binding.etPana.setThreshold(1);
        binding.betSubmit.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.tvDate.setText(common.getCurrDateDay());
        binding.ivBack.setOnClickListener(this);
        binding.listView.setLayoutManager(new LinearLayoutManager(ctx));
        tableAdaper=new TableRecyclerAdapter(list, ctx);
        binding.listView.setAdapter(tableAdaper);


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

        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                min_bid_amt = Integer.parseInt(model.getMin_bet_amt());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        common.getWalletAmount(new OnGetWallet() {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                wallet_amout = Integer.parseInt(walletModel.getWallet_points());
                binding.tvWallet.setText(walletModel.getWallet_points());

            }
        });
        common.getMatkaAndWallet(m_id, new OnGetMatka() {
            @Override
            public void onGetMatka(MatkasObjects model) {
//                item=model;
//                txtWallet_amount.setText(session_management.getUserDetails().get(KEY_WALLET));
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bet_submit:

                common.setBidsDialog(list,m_id,common.getCurrDate(),game_id,
                        binding.tvWallet.getText().toString(),matka_name,loadingBar,binding.betSubmit,start_time,end_time);

                break;
            case R.id.btn_save:
                validate();

                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    void validate()
    {
        String points = binding.etPoints.getText().toString();
        String digit = binding.etDigit.getText().toString();
        String pana = binding.etPana.getText().toString();

        if (bet_type.isEmpty())
        {
            common.showToast("Select Bet Type");

        }

        else if (digit.isEmpty())
        {
            binding.etDigit.setError("Enter Open Pana");
        }
        else if (!Arrays.asList(singlePaana).contains(digit))
        {
            binding.etDigit.setError("Invalid Pana");
        }
        else if (pana.isEmpty())
        {
            binding.etPana.setError("Enter Close Pana");
        }
        else if (!Arrays.asList(singlePaana).contains(pana))
        {
            binding.etDigit.setError("Invalid Pana");
        }
        else if (points.isEmpty())
        {
            binding.etPoints.setError("Enter Points");
        }
        else if (Integer.parseInt(points)<min_bid_amt)
        {
            common.showToast("Minimum bid amount is "+ min_bid_amt);
        }
//        else if (Integer.parseInt(points)>Integer.parseInt(binding.tvWallet.getText().toString()))
//        {
//            common.showToast("You dont have sufficient points ");
//        }
        else
        {
            binding.tvNo.setVisibility(View.GONE);
            binding.listView.setVisibility (View.VISIBLE);
            addData(digit+"-"+pana,points,bet_type);
//            common.addData(FullSangamActivity.this,open_pana+"-"+close_pana,points,"Full Sangam",list,tableAdaper,list_table,btnSave);
        }

    }

    public void addData(String digit ,String point ,String type)
    {
        if (betType==0 )
        {
            common.showToast("Bidding is closed for today !");
        }
        else {
            list.add(new TableModel(digit, point, type));
            tableAdaper.notifyDataSetChanged();
            if (list.size() > 0) {

                binding.betSubmit.setVisibility(View.VISIBLE);
            } else {
                binding.betSubmit.setVisibility(View.GONE);
            }
        }


    }


}