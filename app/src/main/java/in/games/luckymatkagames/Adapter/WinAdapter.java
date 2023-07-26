package in.games.luckymatkagames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.WinModel;
import in.games.luckymatkagames.R;


public class WinAdapter extends RecyclerView.Adapter<WinAdapter.ViewHolder> {
    private final String TAG= WinAdapter.class.getSimpleName();
    private final Activity context;
    Common common ;
    private final ArrayList<WinModel> list;
    private final int flag=0;

    public WinAdapter(Activity context, ArrayList<WinModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_winhistory,null);
        return new ViewHolder (view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            WinModel model = list.get(position);
            holder.txt_matka_name.setText(model.getName());
            holder.txt_bid_time.setText(model.getDate());
            holder.txt_play_for.setText("Bid Id   " + model.getBid_id());
            holder.txt_bid_digit.setText("Digit   " + model.getDigits());
            holder.txt_bid_points.setText("Amount  " + model.getAmt());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_matka_name ,txt_play_on ,txt_play_for,txt_bid_id,txt_bid_digit,txt_bid_points,txt_bid_time,txt_result;
        ImageView iv_icon;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            txt_matka_name= itemView.findViewById(R.id.game_name);
            txt_play_for= itemView.findViewById(R.id.game_type);
            txt_play_on= itemView.findViewById(R.id.txtDate);
            txt_bid_digit= itemView.findViewById(R.id.txtDigit);
            txt_bid_id= itemView.findViewById(R.id.bid_id);
            txt_bid_points= itemView.findViewById(R.id.points);
            txt_bid_time= itemView.findViewById(R.id.txtDate);
            txt_result= itemView.findViewById(R.id.status);
            iv_icon = itemView.findViewById(R.id.iv_icon);
        }
    }
}
