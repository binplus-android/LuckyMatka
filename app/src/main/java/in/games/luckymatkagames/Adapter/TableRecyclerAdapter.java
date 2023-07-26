package in.games.luckymatkagames.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.games.luckymatkagames.Model.TableModel;
import in.games.luckymatkagames.R;

public class TableRecyclerAdapter extends RecyclerView.Adapter<TableRecyclerAdapter.ViewHolder> {
    List<TableModel> list;
    Activity context;

    public TableRecyclerAdapter(List<TableModel> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_add_data,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        try {
            final TableModel tableModel = list.get(i);
            holder.txtDigit.setText(tableModel.getDigits());
            holder.txtPoints.setText(tableModel.getPoints());
            holder.txtType.setText(tableModel.getType());
            holder.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    list.remove(i);
                    notifyDataSetChanged();
                    int we = list.size();
                    int points = Integer.parseInt(tableModel.getPoints());

                    int tot_pnt = we * points;
                    Intent intent = new Intent("update");

                    context.sendBroadcast(intent);

//           btnSave.setText("(BIDS="+we+")(Points="+tot_pnt+")");
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
        TextView txtDigit,txtPoints, txtType;
        ImageView txtDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDigit=(TextView)itemView.findViewById(R.id.txtDigit);
            txtPoints=(TextView)itemView.findViewById(R.id.txtPoints);
            txtType=(TextView)itemView.findViewById(R.id.txtType);
            txtDelete=(ImageView) itemView.findViewById(R.id.txtDelete);

        }
    }
}
