package in.games.luckymatkagames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.MatkasObjects;
import in.games.luckymatkagames.R;


public class MatkaHistoryAdpater extends RecyclerView.Adapter<MatkaHistoryAdpater.ViewHolder> {
    private final String TAG= MatkaHistoryAdpater.class.getSimpleName();
    private final Activity context;
    Common common ;
    private final ArrayList<MatkasObjects> list;
    private final int flag=0;

    public MatkaHistoryAdpater(Activity context, ArrayList<MatkasObjects> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_namehistory,null);
        return new ViewHolder (view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatkasObjects model = list.get(position);
        holder.game_name.setText (model.getName ());


    }


    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView game_name ;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            game_name= itemView.findViewById(R.id.game_name);

        }
    }
}
