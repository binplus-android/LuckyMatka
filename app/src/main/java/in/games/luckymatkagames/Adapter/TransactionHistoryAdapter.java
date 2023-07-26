package in.games.luckymatkagames.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.AddWithdrawModel;
import in.games.luckymatkagames.R;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {
      Context context;
  Common common ;
      ArrayList<AddWithdrawModel> list;

    public TransactionHistoryAdapter(Context context, ArrayList<AddWithdrawModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TransactionHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view= LayoutInflater.from(context).inflate(R.layout.row_transaction,null);
        final ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryAdapter.ViewHolder viewHolder, int i) {
        try {

            AddWithdrawModel objects = list.get(i);
            Log.d("Methodsssss", "onBindViewHolder: " + objects.getMethod());
            if (common.checkNull(list.get(i).getMethod())) {
                viewHolder.method.setVisibility(View.INVISIBLE);
            }
            viewHolder.method.setText("Method: " + list.get(i).getMethod());
            viewHolder.txtStatus.setText("Status: " + objects.getRequest_status());
            if (common.checkNull(objects.getTrans_id())) {
                viewHolder.txtId.setVisibility(View.GONE);
            } else {
                viewHolder.txtId.setText(objects.getTrans_id());
            }
            viewHolder.txtAmount.setText(objects.getRequest_points() + " Pts.");

            String st = objects.getType();

            if (st.equalsIgnoreCase("Withdrawal")) {
                viewHolder.iv_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.transaction_list));
            } else {
                viewHolder.iv_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.add_fund));
            }
            viewHolder.txtDate.setText(objects.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtId,txtAmount,txtDescription,txtStatus,txtDate,method;
        RelativeLayout rel_back ;
        ImageView iv_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            method=itemView.findViewById (R.id.method);
            txtId=(TextView)itemView.findViewById(R.id.trans_id);
            txtAmount=(TextView)itemView.findViewById(R.id.trans_amount);
            txtDescription=(TextView)itemView.findViewById(R.id.description);
            txtStatus=(TextView)itemView.findViewById(R.id.status);
            txtDate=(TextView)itemView.findViewById(R.id.txtDate);
            iv_icon = itemView.findViewById(R.id.iv_icon);
//            rel_back = itemView.findViewById( R.id.rel_transaction );
            common = new Common(context);

        }
    }
}
