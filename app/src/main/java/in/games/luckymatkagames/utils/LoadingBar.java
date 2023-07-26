package in.games.luckymatkagames.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.RelativeLayout;

import in.games.luckymatkagames.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,April,2020
 */
public class LoadingBar {
    Context context;
   Dialog dialog;
   String msg="";
    RelativeLayout rel_loader;


    public LoadingBar(Context context) {
        this.context = context;

        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setContentView(R.layout.loading_layout);

        dialog.setCanceledOnTouchOutside(false);

    }
    public void show()
    {
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
        dialog.show();
    }

    public void dismiss()
    {
        if(dialog.isShowing())
        {
            dialog.dismiss();

        }
    }


    public boolean isShowing()
    {
       return dialog.isShowing();
    }



}
