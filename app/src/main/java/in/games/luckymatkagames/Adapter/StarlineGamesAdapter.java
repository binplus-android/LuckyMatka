package in.games.luckymatkagames.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.luckymatkagames.Activity.GameListActivity;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.StarlineGamesModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.ToastMsg;


public class StarlineGamesAdapter extends RecyclerView.Adapter<StarlineGamesAdapter.ViewHolder> {
    Context context;
    ArrayList<StarlineGamesModel> list;
    Common common;

    public StarlineGamesAdapter(Context context, ArrayList<StarlineGamesModel> list) {
        this.context = context;
        this.list = list;
        common = new Common (context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_starline, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final StarlineGamesModel model=list.get(position);
        StarlineGamesModel postion = list.get(position);
        Date date = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm aa");
        String dr = format1.format(date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String ddt = simpleDateFormat.format(date);
        int c_tm = Integer.parseInt(ddt);
        holder.tv_time.setText(postion.getS_game_time());

        String tm = getCloseStatus(postion.getS_game_end_time(), dr);
        String[] end_time = tm.split(":");
        int h = Integer.parseInt(end_time[0]);
        int m = Integer.parseInt(end_time[1]);
        Log.e("starline", "getView: " + h + "  : " + m);
        holder.tv_open.setText("OPEN\n"+model.getS_game_time());
        holder.tv_close.setText("CLOSE \n"+model.getS_game_end_time());
        Log.e("dfcgthyju",model.getS_game_time()+"     "+model.getS_game_end_time());
        if (h <= 0 && m < 0) {
            holder.tv_number.setText("xxx-xx");
            setActiveStatus(holder.tv_status,holder.iv_play);
        } else {

            holder.tv_number.setText("" + postion.getS_game_number());
            setInactiveStatus(holder.tv_status,holder.rel_matka_color,holder.play_btn);
        }


        holder.rel_starline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGames(model);
            }
        });
        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGames(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time,tv_number,tv_open,tv_close,tv_status;
        ImageView iv_play;
        RelativeLayout rel_starline,rel_matka_color;
        LinearLayout close_btn,play_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rel_matka_color=itemView.findViewById (R.id.rel_matka_color);
            tv_time = itemView.findViewById(R.id.tv_name);
            tv_number = itemView.findViewById(R.id.tv_number);
            tv_open = itemView.findViewById(R.id.tv_open);
            tv_close = itemView.findViewById(R.id.tv_close);
            tv_status  = itemView.findViewById(R.id.tv_status);
            iv_play =itemView.findViewById(R.id.iv_play);
            close_btn =itemView.findViewById(R.id.close_btn);
            play_btn=itemView.findViewById(R.id.play_btn);

            rel_starline = itemView.findViewById(R.id.rel_starline);

        }
    }

    public String getCloseStatus(String gm_time, String current_time)
    {
        int h=0;
        int m=0;
        try {
            int days=0,hours=0,min=0;

            Date date1=new Date();
            Date date2=new Date();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
            boolean st_time=getStatusTime(current_time);
            if(st_time)
            {
                date1 = simpleDateFormat.parse(formatTime(gm_time));
                date2 = simpleDateFormat.parse(current_time);

            }
            else
            {
                date1 = simpleDateFormat.parse(gm_time);
                date2 = simpleDateFormat.parse(current_time);

            }

            long difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000*60*60*24));
            hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

//        hours = (hours < 0 ? -hours : hours);
//        min = (min < 0 ? -min : min);
            h=hours;
            m=min;
            Log.i("======= Hours"," :: "+hours);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText (context, "Something went Wrong", Toast.LENGTH_SHORT).show ( );

//            Toast.makeText(context,"err :--  "+ex.getMessage()+"\n "+gm_time+"\n "+current_time, Toast.LENGTH_LONG).show();
        }
        String d=""+h+":"+m;
        return d;
    }

    public boolean getStatusTime(String current_tim)
    {
        boolean st=false;
        String[] t =current_tim.split(" ");
        String time_type= t[1];
        if(time_type.equals("a.m.") || time_type.equals("p.m."))
        {
            st=true;
        }
        else if(time_type.equals("AM") || time_type.equals("PM"))
        {
            st=false;
        }
        return st;
    }

    public String formatTime(String time)
    {
        String tm="";
        String[] t =time.split(" ");
        String time_type= t[1];

        if(time_type.equals("PM"))
        {
            tm="p.m.";
        }
        else if(time_type.equals("AM"))
        {
            tm="a.m.";
        }
        else
        {
            tm=time_type;
        }

        String c_tm= t[0] +" "+tm;
        return c_tm;
    }
    public void setActiveStatus(TextView tv, ImageView btn){
//        tv.setVisibility(View.GONE);
        tv.setText("Running");
        tv.setTextColor(context.getResources().getColor(R.color.green));
//        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_play_circle_24));
    }

    public void setInactiveStatus(TextView tv, RelativeLayout cbtn,LinearLayout pbtn){

//        tv.setVisibility(View.GONE);
        tv.setText("Closed");
        tv.setTextColor(context.getResources().getColor(R.color.black));
       // cbtn.setVisibility (View.VISIBLE);
        cbtn.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));

        pbtn.setVisibility (View.GONE);

//        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_pause_circle_24));
    }

    public boolean getValidTime(String sTime, String eTime){

        if(sTime.equalsIgnoreCase("00:00:00") && eTime.equalsIgnoreCase("00:00:00")){
            return false;
        }else return !sTime.equalsIgnoreCase("00:00:00.000000") || !eTime.equalsIgnoreCase("00:00:00.000000");
    }

    private void gotoGames(StarlineGamesModel model) {
//        StarlineGamesModel model = sList.get(position);
        String e_t = get24Hours(model.getS_game_end_time());
        String s_t = get24Hours(model.getS_game_time());
        int sTime=common.getTimeFormatStatus(model.getS_game_time());
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
        String ddt=simpleDateFormat.format(date);
        int c_tm=Integer.parseInt(ddt);
        String st= get24Hours(model.getS_game_end_time());
        long tmLong=common.getTimeDifference(st);
        if(tmLong<=0)

        { new ToastMsg(context).toastIconError("Bid is Closed for today");
            return;
        }else {
//            Bundle bundle = new Bundle();
//            Fragment fragment = new SelectGameFragment();
//
//            bundle.putString("m_id",model.getId());
//            bundle.putString("end_time",e_t);
//            bundle.putString("start_time",s_t);
//            bundle.putString("matka_name",common.get24To12Format(model.getS_game_end_time()));
//
//            bundle.putString("type","starline");
//            fragment.setArguments(bundle);
//            FragmentManager fragmentManager = context.getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.main_frame,fragment).addToBackStack(null).commit();

            Intent intent = new Intent( context, GameListActivity.class );
            intent.putExtra( "matkaName", model.getS_game_end_time());
            intent.putExtra( "m_id", model.getId() );
            intent.putExtra("type","starline");
            Log.e("dfgthyu",intent.toString());
            context.startActivity( intent );
        }

    }
    String  get24Hours(String time)
    {
        String[] t =time.split(" ");
        String time_type= t[1];
        String[] t1 =t[0].split(":");

        int tm=Integer.parseInt(t1[0]);

        if(time_type.equalsIgnoreCase("PM") || time_type.equalsIgnoreCase("p.m"))
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }

//       String s ="";
//       String h = time.substring(0,1);
//       if (time.contains("PM")|| time.contains("p.m"))
//       {
//       int hours = Integer.parseInt(h);
//       if (hours<12)
//       {
//          hours =hours+12;
//       }
        String s = tm +":"+t1[1]+":00";

        return s;
    }
}
