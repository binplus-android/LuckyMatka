package in.games.luckymatkagames.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.ArrayList;
import java.util.HashMap;

import in.games.luckymatkagames.Activity.GameRateActivity;
import in.games.luckymatkagames.Activity.HistryActivity;
import in.games.luckymatkagames.Activity.HowtoPlayActivity;
import in.games.luckymatkagames.Activity.LoginActivity;
import in.games.luckymatkagames.Activity.NotifiactionActivity;
import in.games.luckymatkagames.Adapter.MenuAdapter;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.Model.MenuModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.FragmentSettingsBinding;
import in.games.luckymatkagames.utils.CustomVolleyJsonArrayRequest;
import in.games.luckymatkagames.utils.RecyclerTouchListener;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Config.BaseUrl.URL_INDEX;
import static in.games.luckymatkagames.Config.Constants.KEY_CODE;
import static in.games.luckymatkagames.Config.Constants.KEY_MOBILE;
import static in.games.luckymatkagames.Config.Constants.KEY_NAME;
import static in.games.luckymatkagames.Config.Constants.KEY_SHARE_LINK;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;


public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding ;
    Session_management session_management ;
    ArrayList<MenuModel> list ;
    MenuAdapter menuAdapter;
    Common common;
    Common module;
    String share_link=" ";
    private String getCasino_status = "0";
    private String getStarline_status = "0";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        common=new Common(getActivity());
        common.getUserStatus(getActivity());
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                initViews(model.getCasino_status(), model.getStarline_status());
            }
        });
//        initViews("0", "0");


        return binding.getRoot();
    }


    void initViews(String getCasino_status, String getStarline_status)
    {
        module=new Common (getActivity());
        this.getCasino_status = getCasino_status;
        this.getStarline_status = getStarline_status;
        session_management = new Session_management(getActivity());
        binding.tvMobile.setText(session_management.getUserDetails().get(KEY_MOBILE));
        binding.tvName.setText(session_management.getUserDetails().get(KEY_NAME));
       // binding.rvMenu.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.rvMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        otherData();
        getApiData();
//        Log.i("Session_data", String.valueOf(session_management));
        binding.rvMenu.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), binding.rvMenu, new RecyclerTouchListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Intent i = null;

                switch (list.get(position).getName())
                {
//                    Wallet,Bid History,Win History,Starline History,Starline Win History,Casino History,Casino Win History,
//                ,Game Rates,How To Play,Notice Board/Rules,Refer and Earn,Sign Out
                    case "Wallet":
                        i = new Intent(getActivity(), WalletFragment.class);
//                        getFragmentManager().beginTransaction().add(R.id.main_frame,new WalletFragment()).commit();

//                        i = new Intent(getActivity(), MyProfileActivity.class);
                        break;
                    case "Bid History":
                        i = new Intent(getActivity(), HistryActivity.class);
                        i.putExtra("type","bid");
                        break;
                        case "Win History":
                        i = new Intent(getActivity(), HistryActivity.class);
                        i.putExtra("type","win");
                        break;
                    case "Starline History":
                        i = new Intent(getActivity(), HistryActivity.class);
                        i.putExtra("type","star");
                        break;
                    case "Starline Win History":
                        i = new Intent(getActivity(), HistryActivity.class);
                        i.putExtra("type","star_win");
                        break;
//                    case 3:
//                        i = new Intent(getActivity(), MatkanameHistory.class);
//                        i.putExtra("type","matka");
//                        break;

                    case "Casino Win History":
                        i = new Intent(getActivity(), HistryActivity.class);
                          i.putExtra ("type","casino_win");
                        break;
//                    case 6:
//                        i = new Intent(getActivity(), MatkanameHistory.class);
//                        i.putExtra("type","starline");
//                        break;
                        case "Game Rates":
                        i = new Intent(getActivity(), GameRateActivity.class);
                        break;
                        case "How To Play":
                        i = new Intent(getActivity(), HowtoPlayActivity.class);
                        break;

                    case "Notice Board/Rules":
                        i = new Intent(getActivity(), NotifiactionActivity.class);
                        break;
//                    case  9:
//                        i = new Intent(getActivity(), NoticeBoardActivity.class);
//                        break;

                        case "Refer and Earn":
                            Log.e("dfghjk",session_management.getUserDetails().get(KEY_CODE));
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    share_link+"\n"+session_management.getUserDetails().get(KEY_SHARE_LINK)+"\n\n"+"Use my referral code : "+session_management.getUserDetails().get(KEY_CODE));
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        break;
//                    case 7:
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
//                        break;
                    case "Sign Out":

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());

                        builder.setTitle("LOGOUT ??").setMessage("Do you wish to logout ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session_management.logoutSession();
                                        Intent intent=new Intent ( getContext (), LoginActivity.class);
                                        startActivity (intent);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        androidx.appcompat.app.AlertDialog dialog=builder.create();
                        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                            }
                        });
                        dialog.show();
                        break;
                }
                if(i!=null) {
                    startActivity(i);
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            getActivity().finishAffinity();


                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    final androidx.appcompat.app.AlertDialog dialog=builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                    });
                    dialog.show();
                    return true;
                }
                return false;
            }
        });



    }
    private void getApiData() {
//        loadingBar.show();
        String json_tag="json_splash_request";
        HashMap<String,String> params=new HashMap<String, String>();
        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET,URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("starlinehistory",""+response.toString());
                try
                {
                    JSONArray array = new JSONArray(response.toString());
                    JSONObject dataObj=array.getJSONObject(0);

                    share_link = dataObj.getString("share_link");
//                    webView.loadUrl (app_link);
                    Log.e("sharelink",share_link);

                }
                catch (Exception ex)
                {
                    //loadingBar.dismiss();
                    ex.printStackTrace();
                    Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // loadingBar.dismiss();
                error.printStackTrace();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    Toast.makeText (getContext(), ""+msg, Toast.LENGTH_SHORT).show ( );
                }
            }
        });
        common.increaseResponseTimeArray(customVolleyJsonArrayRequest);
        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_tag);

    }
    private void otherData() {
//        Wallet,Bid History,Win History,Starline History,Starline Win History,Casino History,Casino Win History,
//                ,Game Rates,How To Play,Notice Board/Rules,Refer and Earn,Sign Out
//
     list.add(new MenuModel(R.drawable.icons8_wallet,"Wallet"));
     list.add(new MenuModel(R.drawable.icons8_auction_128px,"Bid History"));
     list.add(new MenuModel(R.drawable.win_history,"Win History"));
     if(!this.getStarline_status.equals("0")) {
         list.add(new MenuModel(R.drawable.star, "Starline History"));
         list.add(new MenuModel(R.drawable.history, "Starline Win History"));
     }
//     list.add(new MenuModel(R.drawable.page_64px,"Matka Chart"));
     if(!this.getCasino_status.equals("0")) {
         list.add(new MenuModel(R.drawable.order_history_64px, "Casino History"));
         list.add(new MenuModel(R.drawable.trophy_50px, "Casino Win History"));
     }
//        list.add(new MenuModel(R.drawable.page_64px,"Starline Chart"));
     list.add(new MenuModel(R.drawable.icons8_money_transfer_240px,"Game Rates"));
     list.add(new MenuModel(R.drawable.icons8_video_playlist_128px,"How To Play"));
     list.add(new MenuModel(R.drawable.notification_64px,"Notice Board/Rules"));
     // list.add(new MenuModel(R.drawable.rules_50px,"Notice Board/Rules"));
     list.add(new MenuModel(R.drawable.refer_earn,"Refer and Earn"));
//     list.add(new MenuModel(R.drawable.icons8_rating_100px,"Rate Us"));
     list.add(new MenuModel(R.drawable.logout_img,"Sign Out"));
        menuAdapter =new MenuAdapter(getActivity(),list,"setting");
       binding.rvMenu.setAdapter(menuAdapter);

    }
}