package com.example.kurt.ugood.explore;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kurt.ugood.R;
import com.firebase.ui.auth.data.model.Resource;

import java.util.ArrayList;

public class ExploreContentAdapter extends ArrayAdapter<ExploreContent> {

    private Context mContext;
    private int mResource;

    public ExploreContentAdapter(Context context, int resource, ArrayList<ExploreContent> expContent) {
        super(context, resource, expContent);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        String str = getItem(position).getContent();

        TextView content = (TextView) convertView.findViewById((R.id.content));

        content.setText(str);

        return convertView;
    }
}
