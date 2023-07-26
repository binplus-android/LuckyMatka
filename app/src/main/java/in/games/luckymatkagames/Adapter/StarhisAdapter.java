package in.games.luckymatkagames.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.luckymatkagames.Model.SatrhisModel;
import in.games.luckymatkagames.R;

public class StarhisAdapter extends RecyclerView.Adapter<StarhisAdapter.ViewHolder> implements Filterable {
    private final Context context;
    public ArrayList<SatrhisModel> list;
    ArrayList<SatrhisModel> listFilter;

    public StarhisAdapter(Context context, ArrayList<SatrhisModel> list) {
        this.context = context;
        this.list =list;
        this.listFilter=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_history,null);
        return new ViewHolder (view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SatrhisModel model = list.get(position);
        String bid_time = model.getTime();
        String d = bid_time.substring(0,10);
        String[] dd =d.split("-");
        holder.txt_play_on.setText(dd[2]+"/"+dd[1]+"/"+dd[0]);
        String t = bid_time.substring(11);
        SimpleDateFormat tformat = new SimpleDateFormat("hh:mm:ss");
        try {
            Date tt = tformat.parse(t);
            SimpleDateFormat f = new SimpleDateFormat("hh:mm a");
            String ttt = f.format(tt);
            holder.txt_bid_time.setText(ttt);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(model.getBet_type ().equals ("")){
            holder.txt_matka_name.setText (model.getS_game_time ());
        }else {
            holder.txt_matka_name.setText(model.getS_game_time ()+" ( "+model.getBet_type()+" ) ");
        }


        //holder.txt_matka_name.setText(model.getName()+" ( "+model.getBet_type()+" ) ");
        holder.txt_bid_digit.setText(model.getDigits());
        holder.txt_bid_id.setText(model.getId());
        holder.txt_bid_points.setText(model.getPoints());
        holder.txt_play_for.setText(model.getDate());


        if (model.getStatus().equals("pending"))
        {
            holder.txt_result.setText("Result pending");
        }
        else if(model.getStatus().equals("won")||model.getStatus().equals("win"))
        {
            holder.txt_result.setText("You won the bid");
        }
        else if(model.getStatus().equals("loss")) {
            holder.txt_result.setText("You Lost the bid");
        } else{
            holder.txt_result.setText("Result pending");
        }


    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list = listFilter;
                } else {
                    ArrayList<SatrhisModel> filteredList = new ArrayList<>();
                    for (SatrhisModel row : listFilter) {
                        Log.e("filterdate",row.getDate()+"   "+charString);
                        if  (row.getDate().equals(charString)) {
                            filteredList.add(row);
                            Toast.makeText (context, "Success", Toast.LENGTH_SHORT).show ( );
                        }

                    }

                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<SatrhisModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size ();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
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
          //  iv_icon = itemView.findViewById(R.id.iv_icon);
        }
    }
}
