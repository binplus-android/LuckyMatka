package in.games.luckymatkagames.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.games.luckymatkagames.Model.TableModel;
import in.games.luckymatkagames.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 05,December,2019
 */
public class TableAdaper extends BaseAdapter {
    List<TableModel> list;
    Context context;
    Button btnSave;

    public TableAdaper(List<TableModel> list, Context context, Button btnSave) {
        this.list = list;
        this.context = context;
        this.btnSave = btnSave;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.row_add_data,null);
        TextView txtDigit=(TextView)itemView.findViewById(R.id.txtDigit);
        TextView txtPoints=(TextView)itemView.findViewById(R.id.txtPoints);
        TextView txtType=(TextView)itemView.findViewById(R.id.txtType);
        ImageView txtDelete=(ImageView)itemView.findViewById(R.id.txtDelete);
        try {
            final TableModel tableModel = list.get(i);

            txtDigit.setText(tableModel.getDigits());
            txtPoints.setText(tableModel.getPoints());
            txtType.setText(tableModel.getType());
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    list.remove(i);
                    notifyDataSetChanged();
                    int we = list.size();
                    int points = Integer.parseInt(tableModel.getPoints());
                    int tot_pnt = we * points;

//           btnSave.setText("(BIDS="+we+")(Points="+tot_pnt+")");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return itemView;
    }
}
