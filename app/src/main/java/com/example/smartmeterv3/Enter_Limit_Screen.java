package com.example.smartmeterv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.smartmeterv3.HelperClass.SessionManager;
import com.example.smartmeterv3.HelperClass.UserHelperClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Enter_Limit_Screen extends AppCompatActivity {
    Button submit;
    TextInputLayout slimit,sprice;
    String ereading = "0",name,pin,pnumber,limit,price,url_state="0";

    String sml,smp;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;
    int unit,cunit,cunit1,cunit2,cunit3,iprice;

    FirebaseDatabase rootnode;
    DatabaseReference reference;

    int netAmount = 0, rsPerUnit, ccSubsidy, netCurrentCharge, fixedCost, totalCurrentCharge, newSubsidy=250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_limit_screen);

        name = getIntent().getStringExtra("name_to_limit");
        pin = getIntent().getStringExtra("pin_to_limit");
        pnumber = getIntent().getStringExtra("pnumber_to_limit");

        slimit = findViewById(R.id.l1);
        sprice = findViewById(R.id.enter_inr);
        submit = findViewById(R.id.esubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limit = slimit.getEditText().getText().toString();
                price = sprice.getEditText().getText().toString();

                rootnode = FirebaseDatabase.getInstance();
                reference = rootnode.getReference("Users");

                UserHelperClass addnewuser = new UserHelperClass(pnumber,name,pin, ereading,limit, price,url_state);
                reference.child(pnumber).setValue(addnewuser);

                SessionManager sessionManager = new SessionManager(Enter_Limit_Screen.this);
                sessionManager.createLoginSession(pnumber, name, pin,ereading,limit,price);

                Intent intent = new Intent(getApplicationContext(),connect_iot.class);
                startActivity(intent);

            }
        });
    }
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                SessionManager sessionManager = new SessionManager(Enter_Limit_Screen.this);
                HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

                String p_number = userDetails.get(SessionManager.KEY_PHONENUMBER);
                String n = userDetails.get(SessionManager.KEY_FULLNAME);
                String pin = userDetails.get(SessionManager.KEY_PIN);
                String r = userDetails.get(SessionManager.KEY_READING);
                sml = slimit.getEditText().getText().toString();
                smp = sprice.getEditText().getText().toString();
                if (sml.isEmpty() | smp.isEmpty()){
                    delay = 1000;
                    slimit.getEditText().setText("0");
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

        sml = slimit.getEditText().getText().toString();
        smp = sprice.getEditText().getText().toString();

            unit = Integer.parseInt(sml);

            if (unit <= 100) {
                netAmount = 0;
                sprice.getEditText().setText(String.valueOf(netAmount));
            }
            else if (unit <= 200){
                rsPerUnit = (int) 2.5;
                totalCurrentCharge = unit * rsPerUnit;
                ccSubsidy = unit-100;
                netCurrentCharge = totalCurrentCharge-ccSubsidy;
                fixedCost = 20;
                netAmount = netCurrentCharge + fixedCost - newSubsidy;
                sprice.getEditText().setText(String.valueOf(netAmount));
            }
            else if (unit <= 500){
                rsPerUnit = 3;
                totalCurrentCharge = 500 + (unit - 200) * rsPerUnit;
                ccSubsidy = 50;
                netCurrentCharge = totalCurrentCharge-ccSubsidy;
                fixedCost = 30;
                netAmount = netCurrentCharge + fixedCost - newSubsidy;
                sprice.getEditText().setText(String.valueOf(netAmount));
            }
            else {
                rsPerUnit = (int) 6.6;
                totalCurrentCharge = 1980 + (unit - 500) * rsPerUnit;
                netCurrentCharge = totalCurrentCharge;
                fixedCost = 50;
                netAmount = netCurrentCharge + fixedCost - newSubsidy;
                sprice.getEditText().setText(String.valueOf(netAmount));
            }

        }

    }