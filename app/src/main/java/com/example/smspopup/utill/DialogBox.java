package com.example.smspopup.utill;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.smspopup.R;
import com.universl.smsnotifier.AppOperator;

import java.util.List;


public class DialogBox {
    private AlertDialog alertDialog;
    private Context context;
    private TextView msgCont;
    private TextView title;

    private EditText phone;
    private EditText pin;
    private MyApiManager myApiManager;

    AlertProgress alPro;


    public DialogBox(View inflate, Context context, String title, String msg, List<AppOperator> operators){
        this.context = context;
        this.myApiManager = new MyApiManager(context,operators);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = inflate;
        alert.setView(mView);

        alertDialog = alert.create();
        alertDialog.setCancelable(false);
        this.msgCont = mView.findViewById(R.id.messageContent);
        this.msgCont.setText(msg);

        this.title = mView.findViewById(R.id.messageTitle);
        this.title.setText(title);

        this.phone = mView.findViewById(R.id.phone);
        this.pin = mView.findViewById(R.id.pinNum);

        mView.findViewById(R.id.getPinNumBtn).setOnClickListener(v -> {
            this.myApiManager.requestPin(this.phone.getText().toString());
        });

        mView.findViewById(R.id.registerBtn).setOnClickListener(v -> {
            this.myApiManager.verifyPin(this.pin.getText().toString(), alertDialog);
        });

        mView.findViewById(R.id.cancelBTN).setOnClickListener(v -> {
            alertDialog.dismiss();

            /*Intent mainIntent = new Intent(context, Menuactivity.class);
            context.startActivity(mainIntent);*/
        });
    }

    public void show(){
        alertDialog.show();
    }

}
