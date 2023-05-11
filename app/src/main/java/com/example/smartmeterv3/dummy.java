package com.example.smartmeterv3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.example.smartmeterv3.HelperClass.SessionManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

public class dummy extends AppCompatActivity {
    TextInputLayout slimit,sprice;
    String limit,price;

    String sml,smp;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;
    int units,tprice;
    int netAmount = 0, rsPerUnit, ccSubsidy, netCurrentCharge, fixedCost, totalCurrentCharge, newSubsidy=250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        slimit = findViewById(R.id.dl1);
        sprice = findViewById(R.id.dinr);

        limit = slimit.getEditText().getText().toString();
        price = sprice.getEditText().getText().toString();
    }
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                SessionManager sessionManager = new SessionManager(dummy.this);
                HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

                String p_number = userDetails.get(SessionManager.KEY_PHONENUMBER);
                String n = userDetails.get(SessionManager.KEY_FULLNAME);
                String pin = userDetails.get(SessionManager.KEY_PIN);
                String r = userDetails.get(SessionManager.KEY_READING);
                sml = slimit.getEditText().getText().toString();
                smp = sprice.getEditText().getText().toString();
                if (sml.isEmpty() | smp.isEmpty()){
                    delay = 3000;
                    slimit.getEditText().setText("100");
                    sprice.getEditText().setText("0");
                    String esml,esmp;
                    esml = slimit.getEditText().getText().toString();
                    esmp = sprice.getEditText().getText().toString();
                    sessionManager.createLoginSession(p_number, n, pin,r,esml,esmp);
                }
                else{
                    delay = 1000;
                    changelistener();
                }

            }
        }, delay);
        super.onResume();
    }


    private void changelistener() {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

        String l = userDetails.get(SessionManager.KEY_LIMIT);
        String p = userDetails.get(SessionManager.KEY_PRICE);

        sml = slimit.getEditText().getText().toString();
        smp = sprice.getEditText().getText().toString();

        if (!sml.equals(l)){

            units = Integer.parseInt(sml);

            if (units <= 100) {
                netAmount = 0;
                slimit.getEditText().setText(String.valueOf(netAmount));
            }
            else if (units <= 200){
                rsPerUnit = (int) 2.5;
                totalCurrentCharge = units * rsPerUnit;
                ccSubsidy = units-100;
                netCurrentCharge = totalCurrentCharge-ccSubsidy;
                fixedCost = 20;
                netAmount = netCurrentCharge + fixedCost - newSubsidy;
                slimit.getEditText().setText(String.valueOf(netAmount));
            }
            else if (units <= 500){
                rsPerUnit = 3;
                totalCurrentCharge = 500 + (units - 200) * rsPerUnit;
                ccSubsidy = 50;
                netCurrentCharge = totalCurrentCharge-ccSubsidy;
                fixedCost = 30;
                netAmount = netCurrentCharge + fixedCost - newSubsidy;
                slimit.getEditText().setText(String.valueOf(netAmount));
            }
            else {
                rsPerUnit = (int) 6.6;
                totalCurrentCharge = 1980 + (units - 500) * rsPerUnit;
                netCurrentCharge = totalCurrentCharge;
                fixedCost = 50;
                netAmount = netCurrentCharge + fixedCost - newSubsidy;
                slimit.getEditText().setText(String.valueOf(netAmount));
            }

        }
        else if (!smp.equals(p)){
            tprice = Integer.parseInt(smp);
            slimit.getEditText().setText(String.valueOf(price2Units(tprice)));
        }

    }
    private double price2Units(int price){
        double netUnits = 0;
        if (price < 22){
            netUnits = 100;
        }
        else if (price <= 170){
            netUnits = (2 * price + 260) / 3;
        }
        else if (price < 233){
            netUnits = price2Units(round(price, 170, 233));
        }
        else if (price <= 1130){
            netUnits = (price + 370) / 3;
        }
        else if (price < 1787){
            netUnits = price2Units(round(price, 1130, 1787));
        }
        else {
            netUnits = ((5 * price) + 7600) / 33;
        }
        return Math.floor(netUnits);
    }

    private int round(double value, int start, int end){
        int num = (start + end) / 2;
        if (value <= num){
            return start;
        }
        else {
            return end;
        }
    }
}