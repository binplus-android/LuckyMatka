package in.games.luckymatkagames.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.databinding.ActivityContactUsactivityBinding;
import in.games.luckymatkagames.databinding.ActivityGameRatesBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.ToastMsg;

public class ContactUSActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityContactUsactivityBinding binding ;

    Activity ctx = ContactUSActivity.this;
    ToastMsg toastMsg ;
    LoadingBar loadingBar ;
    String whtsapp="",call_no="",email="";
    Session_management session_management;
    Common common ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding = ActivityContactUsactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);

        common.getConfigData(new OnConfigData () {
            @Override
            public void onGetConfigData(ConfigModel model) {
                whtsapp=model.getWhatsapp();
                call_no=model.getCall_no ();
                email=model.getEmail ();


                binding.tvChat.setText(model.getWhatsapp());
                binding.tvCall.setText(model.getCall_no());
                binding.tvCall1.setText(model.getCall_no());
                binding.tvEmail.setText(model.getEmail ());
            }
        });
        }else {
            common.showToast("No Internet Connection");
        }
        binding.tvCall.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + call_no)); // Data with intent respective action on intent
                startActivity(intent);
            }
        });
        binding.tvCall1.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + call_no)); // Data with intent respective action on intent
                startActivity(intent);
            }
        });
        binding.tvChat.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                common.whatsapp(whtsapp,"Hi ! Admin");
            }
        });
        binding.tvEmail.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

    }

    void initViews()
    {
        toastMsg = new ToastMsg(ctx);
        session_management= new Session_management (ctx);;
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact us");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId ())
        {


        }
    }


}