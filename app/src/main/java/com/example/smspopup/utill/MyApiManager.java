package com.example.smspopup.utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.universl.smsnotifier.APIPinManager;
import com.universl.smsnotifier.AppOperator;
import com.universl.smsnotifier.SMSNotifireUtils;
import com.universl.smsnotifier.model.ApplicationMetaData;
import com.universl.smsnotifier.model.OTPRequest;
import com.universl.smsnotifier.model.OTPResponse;
import com.universl.smsnotifier.model.OTPVerify;
import com.universl.smsnotifier.webservice.AppClient;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApiManager extends APIPinManager {
    private Context context;
    private String refNo;
    private String phoneNumber;
    private List<AppOperator> appOperators;
    private AlertProgress progress;
    public MyApiManager(Context context, List<AppOperator> appOperators) {
        super(context, appOperators);
        this.appOperators = appOperators;
        this.context = context;
        this.progress = new AlertProgress(context);

    }

    public void requestPin(final String phoneNo) {
        this.progress.show();
        AppOperator appOperator = this.getSelectedOperator(phoneNo);
        if (appOperator == null) {
            Toast.makeText(this.context, "Invalid Phone Number!", Toast.LENGTH_LONG).show();
            this.progress.dismiss();
        } else {
            OTPRequest otpRequest = new OTPRequest(appOperator.getAppId(), appOperator.getPassword(), "tel:94" + phoneNo, appOperator.getAppHash());
            otpRequest.setApplicationMetaData(new ApplicationMetaData("MOBILEAPP", Build.MODEL, "android " + Build.VERSION.SDK_INT, "https://play.google.com/store/apps/details?id=" + appOperator.getAppCode()));
            AppClient.getAPIService(SMSNotifireUtils.getApiType(phoneNo)).registerApp(otpRequest).enqueue(new Callback<OTPResponse>() {
                public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                    MyApiManager.this.progress.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        OTPResponse optRes = (OTPResponse)response.body();
                        if ("S1000".equals(optRes.getStatusCode())) {
                            MyApiManager.this.refNo = optRes.getReferenceNo();
                            MyApiManager.this.phoneNumber = phoneNo;
//                            pinText.requestFocus();
                            Toast.makeText(context, "You will receive pin No shortly", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Pin Request failed." + optRes.getStatusDetail(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Pin Request failed!", Toast.LENGTH_LONG).show();
                    }

                }

                public void onFailure(Call<OTPResponse> call, Throwable t) {
                    MyApiManager.this.progress.dismiss();
                    Toast.makeText(context, "Pin Request failed! Msg:" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void verifyPin(String pin, final AlertDialog alertDialog) {
        this.progress.show();
        AppOperator appOperator = this.getSelectedOperator(this.phoneNumber);
        if (appOperator == null) {
            Toast.makeText(this.context, "Invalid Phone Number!", Toast.LENGTH_LONG).show();
            this.progress.dismiss();
        } else {
            AppClient.getAPIService(SMSNotifireUtils.getApiType(this.phoneNumber)).verifyPin(new OTPVerify(appOperator.getAppId(), appOperator.getPassword(), this.refNo, pin)).enqueue(new Callback<OTPResponse>() {
                public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                    MyApiManager.this.progress.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        OTPResponse optRes = (OTPResponse)response.body();
                        if ("S1000".equals(optRes.getStatusCode())) {
                            Toast.makeText(context, "You have registered to the sms notification service.", Toast.LENGTH_LONG).show();

                            SharedPreferences prf = context.getSharedPreferences("details",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prf.edit();
                            editor.putString("phoneNum", MyApiManager.this.phoneNumber);
                            editor.commit();

                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, "Pin verification failed" + optRes.getStatusDetail(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Pin verification failed!", Toast.LENGTH_LONG).show();
                    }

                }

                public void onFailure(Call<OTPResponse> call, Throwable t) {
                    MyApiManager.this.progress.dismiss();
                    Toast.makeText(MyApiManager.this.context, "Pin verification failed!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private AppOperator getSelectedOperator(String phoneNo) {
        Iterator var2 = this.appOperators.iterator();

        AppOperator op;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            op = (AppOperator)var2.next();
        } while(!SMSNotifireUtils.getApiType(phoneNo).equals(op.getProvider()));

        return op;
    }

}
