package in.games.luckymatkagames.Adapter;



import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.luckymatkagames.R;


public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
//    ArrayList<ResultModel>list;
    ArrayList<String> list;
    Context context;
    String result;

    public ResultAdapter(ArrayList<String> list, Context context, String result) {
        this.list = list;
        this.context = context;
        this.result=result;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_number,null);
        return new ViewHolder (view);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        ResultModel model = list.get (position);

//        holder.tv_num.setText (model.getResult ( ));
        holder.tv_num.setText (list.get(position));



//            if (result.equals ("result")) {
//                if (position == 0) {
//                holder.tv_num.setBackgroundTintList (ColorStateList.valueOf (context.getResources ( ).getColor (R.color.green)));
//            }
//        }
//            else{
//                holder.tv_num.setBackgroundTintList (ColorStateList.valueOf (context.getResources ( ).getColor (R.color.red)));
//
//            }
//            int i = Integer.parseInt(model.getResult());
            int i = Integer.parseInt(list.get(position));

            if (i==1||i==3||i==5||i==7||i==9||i==12||i==14||i==18||i==21||i==16||i==19||i==23||i==25||i==27||i==30||i==32||i==34||i==36){
                holder.tv_num.setBackgroundTintList (ColorStateList.valueOf (context.getResources ( ).getColor (R.color.red)));

            }else {
                holder.tv_num.setBackgroundTintList (ColorStateList.valueOf (context.getResources ( ).getColor (R.color.black)));

            }
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
        TextView tv_num;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            tv_num=itemView.findViewById(R.id.tv_num);

        }
    }
}
