package in.games.luckymatkagames.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import in.games.luckymatkagames.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 13,June,2020
 */
public class ToastMsg {
    Context context;
    LayoutInflater layoutInflater;

    public ToastMsg(Context context) {
        this.context = context;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void toastIconError(String s)
    {
        Toast toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        android.view.View view=layoutInflater.inflate(R.layout.toast_icon_text,null);
        ((android.widget.TextView)view.findViewById(R.id.message)).setText(s);
       // ((ImageView)view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
      //  ((ImageView)view.findViewById(R.id.iv_error)).setImageResource(R.drawable.cancel_48px);
        ((RelativeLayout) view.findViewById(R.id.parent_view)).setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));

//        String str = s.substring(0, 1).toUpperCase(Locale.ROOT);
//        String str1 = s.substring(1, s.length()).toLowerCase(Locale.ROOT);
//        String msg_s = str + str1;
//        Log.e("toast,", "" + msg_s);
//
//        tv_msg.setText(msg_s);
//        tv_msg.setTextColor(Color.WHITE);
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public void toastIconSuccess(String s) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        //inflate view
        android.view.View custom_view = layoutInflater.inflate(R.layout.toast_icon_text, null);
        ((android.widget.TextView) custom_view.findViewById(R.id.message)).setText(s);
       //
        // ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((RelativeLayout) custom_view.findViewById(R.id.parent_view)).setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(custom_view);
        toast.show();
    }
}
