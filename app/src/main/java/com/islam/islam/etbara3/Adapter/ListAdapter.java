package com.islam.islam.etbara3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.islam.islam.etbara3.Database.Database;
import com.islam.islam.etbara3.Model.Model;
import com.islam.islam.etbara3.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by islam on 27/01/2017.
 */

public class ListAdapter extends ArrayAdapter<Model> {

    Context activity;
    int layoutresource;
    Model model;
    List<Model> mData = Collections.emptyList();
    private Database db;
    private final int[] mCheckstates;
    String url = "https://api.backendless.com/2836510F-E02A-936B-FF67-2C3E68F6D400/v1/files/image/";

    public ListAdapter(Context act, int resource, List<Model> data) {
        super(act, resource, data);

        activity = act;
        layoutresource = resource;
        mData = data;
        notifyDataSetChanged();
        mCheckstates = new int[data.size()];
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Model getItem(int position) {

        return mData.get(position);
    }

    @Override
    public int getPosition(Model item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null || (row.getTag()) == null) {

            LayoutInflater inflater = LayoutInflater.from(activity);

            row = inflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();

            holder.mKema = (TextView) row.findViewById(R.id.listKema);
            holder.mOrganizationName = (TextView) row.findViewById(R.id.listOrganizationName);
            holder.mOrganizationService = (TextView) row.findViewById(R.id.listOrganizationService);
            holder.mFavorite = (ImageView) row.findViewById(R.id.addToFav);
            holder.mOrganizationImage = (ImageView) row.findViewById(R.id.organizationPhoto);

            row.setTag(holder);

        } else {

            holder = (ViewHolder) row.getTag();
        }

        holder.model = getItem(position);
        holder.mOrganizationName.setText(holder.model.getOrganizationName());
        holder.mOrganizationService.setText(holder.model.getOrganozationService());
        holder.mKema.setText(holder.model.getOrganizationMouny());

        holder.mFavorite.setImageResource(android.R.drawable.btn_star_big_off);

        LoadImageFromURL(url + holder.model.getOrganizationPhoto(), holder);

        db = new Database(activity);
        boolean exist = db.OrganizationExistInFav(holder.model.getOrganizationID());

        if (exist)
            holder.mFavorite.setImageResource(android.R.drawable.btn_star_big_on);

        final ViewHolder finalHolder = holder;
        holder.mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean exist = db.OrganizationExistInFav(finalHolder.model.getOrganizationID());
                Intent intent = new Intent("Pitanja_cigle");
                if (exist) {

                    db.deleteOrganizationFav(finalHolder.model);
                    finalHolder.mFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                    //    Toast.makeText(activity, "تم الازاله من المفضله", Toast.LENGTH_SHORT).show();

                    int count = db.getOrganizationCountFav();
                    if (count == 0) {
                        intent.putExtra("action", "remove");
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                    }

                } else {
                    db.AddOrganizationFavorite(finalHolder.model);
                    finalHolder.mFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                    //      Toast.makeText(activity, "تم الاضافه الى المفضله", Toast.LENGTH_SHORT).show();

                    int count = db.getOrganizationCountFav();
                    if (count > 0) {
                        intent.putExtra("action", "add");
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                    }
                }
            }
        });

        return row;
    }

//    private void LoadImageFromURL(String url, ListAdapter.ViewHolder holder) {
//
//        Picasso.with(holder.poster.getContext()).load(url).placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher).into(holder.poster);
//    }

    private void LoadImageFromURL(String url, ListAdapter.ViewHolder holder) {

        Picasso.with(holder.mOrganizationImage.getContext()).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(holder.mOrganizationImage);
    }

    class ViewHolder {

        Model model;
        TextView mKema;
        TextView mOrganizationName;
        TextView mOrganizationService;
        ImageView mFavorite;
        ImageView mOrganizationImage;
        TextView mOrganizationSMS;

    }
}