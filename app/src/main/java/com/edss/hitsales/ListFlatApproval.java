package com.edss.hitsales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edss.hitsales.Model.ToBeApproveData;
import com.edss.hitsales.Utils.Api;
import com.edss.hitsales.Utils.ConnectionDetector;
import com.edss.hitsales.Utils.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.edss.hitsales.Utils.Constant.maxRetryCount;

public class ListFlatApproval extends AppCompatActivity {

    ConnectionDetector cd;
    int retryCount = 0, Flat_completed = 0;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    String DBName = "", CompanyID, UserRole, UserId;
    ListView lvListFlatApproval;
    TextView txt;
    public AdapterToBeApprove adpt;
    public List<ToBeApproveData> ToBeApprovelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_flat_approval);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cd = new ConnectionDetector(this);

        initViews();

        lvListFlatApproval.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToBeApproveData data = ToBeApprovelist.get(position);
                Log.e("***","RowId = "+data.getRowID());
                Intent in = new Intent(ListFlatApproval.this, MainActivity.class);
                in.putExtra("Action","View");
                in.putExtra("SelectedFlatData", data);
                startActivity(in);
            }
        });
    }

    private void initViews() {

        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        // Display application icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.hitmobile);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Flats To Be Approve");


        loginPreferences = getSharedPreferences(Constant.PREF, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        UserId = loginPreferences.getString("ID", "");
        CompanyID = loginPreferences.getString("CompanyID", "01");
        UserRole = loginPreferences.getString("UserRole", "");

        txt = (TextView) findViewById(R.id.txt);

        lvListFlatApproval = (ListView) findViewById(R.id.lv_flatApproval);

//        ToBeApproveData pd = new ToBeApproveData();
//        pd.setID("1");  Phaselist.add(pd);
//        pd.setID("2");  Phaselist.add(pd);
//        pd.setID("3");  Phaselist.add(pd);
//        pd.setID("4");  Phaselist.add(pd);
//        pd.setID("5");  Phaselist.add(pd);
//        adpt = new ListFlatApproval.AdapterToBeApprove(ListFlatApproval.this, ToBeApprovelist);
//        lvListFlatApproval.setAdapter(adpt);

//        if (cd.isConnectingToInternet())
//            getToBeApprovelist();
//        else
//            Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cd.isConnectingToInternet()) {
            lvListFlatApproval.setAdapter(null);
            getToBeApprovelist();
        }else
            Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addcust, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.FlatAvailability:
                Intent in = new Intent(ListFlatApproval.this, FlatAvailableCheckActivity.class);
                startActivity(in);
                return true;

            case R.id.Logout:
                AlertLogout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void AlertLogout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle("Alert");
        builder.setIcon(R.drawable.ic_warning);
        builder.setMessage("Are you sure you want to Logout?");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //cleaSharedPref()
                loginPrefsEditor.putString("ID", "");
                loginPrefsEditor.putString("FirstName", "");
                loginPrefsEditor.putString("LastName", "");
                loginPrefsEditor.putString("UserRole", "");
                loginPrefsEditor.commit();
                finish();
            }
        });
        builder.show();
    }

    private void getToBeApprovelist() {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ListFlatApproval.this);
        progressDoalog.setMessage("Please wait....");
        progressDoalog.setCancelable(false);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        try {
            OkHttpClient okHttpClient;
            okHttpClient = new OkHttpClient.Builder()
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
            Log.e("***","FlatsToBeApprove ="+CompanyID+" = "+ UserId);
                    Call<List<ToBeApproveData>> call = api.getFlatsToBeApprove(Constant.CompanyId, Constant.UserId);
            call.enqueue(new Callback<List<ToBeApproveData>>() {
                @Override
                public void onResponse(Call<List<ToBeApproveData>> call, Response<List<ToBeApproveData>> response) {
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[No")) {
                            Log.e("***", "Unable to get ToBeApprove list data");
                            Toast.makeText(ListFlatApproval.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            //Constant.SelectedLocationId = SelectedLocId;
                            ToBeApprovelist.clear();
                            ToBeApprovelist = response.body();

                            Log.e("***", response.body()+"=="+ToBeApprovelist.get(0)+" === size ToBeApprove list - " + ToBeApprovelist.size());
                                adpt = new AdapterToBeApprove(ListFlatApproval.this, ToBeApprovelist);
                                lvListFlatApproval.setAdapter(adpt);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<ToBeApproveData>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---ToBeApprove list Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        getToBeApprovelist();
                    else
                        Toast.makeText(ListFlatApproval.this, "To Be Approve list not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(ListFlatApproval.this,"Something went wrong.");
        } catch (Exception e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(ListFlatApproval.this,"Something went wrong.");
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                finish();
                    }
                });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.show();
    }

    public class AdapterToBeApprove extends ArrayAdapter<ToBeApproveData> {
        Context ctx;
        private List<ToBeApproveData> ToBeApprovelist;

        public AdapterToBeApprove(@NonNull Context context, List<ToBeApproveData> ToBeApprovelist) {
            super(context, 0, ToBeApprovelist);
            this.ctx = context;
            this.ToBeApprovelist = ToBeApprovelist;
        }

        public class ViewHolder {
            TextView tvFlatId, tvProjectId, tvWingId, tvFloorId,tvStatus;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ListFlatApproval.AdapterToBeApprove.ViewHolder holder = new ListFlatApproval.AdapterToBeApprove.ViewHolder();
            View v = convertView;

            if (v == null)
                v = LayoutInflater.from(ctx).inflate(R.layout.item_flatapproval, parent, false);

            ToBeApproveData data = ToBeApprovelist.get(position);

            holder.tvFlatId = (TextView) v.findViewById(R.id.tvFlatId);
            holder.tvProjectId = (TextView) v.findViewById(R.id.tvProjectId);
            holder.tvWingId = (TextView) v.findViewById(R.id.tvWingId);
            holder.tvFloorId = (TextView) v.findViewById(R.id.tvFloorId);
            holder.tvStatus = (TextView)v.findViewById(R.id.tvStatus);

            String ObjID,ProjectId, WingId, FloorId, FlatId,FlatType,CategorySelected;
            ObjID = data.getObjectID();  //eg : OFF\MPCHFL\quotation.hitsalesitem/F-101/A/MPCHFL
            String ObjIDProID[] = ObjID.split("\\\\"); //split to get project id
            ProjectId = ObjIDProID[1];
            String ObjIDArr[] = ObjID.split("/");
//            Log.e("***",data.getStatus()+"==>"+ObjIDArr[1]);
            FlatId = ObjIDArr[1];
//            WingId = data.getWingID();
//            FloorId = data.getFloorID();
//            FlatType = data.getFlatType();
//            CategorySelected = data.getCategorySelected();

            holder.tvProjectId.setText(ProjectId);
//            holder.tvWingId.setText(WingId);
//            holder.tvFloorId.setText(FloorId);
            holder.tvFlatId.setText(FlatId);
            if(data.getStatus() == 0)
                holder.tvStatus.setText("To be Approve");
            else if(data.getStatus() == 1)
                holder.tvStatus.setText("Approved");
            else if(data.getStatus() == 2)
                holder.tvStatus.setText("OnHold");
            else if(data.getStatus() == 3)
                holder.tvStatus.setText("Rejected");
            else if(data.getStatus() == 4)
                holder.tvStatus.setText("ByPass");
            else if(data.getStatus() == 5)
                holder.tvStatus.setText("Revoke");

            return v;
        }
    }
}
