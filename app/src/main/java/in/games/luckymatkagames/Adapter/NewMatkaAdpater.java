package in.games.luckymatkagames.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.luckymatkagames.Activity.GameListActivity;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.MatkasObjects;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.ToastMsg;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 09,September,2020
 */
public class NewMatkaAdpater extends RecyclerView.Adapter<NewMatkaAdpater.ViewHolder> {
    private final String TAG= NewMatkaAdpater.class.getSimpleName();
    private final Activity context;
    Common common ;
    private final ArrayList<MatkasObjects> list;
    private int flag=0;
    String startTime="";
    String endTime="";

    public NewMatkaAdpater(Activity context, ArrayList<MatkasObjects> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_home_matka,null);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            final MatkasObjects model = list.get(position);
            holder.txtMatkaName.setText(model.getName());
            startTime = "";
            endTime = "";
            String dy = new SimpleDateFormat("EEEE").format(new Date());
            if (dy.equalsIgnoreCase("Sunday")) {
                if (model.getStart_time().equals("00:00:00") && model.getEnd_time().equals("00:00:00")) {
                    startTime = model.getBid_start_time();
                    endTime = model.getBid_end_time();
                } else {

                    startTime = model.getStart_time();
                    endTime = model.getEnd_time();
                }
            } else if (dy.equalsIgnoreCase("Saturday")) {
                if (getValidTime(model.getSat_start_time(), model.getSat_end_time())) {
                    startTime = model.getSat_start_time();
                    endTime = model.getSat_end_time();
                } else {
                    startTime = model.getBid_start_time();
                    endTime = model.getBid_end_time();
                }
            } else {
                startTime = model.getBid_start_time();
                endTime = model.getBid_end_time();
            }
            Log.e("matka_time", "onBindViewHolder: " + model.getName() + "--" + startTime + "\n " + endTime);


            holder.txtMatka_startingNo.setText(getValidNumber(model.getStarting_num(), 1) + "-" + getValidNumber(model.getNumber(), 2) + "-" + getValidNumber(model.getEnd_num(), 3));

            Date date = new Date();
            SimpleDateFormat sim = new SimpleDateFormat("HH:mm:ss");
            String time1 = startTime;
            String time2 = endTime;

            Date cdate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String time3 = format.format(cdate);
            Date date1 = null;
            Date date2 = null;
            Date date3 = null;
            try {
                date1 = format.parse(time1);
                date2 = format.parse(time2);
                date3 = format.parse(time3);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            long difference = date3.getTime() - date1.getTime();
            long as = (difference / 1000) / 60;

            long diff_close = date3.getTime() - date2.getTime();
            long c = (diff_close / 1000) / 60;
            if (dy.equalsIgnoreCase("Sunday")) {
                if (getValidTime(model.getStart_time(), model.getEnd_time())) {
                    getPlayButton(as, c, holder.tv_status, holder.btnPlay, holder.txtPlayPause, holder.lin_play, holder.rel_matka_color);
                    holder.txtmatkaBid_openTime.setText("Open " + common.get24To12Format(model.getStart_time()));
                    holder.txtmatkaBid_closeTime.setText("Close " + common.get24To12Format(model.getEnd_time()));
                } else {
                    setInactiveStatus(holder.tv_status, holder.btnPlay, holder.txtPlayPause, holder.lin_play, holder.rel_matka_color);
                    holder.txtmatkaBid_openTime.setText("Open " + common.get24To12Format(model.getBid_start_time()));
                    holder.txtmatkaBid_closeTime.setText("Close " + common.get24To12Format(model.getBid_end_time()));
//              holder.btnPlay.setVisibility(View.GONE);
                }
            } else if (dy.equalsIgnoreCase("Saturday")) {
                if (getValidTime(model.getSat_start_time(), model.getSat_end_time())) {
                    getPlayButton(as, c, holder.tv_status, holder.btnPlay, holder.txtPlayPause, holder.lin_play, holder.rel_matka_color);
                    holder.txtmatkaBid_openTime.setText("Open " + common.get24To12Format(model.getSat_start_time()));
                    holder.txtmatkaBid_closeTime.setText("Close " + common.get24To12Format(model.getSat_end_time()));
                } else {
                    setInactiveStatus(holder.tv_status, holder.btnPlay, holder.txtPlayPause, holder.lin_play, holder.rel_matka_color);
                    holder.txtmatkaBid_openTime.setText("Open " + common.get24To12Format(model.getBid_start_time()));
                    holder.txtmatkaBid_closeTime.setText("Close " + common.get24To12Format(model.getBid_end_time()));

//              holder.btnPlay.setVisibility(View.GONE);
                }
            } else {

                if (getValidTime(model.getBid_start_time(), model.getBid_end_time())) {
                    getPlayButton(as, c, holder.tv_status, holder.btnPlay, holder.txtPlayPause, holder.lin_play, holder.rel_matka_color);
                } else {
                    setInactiveStatus(holder.tv_status, holder.btnPlay, holder.txtPlayPause, holder.lin_play, holder.rel_matka_color);

                }
                holder.txtmatkaBid_openTime.setText("Open " + common.get24To12Format(model.getBid_start_time()));
                holder.txtmatkaBid_closeTime.setText("Close " + common.get24To12Format(model.getBid_end_time()));

            }

        holder.rel_matka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGames(model);
            }
        });
//            holder.btnPlay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    gotoGames(model);
//                }
//            });
//            holder.lin_result.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ChartActivity.class);
//                    intent.putExtra("matka_id", model.getId());
//                    intent.putExtra("type", "matka");
//                    context.startActivity(intent);
//                }
//            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtmatkaBid_openTime,txtmatkaBid_closeTime,txtMatkaName,txtMatka_startingNo;
        TextView txtMatka_id ,tv_status,txtPlayPause;
        LinearLayout lin_time,lin_play;
        CardView rel_matka_color;
        LinearLayout rel_matka,lin_result;
        ImageView btnPlay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_play=itemView.findViewById(R.id.lin_play);
            txtmatkaBid_openTime=(TextView)itemView.findViewById(R.id.tv_open);
            txtmatkaBid_closeTime=(TextView)itemView.findViewById(R.id.tv_close);
            txtMatkaName=(TextView)itemView.findViewById(R.id.tv_name);
            txtPlayPause=(TextView)itemView.findViewById(R.id.txtPlayPause);
            txtMatka_startingNo=(TextView)itemView.findViewById(R.id.tv_number);
            tv_status=(TextView)itemView.findViewById(R.id.tv_status);
            rel_matka = itemView.findViewById(R.id.lin_matka);
            rel_matka_color = itemView.findViewById(R.id.rel_matka_color);
            btnPlay=itemView.findViewById(R.id.iv_play);
            lin_time =itemView.findViewById(R.id.lin_time);
            lin_result =itemView.findViewById(R.id.lin_result);
            common = new Common(context);
        }
    }

    public boolean getValidTime(String sTime, String eTime){

        if(sTime.equalsIgnoreCase("00:00:00") && eTime.equalsIgnoreCase("00:00:00")){
            return false;
        }else return !sTime.equalsIgnoreCase("00:00:00.000000") || !eTime.equalsIgnoreCase("00:00:00.000000");
    }

    public String getValidNumber(String str, int palace){
        String validStr="";
        if(str ==null || str.isEmpty() || str.equalsIgnoreCase("null")){
            if(palace==1){
                validStr="***";
            }else if(palace==2){
                validStr="**";
            }else{
                validStr="***";
            }
        }else{
            validStr=str;
        }
        return validStr;
    }
    public void getPlayButton(long as, long c, TextView tv_status, ImageView btnPlay, TextView txtPlayPause,LinearLayout lin_play, CardView rel_matka_color){
        if(as<0) {
            flag=2;
//       tv_status.setVisibility(View.VISIBLE);
            setActiveStatus(tv_status,btnPlay, txtPlayPause, lin_play,  rel_matka_color);
//        btnPlay.setVisibility(View.VISIBLE);

        } else if(c>0) {
            flag=3;
            setInactiveStatus(tv_status,btnPlay, txtPlayPause, lin_play,  rel_matka_color);
//        tv_status.setVisibility(View.VISIBLE);
//        btnPlay.setVisibility(View.GONE);
        } else {
            flag=1;
//        tv_status.setVisibility(View.VISIBLE);
            setActiveStatus(tv_status,btnPlay, txtPlayPause,lin_play,  rel_matka_color);
//        btnPlay.setVisibility(View.GONE);
        }


    }

    public void setActiveStatus(TextView tv, ImageView btn, TextView  txtPlayPause,LinearLayout lin_play, CardView rel_matka_color){
        tv.setVisibility(View.VISIBLE);
       // tv.setText("BID Is Running ");
        tv.setText("Betting is Open");
        tv.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
        tv.setTextColor(context.getResources().getColor(R.color.white));
        txtPlayPause.setText("Play Now");
        lin_play.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimaryDark)));

//        tv.setTextColor(context.getResources().getColor(R.color.green));
//        rel_matka_color.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_circle));
    }

    public void setInactiveStatus(TextView tv, ImageView btn, TextView  txtPlayPause,LinearLayout lin_play, CardView rel_matka_color){
        if(tv.getVisibility()== View.GONE){
            tv.setVisibility(View.VISIBLE);
        }
        tv.setText("Betting is Closed");
        tv.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
        tv.setTextColor(context.getResources().getColor(R.color.white));
        txtPlayPause.setText("Bet Closed");
        lin_play.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
//        rel_matka_color.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
       // btn.setImageDrawable(context.getResources().getDrawable(R.drawable.pause));
        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_circle));
    }

    public void gotoGames(MatkasObjects model){
        String dyClick=new SimpleDateFormat("EEEE").format(new Date());
//                String dyClick="Sunday";
        Log.e("asdaee",""+dyClick);
        String stime ="";
        String etime ="";
        int err=0;
        boolean is_error=false;
        if(dyClick.equalsIgnoreCase("Sunday"))
        {
            if(getValidTime(model.getStart_time(),model.getEnd_time()))
            {err=1;
                stime= model.getStart_time();
                etime= model.getEnd_time();
                Log.e(TAG, "onClick: "+etime );

            }else{
                err=2;
                is_error=true;
            }


        }
        else if(dyClick.equalsIgnoreCase("Saturday"))
        {
            if(getValidTime(model.getSat_start_time(),model.getSat_end_time()))
            {
                err=3;
                stime= model.getSat_start_time();
                etime= model.getSat_end_time();
            }else{
                err=4;
                is_error=true;

            }
        }
        else
        {
            stime= model.getBid_start_time();
            etime= model.getBid_end_time();
        }

        long endDiff=common.getTimeDifference(etime);
//              common.showToast(""+err);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        if(endDiff<0 || is_error==true)
        {
            new ToastMsg(context).toastIconError("Betting is Closed");
        }
        else
        {
            try
            {
                String m_id= model.getId().trim();
                Log.e("mat",m_id);
                String matka_name= model.getName().trim();
                String status = model.getStatus();

                if (status.equals( "active" )) {
                    Intent intent = new Intent( context, GameListActivity.class );
                    //    intent.putExtra("tim",position);

                    intent.putExtra( "matkaName", matka_name );
                    intent.putExtra( "m_id", m_id );
                    intent.putExtra("type","matka");
                    intent.putExtra("start_time",startTime);
                    intent.putExtra("emd_time",endTime);
                    context.startActivity( intent );
                } else {
                    new ToastMsg(context).toastIconError("Bet Closed ");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(context,""+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
