package com.Arzaq.Arzaq.etbara3;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.Arzaq.Arzaq.etbara3.Adapter.ListAdapter;
import com.Arzaq.Arzaq.etbara3.Database.Database;
import com.Arzaq.Arzaq.etbara3.Model.Model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String APP_ID = "2836510F-E02A-936B-FF67-2C3E68F6D400";
    public static final String SECRET_KEY = "D2CA41FB-FD29-AC4E-FFA9-F736ABCB7300";
    public static final String VERSION = "v1";
    private TextView textView;
    private List<Model> list = new ArrayList<>();
    private ListAdapter listAdapter;
    private ListView listView;
    private ArrayList<Model> arrayAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView connection;
    private ProgressDialog progressDialog;
    private Database db = new Database(MainActivity.this);
    private Menu mMenu;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equals("remove")) {

                mMenu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_off);

            } else {

                mMenu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_on);
            }
        }
    }; // To control of action bar by item om list view
    private AlertDialog.Builder builderHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().hide();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_main);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("Pitanja_cigle")); // run brod cast receiver

        /**************************************(Alert Dialog Hint)*********************************/


        SharedPreferences sharedPreferences = getSharedPreferences("firstHint", MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("hint", false);
        if (!check) {
            View dialogHint = LayoutInflater.from(MainActivity.this).inflate(R.layout.first_hint, null);
            CheckBox checkBox = (CheckBox) dialogHint.findViewById(R.id.hintCheckBox);

            builderHint = new AlertDialog.Builder(MainActivity.this);
            builderHint.setView(dialogHint);
            builderHint.setCancelable(false);
            builderHint.setTitle("اخلاء المسؤوليه ");
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = getSharedPreferences("firstHint", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putBoolean("hint", true).commit();
                }
            });

            builderHint.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    getSupportActionBar().show(); // show action bar
                    getSupportActionBar().setTitle("القائمه الرئيسيه"); // set title in action bar
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    listAdapter = new ListAdapter(MainActivity.this, R.layout.list_row, list);//
                    listView.setAdapter(listAdapter);
                    swipeRefreshLayout.setRefreshing(false);//hide refresh
                }
            });

            //         builderHint.show();
        }
        /******************************************************************************************/

        Reload();
    }

    private void Reload() {

        try {

            Backendless.initApp(this, APP_ID, SECRET_KEY, VERSION);

            textView = (TextView) findViewById(R.id.textview);
            connection = (ImageView) findViewById(R.id.fail);
            listView = (ListView) findViewById(R.id.listViewMain);
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

            swipeRefreshLayout.setOnRefreshListener(this);

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("انتظر لحظه من فضلك .. جاري تحميل البيانات");
            progressDialog.setCancelable(false);
            progressDialog.setInverseBackgroundForced(false);
            progressDialog.show();

            //   String data = sharedPreferences.getString("data", "");

            if (db.getOrganizationCount() == 0) {
                WebServiceDataBackEndLess();
            } else {
                getDataFromDatabase();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                private AlertDialog alertDialog;

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Model model = listAdapter.getItem(position);
                    View dialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.cunfarm_dialog, null);
                    final TextView textView = (TextView) dialog.findViewById(R.id.dialog_text);
                    final Button send = (Button) dialog.findViewById(R.id.send);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(dialog);
                    alertDialog = builder.create();
                    textView.setText("سوف تقوم بالتبرع بقيمه (" + model.getOrganizationMouny() + ") جنيه الى (" + model.getOrganizationName() + ") للإستمرار اضغط ارسال ليتم تحويلك الى شاشه الرسائل لإتمام عمليه التبرع ");
                    //   textView.setText("سوف تقوم بالتبرع بقيمه(" + model.getOrganizationMouny() + ")الى (" + model.getOrganizationName() + "للإستمرار اضغط ارسال ليتم تحويلك الى شاشه الرسائل");
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

                }
            });

//            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    Model model = list.get(position);
//                    Intent i = new Intent(MainActivity.this, DetailsActivity.class);
//                    i.putExtra("organizationName", model.getOrganizationName());
//                    i.putExtra("organizationInfo", model.getOrganizationInfo());
//                    i.putExtra("organizationAcountNo", model.getOrganizationAccountNo());
//                    i.putExtra("organizationPhone", model.getOrganizationPhone());
//                    i.putExtra("organizationSMSContent", model.getOrganizationSMSContent());
//                    i.putExtra("organizationSMS", model.getOrganizationSMS());
//                    i.putExtra("organizationYoutubeLink", model.getOrganizationYoutubeLink());
//                    i.putExtra("organizationYoutubeName", model.getOrganizationYoutubeName());
//                    i.putExtra("organizationService", model.getOrganozationService());
//                    i.putExtra("organizationMouny", model.getOrganizationMouny());
//                    startActivity(i);
//
//                    Toast.makeText(getApplicationContext(), "hello " + position, Toast.LENGTH_LONG).show();
//
//                    return true;
//                }
//            });

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "تأكد من اتصالك بالانترنت", Toast.LENGTH_LONG).show();
        }


        // To add animation to listView
//        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.list_layout_controller);
//        listView.setLayoutAnimation(controller);
    }

    private void WebServiceDataBackEndLess() {

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setRelated(Arrays.asList("organizationName"));
        queryOptions.addSortByOption("organizationID");

        BackendlessDataQuery query = new BackendlessDataQuery(queryOptions);
        query.setPageSize(100);
        Backendless.Persistence.of(Model.class).find(query, new AsyncCallback<BackendlessCollection<Model>>() {
            @Override
            public void handleResponse(BackendlessCollection<Model> response) {
                //  BackendlessCollection<Model> collection = response;

                if (list.size() != 0) {
                    listAdapter.clear();
                }

                list.addAll(response.getCurrentPage());

                if (db.getOrganizationCount() == list.size()) {
                    Toast.makeText(getApplicationContext(), "لا توجد بيانات جديده في الوقت الحالي", Toast.LENGTH_LONG).show();

                } else if (db.getOrganizationCount() < list.size()) {
                    int count = list.size() - db.getOrganizationCount();
                    Toast.makeText(getApplicationContext(), "تم اضافه( " + count + ") مؤسسه جديده", Toast.LENGTH_LONG).show();
                    for (int i = 0; i < list.size(); i++) {
                        Model model = list.get(i);

                        try {
                            URL url = new URL("https://api.backendless.com/2836510F-E02A-936B-FF67-2C3E68F6D400/v1/files/image/" + model.getOrganizationPhoto());

                            if (!db.OrganizationExist(model.getOrganizationID())) {
                                db.AddOrganization(model);

                                (new DownloadTask(url, model.getOrganizationID())).execute();
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    int count = -(list.size() - db.getOrganizationCount());
                    Toast.makeText(getApplicationContext(), "تم ازاله( " + count + ") مؤسسه", Toast.LENGTH_LONG).show();
                    db.deleteOrganization();
                    db.deleteOrganizationPhoto();
                    for (int i = 0; i < list.size(); i++) {
                        Model model = list.get(i);
                        if (!db.OrganizationExist(model.getOrganizationID())) {
                            try {
                                URL url = new URL("https://api.backendless.com/2836510F-E02A-936B-FF67-2C3E68F6D400/v1/files/image/" + model.getOrganizationPhoto());
                                Log.v("photoURL", String.valueOf(url));

                                if (!db.OrganizationExist(model.getOrganizationID())) {
                                    db.AddOrganization(model);

                                    (new DownloadTask(url, model.getOrganizationID())).execute();

                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    ArrayList<Model> check = db.getFavourite();
                    for (int i = 0; i < check.size(); i++) {
                        Model model = check.get(i);
                        boolean exist = db.OrganizationExist(model.getOrganizationID());
                        if (!exist) {
                            db.deleteOrganizationFav(model);
                            db.deleteOrganizationFavPhoto(model);
                        }
                    }
                }
                //  textView.setText("");

                // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                //              progressDialog.cancel();// hide loading dialog
//                getSupportActionBar().show(); // show action bar
//                getSupportActionBar().setTitle("القائمه الرئيسيه"); // set title in action bar
//                swipeRefreshLayout.setVisibility(View.VISIBLE);
//                listAdapter = new ListAdapter(MainActivity.this, R.layout.list_row, list);//
//                listView.setAdapter(listAdapter);
                swipeRefreshLayout.setRefreshing(false);//hide refresh
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                int count = db.getOrganizationCount();
                if (count != 0) {
                    listAdapter.clear();
                } // to solve problem of refresh data with disconnect


                //    swipeRefreshLayout.setVisibility(View.INVISIBLE);

                if (db.getOrganizationCount() == 0) {
                    swipeRefreshLayout.setVisibility(View.INVISIBLE);
                    connection.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                getDataFromDatabase();
                swipeRefreshLayout.setRefreshing(false);

                Toast.makeText(MainActivity.this, "تأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();

                connection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        connection.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                        Reload();
                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        connection.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                        Reload();
                    }
                });
            }

        });
    }

    public void getDataFromDatabase() {

        db = new Database(MainActivity.this);
        arrayAdapter = db.getOrganization();

        if (arrayAdapter.size() != 0) {

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

                progressDialog.cancel();
                getSupportActionBar().show();
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                listAdapter = new ListAdapter(MainActivity.this, R.layout.list_row, list);
                listView.setAdapter(listAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        mMenu = menu;

        int arrayFav = db.getOrganizationCountFav();
        if (!(arrayFav == 0)) {
            menu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_on);

        } else {
            menu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_off);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up buttonall, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_favorite) {
            int favCount = db.getOrganizationCountFav();
            if (favCount != 0) {
                swipeRefreshLayout.setRefreshing(false);
                Intent i = new Intent(MainActivity.this, FavourityActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "لا توجد بيانات في قائمه المفضله", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

        WebServiceDataBackEndLess();

        ArrayList<Model> check = db.getFavourite();
        for (int i = 0; i < check.size(); i++) {
            Model model = check.get(i);
            boolean exist = db.OrganizationExist(model.getOrganizationID());
            if (!exist) {
                db.deleteOrganizationFav(model);
            }
        }
    }

    public void getImage(URL url, int id) {
        try {

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            int responceCode = httpURLConnection.getResponseCode();
            if (responceCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                inputStream.close();
                Log.v("photoByte", String.valueOf(byteArray));
                db.AddOrganizationPhoto(id, byteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class DownloadTask extends AsyncTask<Void, Void, byte[]> {

        int mId;
        URL mUrl;

        Database db = new Database(MainActivity.this);

        public DownloadTask(URL url, int id) {

            this.mId = id;
            this.mUrl = url;
        }

        @Override
        protected byte[] doInBackground(Void... voids) {

            try {

                HttpURLConnection httpURLConnection = (HttpURLConnection) mUrl.openConnection();
                int responceCode = httpURLConnection.getResponseCode();
                if (responceCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    inputStream.close();
                    return byteArray;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);

            db.AddOrganizationPhoto(mId, bytes);

            getSupportActionBar().show(); // show action bar
            getSupportActionBar().setTitle("القائمه الرئيسيه"); // set title in action bar
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            listAdapter = new ListAdapter(MainActivity.this, R.layout.list_row, list);//
            listView.setAdapter(listAdapter);
            progressDialog.cancel();// hide loading dialog
            swipeRefreshLayout.setRefreshing(false);//hide refresh

//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 4000);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Toast.makeText(MainActivity.this, "تمت عمليه التبرع بنجاح ... شكرا لتبرعك", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "لم تتم عمليه التبرع", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();


    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);
            return true;
        }
        return false;
    }

}