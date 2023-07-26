package in.games.luckymatkagames.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.luckymatkagames.Model.MenuModel;
import in.games.luckymatkagames.R;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    Context context;
    ArrayList<MenuModel> list;
    String page;

    public MenuAdapter(Context context, ArrayList<MenuModel> list , String page) {
        this.context = context;
        this.list = list;
        this.page = page;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =null;
        if (page.equals("wallet")) {
            view= LayoutInflater.from(context).inflate(R.layout.row_withdraw_options,null);
        }else if (page.equals("home")){
            view= LayoutInflater.from(context).inflate(R.layout.row_menu_main,null);
        } else {
            view= LayoutInflater.from(context).inflate(R.layout.row_menu,null);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {

        holder.img_item.setImageDrawable(context.getResources().getDrawable(list.get(position).getIcon()));
        holder.tv_name.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView tv_name;
        LinearLayout lin_imag,lin_wave,lin_main;
        CardView cv_main;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            lin_wave=itemView.findViewById (R.id.lin_wave);
            cv_main=itemView.findViewById (R.id.cv_main);
            lin_imag=itemView.findViewById (R.id.lin_imag);
            img_item=itemView.findViewById (R.id.iv_icon);
            tv_name=itemView.findViewById (R.id.tv_name);
            lin_main=itemView.findViewById(R.id.lin_main);

        }
    }
}
