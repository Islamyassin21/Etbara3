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
    private SwipeRefreshLayout swipeRefreshLayout;
    //  private ProgressDialog progressDialog;
    private ImageView connection;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

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


            WebServiceDataBackEndLess();


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                private AlertDialog alertDialog;
                Database db = new Database(MainActivity.this);

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Model model = listAdapter.getItem(position);
                    View dialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.cunfarm_dialog, null);
                    final TextView textView = (TextView) dialog.findViewById(R.id.dialog_text);
                    final Button done = (Button) dialog.findViewById(R.id.dialog_done);
                    final Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
                    final ImageView fav = (ImageView) dialog.findViewById(R.id.dialog_fav);

                    boolean exist = db.OrganizationExist(model.getOrganizationID());
                    Toast.makeText(MainActivity.this, model.getOrganizationID() + "", Toast.LENGTH_LONG).show();
                    if (exist)
                        fav.setImageResource(android.R.drawable.btn_star_big_on);


                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(dialog);

                    textView.setText("انت على وشك التبرع بقيمه (" + model.getOrganizationMouny() + ") جنيه لصالح (" + model.getOrganizationName() + ") للإستمرار اضغط موافق و سيقوم البرنامج مباشرة بتحويلك الى شاشه الرسائل لإتمام عمليه التبرع ... او للإلغاء اضغط إلغاء");

                    builder.setCancelable(false);
                    alertDialog = builder.show();

                    final Database db = new Database(MainActivity.this);


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


                    fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            boolean exist = db.OrganizationExist(model.getOrganizationID());

                            if (exist) {

                                db.deleteOrganization(model);
                                fav.setImageResource(android.R.drawable.btn_star_big_off);
                                Toast.makeText(MainActivity.this, "تم الازاله من المفضله", Toast.LENGTH_LONG).show();

                            } else {
                                db.AddOrganization(model);
                                fav.setImageResource(android.R.drawable.btn_star_big_on);
                                Toast.makeText(MainActivity.this, "تم الاضافه الى المفضله", Toast.LENGTH_LONG).show();

                            }

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

                //  textView.setText("");
                
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                getSupportActionBar().show();
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                listAdapter = new ListAdapter(MainActivity.this, R.layout.list_row, list);
                listView.setAdapter(listAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                // Toast.makeText(MainActivity.this, "حدث خطأ اثناء الاتصال .. حاول مره اخرى", Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                connection.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
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

        WebServiceDataBackEndLess();
    }
}
