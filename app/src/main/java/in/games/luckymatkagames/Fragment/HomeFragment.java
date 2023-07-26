package in.games.luckymatkagames.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.games.luckymatkagames.Activity.ChartWebViewActivity;
import in.games.luckymatkagames.Activity.MainActivity;
import in.games.luckymatkagames.Activity.RequestFundsActivity;
import in.games.luckymatkagames.Activity.StarlineActivity;
import in.games.luckymatkagames.Activity.WithdrawRequest;
import in.games.luckymatkagames.Adapter.NewMatkaAdpater;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.Model.MatkasObjects;
import in.games.luckymatkagames.Model.SliderModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.FragmentHomeBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.CustomVolleyJsonArrayRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.ToastMsg;

import static in.games.luckymatkagames.Config.BaseUrl.URL_GET_SLIDER;
import static in.games.luckymatkagames.Config.BaseUrl.URL_Matka;
import static in.games.luckymatkagames.Config.BaseUrl.URL_PLAY;
import static in.games.luckymatkagames.Config.BaseUrl.URL_SLIDER_IMG;

public class HomeFragment extends Fragment implements View.OnClickListener {
    FragmentHomeBinding binding ;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    ArrayList<SliderModel>list= new ArrayList<>();
    String whatsapp_no ="",call_no ="",starname, whatsappText="",walletWhatsApp="" ,walletWhatsAppMsg="",slink="";
    NewMatkaAdpater matkaAdpater ;
    ArrayList<MatkasObjects>matkaList ;
    String set_msg ="";
    public static int finalMatkaID=50;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        initViews();
        common.getMsg();
        return binding.getRoot();
    }
    void initViews() {
        common = new Common(getActivity());
        toastMsg = new ToastMsg(getActivity());
        session_management = new Session_management(getActivity());
        loadingBar = new LoadingBar(getActivity());
        binding.linChart.setOnClickListener(this);
        binding.linCall.setOnClickListener(this);
        binding.linWhtsapp.setOnClickListener(this);
        binding.linAdd.setOnClickListener (this);
        binding.linWithdraw.setOnClickListener (this);
        binding.linPlay.setOnClickListener (this);
        binding.tvMobile.setOnClickListener(this);

        binding.linRefresh.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {
                    getMatkaData();
                    getSliderRequest();
                    ((MainActivity)getActivity()).updateWallet();
                } else {
                    common.showToast("No Internet Connection");
                }
            }
        });
        binding.linStar.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent ( getActivity (), StarlineActivity.class);
                startActivity (intent);
            }
        });


        binding.rvMatka.setLayoutManager(new LinearLayoutManager(getActivity()));
        matkaList = new ArrayList<>();
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectivityReceiver.isConnected()) {
                    ((MainActivity)getActivity()).updateWallet();
                    getMatkaData();
                    getSliderRequest();
                    getHowToPlayData();

                } else {
                    common.showToast("No Internet Connection");
                }
            }
        });

         binding.linLiveResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), ChartWebViewActivity.class);
                        i.putExtra("page_type","live_result");
                        startActivity(i);
                    }
                });

         if (ConnectivityReceiver.isConnected()) {
             common.getConfigData(new OnConfigData() {
                 @Override
                 public void onGetConfigData(ConfigModel model) {
                     whatsapp_no = model.getWhatsapp();
                     call_no = model.getCall_no();
                     starname = model.getStarline_name();
                     binding.tvStar.setText(starname);
                     whatsappText = model.getAdd_fund_whatsapp_text();
                     walletWhatsApp = model.getWhatapp_no();
                     walletWhatsAppMsg = model.getWhatapp_msg();
                     binding.tvMobile.setText(call_no);
//                binding.tvWhatsaap.setText(whatsapp_no);
//                binding.tvCall.setText(call_no);
                     binding.linChart.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             try {
                                 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getChart_link()));
                                 startActivity(browserIntent);
                             } catch (Exception ex) {
                                 ex.printStackTrace();
                             }
                         }
                     });
                     binding.linTelegram.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             intentT0TelegramId(model.getTelegram_link());
                         }
                     });

                     // binding.tvCall.setText(common.trimMobileNumber(call_no));
                     //  binding.tvWhatsaap.setText(common.trimMobileNumber(whatsapp_no));
//                binding.tvHometext.setText(model.getHome_text());
                     binding.tvHometext.setText(Html.fromHtml(model.getHome_text()) + "                                                              ");
                     binding.tvHometext.setSelected(true);

                     if (model.getAdd_point_status().equals("0")) {
                         binding.linAdd.setVisibility(View.GONE);
                     } else {
                         binding.linAdd.setVisibility(View.VISIBLE);
                     }
                     if (model.getWithdraw_status().equals("0")) {
                         binding.linWithdraw.setVisibility(View.GONE);
                     } else {
                         binding.linWithdraw.setVisibility(View.VISIBLE);
                     }

                     if (model.getCasino_status().equals("0")) {
                         binding.relCasino.setVisibility(View.GONE);
                         binding.linCasino.setVisibility(View.GONE);
                     } else {
                         //binding.relStar.setVisibility (View.VISIBLE);
                         binding.linStar.setVisibility(View.VISIBLE);
                     }

                     if (model.getStarline_status().equals("0")) {
                         binding.relStar.setVisibility(View.GONE);
                         binding.linStar.setVisibility(View.GONE);
                     } else {
                         //binding.relStar.setVisibility (View.VISIBLE);
                         binding.linStar.setVisibility(View.VISIBLE);
                     }
                     int v = Integer.parseInt(model.getVersion());

                     if (checkVersion(v)) {
                         if (ConnectivityReceiver.isConnected()) {
                             getMatkaData();
                             getSliderRequest();
                         } else {
                             common.showToast("No Internet Connection");
                         }
                         ((MainActivity) getActivity()).updateWallet();

                     } else {

                         AlertDialog.Builder sayWindows = new AlertDialog.Builder(
                                 getActivity());
                         sayWindows.setTitle("Version Information");
                         sayWindows.setMessage(model.getMessage());
                         sayWindows.setPositiveButton("Yes", null);
                         sayWindows.setNegativeButton("No", null);
                         sayWindows.setCancelable(false);

                         final AlertDialog mAlertDialog = sayWindows.create();
                         mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                             @Override
                             public void onShow(DialogInterface dialog) {

                                 Button p = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                 p.setOnClickListener(new View.OnClickListener() {

                                     @Override
                                     public void onClick(View view) {
                                         String url = null;
                                         try {
                                             url = model.getApp_link();
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                         }

                                         Intent intent = new Intent(Intent.ACTION_VIEW);
                                         intent.setData(Uri.parse(url));
                                         startActivity(intent);
                                     }
                                 });

                                 Button n = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                 n.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         mAlertDialog.dismiss();
                                         getActivity().finishAffinity();
                                     }
                                 });
                             }
                         });
                         mAlertDialog.show();

                     }
                 }
             });

             getHowToPlayData();

         }else {
             common.showToast("No Internet Connection");
         }

        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
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
                    final AlertDialog dialog=builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                    });
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
    }


    public void getMatkaData()
    {
        loadingBar.show();
        if (binding.swipe.isRefreshing())
        {
           binding.swipe.setRefreshing(false);
        }
        HashMap<String,String> params=new HashMap<>();
        common.postRequest(URL_Matka, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                matkaList.clear();
                try
                {
                    JSONArray response=new JSONArray(res);
                    for(int i=0; i<response.length();i++)
                    {

                        JSONObject jsonObject=response.getJSONObject(i);

                        MatkasObjects matkasObjects=new MatkasObjects();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setName(jsonObject.getString("name"));
                        matkasObjects.setStart_time(jsonObject.getString("start_time"));
                        matkasObjects.setEnd_time(jsonObject.getString("end_time"));
                        matkasObjects.setStarting_num(jsonObject.getString("starting_num"));
                        matkasObjects.setNumber(jsonObject.getString("number"));
                        matkasObjects.setEnd_num(jsonObject.getString("end_num"));
                        matkasObjects.setBid_start_time(jsonObject.getString("bid_start_time"));
                        matkasObjects.setBid_end_time(jsonObject.getString("bid_end_time"));
                        matkasObjects.setCreated_at(jsonObject.getString("created_at"));
                        matkasObjects.setUpdated_at(jsonObject.getString("updated_at"));
                        matkasObjects.setSat_start_time(jsonObject.getString("sat_start_time"));
                        matkasObjects.setSat_end_time(jsonObject.getString("sat_end_time"));
                        matkasObjects.setStatus(jsonObject.getString("status"));
//                        matkasObjects.setColor_code(jsonObject.getString("bg_color"));
                        matkaList.add(matkasObjects);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),"Error :"+ex.getMessage(),Toast.LENGTH_LONG).show();

                    return;
                }

                if(matkaList.size()>0){
                    matkaAdpater=new NewMatkaAdpater(getActivity(),matkaList);
                    binding.rvMatka.setAdapter(matkaAdpater);
                    matkaAdpater.notifyDataSetChanged();
                }
                loadingBar.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(""+msg);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_call:
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
//                intent.setData(Uri.parse("tel: " + call_no)); // Data with intent respective action on intent
//                startActivity(intent);
                break;

            case R.id.lin_add:
                Intent i = new Intent(getActivity(), RequestFundsActivity.class);
                startActivity (i);
//                common.whatsapp(walletWhatsApp,walletWhatsAppMsg);
                break;

            case R.id.lin_withdraw:
                Intent inten = new Intent(getActivity(), WithdrawRequest.class);
                startActivity (inten);
               break;

            case R.id.lin_whtsapp:
                common.whatsapp(whatsapp_no,whatsappText);
                 break;

            case R.id.lin_play:
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(slink));
                startActivity(intent2);
                 break;

            case R.id.tv_mobile:
                 Intent intent = new Intent();
                 intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                 intent.setData(Uri.parse("tel: " + call_no)); // Data with intent respective action on intent
                 startActivity(intent);
                 break;
        }
    }

    public boolean checkVersion(int v){
        int vs=1;
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            vs= packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return v == vs;
    }

    private void getSliderRequest() {
        HashMap<String, String> params = new HashMap<>();
        CustomJsonRequest req = new CustomJsonRequest(Request.Method.POST, URL_GET_SLIDER, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("sliders", params.toString() + "\n" + response.toString());
                        try {
//                            String slder_type  = response.getString("slider_type");
                            if (response.has("data")) {
                                list.clear();
                                JSONArray slider_arr = response.getJSONArray("data");
                                Gson gson =new Gson();
                                Type typeList=new TypeToken<List<SliderModel>>(){}.getType();
                                list=gson.fromJson(slider_arr.toString(),typeList);
                                for (int i=0;i<slider_arr.length();i++) {
                                    binding.ivHomeSlider.setImageListener(new ImageListener () {
                                        @Override
                                        public void setImageForPosition(int position, ImageView imageView) {
                                            Log.d("sliders_url", "setImageForPosition: "+URL_SLIDER_IMG+list.get(position).getImage());
                                            Picasso.with(getActivity()).load(URL_SLIDER_IMG+list.get(position).getImage()).fit().into(imageView, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    binding.progressBar.setVisibility(View.GONE);
                                                    binding.ivHomeSlider.setVisibility(View.VISIBLE);
                                                }
                                                @Override
                                                public void onError() {

                                                }
                                            });
                                        }
                                    });
                                    binding.ivHomeSlider.setPageCount(list.size());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            toastMsg.toastIconError(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
           common.showVolleyError(error);
            }
        });
        common.increaseResponseTime(req);
        AppController.getInstance().addToRequestQueue(req);

    }
    public void intentT0TelegramId(String telegrm_link){
        final String appName = "org.telegram.messenger";
        Log.e("TelegramId",telegrm_link);
        try{
            // Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse ("tg://resolve?domain=partsilicon"));
            Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse (telegrm_link));
            intent.setPackage (appName);
            PackageManager pm = getActivity().getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                getActivity().startActivity(intent);
            } else {
//                Toast.makeText(RequestActivity.this, "No Valid Link Found", Toast.LENGTH_LONG).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(telegrm_link));
                getActivity().startActivity(browserIntent);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
            Toast.makeText(getActivity(), "No Valid Link Found", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume ( );
        Log.e("onResume","yes");
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(getActivity());
            getMatkaData();
            getSliderRequest();
            ((MainActivity)getActivity()).updateWallet();
        } else {
            common.showToast("No Internet Connection");
        }

    }
    private void getHowToPlayData() {
        loadingBar.show();
        String json_request_tag="json_how_request";
        HashMap<String, String> params=new HashMap<String, String>();

        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET, URL_PLAY, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    Log.e("fjnngjn",response.toString());
                    JSONObject jsonObject=response.getJSONObject(0);
                    String data=jsonObject.getString("data");
                    slink=jsonObject.getString("link");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingBar.dismiss();
                common.VolleyErrorMessage(error);
            }
        });
        common.increaseResponseTimeArray(customVolleyJsonArrayRequest);
        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_request_tag);


    }
}