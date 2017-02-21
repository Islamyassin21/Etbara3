package com.example.islam.etbara3.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

    public ListAdapter(Context act, int resource, List<Model> data) {
        super(act, resource, data);

        activity = act;
        layoutresource = resource;
        mData = data;
        notifyDataSetChanged();
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

       //     holder.organizationName = (Button) row.findViewById(R.id.button);
            holder.mKema = (TextView) row.findViewById(R.id.listKema);
            holder.mOrganizationName = (TextView) row.findViewById(R.id.listOrganizationName);
            holder.mOrganizationService = (TextView) row.findViewById(R.id.listOrganizationService);
            holder.mOrganizationSMS = (TextView) row.findViewById(R.id.listOrganizationSMS);

            row.setTag(holder);

        } else {

            holder = (ViewHolder) row.getTag();
        }

        holder.model = getItem(position);
      //  holder.organizationName.setText(holder.model.getOrganizationName());
        holder.mOrganizationName.setText(holder.model.getOrganizationName());
        holder.mOrganizationSMS.setText(holder.model.getOrganizationSMS());
        holder.mOrganizationService.setText(holder.model.getOrganozationService());
        holder.mKema.setText(holder.model.getOrganizationMouny());


        final ViewHolder finalHolder = holder;
//        finalHolder.organizationName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Model model = getItem(position);
//                  Toast.makeText(finalHolder.organizationName.getContext(), position + " " + islam.getOrganizationInfo(), Toast.LENGTH_LONG).show();
//                Intent i = new Intent(finalHolder.organizationName.getContext(), DetailsActivity.class);
//                i.putExtra("organizationName", model.getOrganizationName());
//                i.putExtra("organizationInfo", model.getOrganizationInfo());
//                i.putExtra("organizationAcountNo", model.getOrganizationAccountNo());
//                i.putExtra("organizationPhone", model.getOrganizationPhone());
//                i.putExtra("organizationSMSContent", model.getOrganizationSMSContent());
//                i.putExtra("organizationSMS", model.getOrganizationSMS());
//                i.putExtra("organizationYoutubeLink", model.getOrganizationYoutubeLink());
//                i.putExtra("organizationYoutubeName", model.getOrganizationYoutubeName());
//                i.putExtra("organizationService", model.getOrganozationService());
//                i.putExtra("organizationMouny", model.getOrganizationMouny());
//                activity.startActivity(i);
//
//            }
//        });

        return row;
    }

//    private void LoadImageFromURL(String url, ListAdapter.ViewHolder holder) {
//
//        Picasso.with(holder.poster.getContext()).load(url).placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher).into(holder.poster);
//    }

    class ViewHolder {

        Model model;
    //    Button organizationName;
        TextView mKema;
        TextView mOrganizationName;
        TextView mOrganizationService;
        TextView mOrganizationSMS;

    }
}