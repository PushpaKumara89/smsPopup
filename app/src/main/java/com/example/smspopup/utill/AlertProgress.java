package com.example.smspopup.utill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.smspopup.R;


public class AlertProgress {
    private View view;
    private Context context;
    private AlertDialog alertDialog;
    public AlertProgress( Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View prog = inflater.inflate(R.layout.progress_alert,null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = prog;
        alert.setView(mView);
        alertDialog = alert.create();
        alertDialog.setCancelable(false);
    }

    public void show(){
        alertDialog.show();
    }

    public void dismiss(){
        alertDialog.dismiss();
    }

}
