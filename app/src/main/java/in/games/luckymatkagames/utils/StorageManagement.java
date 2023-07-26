package in.games.luckymatkagames.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 25,June,2020
 */
public class StorageManagement {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public final String PREF_NAME="smart_self";
    public final String KEY_REF="referal_code";
    public final String KEY_STATUS="status";
    Context context;
    int PRIVATE_MODE = 0;
    public StorageManagement(Context context) {
        this.context = context;
        prefs=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=prefs.edit();
    }

    public void storePref(String ref_code)
    {
        editor.putString(KEY_REF,ref_code);
        editor.putBoolean(KEY_STATUS,true);
        editor.apply();
    }
    public String getREFCODE()
    {
        return prefs.getString(KEY_REF,"");
    }
    public boolean getRefStatus()
    {
        return prefs.getBoolean(KEY_STATUS,false);
    }

}
