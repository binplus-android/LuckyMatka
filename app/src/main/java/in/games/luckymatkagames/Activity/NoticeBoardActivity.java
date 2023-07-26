package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import in.games.luckymatkagames.Adapter.Notice_Adapter;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.Notice_Model;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.LoadingBar;

import static in.games.luckymatkagames.Config.BaseUrl.URL_NOTICEBOARD;

public class NoticeBoardActivity extends AppCompatActivity {
    in.games.luckymatkagames.databinding.ActivityNoticeBoardBinding binding ;
    RecyclerView rec_notice;
    LoadingBar loadingBar;
    Common common;
    ArrayList<Notice_Model> notice_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_notice_board);
        binding = in.games.luckymatkagames.databinding.ActivityNoticeBoardBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NoticeBoard/Rules");
        initview();
        notice_list=new ArrayList<> (  );
        NoticeBoard();
        if (binding.swipe.isRefreshing())
        {
            NoticeBoard();
            binding.swipe.setRefreshing(false);
        }
    }


    private void initview() {
        rec_notice=findViewById (R.id.rec_notice);
        loadingBar=new LoadingBar (NoticeBoardActivity.this);
        common=new Common (NoticeBoardActivity.this);
        rec_notice.setLayoutManager (new LinearLayoutManager (NoticeBoardActivity .this));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    public void NoticeBoard() {
        notice_list.clear ();
        loadingBar.show();
        HashMap< String,String> params=new HashMap<> (  );
//        params.put ("title",title);
//        params.put ("detail",detail);
        CustomJsonRequest customVolleyJsonArrayRequest=new CustomJsonRequest (Request.Method.POST, URL_NOTICEBOARD, params, new Response.Listener<JSONObject> ( ) {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("notice",response.toString());
                loadingBar.dismiss ();
                try {
                    boolean status = response.getBoolean ("response");
                    if (status)
                    {
                        JSONArray data_arr = response.getJSONArray ("data");
                        for(int i=0; i<=data_arr.length()-1;i++) {
                            JSONObject jsonObject = data_arr.getJSONObject(i);

                            Notice_Model model=new Notice_Model ();
                            model.setTitle (jsonObject.getString("title"));
                            model.setDescription (jsonObject.getString ("description"));
                            notice_list.add (model);
                        }
                        Notice_Adapter notice_adapter=new Notice_Adapter (NoticeBoardActivity.this,notice_list);
                        rec_notice.setAdapter (notice_adapter);
                    }
                    loadingBar.dismiss ();

                } catch (Exception e) {
                    loadingBar.dismiss();
                    e.printStackTrace ( );
                    //new ToastMsg(getActivity()).toastIconError("Something went wrong");
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
//                new ToastMsg(getActivity()).toastIconError("Error");
            }
        });
        common.increaseResponseTime(customVolleyJsonArrayRequest);
        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,"");

//        StringRequest stringRequest=new StringRequest (Request.Method.POST, URL_NOTICEBOARD, params, new Response.Listener<> ( ) {
//            @Override
//            public void onResponse(Object response) {
//
//            }
//        } ,new Response.ErrorListener ( ) {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
    }
}