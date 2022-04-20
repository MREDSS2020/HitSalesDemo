package com.edss.hitsales.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.Toast;

import com.edss.hitsales.R;

import java.text.SimpleDateFormat;


/**
 * Created by EDSS Android Developer on 29-11-2021.
 */

public class Constant {
        public  static String CompanyId = "01",UserId;
    public  static long ConnectTimeOut = 2;
    public  static long readTimeout = 45;
    public  static long writeTimeout = 45;
    public  static int maxRetryCount = 4;
    public static int BudgetDiff = 10;
    public static int SurfaceDiff = 500;
    public static int isFreezone = 0;

    public static int SORTING_REQUEST = 206;
    public static int FREEZONE_REQUEST = 207;
    public  static String CustomerId="";

    public static String PREF = "loginPrefs";

    public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void showDialog(final Activity ctx , String msg) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom);
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.ic_warning);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
//                finish();
            }
        });

        AlertDialog alt = alertDialog.create();
        alt.show();
    }

    public static void colorToast(Context ctx, String MSg, int colr) {
        Toast toast = Toast.makeText(ctx, "  " + MSg + "  ", Toast.LENGTH_LONG);
        toast.getView().setBackgroundColor(colr);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void AlertDialog(final Activity ctx, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                }
        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static String GetObjectID(String ProjectID,String SalesItemId, String WingID){
        String ObjectID = "BLD\\" + ProjectID + "\\builderproject.hitsalesitem/"+SalesItemId+ "/" + ProjectID;//+"/" + WingID
        // e.g :- OFF\TEST\quotation.workitem/F-101
        return ObjectID;
    }

}
