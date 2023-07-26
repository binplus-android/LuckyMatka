package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Fragment.HomeFragment.finalMatkaID;
import static in.games.luckymatkagames.utils.InputData.doublePanna;
import static in.games.luckymatkagames.utils.InputData.group_jodi_digits;
import static in.games.luckymatkagames.utils.InputData.jodiDigits;
import static in.games.luckymatkagames.utils.InputData.panelGroup;
import static in.games.luckymatkagames.utils.InputData.singleDigit;
import static in.games.luckymatkagames.utils.InputData.singlePaana;
import static in.games.luckymatkagames.utils.InputData.triplePanna;

public class DigitPanaActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDigitPanaBinding binding ;
    Activity ctx = DigitPanaActivity.this;
    Session_management session_management;
    LoadingBar loadingBar;
    Common common;
    TextView tv_no;
    int betType =9 ,min_bid_amt =0;
    String matka_name ="" ,game_name="",game_id="",m_id="",end_time="",start_time="",bet_type="";
    List<TableModel> list;
    List<String> digitList;
    TableRecyclerAdapter tableAdaper;
    MatkasObjects item;
    BroadcastReceiver mMessageReceiver;
    final String valPanelGroup="5",valGroupJodi="6",valJodiDigit="3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDigitPanaBinding.inflate(getLayoutInflater());
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
        if (Integer.parseInt(m_id)>finalMatkaID)
        {
            binding.radiogroup.setVisibility(View.GONE);
            binding.relType.setVisibility (View.GONE);
        }
        binding.etDigit.setHint ("Enter digit");
        binding.tvTitle.setText(game_name);
        binding.ivBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.btnReset.setOnClickListener(this);
        binding.tvDate.setText(common.getCurrDateDay());
        binding.listView.setLayoutManager(new LinearLayoutManager(ctx));
        tableAdaper=new TableRecyclerAdapter(list, ctx);
        binding.listView.setAdapter(tableAdaper);

        switch (game_id)
        {
            case "2":
                binding.tvDigit.setText ("Digit");
                binding.tvHead.setText ("Bid Type (SINGLE DIGIT)");
                digitList = Arrays.asList(singleDigit);
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(1) } );
                break;
            case valJodiDigit:
                binding.tvDigit.setText ("Jodi Digit");
                binding.tvHead.setText ("Bid Type (JODI DIGIT)");
                digitList = Arrays.asList(jodiDigits);
                binding.relType.setVisibility (View.GONE);
                binding.rbClose.setVisibility(View.GONE);
                binding.rbOpen.setVisibility(View.GONE);
//                binding.tvDigit.setText("Jodi Digit");
//                if (Integer.parseInt(m_id)>finalMatkaID){
//                    bet_type="open";
//                }else {
//                    bet_type ="open";
//                }
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(2) } );
                break;
            case "7":
                binding.tvDigit.setText ("Pana");
                binding.tvHead.setText ("Bid Type (SINGLE PANA)");
                digitList = Arrays.asList(singlePaana);
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(3) } );
                break;
            case "8":
                binding.tvDigit.setText ("Pana");
                binding.tvHead.setText ("Bid Type (DOUBLE PANA)");
                digitList = Arrays.asList(doublePanna);
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(3) } );
                break;
            case "9":
                binding.tvDigit.setText ("Pana");
                binding.tvHead.setText ("Bid Type (TRIPPLE PANA)");
                digitList = Arrays.asList(triplePanna);
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(3) } );
                break;

            //temppp
            case valPanelGroup:
                //for panel group
                binding.tvDigit.setText ("Pana");
                binding.tvHead.setText ("Bid Type (PANEL GROUP)");
                digitList = Arrays.asList(panelGroup);
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(3) } );
                break;

            case valGroupJodi:
                binding.tvDigit.setText ("Jodi Digit");
                binding.tvHead.setText ("Bid Type (JODI DIGIT)");
                digitList = Arrays.asList(group_jodi_digits);
                binding.relType.setVisibility (View.GONE);
                binding.rbClose.setVisibility(View.GONE);
                binding.rbOpen.setVisibility(View.GONE);
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(2) } );
                break;
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,digitList);
        binding.etDigit.setAdapter(adapter);
        binding.etDigit.setThreshold(1);

        if (Integer.parseInt(m_id)<finalMatkaID){
            binding.rbClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {

                        if((game_id.equalsIgnoreCase ("7"))||(game_id.equalsIgnoreCase ("8"))
                                ||(game_id.equalsIgnoreCase ("9"))||game_id.equals(valPanelGroup)) {
                            binding.tvDigit.setText ("Close Pana");
                        }else if (game_id.equals(valGroupJodi)){
                            binding.tvDigit.setText ("Jodi Digit");
                        }else {
                            binding.tvDigit.setText ("Close Digit");
                        }
                        binding.rbOpen.setChecked(false);
                        bet_type ="close";
                    }
                }
            });
            binding.rbOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        binding.rbClose.setChecked(false);
                        bet_type="open";

                        if((game_id.equalsIgnoreCase ("7"))||(game_id.equalsIgnoreCase ("8"))
                                ||(game_id.equalsIgnoreCase ("9"))||game_id.equals(valPanelGroup)) {
                            binding.tvDigit.setText ("Open Pana");
                        }else if (game_id.equals(valGroupJodi)){
                            binding.tvDigit.setText ("Jodi Digit");
                        } else {
                            binding.tvDigit.setText ("Open Digit");
                        }
                    }
                }
            });
            if (game_id.equals(valGroupJodi)||game_id.equals(valJodiDigit)){
                bet_type="close";
            }
        } else {
            bet_type = "open";
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();

        common.getWalletAmount(new OnGetWallet() {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                binding.tvWallet.setText(walletModel.getWallet_points());

            }
        });
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                min_bid_amt = Integer.parseInt(model.getMin_bet_amt());
            }
        });
        common.getMatkaAndWallet(m_id, new OnGetMatka() {
            @Override
            public void onGetMatka(MatkasObjects model) {
                item=model;
                String sTime= common.getStartEndTime(model)[0];
                String eTime= common.getStartEndTime(model)[1];
                betType=common.getBetType(common.getASandC(sTime,eTime));
                Log.e("betType",betType +"--"+bet_type);

                if (betType==1) {
                    binding.rbClose.setChecked(true);
                    binding.rbOpen.setEnabled(false);
                } else if (betType== 0) {
                    binding.rbOpen.setChecked(true);
//                    binding.rbClose.setEnabled(false);
                } else {
                    binding.rbOpen.setChecked(true);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_submit:

                common.setBidsDialog(list,m_id,common.getCurrDate(),game_id,
                        binding.tvWallet.getText().toString(),matka_name,loadingBar,binding.btnSubmit,start_time,end_time);
                break;

            case R.id.btn_save:
                validate();
                break;

            case R.id.btn_reset:
                binding.etDigit.setText("");
                binding.etPoints.setText("");
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


        if (bet_type.isEmpty())
        {
            common.showToast("Select Bet Type");

        } else if (digit.isEmpty()) {
            binding.etDigit.setError("Enter Digit");
        } else if (!digitList.contains(digit)) {
            binding.etDigit.setError("Invalid Digit");
        } else if (points.isEmpty()) {
            binding.etPoints.setError("Enter Points");
        }else if (Integer.parseInt(points)<min_bid_amt) {
            common.showToast("Minimum bid amount is "+ min_bid_amt);
        }
//        else if(Integer.parseInt(points)>Integer.parseInt(binding.tvWallet.getText().toString())) {
//            common.showToast("You don't have sufficient points");
//        }
        else {
            if (betType==0)
            {
                common.showToast("Bidding is closed for today !");
            }
            else {
                if (game_id.equals(valPanelGroup)){
                    addPanelGroupData(DigitPanaActivity.this,digit,points,bet_type,list,binding.listView,binding.btnSubmit);
                    return;
                }if (game_id.equals(valGroupJodi)){
                    addGorupJodi(digit,points,bet_type,list,binding.btnSubmit);
                    binding.etDigit.setText("");
                    binding.etDigit.clearListSelection();
                    binding.etPoints.setText("");
                    return;
                }else {
                    binding.tvNo.setVisibility(View.GONE);
                    binding.listView.setVisibility(View.VISIBLE);

                    list.add(new TableModel(digit, points, bet_type));
                    binding.etDigit.setText("");
                    binding.etDigit.clearListSelection();
                    binding.etPoints.setText("");
                    Log.e("list", "---" + list.size() + "---" + list.toString());
                    tableAdaper.notifyDataSetChanged();
                    if (list.size() > 0) {

                        binding.btnSubmit.setVisibility(View.VISIBLE);
                    } else {
                        binding.btnSubmit.setVisibility(View.GONE);
                    }
                }

            }

        }

    }


    public void addPanelGroupData(Activity activity, String digit, String point, String type, List<TableModel> list, RecyclerView list_table, Button btnSave) {
        binding.etDigit.setText("");
        binding.etDigit.clearListSelection();
        binding.etPoints.setText("");

        String str1 = digit.substring(0,1);
        String str2 = digit.substring(1,2);
        String str3 = digit.substring(2,3);
        int dg1 = Integer.parseInt(str1);
        int dg2 = Integer.parseInt(str2);
        int dg3 = Integer.parseInt(str3);
        List<TableModel> listTemp = new ArrayList<>();
        listTemp.add(new TableModel( sortingData(digit), point, type));
        listTemp.add(new TableModel( sortingData(findLastDigit(dg1+5)+findLastDigit(dg2+0)+findLastDigit(dg3+5)), point, type));
        listTemp.add(new TableModel( sortingData(findLastDigit(dg1+5)+findLastDigit(dg2+5)+findLastDigit(dg3+5)), point, type));
        listTemp.add(new TableModel( sortingData(findLastDigit(dg1+0)+findLastDigit(dg2+5)+findLastDigit(dg3+5)), point, type));
        listTemp.add(new TableModel( sortingData(findLastDigit(dg1+5)+findLastDigit(dg2+0)+findLastDigit(dg3+0)), point, type));
        listTemp.add(new TableModel(sortingData(findLastDigit(dg1+0)+findLastDigit(dg2+0)+findLastDigit(dg3+5)), point, type));
        listTemp.add(new TableModel( sortingData(findLastDigit(dg1+5)+findLastDigit(dg2+5)+findLastDigit(dg3+0)), point, type));
        listTemp.add(new TableModel( sortingData(findLastDigit(dg1+0)+findLastDigit(dg2+5)+findLastDigit(dg3+0)), point, type));

//        List<TableModel> lists = new ArrayList<> ( );
        List<TableModel> newlists = new ArrayList<> ( );
        for (int i=0;i< listTemp.size ( );i++) {
            Log.e ("whole_list", "addPan: " + listTemp.get (i).getDigits ( ));
        }

        for(Integer i=0;i<listTemp.size ();i++) {
            Log.e ("ligits", listTemp.get (i).getDigits ( ));
            String dgt= "";
            String zero= "";

            String str1s = listTemp.get (i).getDigits ( ).substring (0, 1);
            String str2s = listTemp.get (i).getDigits ( ).substring (1, 2);
            String str3s = listTemp.get (i).getDigits ( ).substring (2, 3);
            ArrayList<String> listss = new ArrayList<> ( );
            listss.add (str1s);
            listss.add (str2s);
            listss.add (str3s);
            Collections.sort (listss);
            for (Integer il = 0;  il< 3; il++) {
                if (listss.get (il).equals ("0"))
                    zero += listss.get (il);
                else
                    dgt += listss.get (il);
            }
            dgt += zero;

            Log.e ("pannel_grp_list", dgt);

            list.add (new TableModel ( dgt, point, type));

            for (int j=0;j<list.size();j++){
                for (int k=j+1;k<list.size();k++){
                    if (list.get(j).getDigits().equals(list.get(k).getDigits())){
                        Log.e("sadfrgt",list.get(k).getDigits());
                        list.remove(k);
                    }
                }
            }

        }
        Log.e("zsxdcfvgbh",list.get(list.size()-1).getDigits());
        Log.e("zsxdcfvgbcfgh", String.valueOf(list.size()));
        tableAdaper.notifyDataSetChanged();
        if (list.size()>0){
            btnSave.setVisibility(View.VISIBLE);
        }else {
            btnSave.setVisibility(View.GONE);
        }

    }

    public String findLastDigit(int str){
        String dg = String.valueOf(str);
        String digit = dg.substring(dg.length()-1,dg.length());
        return digit;
    }
    public String sortingData(String digit)
    {
        String str1 = digit.substring(0,1);
        String str2 = digit.substring(1,2);
        String str3 = digit.substring(2,3);
        ArrayList<String> listSort = new ArrayList<>();
        listSort.add(str1);
        listSort.add(str2);
        listSort.add(str3);
        Collections.sort(listSort);
        Log.e("Module_sortingData", String.valueOf(listSort));
        String dgt= listSort.get(0)+listSort.get(1)+listSort.get(2);
        Log.e("digit_after_sorting", dgt);

        return dgt;
    }
    public String reverseDigit(String str){

        return String.valueOf(new StringBuilder(str).reverse());
    }

    public void addGorupJodi(String digit, String point, String type, List<TableModel> list, Button btnSave) {
        String str1 = digit.substring(0,1);
        String str2 = digit.substring(1,2);
        int dg1 = Integer.parseInt(str1);
        int dg2 = Integer.parseInt(str2);

        list.add(new TableModel(digit, point, type));
        list.add(new TableModel(findLastDigit(dg1+5)+findLastDigit(dg2+0), point, type));
        list.add(new TableModel(findLastDigit(dg1+0)+findLastDigit(dg2+5), point, type));
        list.add(new TableModel(findLastDigit(dg1+5)+findLastDigit(dg2+5), point, type));
        list.add(new TableModel(reverseDigit(digit), point, type));
        list.add(new TableModel(reverseDigit(findLastDigit(dg1+5)+findLastDigit(dg2+0)), point, type));
        list.add(new TableModel(reverseDigit(findLastDigit(dg1+0)+findLastDigit(dg2+5)), point, type));
        list.add(new TableModel(reverseDigit(findLastDigit(dg1+5)+findLastDigit(dg2+5)), point, type));

        for (int j=0;j<list.size();j++){
            for (int k=j+1;k<list.size();k++){
                if (list.get(j).getDigits().equals(list.get(k).getDigits())){
                    Log.e("sadfrgt",list.get(k).getDigits());
                    list.remove(k);
                }
            }
        }

        tableAdaper.notifyDataSetChanged ( );
        if (list.size()>0){
            btnSave.setVisibility(View.VISIBLE);
        }else {
            btnSave.setVisibility(View.GONE);
        }

    }

}