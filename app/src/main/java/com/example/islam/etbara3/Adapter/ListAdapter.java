package com.example.islam.etbara3.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.islam.etbara3.Database.Database;
import com.example.islam.etbara3.MainActivity;
import com.example.islam.etbara3.Model.Model;
import com.example.islam.etbara3.R;

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


            row.setTag(holder);

        } else {

            holder = (ViewHolder) row.getTag();
        }

        holder.model = getItem(position);
        holder.mOrganizationName.setText(holder.model.getOrganizationName());
        holder.mOrganizationService.setText(holder.model.getOrganozationService());
        holder.mKema.setText(holder.model.getOrganizationMouny());

        holder.mFavorite.setImageResource(android.R.drawable.btn_star_big_off);

        db = new Database(activity);

        boolean exist = db.OrganizationExistInFav(holder.model.getOrganizationID());
        if (exist)
            holder.mFavorite.setImageResource(android.R.drawable.btn_star_big_on);

        final ViewHolder finalHolder = holder;
        holder.mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean exist = db.OrganizationExistInFav(finalHolder.model.getOrganizationID());

                if (exist) {

                    db.deleteOrganizationFav(finalHolder.model);
                    finalHolder.mFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                    Toast.makeText(activity, "تم الازاله من المفضله", Toast.LENGTH_SHORT).show();


                } else {
                    db.AddOrganizationFavorite(finalHolder.model);
                    finalHolder.mFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                    Toast.makeText(activity, "تم الاضافه الى المفضله", Toast.LENGTH_SHORT).show();

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

    class ViewHolder {

        Model model;
        TextView mKema;
        TextView mOrganizationName;
        TextView mOrganizationService;
        ImageView mFavorite;
        TextView mOrganizationSMS;

    }
}