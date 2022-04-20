package com.edss.hitsales;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.edss.hitsales.Model.CategoryData;
import com.edss.hitsales.Model.DetailsData;
import com.edss.hitsales.Model.SalesItem;
import com.edss.hitsales.Model.Project;
import com.edss.hitsales.Model.ToBeApproveData;
import com.edss.hitsales.Utils.Api;
import com.edss.hitsales.Utils.ConnectionDetector;
import com.edss.hitsales.Utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.edss.hitsales.Utils.Constant.maxRetryCount;
import static com.edss.hitsales.Utils.Constant.AlertDialog;

public class MainActivity extends AppCompatActivity {


    Spinner sp_select_project, sp_Select_wing, sp_Select_floor, sp_Select_flat, sp_Select_Type, sp_Select_category;
    Button btn_Display, btn_Apply;//, btn_Submit, btn_Approve, btn_Reject;
    TextView tv_NoData, tvproject, tvwing, tvfloor, tvflat, tvType, tvcategory;
    EditText et_Amount, et_Discount, et_FinalAmt, et_DiscountReason;
    TableRow llFinalAmt, llApply, llDiscount, llDiscountReason;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ConnectionDetector cd;
    int retryCount = 0, Flat_completed = 0;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    public String Selected_prj_id, Selected_wing_id, Selected_floor_id, Selected_flat_id = "-1", Selected_category_id, Selected_type_id,
            UserId, Selected_flat_status = "", Selected_wing_name;
    public List<Project> Projects = new ArrayList<>();
    ArrayList<String> spProject = new ArrayList<>();
    public List<DetailsData> Wings = new ArrayList<>();
    ArrayList<String> spWings = new ArrayList<>();
    public List<DetailsData> Type = new ArrayList<>();
    ArrayList<String> spType = new ArrayList<>();
    public List<DetailsData> Floor = new ArrayList<>();
    ArrayList<String> spFloor = new ArrayList<>();
    public List<SalesItem> Flat = new ArrayList<>();
    ArrayList<String> spFlat = new ArrayList<>();
    public List<CategoryData> Category = new ArrayList<>();
    ArrayList<String> spCategory = new ArrayList<>();
    SalesItem FlatDtl;
    //    public static UserRole_data UserRole = new UserRole_data();
    String DBName = "", CompanyID, UserRole, DiscountReason = "", Action, FlatId, ProjID, ObjectID,Creator;
    Double Amount = 0.0, Discount = 0.0, FinalAmount = 0.0;
    Menu myMenu;
    ToBeApproveData data;
    int check = 0, RowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cd = new ConnectionDetector(this);

        initViews();

        sp_select_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    et_Amount.setFocusable(true);
                    et_Amount.setClickable(true);
                    et_Amount.setEnabled(true);
                    //Clear all selection
                    Selected_wing_id = "-1";
                    Selected_floor_id = "-1";
                    Selected_flat_id = "-1";
                    Selected_flat_status = "";
                    Flat_completed = 0;
                    Selected_type_id = "-1";
                    sp_Select_Type.setAdapter(null);
                    spType.clear();
                    Selected_category_id = "-1";
                    sp_Select_wing.setAdapter(null);
                    sp_Select_floor.setAdapter(null);
                    sp_Select_flat.setAdapter(null);
                    sp_Select_category.setAdapter(null);
                    spWings.clear();
                    spType.clear();
                    spFloor.clear();
                    spFlat.clear();
                    spCategory.clear();
                    et_Amount.setText("0.00");
                    clearfields(0);
                    tvType.setText("");

                    if (i == 0) {
                        DBName = "";
                        Selected_wing_id = "-1";
                        Selected_floor_id = "-1";
                        Selected_flat_id = "-1";
                        Selected_flat_status = "";
                        Flat_completed = 0;
                        Selected_category_id = "-1";
                        sp_Select_wing.setAdapter(null);
                        sp_Select_floor.setAdapter(null);
                        sp_Select_flat.setAdapter(null);
                        sp_Select_category.setAdapter(null);
                        spWings.clear();
                        spType.clear();
                        spFloor.clear();
                        spFlat.clear();
                        spCategory.clear();
                        Selected_type_id = "-1";
                        sp_Select_Type.setAdapter(null);
                        spType.clear();
                        et_Amount.setText("0.00");
                        clearfields(0);
                        tvType.setText("");
//                        Phaselist.clear();
//                        adpt.notifyDataSetChanged();

                    } else {
                        Selected_prj_id = Projects.get(sp_select_project.getSelectedItemPosition() - 1).getID();
//                        Log.e("***", "spinner Selected_project_id = " + Selected_prj_id+
//                                "\n prj name = " + Projects.get(sp_select_project.getSelectedItemPosition()-1).getDescription());
                        DBName = DbNameConvert(Selected_prj_id);
//                        Log.e("***"," DBName ="+DBName);
                        getWingList(Selected_prj_id);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Select_wing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    et_Amount.setFocusable(true);
                    et_Amount.setClickable(true);
                    et_Amount.setEnabled(true);
                    //Clear all selection
                    Selected_floor_id = "-1";
                    Selected_flat_id = "-1";
                    Selected_flat_status = "";
                    Flat_completed = 0;
                    Selected_category_id = "-1";
                    sp_Select_floor.setAdapter(null);
                    sp_Select_flat.setAdapter(null);
                    sp_Select_category.setAdapter(null);
                    spType.clear();
                    spFloor.clear();
                    spFlat.clear();
                    spCategory.clear();
                    Selected_type_id = "-1";
                    sp_Select_Type.setAdapter(null);
                    spType.clear();
                    et_Amount.setText("0.00");
                    clearfields(0);
                    tvType.setText("");

                    if (i == 0) {
                        Selected_floor_id = "-1";
                        Selected_flat_id = "-1";
                        Selected_flat_status = "";
                        Flat_completed = 0;
                        Selected_category_id = "-1";
                        sp_Select_floor.setAdapter(null);
                        sp_Select_flat.setAdapter(null);
                        sp_Select_category.setAdapter(null);
                        spType.clear();
                        spFloor.clear();
                        spFlat.clear();
                        spCategory.clear();
                        Selected_type_id = "-1";
                        sp_Select_Type.setAdapter(null);
                        spType.clear();
                        et_Amount.setText("0.00");
                        clearfields(0);
                        tvType.setText("");
//                        Phaselist.clear();
//                        adpt.notifyDataSetChanged();
                    } else {
                        Selected_wing_id = Wings.get(sp_Select_wing.getSelectedItemPosition() - 1).getID().toString();
                        Selected_wing_name = Wings.get(sp_Select_wing.getSelectedItemPosition() - 1).getDescription().toString();
//                        Log.e("***", i+" = i spinner Selected_bld_id = " + Selected_wing_id +
//                                "\n wing name = " + Wings.get(sp_Select_wing.getSelectedItemPosition()-1).getDescription().toString());
//                        Log.e("***"," DBName ="+DBName);
//                        getActivityList(Selected_wing_id);
                        getFloorList(Selected_wing_id);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Select_floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    et_Amount.setFocusable(true);
                    et_Amount.setClickable(true);
                    et_Amount.setEnabled(true);
                    //Clear all selection
                    Selected_flat_id = "-1";
                    Selected_flat_status = "";
                    Flat_completed = 0;
                    Selected_category_id = "-1";
                    sp_Select_flat.setAdapter(null);
                    sp_Select_category.setAdapter(null);
                    spFlat.clear();
                    spCategory.clear();
                    Selected_type_id = "-1";
                    sp_Select_Type.setAdapter(null);
                    spType.clear();
                    et_Amount.setText("0.00");
                    clearfields(0);
                    tvType.setText("");

                    if (i == 0) {
                        Selected_flat_id = "-1";
                        Selected_flat_status = "";
                        Flat_completed = 0;
                        Selected_category_id = "-1";
                        sp_Select_flat.setAdapter(null);
                        sp_Select_category.setAdapter(null);
                        spFlat.clear();
                        spCategory.clear();
                        Selected_type_id = "-1";
                        sp_Select_Type.setAdapter(null);
                        spType.clear();
                        et_Amount.setText("0.00");
                        clearfields(0);
                        tvType.setText("");
//                        Phaselist.clear();
//                        adpt.notifyDataSetChanged();
                    } else {
                        Selected_floor_id = Floor.get(sp_Select_floor.getSelectedItemPosition() - 1).getID().toString();
//                        Log.e("***", "spinner Selected_floor_id = " + Selected_floor_id +
//                                "\n floor name = " + Floor.get(sp_Select_floor.getSelectedItemPosition()-1).getDescription().toString());
//
                        Log.e("***", " DBName =" + DBName + " == " + Selected_floor_id + " == " + Selected_wing_id);
                        getFlatList(Selected_floor_id, Selected_wing_name);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Select_flat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    et_Amount.setFocusable(true);
                    et_Amount.setClickable(true);
                    et_Amount.setEnabled(true);
                    Selected_type_id = "-1";
                    sp_Select_Type.setAdapter(null);
                    spType.clear();
                    Selected_category_id = "-1";
                    sp_Select_category.setAdapter(null);
                    spCategory.clear();
                    et_Amount.setText("0.00");
                    clearfields(0);
                    tvType.setText("");
                    if (i == 0) {
                        Selected_flat_status = "";
                        Selected_category_id = "-1";
                        sp_Select_category.setAdapter(null);
                        spCategory.clear();
                        et_Amount.setText("0.00");
                        clearfields(0);
                        tvType.setText("");
//                        Phaselist.clear();
//                        adpt.notifyDataSetChanged();
                    } else {
                        Selected_flat_id = Flat.get(sp_Select_flat.getSelectedItemPosition() - 1).getID();
                        Discount = Flat.get(sp_Select_flat.getSelectedItemPosition() - 1).getDiscount() == null ? 0.0 : Flat.get(sp_Select_flat.getSelectedItemPosition() - 1).getDiscount();
                        DiscountReason = Flat.get(sp_Select_flat.getSelectedItemPosition() - 1).getDiscountReason();

                        //   Selected_flat_status = Flat.get(sp_Select_flat.getSelectedItemPosition()-1).getStatus();
                        //    Flat_completed = Integer.parseInt(Flat.get(sp_Select_flat.getSelectedItemPosition()-1).getUnchecked());
                        Log.e("***", "spinner Selected_Flat_id = " + Selected_flat_id +
                                "\n Discount = " + Discount + "==>" + DiscountReason +
                                "\n Selected_flat_Status = " + Selected_flat_status);
                        if (Discount <= 0) {
                            et_Discount.setText("");
                            et_DiscountReason.setText("");
                        } else {
                            et_Discount.setText("" + Discount);
                            et_DiscountReason.setText("" + DiscountReason);
                        }

                        String flatType = Flat.get(sp_Select_flat.getSelectedItemPosition() - 1).getShortName();
                        if (null != flatType || flatType.length() > 1) {
                            tvType.setVisibility(View.VISIBLE);
                            sp_Select_Type.setVisibility(View.GONE);
                            tvType.setText("" + flatType);
                            //Get category list
                            getCategoryList(flatType,Selected_flat_id);
                        } else {
                            tvType.setVisibility(View.GONE);
                            sp_Select_Type.setVisibility(View.VISIBLE);
                            getTypeList(Selected_flat_id);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Select_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    et_Amount.setFocusable(true);
                    et_Amount.setClickable(true);
                    et_Amount.setEnabled(true);
                    //Clear all selection
                    Selected_category_id = "-1";
                    sp_Select_category.setAdapter(null);
                    spCategory.clear();
                    et_Amount.setText("0.00");
                    if (Discount <= 0 && (DiscountReason == null || DiscountReason.equalsIgnoreCase("")))
                        clearfields(0);
                    else
                        clearfields(1);

                    if (i == 0) {
                        Selected_category_id = "-1";
                        sp_Select_category.setAdapter(null);
                        spCategory.clear();
                        et_Amount.setText("0.00");
                        if (Discount <= 0 && (DiscountReason == null || DiscountReason.equalsIgnoreCase("")))
                            clearfields(0);
                        else
                            clearfields(1);
//                        Phaselist.clear();
//                        adpt.notifyDataSetChanged();
                    } else {
                        Selected_type_id = Type.get(sp_Select_Type.getSelectedItemPosition() - 1).getDescription().toString();
//                        Log.e("***", "spinner Selected_Type_id = " + Selected_type_id+
//                                "\n Type name = " + Type.get(sp_Select_Type.getSelectedItemPosition()-1).getDescription().toString());
//
//                        Log.e("***",Selected_type_id+"<= DBName ="+DBName);

                        //Get category list
                        getCategoryList(Selected_type_id,Selected_flat_id);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Select_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    et_Amount.setFocusable(true);
                    et_Amount.setClickable(true);
                    et_Amount.setEnabled(true);
                    if (Discount <= 0 && (DiscountReason == null || DiscountReason.equalsIgnoreCase("")))
                        clearfields(0);
                    else
                        clearfields(1);

                    if (i == 0) {
                        Selected_category_id = "-1";
                        et_Amount.setText("0.00");
                        if (Discount <= 0 && (DiscountReason == null || DiscountReason.equalsIgnoreCase("")))
                            clearfields(0);
                        else
                            clearfields(1);

//                        Phaselist.clear();
//                        adpt.notifyDataSetChanged();
                    } else {
                        et_Amount.setFocusable(false);
                        et_Amount.setClickable(false);
                        et_Amount.setEnabled(false);
                        Selected_category_id = Category.get(sp_Select_category.getSelectedItemPosition() - 1).getSalesItemID().toString();
                        String amount = Category.get(sp_Select_category.getSelectedItemPosition() - 1).getAmountExclTax();

                        Log.e("***", "spinner Selected_Categoryn_id = " + Selected_category_id +
                                "\n Amount name = " + amount);
                        et_Amount.setText("" + amount);
                        //display list
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("***", "------Apply-----");
                double discount, finalAmt, amt;
                Amount = Double.parseDouble(et_Amount.getText().toString().trim());
//                if(Selected_flat_id.equalsIgnoreCase("-1"))
//                    AlertDialog(MainActivity.this, "Alert", "Please select flat first.");
                if (et_Amount.getText().toString().trim().length() == 0 || et_Amount.getText().toString().trim().isEmpty()) {
                    et_Amount.setError("Enter amount");
                    Amount = 0.0;
                } else if (et_Discount.getText().toString().trim().length() == 0 || et_Discount.getText().toString().trim().isEmpty()) {
                    et_Discount.setError("Enter discount");
                    Discount = 0.0;
                } else if (et_DiscountReason.getText().toString().trim().length() == 0 || et_DiscountReason.getText().toString().trim().isEmpty()) {
                    et_DiscountReason.setError("Enter discount reason");
                    DiscountReason = "";
                } else {
                    Discount = Double.valueOf(et_Discount.getText().toString().trim());
                    DiscountReason = et_DiscountReason.getText().toString().trim();
                    if (Discount >= 0) {
                        FinalAmount = Amount - Discount;
                        et_FinalAmt.setText("" + FinalAmount);
                    }
                }
            }
        });

        btn_Display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
//                    if (Selected_flat_id.equalsIgnoreCase("-1"))
//                        AlertDialog(MainActivity.this, "Alert", "Select flat first");
//                    else {

                    try {
                    String amount = "0", discount = "0", finalamt = "0";
                    Log.e("***", " Action==>" + Action);
                    if (Action.equalsIgnoreCase("Add")) {
                        Amount = Double.valueOf(et_Amount.getText().toString().trim());
                        if (Amount == 0.0)
                            Toast.makeText(getApplicationContext(), "Please enter amount.", Toast.LENGTH_SHORT).show();
                        else
//                        if (et_FinalAmt.getText().toString().trim().length() != 0 || !et_FinalAmt.getText().toString().isEmpty())
//                            FinalAmount = Double.valueOf(et_FinalAmt.getText().toString().trim());
//                        else
                            FinalAmount = Amount - Discount;

                        Log.e("***", "@===> " + Amount + "=" + Discount + " = " + FinalAmount);

                    } else {
//                        et_Amount.setText("" + FlatDtl.getTotalContract());
//                        Amount = FlatDtl.getTotalContract();
//                        et_Discount.setText("" + FlatDtl.getDiscount());
//                        Discount = FlatDtl.getDiscount();
//                        et_DiscountReason.setText("" + FlatDtl.getDiscountReason());
//                        DiscountReason = FlatDtl.getDiscountReason();
//                        Double FinAmt = FlatDtl.getTotalBasicContractExTax();
                        //FinalAmount = Amount - Discount;//FlatDtl.getTotalBasicContractExTax();
                        if (et_Amount.getText().toString().trim().length() == 0 || et_Amount.getText().toString().trim().isEmpty()) {
                            et_Amount.setError("Enter amount");
                            Amount = 0.0;
                        } else if (et_Discount.getText().toString().trim().length() == 0 || et_Discount.getText().toString().trim().isEmpty()) {
                            et_Discount.setError("Enter discount");
                            Discount = 0.0;
                        } else if (et_DiscountReason.getText().toString().trim().length() == 0 || et_DiscountReason.getText().toString().trim().isEmpty()) {
                            et_DiscountReason.setError("Enter discount reason");
                            DiscountReason = "";
                        } else {
                            Discount = Double.valueOf(et_Discount.getText().toString().trim());
                            DiscountReason = et_DiscountReason.getText().toString().trim();
                            if (Discount >= 0) {
                                FinalAmount = Amount - Discount;
                                et_FinalAmt.setText("" + FinalAmount);
                            }
                        }
                        Creator = data.getCreatorID();
                        Log.e("***", Creator+"@@===> " + Amount + "=" + Discount + " = " + FinalAmount);
                    }

//                            Amount = Double.valueOf(amount);
//                            Discount = Double.valueOf(discount);
//                        FinalAmount = Amount - Discount;
//                            DiscountReason = et_DiscountReason.getText().toString().trim();

                        Log.e("***", DBName + " = amount => " + Amount + "=" + Discount + " = " + FinalAmount);
                        Intent in = new Intent(MainActivity.this, PhaseListActivity.class);
                        in.putExtra("Action", Action);
                        in.putExtra("DBName", DBName);
                        if (Action.equalsIgnoreCase("View"))
                            in.putExtra("SelectedFlatId", FlatId);
                        else
                            in.putExtra("SelectedFlatId", Selected_flat_id);
                        in.putExtra("Amount", Amount);
                        in.putExtra("Discount", Discount);
                        in.putExtra("DiscountReason", DiscountReason);
                        in.putExtra("FinalAmount", FinalAmount);
                        in.putExtra("WingID", Selected_wing_id);
                        in.putExtra("ProjectID", Selected_prj_id);
                        in.putExtra("SelectedCatId", Selected_category_id);
                        in.putExtra("RowID", RowID);
                        if (Action.equalsIgnoreCase("View")) {
                            in.putExtra("ObjectID", data.getObjectID());
                            in.putExtra("Creator",Creator);
                        }
                        startActivity(in);
                        if (Action.equalsIgnoreCase("View"))
                            finish();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    }
                } else
                    Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clearfields(int check) {
        Amount = 0.0;
        et_Amount.setText("0.0");
        if (check == 0) {
            FinalAmount = 0.0;
            Discount = 0.0;
            DiscountReason = "";
            et_Discount.setText("0.0");
            et_DiscountReason.setText("");
            et_FinalAmt.setText("0.0");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        et_Amount.setFocusable(true);
        et_Amount.setClickable(true);
        et_Amount.setEnabled(true);
        //Clear all selection
        Selected_floor_id = "-1";
        Selected_flat_id = "-1";
        Selected_flat_status = "";
        Flat_completed = 0;
        Selected_category_id = "-1";
        sp_Select_flat.setAdapter(null);
        sp_Select_category.setAdapter(null);
        spFlat.clear();
        spCategory.clear();
        Selected_type_id = "-1";
        sp_Select_Type.setAdapter(null);
        spType.clear();
        et_Amount.setText("0.00");
        clearfields(0);
        tvType.setText("");
       /* if(Constant.SelectedLocationId.equalsIgnoreCase("")){

        }else{
            getDiplayChecklist(Constants.SelectedLocationId);
        }*/
    }

    public String DbNameConvert(String dbname) {
        String smallDb = dbname.toLowerCase();
        dbname = smallDb.replaceAll("-", "_");
        Log.e("***", " DBName =" + dbname);
        return dbname;
    }

    public void initViews() {
// assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);


        // Display application icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.hitmobile);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Hit Sales");


        loginPreferences = getSharedPreferences(Constant.PREF, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        UserId = loginPreferences.getString("ID", "");
        CompanyID = loginPreferences.getString("CompanyID", "01");
        UserRole = loginPreferences.getString("UserRole", "");

        sp_select_project = (Spinner) findViewById(R.id.sp_select_project);
        sp_Select_wing = (Spinner) findViewById(R.id.sp_Select_wing);
//        sp_Select_activity = (Spinner) findViewById(R.id.sp_Select_activity);
        sp_Select_Type = (Spinner) findViewById(R.id.sp_Select_type);
        sp_Select_floor = (Spinner) findViewById(R.id.sp_select_floor);
        sp_Select_flat = (Spinner) findViewById(R.id.sp_select_flatnumber);
        sp_Select_category = (Spinner) findViewById(R.id.sp_Select_category);

        btn_Display = (Button) findViewById(R.id.btn_Display);
//        btn_Submit = (Button) findViewById(R.id.btn_Submit);
//        btn_Approve = (Button) findViewById(R.id.btn_Approve);
//        btn_Reject = (Button) findViewById(R.id.btn_Reject);
        tv_NoData = (TextView) findViewById(R.id.tv_NoData);
        et_Amount = (EditText) findViewById(R.id.et_Amount);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        et_Discount = (EditText) findViewById(R.id.et_Discount);
        et_DiscountReason = (EditText) findViewById(R.id.et_DiscountReason);
        et_FinalAmt = (EditText) findViewById(R.id.et_FinalAmount);
        btn_Apply = (Button) findViewById(R.id.btn_Apply);
        //Visible only for Checker
        llFinalAmt = (TableRow) findViewById(R.id.llFinalAmt);
        llApply = (TableRow) findViewById(R.id.llApply);
        llDiscount = (TableRow) findViewById(R.id.llDiscount);
        llDiscountReason = (TableRow) findViewById(R.id.llDiscountReason);
        tvproject = (TextView) findViewById(R.id.tv_proj);
        tvwing = (TextView) findViewById(R.id.tv_wing);
        tvfloor = (TextView) findViewById(R.id.tv_floor);
        tvflat = (TextView) findViewById(R.id.tv_flat);
        tvType = (TextView) findViewById(R.id.tv_type);
        tvcategory = (TextView) findViewById(R.id.tv_cat);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            Action = b.getString("Action");
            if (Action.equalsIgnoreCase("View")) {
                data = b.getParcelable("SelectedFlatData");
                String ObjID = data.getObjectID();
                String[] ObjIdArr = ObjID.split("/");
                FlatId = ObjIdArr[1];
                String ObjIDProID[] = ObjID.split("\\\\"); //split to get project id
                ProjID = ObjIDProID[1];
                DBName = DbNameConvert(ProjID);
                Selected_prj_id = ProjID;
//                ProjID = ObjIdArr[3];
                RowID = data.getRowID();
//                Selected_flat_id = ObjIdArr[1];
//                Selected_wing_id = ObjIdArr[2];
                ObjectID = data.getObjectID();
            }
        }
        if (Action.equalsIgnoreCase("Add")) {
            sp_select_project.setVisibility(View.VISIBLE);
            sp_Select_wing.setVisibility(View.VISIBLE);
            sp_Select_floor.setVisibility(View.VISIBLE);
            sp_Select_flat.setVisibility(View.VISIBLE);
            sp_Select_Type.setVisibility(View.VISIBLE);
            sp_Select_category.setVisibility(View.VISIBLE);
            tvproject.setVisibility(View.GONE);
            tvwing.setVisibility(View.GONE);
            tvfloor.setVisibility(View.GONE);
            tvflat.setVisibility(View.GONE);
            tvType.setVisibility(View.GONE);
            tvcategory.setVisibility(View.GONE);
        } else {//View
            sp_select_project.setVisibility(View.GONE);
            sp_Select_wing.setVisibility(View.GONE);
            sp_Select_floor.setVisibility(View.GONE);
            sp_Select_flat.setVisibility(View.GONE);
            sp_Select_Type.setVisibility(View.GONE);
            sp_Select_category.setVisibility(View.GONE);
            tvproject.setVisibility(View.VISIBLE);
            tvwing.setVisibility(View.VISIBLE);
            tvfloor.setVisibility(View.VISIBLE);
            tvflat.setVisibility(View.VISIBLE);
            tvType.setVisibility(View.VISIBLE);
            tvcategory.setVisibility(View.VISIBLE);
        }

        if (UserRole.equalsIgnoreCase("Checker")) {
            llFinalAmt.setVisibility(View.VISIBLE);
            llApply.setVisibility(View.VISIBLE);
            llDiscount.setVisibility(View.VISIBLE);
            llDiscountReason.setVisibility(View.VISIBLE);
        } else {
            llFinalAmt.setVisibility(View.GONE);
            llApply.setVisibility(View.GONE);
            llDiscount.setVisibility(View.GONE);
            llDiscountReason.setVisibility(View.GONE);
        }

        if (cd.isConnectingToInternet()) {
            Log.e("***", UserRole + "<= UserId =" + UserId + " <=> Company id =" + CompanyID);
            if (Action.equalsIgnoreCase("Add"))
                getProjectList();
            else
                getFlatDetails(FlatId);
        } else
            Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        myMenu = menu;
        MenuItem item = menu.findItem(R.id.Logout);
        if (UserRole.equalsIgnoreCase("Checker"))
            item.setVisible(false);
        else
            item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.FlatAvailability:
                Intent in = new Intent(MainActivity.this, FlatAvailableCheckActivity.class);
                startActivity(in);
                return true;

            case R.id.Logout:
                AlertLogout();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return super.onOptionsItemSelected(item);

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

    @Override
    public void onBackPressed() {

        if (UserRole.equalsIgnoreCase("Checker")) {
            this.finish();
        } else {
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
    }

    private void getFlatDetails(final String FlatId) {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
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

            Call<List<SalesItem>> call = api.getFlatDetails(CompanyID, UserId, FlatId, DBName);
            call.enqueue(new Callback<List<SalesItem>>() {
                @Override
                public void onResponse(Call<List<SalesItem>> call, Response<List<SalesItem>> response) {
                    Log.e("***", "response Flat Details - " + response.body());
                    try {
                        if (response.body() == null || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get Flat Details data");
                            Toast.makeText(MainActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            FlatDtl = response.body().get(0);
                            Log.e("***", spFlat.size() + " === size Flat - " + Flat.size());

                            Selected_flat_id = FlatDtl.getID();
                            Selected_wing_id = FlatDtl.getWing();

                            tvproject.setText(ProjID);
                            tvwing.setText(FlatDtl.getWing() + "");
                            tvfloor.setText(FlatDtl.getFloorID() + "");
                            tvflat.setText(FlatDtl.getID() + "");
                            tvType.setText(FlatDtl.getShortName() + "");
                            tvcategory.setText(FlatDtl.getCategorySelected() + "");
                            et_Amount.setText("" + FlatDtl.getTotalContract());
                            Amount = FlatDtl.getTotalContract();
                            Discount = FlatDtl.getDiscount();
                            if (Discount == null)
                                et_Discount.setText("0.0");
                            else
                                et_Discount.setText("" + FlatDtl.getDiscount());
                            DiscountReason = FlatDtl.getDiscountReason();
                            if (DiscountReason == null)
                                et_Discount.setText("");
                            else
                                et_DiscountReason.setText("" + FlatDtl.getDiscountReason());
                            Double FinAmt = FlatDtl.getTotalBasicContractExTax();
                            FinalAmount = FlatDtl.getTotalBasicContractExTax();
                            et_FinalAmt.setText("" + FinAmt);
                            Log.e("***", Amount + " = " + Discount + " = " + DiscountReason + " = " + FinalAmount);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<SalesItem>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---Flat Details Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        getFlatDetails(FlatId);
                    else
                        Toast.makeText(MainActivity.this, "Flat Details not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
        } catch (Exception e) {
            progressDoalog.dismiss();
            e.printStackTrace();
        }
    }

    private void getProjectList() {
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

            Call<List<Project>> call = api.getProjectList(UserId, CompanyID);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(MainActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setCancelable(false);
            progressDoalog.setCanceledOnTouchOutside(false);
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            call.enqueue(new Callback<List<Project>>() {
                @Override
                public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                    Log.e("***", "response ProjectList - " + response.body());
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get ProjectList data");
                            Toast.makeText(MainActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            Projects.clear();
                            spProject.clear();
                            spProject.add("Select Project");
                            Projects = response.body();
                            for (int i = 0; i < Projects.size(); i++) {
                                spProject.add(Projects.get(i).getID());
                            }
                            Log.e("***", spProject.size() + " === size Projects - " + Projects.size());
                            ArrayAdapter adpt = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, spProject);
                            sp_select_project.setAdapter(adpt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Project>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---Projects Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        getProjectList();
                    else
                        Toast.makeText(MainActivity.this, " Projects data not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        } catch (Exception e) {
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        }
    }

    private void getWingList(final String SelectedPrjId) {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
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

            Call<List<DetailsData>> call = api.getWingList(UserId, CompanyID, SelectedPrjId, DBName);
            call.enqueue(new Callback<List<DetailsData>>() {
                @Override
                public void onResponse(Call<List<DetailsData>> call, Response<List<DetailsData>> response) {
                    Log.e("***", "response Wings - " + response.body());
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get Wings data");
                            Toast.makeText(MainActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            Wings.clear();
                            spWings.clear();
                            spWings.add("Select Wing");
                            Wings = response.body();
                            for (int i = 0; i < Wings.size(); i++) {
                                spWings.add(Wings.get(i).getID());
                            }
                            Log.e("***", spWings.size() + " === size Wings - " + Wings.size());
                            ArrayAdapter adpt = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, spWings);
                            sp_Select_wing.setAdapter(adpt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<DetailsData>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---Wings Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        getWingList(SelectedPrjId);
                    else
                        Toast.makeText(MainActivity.this, " Buildings data not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        } catch (Exception e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        }
    }

    private void getTypeList(final String SelectedActivityId) {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
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

            Call<List<DetailsData>> call = api.getTypeList(CompanyID, UserId, SelectedActivityId, DBName);
            call.enqueue(new Callback<List<DetailsData>>() {
                @Override
                public void onResponse(Call<List<DetailsData>> call, Response<List<DetailsData>> response) {
                    Log.e("***", "response TypeList - " + response.body());
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get TypeList data");
                            Toast.makeText(MainActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            Type.clear();
                            spType.clear();
                            spType.add("Select Flat Type");
                            Type = response.body();
                            for (int i = 0; i < Type.size(); i++) {
                                spType.add(Type.get(i).getDescription());
                            }
                            Log.e("***", spType.size() + " === size Type - " + Type.size());
                            ArrayAdapter adpt = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, spType);
                            sp_Select_Type.setAdapter(adpt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<DetailsData>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---Type Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        getTypeList(SelectedActivityId);
                    else
                        Toast.makeText(MainActivity.this, "Flat Type data not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        } catch (Exception e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        }
    }

    private void getFloorList(final String SelectedTypeId) {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
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

            Call<List<DetailsData>> call = api.getFloorList(CompanyID, UserId, SelectedTypeId, DBName);
            call.enqueue(new Callback<List<DetailsData>>() {
                @Override
                public void onResponse(Call<List<DetailsData>> call, Response<List<DetailsData>> response) {
                    Log.e("***", "response FloorList - " + response.body());
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get FloorList data");
                            Toast.makeText(MainActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            Floor.clear();
                            spFloor.clear();
                            spFloor.add("Select Floor");
                            Floor = response.body();
                            for (int i = 0; i < Floor.size(); i++) {
                                spFloor.add(Floor.get(i).getDescription());
                            }
                            Log.e("***", spFloor.size() + " === size Floor - " + Floor.size());
                            ArrayAdapter adpt = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, spFloor);
                            sp_Select_floor.setAdapter(adpt);
                            //get user role as per selected project and building
                            //  getUserRole(UserId,CompanyID);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<DetailsData>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---Floor Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        getFloorList(SelectedTypeId);
                    else
                        Toast.makeText(MainActivity.this, "Floor data not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
        } catch (Exception e) {
            progressDoalog.dismiss();
            e.printStackTrace();
        }
    }

    private void getFlatList(final String SelectedFloorId, final String SelectedWingId) {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
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

            Call<List<SalesItem>> call = api.getFlatList(CompanyID, UserId, SelectedFloorId, SelectedWingId, DBName);
            call.enqueue(new Callback<List<SalesItem>>() {
                @Override
                public void onResponse(Call<List<SalesItem>> call, Response<List<SalesItem>> response) {
                    Log.e("***", "response FlatList - " + response.body());
                    try {
                        if (response.body() == null || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get FlatList data");
                            Toast.makeText(MainActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            Flat.clear();
                            spFlat.clear();
                            spFlat.add("Select Flat");
                            Flat = response.body();
                            for (int i = 0; i < Flat.size(); i++) {
                                spFlat.add(Flat.get(i).getID());
                            }
                            Log.e("***", spFlat.size() + " === size Flat - " + Flat.size());
                            ArrayAdapter adpt = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, spFlat);
                            sp_Select_flat.setAdapter(adpt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<SalesItem>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---Flat Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        getFlatList(SelectedFloorId, SelectedWingId);
                    else
                        Toast.makeText(MainActivity.this, "Flat data not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        } catch (Exception e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        }
    }

    private void getCategoryList(final String SelectedId,final String SelectedFlatId) {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
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

            Call<List<CategoryData>> call = api.getCategoryList(CompanyID, UserId, SelectedId,SelectedFlatId, DBName);
            call.enqueue(new Callback<List<CategoryData>>() {
                @Override
                public void onResponse(Call<List<CategoryData>> call, Response<List<CategoryData>> response) {
                    Log.e("***", "response CategoryList - " + response.body());
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get CategoryList data");
                            Toast.makeText(MainActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            Category.clear();
                            spCategory.clear();
                            spCategory.add("Select Category");
                            Category = response.body();
                            for (int i = 0; i < Category.size(); i++) {
                                spCategory.add(Category.get(i).getName());
                            }
                            Log.e("***", spCategory.size() + " === size Category - " + Category.size());
                            ArrayAdapter adpt = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, spCategory);
                            sp_Select_category.setAdapter(adpt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<List<CategoryData>> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---Category Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        getCategoryList(SelectedId,SelectedFlatId);
                    else
                        Toast.makeText(MainActivity.this, "Category data not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        } catch (Exception e) {
            progressDoalog.dismiss();
            e.printStackTrace();
            Constant.showDialog(MainActivity.this, "Something went wrong.");
        }
    }

  /*  private void UpdateFlatStatus(final String st) {
        try {
            Log.e("***","Update Flat Status = "+st);
            OkHttpClient okHttpClient;
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(Constants.ConnectTimeOut, TimeUnit.MINUTES)
                    .readTimeout(Constants.readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(Constants.writeTimeout, TimeUnit.SECONDS)
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

            Call<String> call = api.UpdateFlatStatus(DBName,UserId,CompanyID,Selected_flat_id,st);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(MainActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setCancelable(false);
            progressDoalog.setCanceledOnTouchOutside(false);
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e("***", "response Status - " + response.body());
                    try {
                        if (response.body().toString().isEmpty() || response.body().toString().contains("[]")) {
                            Log.e("***", "Unable to get Status data");
                            Toast.makeText(MainActivity.this, "Unable to get Data ...", Toast.LENGTH_SHORT).show();
                        } else {
                            String Status = response.body();
                            Selected_flat_status = st;
                            Log.e("***", Selected_flat_status+"Update status RESP =" + Status);
                            if (response.body().toString().contains("success")) {
                                if(Selected_flat_status.equalsIgnoreCase("s"))
                                    Toast.makeText(MainActivity.this, "Flat submitted successfully...", Toast.LENGTH_SHORT).show();
                                else if(Selected_flat_status.equalsIgnoreCase("a"))
                                    Toast.makeText(MainActivity.this, "Flat approved successfully...", Toast.LENGTH_SHORT).show();
                                else if(Selected_flat_status.equalsIgnoreCase("r"))
                                    Toast.makeText(MainActivity.this, "Flat rejected successfully...", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Fail to update status..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    progressDoalog.dismiss();
                    Log.e("***", retryCount + "---Status Exception----" + t);

                    if (++retryCount < maxRetryCount)
                        UpdateFlatStatus(st);
                    else
                        Toast.makeText(MainActivity.this, " Status data not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
