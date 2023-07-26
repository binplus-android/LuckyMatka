package in.games.luckymatkagames.Adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import in.games.luckymatkagames.Model.ApiGameModel;
import in.games.luckymatkagames.R;


public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    Activity activity;
    ArrayList<ApiGameModel> game_list;

    public GameAdapter(Activity activity, ArrayList<ApiGameModel> game_list) {
        this.activity = activity;
        this.game_list = game_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_games,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {

            final ApiGameModel model = game_list.get(position);
            if(model.getGame_id ().equals ("-1")) {
                holder.lin_game.setVisibility (View.INVISIBLE);
            }

            if(model.getIs_visible ()==false){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.lin_game.setAlpha(.5f);
                }
            }
            holder.game_name.setText(model.getGame_name());
            holder.game_img.setImageDrawable(activity.getDrawable(model.getGame_logo()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return game_list.size();
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
