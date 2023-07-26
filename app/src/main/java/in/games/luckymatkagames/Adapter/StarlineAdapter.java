package in.games.luckymatkagames.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.games.luckymatkagames.Model.StarlineModel;
import in.games.luckymatkagames.R;


public class StarlineAdapter extends RecyclerView.Adapter<StarlineAdapter.ViewHolder> {
    Context context;
    ArrayList<StarlineModel> list;

    public StarlineAdapter(Context context, ArrayList<StarlineModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_games,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final StarlineModel model = list.get(position);
        holder.game_name.setText(model.getGame_name ());
//        holder.game_img.setImageResource (list.get (position).getImg ());
        Glide.with(context).load(model.getGame_logo()).into(holder.game_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView game_img ;
        TextView game_name ;
        RelativeLayout lin_game;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            game_img = itemView.findViewById(R.id.game_img);
            game_name = itemView.findViewById(R.id.game_name);
            lin_game = itemView.findViewById(R.id.lin_game);
        }
    }
}
