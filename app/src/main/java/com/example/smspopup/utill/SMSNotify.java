package com.example.smspopup.utill;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.smspopup.R;
import com.universl.smsnotifier.AppOperator;
import com.universl.smsnotifier.Constants;
import com.universl.smsnotifier.SMSNotifireUtils;

import java.util.Arrays;
import java.util.List;


public class SMSNotify {
    public static void smsNotify(Activity context) {
        SharedPreferences prf = context.getSharedPreferences("details",Context.MODE_PRIVATE);
        String phone = prf.getString("phoneNum", "null");
        if (phone.equalsIgnoreCase("null")){

            Log.d("phone Number", phone);
            String msg = "දිනුමයි දැනුමයි තරගයට සම්බන්ධ වීමට කැමතිද? ඔබේ දුරකතන අංකය ඇතුලත් කරන්න.\n" +
                    "\n";
            String charge = "5 LKR+Tax P/D";
            String serviceProvider = SMSNotifireUtils.getServiceProvider(context);
            String msgApi = msg + " " + charge;
            if (Constants.SP_DIALOG1.equalsIgnoreCase(serviceProvider)
                    || Constants.SP_DIALOG2.equalsIgnoreCase(serviceProvider)
                    || Constants.SP_DIALOG3.equalsIgnoreCase(serviceProvider)
                    || Constants.SP_AIRTEL.equalsIgnoreCase(serviceProvider)
                    || Constants.SP_HUTCH.equalsIgnoreCase(serviceProvider)
                    || Constants.SP_MOBITEL.equalsIgnoreCase(serviceProvider)) {

                List<AppOperator> operators = Arrays.asList(
                        new AppOperator("APP_003229", "a5ad86d47c571e546e073197b5480190", null, "lk.wixis360.project.gradefivetuition", Constants.API_TYPE_MOBI),
                        new AppOperator("APP_052026", "7d8164bc4c686447136295424c9d75e7", null, "lk.wixis360.project.gradefivetuition", Constants.API_TYPE_IDEAMART));

                DialogBox dialogBox = new DialogBox(context.getLayoutInflater().inflate(R.layout.dialog_l, null), context, "SMS Alert Manager", msgApi, operators);
                dialogBox.show();
            }
        }
    }
}
