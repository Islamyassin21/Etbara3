package com.Arzaq.Arzaq.etbara3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Arzaq.Arzaq.etbara3.Model.Model;
import com.Arzaq.Arzaq.etbara3.R;

import java.util.List;

/**
 * Created by islam on 30/01/2017.
 */

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolder> {

    public List<Model> mDataset;
    public Context mContext;

    public YoutubeAdapter(Context context, List<Model> dataset) {
        super();
        mDataset = dataset;
        mContext = context;
    }

    @Override
    public YoutubeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.youtube_list, null);
        YoutubeAdapter.ViewHolder holder = new YoutubeAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(YoutubeAdapter.ViewHolder viewHolder, int i) {
        final Model model = mDataset.get(i);
        viewHolder.mTextView.setText(model.getOrganizationYoutubeName());

        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(mContext, model.getOrganizationYoutubeLink(), Toast.LENGTH_LONG).show();
                String youtube = "http://www.youtube.com/watch?v=" + model.getOrganizationYoutubeLink();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube));
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.youtubeText);
        }
    }
}
