package com.Arzaq.Arzaq.etbara3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Arzaq.Arzaq.etbara3.Adapter.FavoriteAdapter;
import com.Arzaq.Arzaq.etbara3.Database.Database;
import com.Arzaq.Arzaq.etbara3.Model.Model;

import java.util.ArrayList;

public class FavourityActivity extends AppCompatActivity {

    private ListView listFav;
    private TextView textFav;
    private Button buttonFav;
    private ArrayList<Model> arrayAdapter;
    private ArrayList<Model> list = new ArrayList<>();
    private FavoriteAdapter favAdapter;
    private Database db;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourity);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverFav, new IntentFilter("islam_yassin"));

        listFav = (ListView) findViewById(R.id.listViewFav);
        textFav = (TextView) findViewById(R.id.text_fav);
        buttonFav = (Button) findViewById(R.id.button_fav);

        getSupportActionBar().setTitle("المفضله");
//        toolbar = (Toolbar) findViewById(R.id.toolbarfav);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.action_bar_fav);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getData();

        favAdapter = new FavoriteAdapter(FavourityActivity.this, R.layout.list_row, list);
        listFav.setAdapter(favAdapter);
        favAdapter.notifyDataSetChanged();

        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavourityActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                //  startActivity(new Intent(FavourityActivity.this, MainActivity.class));
            }
        });

        listFav.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private AlertDialog alertDialog;
            Database db = new Database(FavourityActivity.this);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Model model = favAdapter.getItem(position);
                View dialog = LayoutInflater.from(FavourityActivity.this).inflate(R.layout.cunfarm_dialog, null);
                final TextView textView = (TextView) dialog.findViewById(R.id.dialog_text);
                final Button send = (Button) dialog.findViewById(R.id.send);

                final AlertDialog.Builder builder = new AlertDialog.Builder(FavourityActivity.this);
                builder.setView(dialog);

                textView.setText("سوف تقوم بالتبرع بقيمه (" + model.getOrganizationMouny() + ") جنيه الى (" + model.getOrganizationName() + ") للإستمرار اضغط ارسال ليتم تحويلك الى شاشه الرسائل لإتمام عمليه التبرع ");

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + model.getOrganizationSMS()));
                        i.putExtra("sms_body", model.getOrganizationSMSContent() + "");
                        startActivityForResult(i, 1);
                        alertDialog.cancel();
                    }
                });
                builder.setCancelable(true);
                alertDialog = builder.show();

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final Database db = new Database(FavourityActivity.this);


//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        alertDialog.cancel();
//                        favAdapter.clear();
//                        getData();
//                        favAdapter = new FavoriteAdapter(FavourityActivity.this, R.layout.list_row, list);
//                        listFav.setAdapter(favAdapter);
//                        favAdapter.notifyDataSetChanged();
//                    }
//                });
            }
        });


        listFav.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Model model = favAdapter.getItem(i);
                Intent k = new Intent(FavourityActivity.this, ScrollingActivity.class);
                k.putExtra("MyClass", model);
                startActivity(k);
                return true;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(FavourityActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Toast.makeText(FavourityActivity.this, "تمت عمليه التبرع بنجاح ... شكرا لتبرعك", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(FavourityActivity.this, "لم تتم عمليه التبرع", Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver broadcastReceiverFav = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("fav");
            if (action.equals("fav_remove")) {

//                listFav.setVisibility(View.INVISIBLE);
//                textFav.setVisibility(View.VISIBLE);
//                buttonFav.setVisibility(View.VISIBLE);
                Intent i = new Intent(FavourityActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourite_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up buttonall, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //    Toast.makeText(getApplicationContext(), "HOME", Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(this);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
