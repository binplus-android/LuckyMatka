package in.games.luckymatkagames.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.NotifyModel;
import in.games.luckymatkagames.R;


public class NotificationAdapter extends RecyclerView.Adapter<in.games.luckymatkagames.Adapter.NotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<NotifyModel> list;
     Common module;

    public NotificationAdapter(Context context, ArrayList<NotifyModel> list) {
        this.context = context;
        this.list = list;
        module = new Common(context);
    }

    @NonNull
    @Override
    public in.games.luckymatkagames.Adapter.NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_notification,null);
        return new in.games.luckymatkagames.Adapter.NotificationAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull in.games.luckymatkagames.Adapter.NotificationAdapter.ViewHolder holder, int position) {
        NotifyModel postion=list.get(position);

        holder.tv_info.setText(Html.fromHtml(postion.getNotification()));
      //  holder.tv_title.setText(postion.getTitle());

        String dateTime = postion.getTime();
        String date = dateTime.substring(0,10);
        String time = module.get24To12Format(dateTime.substring(11,19));
//        Log.e("time",dateTime.substring(11,19));
        holder.tv_time.setText(date+"   :   "+time);
    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_info,tv_time;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_info = itemView.findViewById(R.id.tv_info);
        }
    }
}
