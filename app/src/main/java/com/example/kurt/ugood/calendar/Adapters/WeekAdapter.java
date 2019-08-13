package com.example.kurt.ugood.calendar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kurt.ugood.R;

import java.util.ArrayList;

public class WeekAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<String[]> mText;

    public WeekAdapter(Context context, ArrayList<String[]> text) {
        super(context, R.layout.calendar_week_fragment);
        mText = text;
        mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mText.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.calendar_week_list, parent, false);

        String[] str = mText.get(position);
        TextView content = convertView.findViewById((R.id.title));
        content.setText(str[0]);
        TextView val1 = convertView.findViewById((R.id.thumb_val));
        val1.setText(str[1]);
        TextView val2 = convertView.findViewById((R.id.grid_val));
        val2.setText(str[2]);
        TextView val3 = convertView.findViewById((R.id.search_val));
        val3.setText(str[3]);


        return convertView;
    }
}
