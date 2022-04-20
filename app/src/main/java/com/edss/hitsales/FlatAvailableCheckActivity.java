package com.edss.hitsales;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edss.hitsales.Model.FlatData;
import com.edss.hitsales.Model.FlatStatus;
import com.edss.hitsales.Model.Project;
import com.edss.hitsales.Utils.Api;
import com.edss.hitsales.Utils.ConnectionDetector;
import com.edss.hitsales.Utils.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlatAvailableCheckActivity extends AppCompatActivity {

    EditText etProject;
    TextView tvSoldCount,tvUnSoldCount,tvOnHoldCount,tvNotToSellCount,tvTotalCount;
    private RecyclerView recyclerView;
    private List<FlatData> FlatDataList ;//= new ArrayList<>();
    public List<Project> ProjectList = new ArrayList<>();
    ArrayList<String> ProjectL = new ArrayList<>();
    int maxRetryCount = 4, retryCountObject = 0, viewCount =5;
    RecyclerViewAdapter adapter;
    GridLayoutManager layoutManager;
    String CompanyID ,UserID;
    ConnectionDetector cd;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private static final String STATE_ITEMS = "items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_available_check);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

       /* if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            FlatDataList = savedInstanceState.getParcelableArrayList("key");
            adapter = new RecyclerViewAdapter(FlatDataList, FlatAvailableCheckActivity.this);
            recyclerView.setAdapter(adapter);
        }*/

        cd = new ConnectionDetector(this);

        initView();

        etProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ProjectL.size() >0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FlatAvailableCheckActivity.this);
                    builder.setTitle("Select Project");
                    final String[] proj = ProjectL.toArray(new String[ProjectL.size()]);
                    builder.setItems(proj, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            etProject.getText().clear();
                            String selectedProjId = ProjectList.get(i).getID();
                            String selectedProjName = ProjectList.get(i).getDescription();
                            etProject.setText(selectedProjId);
                            String projectId = DbNameConvert(selectedProjId);
                            Log.e("***", "Project str = " + selectedProjId + " sel Prj  = " + projectId);
                            if (selectedProjId.length() > 0)
                                getFlatAvailabilityList(projectId);
                        }
                    });
                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(FlatAvailableCheckActivity.this,"Projects not found. ",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initView() {

// assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);


        // Display application icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.dots);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Flat Availability");


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        UserID = loginPreferences.getString("ID", "");
        CompanyID = loginPreferences.getString("CompanyID", "");
        Log.e("***", " UserID - " + UserID);
        etProject = (EditText) findViewById(R.id.et_Project);
        recyclerView = findViewById(R.id.rvFlat);
        tvTotalCount=(TextView)findViewById(R.id.tvTotalCount);
        tvSoldCount=(TextView)findViewById(R.id.tvSoldCount);
        tvOnHoldCount=(TextView)findViewById(R.id.tvOnHoldCount);
        tvNotToSellCount=(TextView)findViewById(R.id.tvNotToSellCount);
        tvUnSoldCount=(TextView)findViewById(R.id.tvUnSoldCount);

        if(cd.isConnectingToInternet()){
            getProjectList();
        }else{
            //Offline
            Constant.showDialog(FlatAvailableCheckActivity.this,"Please check internet connection.");
        }
    }

    public String DbNameConvert(String dbname){
        String smallDb = dbname.toLowerCase();
        dbname = smallDb.replaceAll("-","_");
        Log.e("***"," DBName ="+dbname);
        return dbname;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Setting:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FlatAvailableCheckActivity.this);
//                alertDialog.setTitle("PASSWORD");
                alertDialog.setMessage("Enter Flat View Count");

                final EditText input = new EditText(FlatAvailableCheckActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setText(viewCount+"");
                alertDialog.setView(input); // uncomment this line

                alertDialog.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    viewCount = Integer.parseInt(input.getText().toString().trim());
                                    if (viewCount <= 15) {
                                        if (FlatDataList.isEmpty() || FlatDataList.size() > 0)
                                            adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(FlatAvailableCheckActivity.this, "Count should not be more than 15", Toast.LENGTH_SHORT).show();
                                        viewCount = 5;
                                    }
                                }catch (NullPointerException e) {
                                    e.printStackTrace();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                alertDialog.show();

                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if( FlatDataList == null || FlatDataList.isEmpty()){

            }else {
                Log.e("***", FlatDataList.size() + " onpause = view count =" + viewCount);
                SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("viewCount", viewCount);
                editor.apply();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            viewCount = settings.getInt("viewCount", 5);
            if( FlatDataList == null || FlatDataList.isEmpty()){

            }else {
                Log.e("***", FlatDataList.size() + " onresume = view count =" + viewCount);
                adapter = new RecyclerViewAdapter(FlatDataList, FlatAvailableCheckActivity.this);
                recyclerView.setAdapter(adapter);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
     try{
        outState.putSerializable("d", (Serializable) FlatDataList);
        super.onSaveInstanceState(outState);
    }catch (NullPointerException e){
        e.printStackTrace();
    }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FlatDataList = (List<FlatData>) savedInstanceState.getSerializable("d");
            adapter = new RecyclerViewAdapter(FlatDataList,FlatAvailableCheckActivity.this);
//            ListView listView = (ListView) findViewById(R.id.layout_listview);
            recyclerView.setAdapter(adapter);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) FlatDataList);
        super.onSaveInstanceState(outState);
        Log.e("***",FlatDataList.size()+" onresume = view count ="+viewCount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        FlatDataList = savedInstanceState.getParcelableArrayList("key");
        adapter = new RecyclerViewAdapter(FlatDataList, FlatAvailableCheckActivity.this);
        recyclerView.setAdapter(adapter);
    }
    */

    @Override
    public void onBackPressed() {
       finish();
    }

    //Get ProjectList data from server
    private void getProjectList() {
        int IsAdd = 1;
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(Constant.ConnectTimeOut, TimeUnit.MINUTES)
                    .readTimeout(Constant.readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(Constant.writeTimeout, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            Call<List<Project>> call = api.getProjectList(UserID,CompanyID);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(FlatAvailableCheckActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setCancelable(false);
            progressDoalog.setCanceledOnTouchOutside(false);
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            final int finalIsAdd = IsAdd;
            call.enqueue(new Callback<List<Project>>() {
                @Override
                public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                    Log.e("***", "response getProjectList - " + response.body().size());
                    try {
                        if (response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get ProjectList data");
                            Toast.makeText(FlatAvailableCheckActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
//                            FlatDataList.clear();
//                            adapter.notifyDataSetChanged();
                            ProjectList.clear();
                            ProjectL.clear();
                            ProjectList = response.body();

                            for (int i = 0; i < ProjectList.size(); i++) {
                                ProjectL.add(ProjectList.get(i).getID());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Project>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCountObject + "---get ProjectList Exception----" + t);

                    if (++retryCountObject < maxRetryCount)
                        getProjectList();
                    else
                        Toast.makeText(FlatAvailableCheckActivity.this, "Fail to load ProjectList data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Constant.showDialog(FlatAvailableCheckActivity.this,"Something went wrong.");
        } catch (Exception e) {
            e.printStackTrace();
            Constant.showDialog(FlatAvailableCheckActivity.this,"Something went wrong.");
        }
    }

    private void getFlatAvailabilityList(final String DBName) {
        int IsAdd = 1;
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            Call<List<FlatData>> call = api.getFlatAvailabilityList(DBName, CompanyID);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(FlatAvailableCheckActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setCancelable(false);
            progressDoalog.setCanceledOnTouchOutside(false);
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            final int finalIsAdd = IsAdd;
            call.enqueue(new Callback<List<FlatData>>() {
                @Override
                public void onResponse(Call<List<FlatData>> call, Response<List<FlatData>> response) {
                    Log.e("***", "response FlatAvailable List - " + response.body().size());
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get FlatAvailable data");
                            Toast.makeText(FlatAvailableCheckActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("***", "response FlatAvailable List - " + response.body().size());
//                                FlatDataList.clear();
                            FlatDataList = response.body();
                            Log.e("***", "viewCount = "+viewCount+"  =  FlatDataList = "+FlatDataList.size());
                            // setting grid layout manager to implement grid view.
                            // in this method '2' represents number of columns to be displayed in grid view.
                            layoutManager = new GridLayoutManager(FlatAvailableCheckActivity.this, viewCount);
                            // at last set adapter to recycler view.
                            recyclerView.setLayoutManager(layoutManager);
                            // added data from arraylist to adapter class.
                            adapter = new RecyclerViewAdapter(FlatDataList, FlatAvailableCheckActivity.this);
                            recyclerView.setAdapter(adapter);

                            getFlatAvailabilityCount(DBName);
                        }
                        Log.e("***", "size FlatDataList - " + FlatDataList.size());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(FlatAvailableCheckActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<FlatData>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCountObject + "---getFlatDataList Exception----" + t);

                    if (++retryCountObject < maxRetryCount)
                        getFlatAvailabilityList(DBName);
                    else
                        Toast.makeText(FlatAvailableCheckActivity.this, "No Flat data found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Constant.showDialog(FlatAvailableCheckActivity.this,"Something went wrong.");
        } catch (Exception e) {
            e.printStackTrace();
            Constant.showDialog(FlatAvailableCheckActivity.this,"Something went wrong.");
        }
    }

    private void getFlatAvailabilityCount(final String DBName) {
        int IsAdd = 1;
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            Call<List<FlatStatus>> call = api.getFlatAvailabilityCount(DBName, CompanyID);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(FlatAvailableCheckActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setCancelable(false);
            progressDoalog.setCanceledOnTouchOutside(false);
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            final int finalIsAdd = IsAdd;
            call.enqueue(new Callback<List<FlatStatus>>() {
                @Override
                public void onResponse(Call<List<FlatStatus>> call, Response<List<FlatStatus>> response) {
                    Log.e("***", "response FlatAvailable status - " + response.body());
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get Flatstatus  data");
                            Toast.makeText(FlatAvailableCheckActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("***", "response Flatstatus List - " + response.body());
//                                FlatDataList.clear();
                            List<FlatStatus> FlatData = response.body();
                            Log.e("***", "FlatStatusData = "+FlatData.get(0).getSold());
                            tvTotalCount.setText(" "+FlatData.get(0).getTotal()+" ");
                            tvSoldCount.setText(" "+FlatData.get(0).getSold()+" ");
                            tvOnHoldCount.setText(" "+FlatData.get(0).getOnHold()+" ");
                            tvNotToSellCount.setText(" "+FlatData.get(0).getNotToSell()+" ");
                            int UnSoldCount =FlatData.get(0).getTotal()-(FlatData.get(0).getSold()+FlatData.get(0).getOnHold()+FlatData.get(0).getNotToSell());
                            tvUnSoldCount.setText(" "+UnSoldCount+" ");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(FlatAvailableCheckActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<FlatStatus>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCountObject + "---getFlatstatus  Exception----" + t);

                    if (++retryCountObject < maxRetryCount)
                        getFlatAvailabilityCount(DBName);
                    else
                        Toast.makeText(FlatAvailableCheckActivity.this, "No Flat status data found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Constant.showDialog(FlatAvailableCheckActivity.this,"Something went wrong.");
        } catch (Exception e) {
            e.printStackTrace();
            Constant.showDialog(FlatAvailableCheckActivity.this,"Something went wrong.");
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

        private List<FlatData> flatList;
        private Context mcontext;

        public RecyclerViewAdapter(List<FlatData> FlatDataList, Context mcontext) {
            this.flatList = FlatDataList;
            this.mcontext = mcontext;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate Layout
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            // Set the data to textview and imageview.
            String Flatname = flatList.get(position).getID();
            holder.tvFlatname.setText(Flatname);
            if(flatList.get(position).getSold() == 1)
                holder.tvFlatname.setBackgroundColor(getResources().getColor(R.color.Sold));
            else if(flatList.get(position).getOnHold() == 1)
                holder.tvFlatname.setBackgroundColor(getResources().getColor(R.color.OnHold));
            else if(flatList.get(position).getNotToSell() == 1)
                holder.tvFlatname.setBackgroundColor(getResources().getColor(R.color.NotToSell));
            else
                holder.tvFlatname.setBackgroundColor(getResources().getColor(R.color.UnSold));
        }

        @Override
        public int getItemCount() {
            // this method returns the size of recyclerview
            return flatList.size();
        }

        // View Holder Class to handle Recycler View.
        public class RecyclerViewHolder extends RecyclerView.ViewHolder {

            private TextView tvFlatname;

            public RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                tvFlatname = itemView.findViewById(R.id.tvFlat);
            }
        }
    }

}
