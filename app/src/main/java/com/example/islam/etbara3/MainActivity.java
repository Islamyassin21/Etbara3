package com.example.islam.etbara3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.islam.etbara3.Adapter.ListAdapter;
import com.example.islam.etbara3.Database.Database;
import com.example.islam.etbara3.Model.Model;

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
    //  private ProgressDialog progressDialog;
    private ImageView connection;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private Database db = new Database(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
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


            if (db.getOrganizationCount() <= 0) {
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
                    final Button done = (Button) dialog.findViewById(R.id.dialog_done);
                    final Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel);


                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(dialog);

                    textView.setText("انت على وشك التبرع بقيمه (" + model.getOrganizationMouny() + ") جنيه لصالح (" + model.getOrganizationName() + ") للإستمرار اضغط موافق و سيقوم البرنامج مباشرة بتحويلك الى شاشه الرسائل لإتمام عمليه التبرع ... او للإلغاء اضغط إلغاء");

                    builder.setCancelable(false);
                    alertDialog = builder.show();

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
                        }
                    });

                }
            });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    Model model = list.get(position);
                    Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                    i.putExtra("organizationName", model.getOrganizationName());
                    i.putExtra("organizationInfo", model.getOrganizationInfo());
                    i.putExtra("organizationAcountNo", model.getOrganizationAccountNo());
                    i.putExtra("organizationPhone", model.getOrganizationPhone());
                    i.putExtra("organizationSMSContent", model.getOrganizationSMSContent());
                    i.putExtra("organizationSMS", model.getOrganizationSMS());
                    i.putExtra("organizationYoutubeLink", model.getOrganizationYoutubeLink());
                    i.putExtra("organizationYoutubeName", model.getOrganizationYoutubeName());
                    i.putExtra("organizationService", model.getOrganozationService());
                    i.putExtra("organizationMouny", model.getOrganizationMouny());
                    startActivity(i);

                    Toast.makeText(getApplicationContext(), "hello " + position, Toast.LENGTH_LONG).show();

                    return true;
                }
            });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "حدث خطأ اثناء الاتصال .. حاول مره اخرى", Toast.LENGTH_LONG).show();
        }

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.list_layout_controller);
        listView.setLayoutAnimation(controller);
    }


    private void WebServiceDataBackEndLess() {

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setRelated(Arrays.asList("organizationName"));

        BackendlessDataQuery query = new BackendlessDataQuery(queryOptions);
        query.setPageSize(100);
        Backendless.Persistence.of(Model.class).find(query, new AsyncCallback<BackendlessCollection<Model>>() {
            @Override
            public void handleResponse(BackendlessCollection<Model> response) {
                //  BackendlessCollection<Model> collection = response;
                list.addAll(response.getCurrentPage());

                if (db.getOrganizationCount() == list.size()) {
                    Toast.makeText(getApplicationContext(), "لا توجد بيانات جديده في الوقت الحالي", Toast.LENGTH_LONG).show();

                } else if (db.getOrganizationCount() < list.size()) {
                    int count = list.size() - db.getOrganizationCount();
                    Toast.makeText(getApplicationContext(), "تم اضافه( " + count + ") مؤسسه جديده", Toast.LENGTH_LONG).show();
                    for (int i = 0; i < list.size(); i++) {
                        Model model = list.get(i);
                        if (!db.OrganizationExist(model.getOrganizationID())) {
                            db.AddOrganization(model);
                        }
                    }
                } else {
                    int count = -(list.size() - db.getOrganizationCount());
                    Toast.makeText(getApplicationContext(), "تم ازاله( " + count + ") مؤسسه", Toast.LENGTH_LONG).show();
                    db.deleteOrganization();
                    for (int i = 0; i < list.size(); i++) {
                        Model model = list.get(i);
                        if (!db.OrganizationExist(model.getOrganizationID())) {
                            db.AddOrganization(model);
                        }
                    }

                    ArrayList<Model> check = db.getFavourite();
                    for (int i = 0; i < check.size(); i++) {
                        Model model = check.get(i);
                        boolean exist = db.OrganizationExist(model.getOrganizationID());
                        if (!exist) {
                            db.deleteOrganizationFav(model);
                        }
                    }


                }
                //  textView.setText("");

                // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                getSupportActionBar().show();
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                listAdapter = new ListAdapter(MainActivity.this, R.layout.list_row, list);
                listView.setAdapter(listAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MainActivity.this, "حدث خطأ اثناء الاتصال .. تأكد من اتصالك بالانترنت", Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                //    swipeRefreshLayout.setVisibility(View.INVISIBLE);

                if (db.getOrganizationCount() == 0) {
                    connection.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                getDataFromDatabase();
                swipeRefreshLayout.setRefreshing(false);


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

        ArrayList<Model> arrayFav = db.getFavourite();
        Toast.makeText(MainActivity.this, arrayFav.size() + "", Toast.LENGTH_LONG).show();
        if (!(arrayFav.size() == 0)) {
            menu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_on);

        } else {
            menu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_off);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_favorite) {
            Intent i = new Intent(MainActivity.this, FavourityActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {


        listView.setVisibility(View.INVISIBLE);
//        //Save where you last were in the list.
//        int index = listView.getFirstVisiblePosition();
//        View v = listView.getChildAt(0);
//        // Call to notify, or updated(change, remove, move, add)
//
//        listAdapter.notifyDataSetChanged();
//        int top = (v == null) ? 0 : v.getTop();
//
//        //Prevents the scroll to the new item at the new position
//        listView.setSelectionFromTop(index, top);

        list.clear();
        WebServiceDataBackEndLess();
        listView.setVisibility(View.VISIBLE);
        //   Model model = new Model();
        ArrayList<Model> check = db.getFavourite();
        for (int i = 0; i < check.size(); i++) {
            Model model = check.get(i);
            boolean exist = db.OrganizationExist(model.getOrganizationID());
            if (!exist) {
                db.deleteOrganizationFav(model);
            }
        }
    }
}