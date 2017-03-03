package com.example.islam.etbara3;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.islam.etbara3.Adapter.ListAdapter;
import com.example.islam.etbara3.Database.Database;
import com.example.islam.etbara3.Model.Model;

import java.util.ArrayList;
import java.util.List;

public class FavourityActivity extends AppCompatActivity {

    private ListView listFav;
    private TextView textFav;
    private Button buttonFav;
    private ArrayList<Model> arrayAdapter;
    private ArrayList<Model> list = new ArrayList<>();
    private ListAdapter listAdapter;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourity);


        listFav = (ListView) findViewById(R.id.listViewFav);
        textFav = (TextView) findViewById(R.id.text_fav);
        buttonFav = (Button) findViewById(R.id.button_fav);


        getData();

        listAdapter = new ListAdapter(FavourityActivity.this, R.layout.list_row, list);
        listFav.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //  startActivity(new Intent(FavourityActivity.this, MainActivity.class));
            }
        });

        listFav.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private AlertDialog alertDialog;
            Database db = new Database(FavourityActivity.this);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Model model = listAdapter.getItem(position);
                View dialog = LayoutInflater.from(FavourityActivity.this).inflate(R.layout.cunfarm_dialog, null);
                final TextView textView = (TextView) dialog.findViewById(R.id.dialog_text);
                final Button done = (Button) dialog.findViewById(R.id.dialog_done);
                final Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
                final ImageView fav = (ImageView) dialog.findViewById(R.id.dialog_fav);

                boolean exist = db.OrganizationExistInFav(model.getOrganizationID());
               // Toast.makeText(FavourityActivity.this, model.getOrganizationID() + "", Toast.LENGTH_LONG).show();
                if (exist)
                    fav.setImageResource(android.R.drawable.btn_star_big_on);


                final AlertDialog.Builder builder = new AlertDialog.Builder(FavourityActivity.this);
                builder.setView(dialog);

                textView.setText("انت على وشك التبرع بقيمه (" + model.getOrganizationMouny() + ") جنيه لصالح (" + model.getOrganizationName() + ") للإستمرار اضغط موافق و سيقوم البرنامج مباشرة بتحويلك الى شاشه الرسائل لإتمام عمليه التبرع");

                builder.setCancelable(false);
                alertDialog = builder.show();

                final Database db = new Database(FavourityActivity.this);

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + model.getOrganizationSMS()));
                        i.putExtra("sms_body", model.getOrganizationSMSContent() + "");
                        startActivity(i);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.cancel();
                        listAdapter.clear();
                        getData();
                        listAdapter = new ListAdapter(FavourityActivity.this, R.layout.list_row, list);
                        listFav.setAdapter(listAdapter);
                        listAdapter.notifyDataSetChanged();
                    }
                });

                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        boolean exist = db.OrganizationExistInFav(model.getOrganizationID());

                        if (exist) {

                            db.deleteOrganizationFav(model);
                            fav.setImageResource(android.R.drawable.btn_star_big_off);
                            Toast.makeText(FavourityActivity.this, "تم الازاله من المفضله", Toast.LENGTH_LONG).show();

                        } else {
                            db.AddOrganizationFavorite(model);
                            fav.setImageResource(android.R.drawable.btn_star_big_on);
                            Toast.makeText(FavourityActivity.this, "تم الاضافه الى المفضله", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
    }

    public void getData() {

        db = new Database(FavourityActivity.this);
        arrayAdapter = db.getFavourite();

        if (arrayAdapter.size() != 0) {

            listFav.setVisibility(View.VISIBLE);
            textFav.setVisibility(View.INVISIBLE);
            buttonFav.setVisibility(View.INVISIBLE);

            for (int i = 0; i < arrayAdapter.size(); i++) {

                Model model = new Model();

                model.setOrganizationID(arrayAdapter.get(i).getOrganizationID());
                model.setOrganizationYoutubeLink(arrayAdapter.get(i).getOrganizationYoutubeLink());
                model.setOrganizationSMSContent(arrayAdapter.get(i).getOrganizationSMSContent());
                model.setOrganizationSMS(arrayAdapter.get(i).getOrganizationSMS());
                model.setOrganizationYoutubeName(arrayAdapter.get(i).getOrganizationYoutubeName());
                model.setOrganizationAccountNo(arrayAdapter.get(i).getOrganizationAccountNo());
                model.setOrganizationInfo(arrayAdapter.get(i).getOrganizationInfo());
                model.setOrganizationMouny(arrayAdapter.get(i).getOrganizationMouny());
                model.setOrganizationName(arrayAdapter.get(i).getOrganizationName());
                model.setOrganizationPhone(arrayAdapter.get(i).getOrganizationPhone());
                model.setOrganizationPhoto(arrayAdapter.get(i).getOrganizationPhoto());
                model.setOrganozationService(arrayAdapter.get(i).getOrganozationService());

                list.add(model);
            }
        } else {
            listFav.setVisibility(View.INVISIBLE);
            textFav.setVisibility(View.VISIBLE);
            buttonFav.setVisibility(View.VISIBLE);
        }
    }
}
