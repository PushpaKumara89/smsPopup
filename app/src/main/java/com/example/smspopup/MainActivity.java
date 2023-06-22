package com.example.smspopup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smspopup.utill.SMSNotify;
import com.universl.smsnotifier.APIPinManager;
import com.universl.smsnotifier.AppOperator;
import com.universl.smsnotifier.Constants;
import com.universl.smsnotifier.SMSNotifireUtils;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Button dftPopup;
    private Button orrPopup;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        dftPopup = findViewById(R.id.dftPopup);
        dftPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defSMSNotify();
            }
        });
        orrPopup = findViewById(R.id.orrPopup);
        orrPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMSNotify.smsNotify((Activity) context);
            }
        });

    }

    private void defSMSNotify() {
        String msg = "Obe sahakaru/ sahakariya ho mithuru mithuriyan samga dawasa pura " +
                "sms magin chat kireemta kamathida?\n";
        String charge = "6LKR + Tax P/D";
        String serviceProvider = SMSNotifireUtils.getServiceProvider(this);
        String msgApi = msg + " " + charge;
        if (Constants.SP_DIALOG1.equalsIgnoreCase(serviceProvider)
                || Constants.SP_DIALOG2.equalsIgnoreCase(serviceProvider)
                || Constants.SP_DIALOG3.equalsIgnoreCase(serviceProvider)
                || Constants.SP_AIRTEL.equalsIgnoreCase(serviceProvider)
                || Constants.SP_HUTCH.equalsIgnoreCase(serviceProvider)
                || Constants.SP_MOBITEL.equalsIgnoreCase(serviceProvider)
                || Constants.SP_ETISALAT.equalsIgnoreCase(serviceProvider)
        ) {

            List<AppOperator> operators = Arrays.asList(
                    new AppOperator("APP_003748", "7275fe5b5f2a22e0c451631ec7fbdb55", null, getPackageName(),Constants.API_TYPE_MOBI),
                    new AppOperator("APP_005199", "1219fce95325d486c2aa4c3d108c8be7", null, getPackageName(),Constants.API_TYPE_IDEAMART));
            APIPinManager pinManager = new APIPinManager(this,operators);
            pinManager.notifyService("SMS Alerts Manager", msgApi);
        }
    }
}