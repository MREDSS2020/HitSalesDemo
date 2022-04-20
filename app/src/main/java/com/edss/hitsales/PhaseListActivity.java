package com.edss.hitsales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edss.hitsales.Model.ProPhaseData;
import com.edss.hitsales.Utils.Api;
import com.edss.hitsales.Utils.ConnectionDetector;
import com.edss.hitsales.Utils.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.RowId;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;

import static com.edss.hitsales.Utils.Constant.maxRetryCount;
import static com.edss.hitsales.Utils.Constant.yearFormat;

public class PhaseListActivity extends AppCompatActivity {

    public List<ProPhaseData> Phaselist = new ArrayList<>();
    ListView lvPhaselist;
    LinearLayout llBaseBtn;
    Button btnSave, btn_Approve, btn_Reject, btn_OnHold, btn_Revoke;
    public AdapterPlaseList adpt;
    String DBName = "", CompanyID, Role, UserId, SelectedFlatId, DiscountReason, ObjectID,
            DateToday, ProjectID, WingID,SelectedCatId,Status="",Action,Creator,StatusComment="";
    ConnectionDetector cd;
    int retryCount = 0, Flat_completed = 0,RowID;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    Double Amount = 0.0, Discount = 0.0, FinalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase_list);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cd = new ConnectionDetector(this);

        initViews();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet())
                    SaveFlatQuotation();
                else
                    Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();

            }
        });

        btn_Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    Status = "1";
                    StatusComment="Approve";
                    UpdateApprovalStatus();
                }else
                    Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();

            }
        });

        btn_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    Status = "3";
                    StatusComment="Reject";
                    UpdateApprovalStatus();
                }else
                    Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();

            }
        });

        btn_OnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    Status = "2";
                    StatusComment="OnHold";
                    UpdateApprovalStatus();
                }else
                    Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();

            }
        });

        btn_Revoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    Status = "5";
                    StatusComment="Revoke";
                    UpdateApprovalStatus();
                }else
                    Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();

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
        getSupportActionBar().setTitle("Phase list");


        loginPreferences = getSharedPreferences(Constant.PREF, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        UserId = loginPreferences.getString("ID", "");
        CompanyID = loginPreferences.getString("CompanyID", "01");
        Role = loginPreferences.getString("UserRole", "");
        Log.e("***", "Role=>" + Role);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            Action=b.getString("Action");
            SelectedFlatId = b.getString("SelectedFlatId");
            DBName = b.getString("DBName");
            Amount = b.getDouble("Amount");
            Discount = b.getDouble("Discount");
            DiscountReason = b.getString("DiscountReason");
            FinalAmount = b.getDouble("FinalAmount");
            ProjectID = b.getString("ProjectID");
            WingID = b.getString("WingID");
            SelectedCatId = b.getString("SelectedCatId");
            RowID = b.getInt("RowID");
            if(Action.equalsIgnoreCase("View"))
            ObjectID= b.getString("ObjectID");
            Creator = b.getString("Creator");
            Log.e("***", Creator+"@@@"+Amount+"="+Discount+"="+DiscountReason+"="+FinalAmount);
        }

        btnSave = (Button) findViewById(R.id.btn_Save);
        btn_Approve = (Button) findViewById(R.id.btn_Approve);
        btn_Reject = (Button) findViewById(R.id.btn_Reject);
        btn_OnHold = (Button) findViewById(R.id.btn_OnHold);
        btn_Revoke = (Button) findViewById(R.id.btn_Revoke);
        lvPhaselist = (ListView) findViewById(R.id.lv_phaselist);
        llBaseBtn = (LinearLayout) findViewById(R.id.llBaseBtn);

        if(Action.equalsIgnoreCase("Add"))
        ObjectID = Constant.GetObjectID(ProjectID,SelectedFlatId,WingID);
                //"BLD\\" + ProjectID + "\\project.hitsalesitem/"+SelectedFlatId+"/" + WingID + "/" + ProjectID;//e.g :- OFF\TEST\quotation.workitem/F/TEST
        try {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            //String formattedDate = //df.format(c);
            DateToday = yearFormat.format(c);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("***",DateToday+" <= ObjectID = "+ObjectID);

        if (Role.equalsIgnoreCase("Checker")) {
            llBaseBtn.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
        } else {
            llBaseBtn.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        }

        if (cd.isConnectingToInternet())
            getPhaselist();
        else
            Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.finish();
    }

    private void getPhaselist() {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(PhaseListActivity.this);
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

            Call<List<ProPhaseData>> call = api.getProPhaseList(CompanyID, UserId, "", DBName);
            call.enqueue(new Callback<List<ProPhaseData>>() {
                @Override
                public void onResponse(Call<List<ProPhaseData>> call, Response<List<ProPhaseData>> response) {
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get Phaselist data");
                            Toast.makeText(PhaseListActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            //Constant.SelectedLocationId = SelectedLocId;
                            Phaselist.clear();
                            Phaselist = response.body();
                            Log.e("***", " === size Phaselist - " + Phaselist.size());
                            adpt = new AdapterPlaseList(PhaseListActivity.this, Phaselist);
                            lvPhaselist.setAdapter(adpt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<ProPhaseData>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---Phaselist Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        getPhaselist();
                    else
                        Toast.makeText(PhaseListActivity.this, "Phaselist data not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(PhaseListActivity.this,"Something went wrong.");
        } catch (Exception e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(PhaseListActivity.this,"Something went wrong.");
        }
    }

    private void SaveFlatQuotation() {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(PhaseListActivity.this);
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

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Api api = retrofit.create(Api.class);

             Log.e("***",UserId+" = "+CompanyID+" = "+SelectedFlatId+" = "+DBName+" = "+Amount+" = "+Discount+" = "+DiscountReason+" = "+FinalAmount+"");
            Call<String> call = api.SaveFlatQuotation(UserId, CompanyID, SelectedFlatId, DBName, Amount + "",
                     FinalAmount + "", ObjectID, DateToday,SelectedCatId,Role);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        progressDoalog.dismiss();
                        if (response.body().contains("already")){
                            Log.e("***","resp==>"+response.body());
                            Constant.colorToast(getApplicationContext(),"Flat already send for approval",R.color.colorAccent);
                            Toast.makeText(getApplicationContext(), "Flat already send for approval", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (response.body().toString().isEmpty() || response.body().toString().contains("Error")) {
                            Log.e("***", "1 Unable to save Flat Quotation data");
                            Toast.makeText(PhaseListActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else if (response.body().contains("Success")) {
                            Toast.makeText(getApplicationContext(), "Flat Quotation saved.", Toast.LENGTH_SHORT).show();
//                            Intent in = new Intent(PhaseListActivity.this,ListFlatApproval.class);
//                            startActivity(in);
                            finish();
                        } else
                            Log.e("***", "Unable to save Flat Quotation data");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---SaveFlatQuotation Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        SaveFlatQuotation();
                    else
                        Toast.makeText(PhaseListActivity.this, "Flat Quotation data not saved", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(PhaseListActivity.this,"Something went wrong.");
        } catch (Exception e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(PhaseListActivity.this,"Something went wrong.");
        }
    }

    private void UpdateApprovalStatus() {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(PhaseListActivity.this);
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

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Api api = retrofit.create(Api.class);

             Log.e("***",UserId+" = "+CompanyID+" = "+SelectedFlatId+" = "+DBName+" = "+Amount+" = "+Discount+" = "+
                     DiscountReason+" = "+FinalAmount+" = "+Status);//SelectedCatId+" = "+
            Call<String> call = api.UpdateApprovalStatus(UserId, CompanyID, SelectedFlatId, DBName, Amount + "",
                    Discount + "", DiscountReason, FinalAmount + "", ObjectID, DateToday,Status,Role, RowID,Creator,
                    StatusComment);//,SelectedCatId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        Log.e("***","==resp==>"+response.body());
                        progressDoalog.dismiss();
//                        if (response.body().toString().isEmpty() || response.body().toString().contains("Error")) {
//                            Log.e("***", "Unable to update Flat Quotation status");
//                            Toast.makeText(PhaseListActivity.this, "Unable to update ...", Toast.LENGTH_SHORT).show();
//                        } else
                            if (response.body().contains("Success")) {
                            Log.e("***","resp1==>"+response.body());
                            Toast.makeText(getApplicationContext(), "Flat status updated.", Toast.LENGTH_SHORT).show();
//                            Intent in = new Intent(PhaseListActivity.this,ListFlatApproval.class);
//                            startActivity(in);
                            finish();
                        }else {
                            Log.e("***", "Unable to update Flat Quotation status=>"+response.body());
                            Constant.showDialog(PhaseListActivity.this,"Unable to update Flat Quotation status");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "--- status updated Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        UpdateApprovalStatus();
                    else
                        Toast.makeText(PhaseListActivity.this, "Flat Quotation not updated", Toast.LENGTH_SHORT).show();                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(PhaseListActivity.this,"Something went wrong.");
        } catch (Exception e) {
            progressDoalog.dismiss();
            Constant.showDialog(PhaseListActivity.this,"Something went wrong.");
            e.printStackTrace();
        }
    }


    public class AdapterPlaseList extends ArrayAdapter<ProPhaseData> {
        Context ctx;
        private List<ProPhaseData> Phaselist;

        public AdapterPlaseList(@NonNull Context context, List<ProPhaseData> Phaselist) {
            super(context, 0, Phaselist);
            this.ctx = context;
            this.Phaselist = Phaselist;
        }

        public class ViewHolder {
            TextView tvBldPhase, tvPer, tvAmtExdTax, tvAmtIndTax, tvTaxAmt1, tvTaxCode1, tvTaxAmt2, tvTaxCode2;
            Button btnViewDetail;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            View v = convertView;

            if (v == null)
                v = LayoutInflater.from(ctx).inflate(R.layout.phaselist_item, parent, false);

            holder.tvBldPhase = (TextView) v.findViewById(R.id.tv_bldPhase);
            holder.tvPer = (TextView) v.findViewById(R.id.tv_per);
            holder.tvAmtExdTax = (TextView) v.findViewById(R.id.tv_AmtExdTax);
            holder.tvAmtIndTax = (TextView) v.findViewById(R.id.tv_AmtIndTax);
            holder.tvTaxAmt1 = (TextView) v.findViewById(R.id.tv_TaxAmt1);
            holder.tvTaxCode1 = (TextView) v.findViewById(R.id.tv_TaxCode1);
            holder.tvTaxAmt2 = (TextView) v.findViewById(R.id.tv_TaxAmt2);
            holder.tvTaxCode2 = (TextView) v.findViewById(R.id.tv_TaxCode2);

            ProPhaseData pd = Phaselist.get(position);

            double TaxAmt1 = 0.0, TaxAmt2 = 0.0, AmtIndTax, AmtExdTax , per, tax1, tax2, ApplicableVal ;
            per = pd.getPercentage();
            ApplicableVal = pd.getApplicableValue();
            //Log.e("***", per + "--1--->" + ApplicableVal);
            //tax1 = pd.getTaxCode1()
            try {
                if (per <= 0.0 && ApplicableVal > 0) {
                    Log.e("***", "2--->" + ApplicableVal);
                    AmtExdTax = FinalAmount * (ApplicableVal / 100);
                    Log.e("***", "3--->" + AmtExdTax);
                } else {
                    AmtExdTax = FinalAmount * (per / 100);
                    Log.e("***", "4--->" + AmtExdTax);
                }
                AmtIndTax = AmtExdTax + TaxAmt1 + TaxAmt2;

                DecimalFormat formatter = new DecimalFormat("#,###,###");

                String amt = formatter.format(AmtExdTax);
              //  Log.e("***", "==>" + amt);
                holder.tvBldPhase.setText(pd.getName());
                holder.tvPer.setText("" + pd.getPercentage());

                holder.tvAmtExdTax.setText("" + String.format("%.2f", AmtExdTax));
                holder.tvAmtIndTax.setText("" + String.format("%.2f", AmtIndTax));

                holder.tvTaxAmt1.setText("" + String.format("%.2f", TaxAmt1));
                holder.tvTaxCode1.setText("" + pd.getTaxCode());

                holder.tvTaxAmt2.setText("" + String.format("%.2f", TaxAmt2));
                holder.tvTaxCode2.setText("" + pd.getTaxCode1());

            } catch (Exception e) {
                e.printStackTrace();
            }
//            holder.btnViewDetail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("***",Phaselist.size()+" = position ="+position+ "   Selected_flat_status ="+Selected_flat_status);
//                    Intent in = new Intent(PhaseListActivity.this,EditCheckPointActivity.class);
//                    in.putExtra("position",position);
//                    in.putExtra("ActivityName",SelectedActivity);
//                    in.putExtra("SelectedFlatStatus",Selected_flat_status);
//                    in.putExtra("DBName",DBName);
//                    startActivity(in);
//                }
//            });

            return v;
        }
    }
}
