package in.games.luckymatkagames.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.Model.IndexResponse;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.StorageManagement;

import static in.games.luckymatkagames.Config.BaseUrl.URL_INDEX;

public class SplashActivity extends AppCompatActivity {
    Session_management session_management ;
    Common common ;
    int index=0,startTime=5000;
    String link="";
    StorageManagement storageManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        FirebaseApp.initializeApp(getApplicationContext());
        common = new Common(this);
        session_management = new Session_management(this);
        storageManagement = new StorageManagement(this);
        common.generateToken();
        if (!session_management.getToken().equals("")){
            startTime=0;
        }
        common.getMsg();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                getLinksData();
            }
        },startTime);

//        Thread background = new Thread() {
//            public void run() {
//                try {
//                    // Thread will sleep for 5 seconds
//                    sleep(5*1000);
//
//                    // After 5 seconds redirect to another intent
//
//
//                } catch (Exception e) {
//                }
//            }
//        };
//        // start thread
//        background.start();

    }

    public void getConfigData(){
        HashMap<String,String> params=new HashMap<>();
        common.postRequest(URL_INDEX, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                Log.e("Common", "onResponse: "+ resp);
                try{
                    JSONArray object = new JSONArray(resp);

                    ArrayList<IndexResponse> list=new ArrayList<>();
                    Gson gson =new Gson();
                    Type typeList=new TypeToken<List<IndexResponse>>(){}.getType();
                    list=gson.fromJson(object.toString(),typeList);
                   String error_msg = list.get(0).getErrorMsg();
//                   session_management.updateItem(REQUEST_TIMEOUT_ERR,error_msg);
                    if (session_management.isLoggedIn()) {
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        if(index==1) {
                            Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                            i.putExtra("link",link.toString());
                            startActivity(i);
                            finish();
                        }else if(index==2) {
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.showVolleyError(error);
                error.printStackTrace();
            }
        });
    }
    public void getLinksData()
    {
        common.generateToken();
        if (!session_management.getToken().equals("")){
            startTime=0;
        }

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        common.generateToken();
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            // Toast.makeText(SplashscreenActivity.this,"regg",Toast.LENGTH_LONG).show();
                            deepLink = pendingDynamicLinkData.getLink();
                            index=1;
                            link=deepLink.toString();
                            String refCode=common.checkNullString(link);
                            if (!refCode.equals("")){
                                storageManagement.storePref(refCode);
                            }
                            Log.e("splasdasdasd","mt refer link -- "+deepLink.toString());
                        }
                        else if(pendingDynamicLinkData==null)
                        {
                            index=2;
                        }
                        else
                        {
                            index=2;
                        }

                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...

                        common.getConfigData(new OnConfigData() {
                            @Override
                            public void onGetConfigData(ConfigModel model) {
                                Log.e("cfvgbhjk",model.getError_msg());

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (session_management.isLoggedIn())
                                        {
                                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else {
                                            if(index==1)
                                            {
                                                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                                                i.putExtra("link",link.toString());
                                                startActivity(i);
                                                finish();
                                            }else if(index==2) {
                                                if (storageManagement.getREFCODE()!=null&&!storageManagement.getREFCODE().equals("")
                                                        &&!storageManagement.getREFCODE().isEmpty()){

                                                    Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                                                    i.putExtra("link",storageManagement.getREFCODE());
                                                    startActivity(i);
                                                    finish();
                                                }else {
                                                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }
                                        }

                                    }
                                },startTime);
//                                session_management.updateItem(REQUEST_TIMEOUT_ERR,model.getError_msg());

                            }
                        });
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("taskerrrrr", "getDynamicLink:onFailure", e);
                    }
                });


    }
}