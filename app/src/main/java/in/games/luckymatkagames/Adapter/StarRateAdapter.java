package in.games.luckymatkagames.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.luckymatkagames.Model.GameRateModel;
import in.games.luckymatkagames.R;


public class StarRateAdapter extends RecyclerView.Adapter<StarRateAdapter.ViewHolder> {
    ArrayList<GameRateModel> ratelist ;
    Context context ;

    public StarRateAdapter(ArrayList<GameRateModel> ratelist, Context context) {
        this.ratelist = ratelist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( context ).inflate( R.layout.row_star_rates ,null);
        ViewHolder viewHolder = new ViewHolder( view );
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
      GameRateModel rlist = ratelist.get( i );
      viewHolder.game_rate.setText(rlist.getRate_range()+" - " +rlist.getRate());
      viewHolder.game_name.setText( rlist.getName() );
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView game_name ,game_rate ;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            game_name = itemView.findViewById( R.id.tv_name );
            game_rate=itemView.findViewById( R.id.tv_rate );
        }
    }
}
