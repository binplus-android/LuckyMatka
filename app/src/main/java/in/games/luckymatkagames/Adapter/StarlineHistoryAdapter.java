package in.games.luckymatkagames.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.luckymatkagames.Model.MatkasObjects;
import in.games.luckymatkagames.R;

public class StarlineHistoryAdapter extends RecyclerView.Adapter<StarlineHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<MatkasObjects> list;

    //ArrayList<StarlineHistoryModel> list;

    public StarlineHistoryAdapter(Context context, ArrayList<MatkasObjects> list) {
        this.context = context;
        this.list= list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.row_starhistory,null);
        View view= LayoutInflater.from(context).inflate(R.layout.row_starhistory,null);
        return new ViewHolder (view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatkasObjects model = list.get(position);
        holder.tv_name.setText (model.getName ());
        holder.tv_number.setText (model.getNumber ());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */




    @Override
    public int getItemCount() {
        return list.size ();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_number;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            tv_number=itemView.findViewById (R.id.tv_number);
            tv_name=itemView.findViewById (R.id.tv_name);
        }
    }
}
