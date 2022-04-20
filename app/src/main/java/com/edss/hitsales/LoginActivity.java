package com.edss.hitsales;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edss.hitsales.Model.GetUser;
import com.edss.hitsales.Model.Login;
import com.edss.hitsales.Utils.Api;
import com.edss.hitsales.Utils.ConnectionDetector;
import com.edss.hitsales.Utils.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername , etPassword;
    Button btn_Login;
    public static ConnectionDetector cd;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    String ID = "", FirstName = "", LastName = "",CompanyID = "",UserRole="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getAppInfo();

        etUsername = (EditText)findViewById(R.id.et_username);
        etPassword = (EditText)findViewById(R.id.et_password);
        btn_Login = (Button)findViewById(R.id.btn_login);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();


                    String userid = etUsername.getText().toString().trim();
                    String UserID = userid.toUpperCase();
                    String Password = etPassword.getText().toString().trim();

                    if (UserID.length() == 0 || UserID.isEmpty()){
                        etUsername.setError("Enter vaild username");
                    }
//                    else if(Password.length() == 0 || Password.isEmpty()) {
//                        etPassword.setError("Enter vaild password");
//                    }
                    else{
                        Log.e("***"," UserID = "+ UserID + " Password = "+ Password);

                        Login logindata = new Login();
                        logindata.setUsername(UserID);
                        logindata.setPassword(Password);
                        logindata.setAppName("HitSales");
                        cd = new ConnectionDetector(getApplicationContext());
                        if (cd.isConnectingToInternet()) {
                            getLogin(logindata);
                        }else{
                            showNoIternetDilog(LoginActivity.this);
                        }
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void showNoIternetDilog(Activity context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle("No Internet");
        alertDialogBuilder.setMessage("Make sure that you are connected to internet.")
                .setIcon(R.drawable.ic_warning)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showLoginFailed(Activity context) {
        try {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
            alertDialogBuilder.setTitle("Login Failed");
            alertDialogBuilder.setMessage("Wrong User Id or Password.")
                    .setIcon(R.drawable.ic_warning)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            System.out.println("ex" + e);
        }

    }
    private String getAppInfo() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getAppInfo()", e.getMessage());
        }

        String version = pInfo.versionName;

        TextView versionText = (TextView) findViewById(R.id.txtViewVersioCode);
        versionText.setText("Version" + " - " + version);

        return version;
    }

    private void getLogin(Login logindata) {
        try{
            OkHttpClient client = new OkHttpClient();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Api api = retrofit.create(Api.class);

            Call<GetUser> call = api.UserLogin(logindata);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(LoginActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setCancelable(false);
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            call.enqueue(new Callback<GetUser>() {
                @Override
                public void onResponse(Call<GetUser> call, retrofit2.Response<GetUser> response) {

                    progressDoalog.dismiss();
                    try {
                        Log.e("***", "Login resp = " + response.body());

                        if (response.body().toString().contains("{}") || response.body() == null) {

                            showLoginFailed(LoginActivity.this);
                            Log.e("***", "Login resp fail");

                        } else {

                            Log.e("***", "Login resp successfully");
                            Toast.makeText(LoginActivity.this, "Login successfully..", Toast.LENGTH_SHORT).show();

                            GetUser UserList = new GetUser();
                            UserList = response.body();
                            ID = UserList.getID();
                            FirstName = UserList.getFirstName();
                            LastName = UserList.getLastName();
                            CompanyID  = UserList.getDefaultCompany();
                            Constant.CompanyId = UserList.getDefaultCompany();
                            UserRole = UserList.getDesignation();

                            String mode = "Online";
                            loginPrefsEditor.putString("SwitchMode", mode);
                            loginPrefsEditor.putString("ID", ID);
                            loginPrefsEditor.putString("FirstName", FirstName);
                            loginPrefsEditor.putString("LastName", LastName);
                            loginPrefsEditor.putString("CompanyID", CompanyID);
                            loginPrefsEditor.putString("UserRole", UserRole);
                            loginPrefsEditor.commit();
                            Constant.CompanyId = CompanyID;
                            Constant.UserId = ID;

                            Log.e("***", "Login UserRole = " + UserRole + "Login ID = " + ID + "FirstName = " + FirstName + "LastName = " + LastName + " CompanyID="+CompanyID);

                            if(UserRole.equalsIgnoreCase("Checker")){
                                Intent intent = new Intent(LoginActivity.this, ListFlatApproval.class);
                                intent.putExtra("Action","View");
                                startActivity(intent);
                                finish();
                            }else {//UserRole = Doer
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("Action","Add");
                                startActivity(intent);
                                finish();
                            }
                        }

                    }catch (Exception e){
                        progressDoalog.dismiss();
                        Constant.showDialog(LoginActivity.this,"Something went wrong.");
                        Log.e("***", "Login resp fail");
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<GetUser> call, Throwable t) {

                    Log.e("***","---Login fail----"+t.toString());
                    progressDoalog.dismiss();
                    Constant.showDialog(LoginActivity.this,"Something went wrong.");
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
