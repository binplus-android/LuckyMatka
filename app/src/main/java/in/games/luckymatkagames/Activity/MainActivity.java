package in.games.luckymatkagames.Activity;

import static in.games.luckymatkagames.Config.BaseUrl.URL_GET_NOTIFICATIONS_STATUS;
import static in.games.luckymatkagames.Config.BaseUrl.URL_INDEX;
import static in.games.luckymatkagames.Config.BaseUrl.URL_SET_NOTIFICATIONS_STATUS;
import static in.games.luckymatkagames.Config.Constants.KEY_CODE;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;
import static in.games.luckymatkagames.Config.Constants.KEY_MOBILE;
import static in.games.luckymatkagames.Config.Constants.KEY_NAME;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.luckymatkagames.Adapter.MenuAdapter;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Fragment.HomeFragment;
import in.games.luckymatkagames.Fragment.WalletFragment;
import in.games.luckymatkagames.Interfaces.OnGetWallet;
import in.games.luckymatkagames.Model.MenuModel;
import in.games.luckymatkagames.Model.WalletObjects;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.CustomVolleyJsonArrayRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.RecyclerTouchListener;
import in.games.luckymatkagames.utils.Session_management;

public class MainActivity extends AppCompatActivity {
    Common common;
    Session_management session_management;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    RecyclerView rec_menu;
    ArrayList<MenuModel> mList;
    MenuAdapter menuAdapter;
    TextView tv_wallet,tv_title,tv_name,tv_mobile;
    ImageView img_logout;
    String share_link="", main_notification="1";
    LoadingBar loadingBar;
    Switch notificationSwitch;
    public  static int selectedPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingBar = new LoadingBar(MainActivity.this);
        mList = new ArrayList<>();
        session_management = new Session_management(MainActivity.this);
        common = new Common(MainActivity.this);
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        rec_menu = findViewById(R.id.rec_menu);
        tv_wallet = findViewById(R.id.tv_wallet);
        tv_title = findViewById(R.id.tv_title);
        notificationSwitch = findViewById(R.id.notification_switch);
        tv_name = findViewById(R.id.tv_user_name);
        tv_mobile = findViewById(R.id.tv_user_mobile);
        img_logout = findViewById(R.id.img_logout);

        tv_name.setText("Name : "+session_management.getUserDetails().get(KEY_NAME));
        tv_mobile.setText("Mobile : "+session_management.getUserDetails().get(KEY_MOBILE));

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeLogout();
            }
        });

        updateWallet();

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    main_notification="1";
                    setStatus(main_notification);

                    // The toggle is enabled
                } else {
                    main_notification="0";
                    setStatus(main_notification);

                    // The toggle is disabled
                }
            }
        });

        getApiData();
        getNotificationStatus();

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_frame,fragment).addToBackStack(null).commit();

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, R.string.drawer_open, R.string.drawer_close);
        toggle.getDrawerArrowDrawable().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
                String frgmentName = fragment.getClass().getSimpleName();
                Log.e("fragment", frgmentName);
                if (frgmentName.contains("HomeFragment")) {
                    setTitles(getResources().getString(R.string.app_name));
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                    toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
                    toggle.getDrawerArrowDrawable().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                    drawer.setDrawerListener(toggle);
                    toggle.syncState();
                } else {
                    toggle.setDrawerIndicatorEnabled(false);
                    toggle.syncState();
                }
            }
        });

        rec_menu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        getMenu();
        rec_menu.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, rec_menu, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String title = mList.get(position).getName();
                Fragment fm = null;
                Bundle args = new Bundle();
                Intent i = null;
                selectedPosition=position;
                menuAdapter.notifyDataSetChanged();

                switch (title) {
                    case "Help and Guide":
                        i =new Intent(MainActivity.this,HowtoPlayActivity.class);
                        break;

                    case "Home":
                        fm = new HomeFragment();
                        break;
                    case "Profile":
                      i = new Intent(MainActivity.this,MyProfileActivity.class);
                        break;
                    case "Wallet":
//                        fm = new WalletFragment();
                        i = new Intent(MainActivity.this, WalletFragment.class);
                        break;
                    case "Withdrawal Method":
                        i = new Intent(MainActivity.this, WithdrawRequest.class);
                        break;
                    case "Win History":
                        i = new Intent(MainActivity.this, HistryActivity.class);
                        i.putExtra("type","win");
                        break;
                    case "Bid History":
                        i = new Intent(MainActivity.this, HistryActivity.class);
                        i.putExtra("type","bid");
                        break;
                    case "Win Rates":
                        i = new Intent(MainActivity.this, GameRateActivity.class);
                        break;
                    case "Share":
                        Log.e("dfghjk",share_link+"--"+session_management.getUserDetails().get(KEY_CODE));
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                share_link);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case "Rating":
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
                        break;
                    case "Contact Us":
                        Intent submit = new Intent(MainActivity.this, ContactUSActivity.class);
                        startActivity(submit);
                        break;
                    case "Change Password":
                        Intent submisdct = new Intent(MainActivity.this, ChangeActivity.class);
                       startActivity(submisdct);
                        break;
                }

                if (fm != null) {
                    args.putString("title", title);
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.main_frame, fm).addToBackStack(null).commit();

                }
                if(i!=null) {
                    startActivity(i);
                }

                drawer.closeDrawer(GravityCompat.START);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    public void makeLogout(){
        Dialog dialog=new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);

        Button btn_yes,btn_no;
        btn_no=dialog.findViewById (R.id.btn_no);
        btn_yes=dialog.findViewById (R.id.btn_yes);
        btn_no.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ();
            }
        });
        btn_yes.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                session_management.logoutSession();
                finish();

            }
        });
        dialog.show ();
    }


        private void getMenu() {
        mList.clear();
        mList.add(new MenuModel(R.drawable.home,"Home"));
//        mList.add(new MenuModel(R.drawable.profile,"Profile"));
       // mList.add(new MenuModel(R.drawable.wallet_32px,"Wallet"));
        mList.add(new MenuModel(R.drawable.img_winhistory,"Win History"));
        mList.add(new MenuModel(R.drawable.history,"Bid History"));
        mList.add(new MenuModel(R.drawable.ic_withdrawal,"Withdrawal Method"));
       // mList.add(new MenuModel(R.drawable.img_help,"Help and Guide"));
        mList.add(new MenuModel(R.drawable.ic_rate,"Win Rates"));
//        mList.add(new MenuModel(R.drawable.img_share,"Share"));
//        mList.add(new MenuModel(R.drawable.ic_rate,"Rating"));
        mList.add(new MenuModel(R.drawable.ic_baseline_person_24,"Contact Us"));
        mList.add(new MenuModel(R.drawable.ic_password,"Change Password"));
//        mList.add(new MenuModel(R.drawable.logout_img,"Logout"));

        menuAdapter = new MenuAdapter(MainActivity.this, mList,"home");
        menuAdapter.notifyDataSetChanged();
        rec_menu.setAdapter(menuAdapter);

}
    public void setTitles(String str) {
        tv_title.setText(str);
        tv_title.setSelected(true);
    }

    private void getApiData() {
        loadingBar.show();
        String json_tag="json_splash_request";
        HashMap<String,String> params=new HashMap<String, String>();
        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET,URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Index_API",""+response.toString());
                try
                {
                    loadingBar.dismiss();
                    JSONArray array = new JSONArray(response.toString());
                    JSONObject dataObj=array.getJSONObject(0);
                    share_link = dataObj.getString("share_link");
                }
                catch (Exception ex)
                {
                    loadingBar.dismiss();
                    ex.printStackTrace();
                    Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // loadingBar.dismiss();
                error.printStackTrace();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    Toast.makeText (MainActivity.this, ""+msg, Toast.LENGTH_SHORT).show ( );
                }
            }
        });
        common.increaseResponseTimeArray(customVolleyJsonArrayRequest);
        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_tag);
    }

    public void getNotificationStatus()
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",session_management.getUserDetails().get(KEY_ID));
        common.postRequest(URL_GET_NOTIFICATIONS_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("GET_NOTIFICATIONS",response.toString());
                loadingBar.dismiss();
                try{
                    JSONObject object = new JSONObject(response.toString());
                    boolean resp=object.getBoolean("responce");
                    if(resp){
                        if (object.getJSONObject("data").getString("notification_status").equals("1")) {
                            notificationSwitch.setChecked(true);
                        }else {
                            notificationSwitch.setChecked(false);
                        }
                    }else{
                        common.showToast(object.getString("message"));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);
            }
        });

    }

    public void setStatus(final String st)
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",session_management.getUserDetails().get(KEY_ID));
        params.put("notification_status",st);
        Log.e("param",params.toString());
        common.postRequest(URL_SET_NOTIFICATIONS_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("SET_NOTIFICATIONS",response.toString());
                loadingBar.dismiss();
                try{
                    JSONObject object = new JSONObject(response.toString());
                    boolean resp=object.getBoolean("responce");
                    if(resp){
                        common.showToast(object.getString("data"));
                    }else{
                        common.showToast(object.getString("data"));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);
            }
        });

    }

    public void updateWallet(){
        common.getWalletAmount(new OnGetWallet() {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                tv_wallet.setText(walletModel.getWallet_points());
            }
        });
    }

}