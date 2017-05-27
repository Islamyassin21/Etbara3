package com.Arzaq.Arzaq.etbara3;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Arzaq.Arzaq.etbara3.Adapter.YoutubeAdapter;
import com.Arzaq.Arzaq.etbara3.Model.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private TextView name, info, phone, account;
    private ImageView imageView;
    private Button call, sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final RecyclerView anotherRecyclerView = (RecyclerView) findViewById(R.id.recyclerHorizontalView);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this);
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        anotherRecyclerView.setLayoutManager(horizontalLayoutManager);

//        List<String> dataset = new LinkedList<String>();
//        for (int i = 0; i < 100; i++) {
//            dataset.add("item" + i);
//        }


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.detailsOrganizationName);
        info = (TextView) findViewById(R.id.detailsOrganizationInfo);
        phone = (TextView) findViewById(R.id.detailsOrganizationPhone);
        account = (TextView) findViewById(R.id.detailsOrganizationAcountNo);
        imageView = (ImageView) findViewById(R.id.detailsImage);
        call = (Button) findViewById(R.id.detailsCall);
        sms = (Button) findViewById(R.id.detailsSend);

        final Bundle bundle = getIntent().getExtras();
//        Model model = new Model();
//        model.setOrganizationName(bundle.get("organizationName") + "");
//        model.setOrganizationInfo(bundle.get("organizationInfo") + "");
//        model.setOrganizationPhone(bundle.get("organizationPhone") + "");
//        model.setOrganizationAccountNo(bundle.get("organizationAcountNo") + "");
//        model.setOrganizationYoutubeLink(bundle.get("organizationYoutubeLink") + "");
//        model.setOrganizationYoutubeName(bundle.get("organizationYoutubeName") + "");
        if (bundle.get("organizationName") == "مستشفى57357") {
            imageView.setImageResource(R.drawable.mostasfa57357);
        } else {
            imageView.setImageResource(R.drawable.mostasfa57357);
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri number = Uri.parse("tel:01060608878");
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(number);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(i);

            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(DetailsActivity.this).inflate(R.layout.custom_dialog, null);
                final TextView msg = (TextView) view.findViewById(R.id.dialogText);
                final CheckBox check = (CheckBox) view.findViewById(R.id.dialogCheckBox);
                final AlertDialog.Builder dialog = new AlertDialog.Builder(DetailsActivity.this);
                dialog.setView(view);
                msg.setText("ارسال رساله الى " + bundle.get("organizationSMS") + "");
                msg.setTextSize(20);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + bundle.get("organizationSMS")));
                        i.putExtra("sms_body", bundle.get("organizationSMSContent") + "");
                        startActivity(i);
                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();

                String messageToSend = "this is a message";
                String number = "+201119155799";
                // sendSMS(number, messageToSend);

                // PendingIntent piSent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("in.wptrafficanalyzer.sent") , 0);

                /** Creating a pending intent which will be broadcasted when an sms message is successfully delivered */
                //  PendingIntent piDelivered = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("in.wptrafficanalyzer.delivered"), 0);

                /** Getting an instance of SmsManager to sent sms message from the application*/
                //  SmsManager smsManager = SmsManager.getDefault();

                /** Sending the Sms message to the intended party */
                // SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);


            }
        });


        name.setText(bundle.get("organizationName") + "");
        info.setText(bundle.get("organizationInfo") + "");
        phone.setText(bundle.get("organizationPhone") + "");
        account.setText(bundle.get("organizationAcountNo") + "");
        String link = String.valueOf(bundle.get("organizationYoutubeLink"));
        String name = String.valueOf(bundle.get("organizationYoutubeName"));

        String[] ArrayLink = link.split("[@]");
        List<String> dataLink = new LinkedList<String>();
        for (int i = 0; i < ArrayLink.length; i++) {

            dataLink.add(ArrayLink[i]);
        }

        String[] ArrayName = name.split("[@]");
        List<String> dataName = new LinkedList<String>();
        for (int i = 0; i < ArrayName.length; i++) {

            dataName.add(ArrayName[i]);
        }

        ArrayList<Model> allData = new ArrayList<>();
        for (int i = 0; i < dataLink.size(); i++) {
            Model model = new Model();
            model.setOrganizationYoutubeName(dataName.get(i));
            model.setOrganizationYoutubeLink(dataLink.get(i));

            allData.add(model);
        }

        final YoutubeAdapter adapter = new YoutubeAdapter(DetailsActivity.this, allData);
        anotherRecyclerView.setAdapter(adapter);

        //anotherRecyclerView.addOnItemTouchListener();
        //   Toast.makeText(DetailsActivity.this, ar[0] + " " + ar2[0], Toast.LENGTH_LONG).show();

    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


//    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//
//        public List<String> mDataset;
//
//        public MyAdapter(List<String> dataset) {
//            super();
//            mDataset = dataset;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View view = View.inflate(viewGroup.getContext(), android.R.layout.simple_list_item_1, null);
//            ViewHolder holder = new ViewHolder(view);
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder viewHolder, int i) {
//            viewHolder.mTextView.setText(mDataset.get(i));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mDataset.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            public TextView mTextView;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                mTextView = (TextView) itemView;
//            }
//        }
//    }

}
