package in.games.luckymatkagames.Adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import in.games.luckymatkagames.Model.SpinnerModel;
import in.games.luckymatkagames.R;


public class SpinnerAdapter extends ArrayAdapter<Integer> {
    ArrayList<SpinnerModel> list;
    ArrayList<Integer> ilist;

    public SpinnerAdapter(Context context,ArrayList<SpinnerModel> list,ArrayList<Integer>ilist) {
        super (context,R.layout.support_simple_spinner_dropdown_item ,ilist);
        // super (context, android.R.layout.simple_spinner_item, list);
        this.list = list;
        this.ilist = ilist;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition (position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition (position);
    }

    private View getImageForPosition(int position) {
        ImageView imageView = new ImageView (getContext ( ));
        imageView.setBackgroundResource (ilist.get (position));

        imageView.setLayoutParams (new AbsListView.LayoutParams (250,110));
        return imageView;
    }
}