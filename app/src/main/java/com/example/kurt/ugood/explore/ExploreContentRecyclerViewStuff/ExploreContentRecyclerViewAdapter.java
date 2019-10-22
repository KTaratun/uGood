package com.example.kurt.ugood.explore.ExploreContentRecyclerViewStuff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.classes.Interfaces.ExploreContentButtonClickListener;
import com.example.kurt.ugood.explore.ExploreContent;

import java.util.ArrayList;

public class ExploreContentRecyclerViewAdapter extends RecyclerView.Adapter<ExploreContentRecyclerViewAdapter.ExploreContentViewHolder> {

    ArrayList<ExploreContent> mAllExploreConent;
    Context mContext;
    private final ExploreContentButtonClickListener listener;



    public class ExploreContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mConent;
        ImageButton mHeartButton;
        ImageButton mCrownButton;

        public ExploreContentViewHolder(@NonNull View itemView) {
            super(itemView);

            mConent = itemView.findViewById(R.id.content);
            mHeartButton = itemView.findViewById(R.id.heart);
            mCrownButton = itemView.findViewById(R.id.crown);

            mCrownButton.setOnClickListener(this);
            mHeartButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == mCrownButton){
                listener.onPositionCrownClicked(getAdapterPosition());
            }
            if(view == mHeartButton){
                listener.onPositionHeartClicked(getAdapterPosition());
            }
        }
    }

    public ExploreContentRecyclerViewAdapter(ArrayList<ExploreContent> mAllExploreContent, Context mContext, ExploreContentButtonClickListener listener) {
        this.mAllExploreConent = mAllExploreContent;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExploreContentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.explore_new_content_list, viewGroup, false);
        return new ExploreContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreContentViewHolder exploreContentViewHolder, int i) {
        exploreContentViewHolder.mConent.setText(mAllExploreConent.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return mAllExploreConent.size();
    }

}
