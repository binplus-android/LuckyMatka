package in.games.luckymatkagames.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.luckymatkagames.Activity.UpiDetailsActivity;
import in.games.luckymatkagames.Model.MenuModel;
import in.games.luckymatkagames.R;


public class WithdrawMethodAdapter extends RecyclerView.Adapter<WithdrawMethodAdapter.ViewHolder> {
    Context context;
    ArrayList<MenuModel> list;

    public WithdrawMethodAdapter(Context context, ArrayList<MenuModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WithdrawMethodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_withdraw_methods,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawMethodAdapter.ViewHolder holder, int position) {
        try {
            holder.img_item.setImageDrawable(context.getResources().getDrawable(list.get(position).getIcon()));
            holder.tv_name.setText(list.get(position).getName());
            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, UpiDetailsActivity.class);
                    i.putExtra("title", list.get(position).getName());
                    context.startActivity(i);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView tv_name;
        Button btn_add;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            img_item=itemView.findViewById (R.id.iv_icon);
            tv_name=itemView.findViewById (R.id.tv_name);
            btn_add=itemView.findViewById (R.id.btn_add);

        }
    }
}
