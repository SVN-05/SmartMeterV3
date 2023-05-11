package com.example.smartmeterv3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.smartmeterv3.HelperClass.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Dashboard_Screen extends AppCompatActivity {
    TextView d,u,note;
    int i=1;

    private final int ID_ABOUTUS = 1;
    private final int ID_SETLIMIT = 2;
    private final int ID_HOME = 3;
    private final int ID_PROFILE = 4;
    private final int ID_FAQ = 5;

    TextInputLayout modify_limit,modify_price;
    String sml,smp;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;
    int unit,aunit,acunit,acunit1,acunit2,acunit3,aprice;
    TextView dashboar_l,dashboard_p,p_percent,display_r,display_p;
    ProgressBar progressBar;

    LinearLayout parent_faq,parent_limit,parent_aboutus,parent_profile,parent_dashboard;

    TextView vfaq1,vfaq2,vfaq3,vfaq4, sfaq1,sfaq2,sfaq3,sfaq4;
    View v1,v2,v3,v4;
    ConstraintLayout bg1,bg2,bg3,bg4;
    Drawable up,down;
    int change=1;

    int netAmount = 0, rsPerUnit, ccSubsidy, netCurrentCharge, fixedCost, totalCurrentCharge, newSubsidy=250;

    TextInputLayout pnumber,pname,ppin;

    DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        d = findViewById(R.id.d_title);
        u = findViewById(R.id.username);
        note = findViewById(R.id.note);

        modify_limit = findViewById(R.id.ml1);
        modify_price = findViewById(R.id.minr);

        pnumber = findViewById(R.id.mphoneno);
        pname = findViewById(R.id.musername);
        ppin = findViewById(R.id.mpin);

        dashboar_l = findViewById(R.id.d_limit);
        dashboard_p = findViewById(R.id.d_price);
        p_percent = findViewById(R.id.set_percent);
        display_r = findViewById(R.id.dashboard_rl);
        display_p = findViewById(R.id.dashboard_rp);
        progressBar = findViewById(R.id.progress_bar);


        parent_dashboard  = findViewById(R.id.parent_dashboard_layout);
        parent_faq = findViewById(R.id.parent_faq_layout);
        parent_limit = findViewById(R.id.parent_limit_layout);
        parent_aboutus = findViewById(R.id.parent_aboutus_layout);
        parent_profile = findViewById(R.id.parent_profile_layout);
        parent_faq.setVisibility(View.GONE);
        parent_limit.setVisibility(View.GONE);
        parent_aboutus.setVisibility(View.GONE);
        parent_profile.setVisibility(View.GONE);
        parent_dashboard.setVisibility(View.GONE);

        vfaq1 = findViewById(R.id.view_faq1);
        vfaq2 = findViewById(R.id.view_faq2);
        vfaq3 = findViewById(R.id.view_faq3);
        vfaq4 = findViewById(R.id.view_faq4);
        sfaq1 = findViewById(R.id.faq_textview1);
        sfaq2 = findViewById(R.id.faq_textview2);
        sfaq3 = findViewById(R.id.faq_textview3);
        sfaq4 = findViewById(R.id.faq_textview4);
        bg1 = findViewById(R.id.fvbackground1);
        bg2 = findViewById(R.id.fvbackground2);
        bg3 = findViewById(R.id.fvbackground3);
        bg4 = findViewById(R.id.fvbackground4);
        v1 = findViewById(R.id.fview1);
        v2 = findViewById(R.id.fview2);
        v3 = findViewById(R.id.fview3);
        v4 = findViewById(R.id.fview4);
        clearviews();
        up = getDrawable(R.drawable.collapse_arrpw);
        down = getDrawable(R.drawable.expand);
        up.setTint(getColor(R.color.blue));
        down.setTint(getColor(R.color.blue));


        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();
        String spn = userDetails.get(SessionManager.KEY_PHONENUMBER);
        String name = userDetails.get(SessionManager.KEY_FULLNAME);
        String spin = userDetails.get(SessionManager.KEY_PIN);
        String sl = userDetails.get(SessionManager.KEY_LIMIT);
        String sp = userDetails.get(SessionManager.KEY_PRICE);

        dashboar_l.setText(sl);
        dashboard_p.setText(sp);

        pnumber.getEditText().setText(spn);
        pname.getEditText().setText(name);
        ppin.getEditText().setText(spin);

        modify_limit.getEditText().setText(sl);
        modify_price.getEditText().setText(sp);

        u.setText(name);

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ABOUTUS,R.drawable.aboutus));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_SETLIMIT,R.drawable.limit_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME,R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE,R.drawable.profile_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_FAQ,R.drawable.faq));


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String name;
                switch (item.getId()){
                    case ID_ABOUTUS: name="ABOUT US";
                        parent_faq.setVisibility(View.GONE);
                        parent_limit.setVisibility(View.GONE);
                        parent_profile.setVisibility(View.GONE);
                        parent_dashboard.setVisibility(View.GONE);
                        parent_aboutus.setVisibility(View.VISIBLE);
                        d.setText(name);
                        break;
                    case ID_SETLIMIT: name="SET_LIMIT";
                        parent_faq.setVisibility(View.GONE);
                        parent_aboutus.setVisibility(View.GONE);
                        parent_profile.setVisibility(View.GONE);
                        parent_dashboard.setVisibility(View.GONE);
                        parent_limit.setVisibility(View.VISIBLE);
                        d.setText(name);
                        break;
                    case ID_HOME: name="DASHBOARD";
                        parent_faq.setVisibility(View.GONE);
                        parent_limit.setVisibility(View.GONE);
                        parent_aboutus.setVisibility(View.GONE);
                        parent_profile.setVisibility(View.GONE);
                        parent_dashboard.setVisibility(View.VISIBLE);
                        d.setText(name);
                        break;
                    case ID_PROFILE: name="PROFILE";
                        parent_faq.setVisibility(View.GONE);
                        parent_limit.setVisibility(View.GONE);
                        parent_aboutus.setVisibility(View.GONE);
                        parent_dashboard.setVisibility(View.GONE);
                        parent_profile.setVisibility(View.VISIBLE);
                        d.setText(name);
                        break;
                    case ID_FAQ: name="FAQ";
                        d.setText(name);
                        parent_limit.setVisibility(View.GONE);
                        parent_aboutus.setVisibility(View.GONE);
                        parent_profile.setVisibility(View.GONE);
                        parent_dashboard.setVisibility(View.GONE);
                        parent_faq.setVisibility(View.VISIBLE);
                        break;

                }

            }
        });
        bottomNavigation.show(ID_HOME,true);

    }
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                handler.postDelayed(runnable, delay);
                getreadingfromdatabase();
                SessionManager sessionManager = new SessionManager(Dashboard_Screen.this);
                HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

                String p_number = userDetails.get(SessionManager.KEY_PHONENUMBER);
                String n = userDetails.get(SessionManager.KEY_FULLNAME);
                String pin = userDetails.get(SessionManager.KEY_PIN);
                String r = userDetails.get(SessionManager.KEY_READING);
                sml = modify_limit.getEditText().getText().toString();
                smp = modify_price.getEditText().getText().toString();
                if (sml.isEmpty() | smp.isEmpty()){
                    delay = 500;
                    modify_limit.getEditText().setText("0");
                    modify_price.getEditText().setText("0");
                    String esml,esmp;
                    esml = modify_limit.getEditText().getText().toString();
                    esmp = modify_price.getEditText().getText().toString();
                    sessionManager.createLoginSession(p_number, n, pin,r,esml,esmp);
                }
                else{
                    delay = 500;
                    changelistener();
                }

            }
        }, delay);
        super.onResume();
    }



    /**
     * A function
     * */
    private void changelistener() {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

        String l = userDetails.get(SessionManager.KEY_LIMIT);

        sml = modify_limit.getEditText().getText().toString();
        smp = modify_price.getEditText().getText().toString();

            unit = Integer.parseInt(sml);

            if (unit <= 100) {
                netAmount = 0;
                modify_price.getEditText().setText(String.valueOf(netAmount));

            }
            else if (unit <= 200){
                rsPerUnit = (int) 2.5;
                totalCurrentCharge = unit * rsPerUnit;
                ccSubsidy = unit-100;
                netCurrentCharge = totalCurrentCharge-ccSubsidy;
                fixedCost = 20;
                netAmount = netCurrentCharge + fixedCost - newSubsidy;
                modify_price.getEditText().setText(String.valueOf(netAmount));
            }
            else if (unit <= 500){
                rsPerUnit = 3;
                totalCurrentCharge = 500 + (unit - 200) * rsPerUnit;
                ccSubsidy = 50;
                netCurrentCharge = totalCurrentCharge-ccSubsidy;
                fixedCost = 30;
                netAmount = netCurrentCharge + fixedCost - newSubsidy;
                modify_price.getEditText().setText(String.valueOf(netAmount));
            }
            else {
                rsPerUnit = (int) 6.6;
                totalCurrentCharge = 1980 + (unit - 500) * rsPerUnit;
                netCurrentCharge = totalCurrentCharge;
                fixedCost = 50;
                netAmount = netCurrentCharge + fixedCost - newSubsidy;
                modify_price.getEditText().setText(String.valueOf(netAmount));
            }

        }

    private void clearviews() {
        sfaq1.setVisibility(View.GONE);
        sfaq2.setVisibility(View.GONE);
        sfaq3.setVisibility(View.GONE);
        sfaq4.setVisibility(View.GONE);
        bg1.setVisibility(View.GONE);
        bg2.setVisibility(View.GONE);
        bg3.setVisibility(View.GONE);
        bg4.setVisibility(View.GONE);
    }
    public void callfaq1(View view) {
        switch (change){
            case 1:vfaq1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse_arrpw, 0);
                vfaq1.setBackgroundResource(R.color.white);
                sfaq1.setVisibility(View.VISIBLE);
                bg1.setVisibility(View.VISIBLE);
                v1.setVisibility(View.GONE);
                break;
            case 2:vfaq1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                vfaq1.setBackgroundResource(R.color.white);
                sfaq1.setVisibility(View.GONE);
                bg1.setVisibility(View.GONE);
                v1.setVisibility(View.VISIBLE);
                break;
        }
        change++;
        if (change==3){
            change=1;
        }
    }
    public void callfaq2(View view) {
        switch (change){
            case 1:vfaq2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse_arrpw, 0);
                vfaq2.setBackgroundResource(R.color.white);
                sfaq2.setVisibility(View.VISIBLE);
                bg2.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
                break;
            case 2:vfaq2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                vfaq2.setBackgroundResource(R.color.white);
                sfaq2.setVisibility(View.GONE);
                bg2.setVisibility(View.GONE);
                v2.setVisibility(View.VISIBLE);
                break;
        }
        change++;
        if (change==3){
            change=1;
        }
    }
    public void callfaq3(View view) {
        switch (change){
            case 1:vfaq3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse_arrpw, 0);
                vfaq3.setBackgroundResource(R.color.white);
                sfaq3.setVisibility(View.VISIBLE);
                bg3.setVisibility(View.VISIBLE);
                v3.setVisibility(View.GONE);
                break;
            case 2:vfaq3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                vfaq3.setBackgroundResource(R.color.white);
                sfaq3.setVisibility(View.GONE);
                bg3.setVisibility(View.GONE);
                v3.setVisibility(View.VISIBLE);
                break;
        }
        change++;
        if (change==3){
            change=1;
        }
    }
    public void callfaq4(View view) {
        switch (change){
            case 1:vfaq4.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse_arrpw, 0);
                vfaq4.setBackgroundResource(R.color.white);
                sfaq4.setVisibility(View.VISIBLE);
                bg4.setVisibility(View.VISIBLE);
                v4.setVisibility(View.GONE);
                break;
            case 2:vfaq4.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                vfaq4.setBackgroundResource(R.color.white);
                sfaq4.setVisibility(View.GONE);
                bg4.setVisibility(View.GONE);
                v4.setVisibility(View.VISIBLE);
                break;
        }
        change++;
        if (change==3){
            change=1;
        }
    }

    public void limit_modify_func(View view) {
        SessionManager sessionManager = new SessionManager(Dashboard_Screen.this);
        HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

        String n = userDetails.get(SessionManager.KEY_FULLNAME);
        String pin = userDetails.get(SessionManager.KEY_PIN);
        String r = userDetails.get(SessionManager.KEY_READING);

        String p_number = userDetails.get(SessionManager.KEY_PHONENUMBER);

        String update_limit,update_price;
        update_limit = modify_limit.getEditText().getText().toString();
        update_price = modify_price.getEditText().getText().toString();
        reference.child(p_number).child("limit").setValue(update_limit);
        reference.child(p_number).child("price").setValue(update_price);

        sessionManager.createLoginSession(p_number, n, pin, r, update_limit, update_price);

        Toast.makeText(Dashboard_Screen.this, "Data Updated Successfully! ", Toast.LENGTH_LONG).show();
    }

    public void profile_modify_func(View view) {
        SessionManager sessionManager = new SessionManager(Dashboard_Screen.this);
        HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

        String p_number = userDetails.get(SessionManager.KEY_PHONENUMBER);
        String r = userDetails.get(SessionManager.KEY_READING);
        String l = userDetails.get(SessionManager.KEY_LIMIT);
        String p = userDetails.get(SessionManager.KEY_PRICE);

        String lu,lp;
        lu = pname.getEditText().getText().toString();
        lp = ppin.getEditText().getText().toString();

        u.setText(lu);
        reference.child(p_number).child("name").setValue(lu);
        reference.child(p_number).child("pin").setValue(lp);
        sessionManager.createLoginSession(p_number, lu, lp,r,l,p);


    }

    private void getreadingfromdatabase() {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

        String p_number = userDetails.get(SessionManager.KEY_PHONENUMBER);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkuser = reference.orderByChild("phone_number").equalTo(p_number);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String readings_value = snapshot.child(p_number).child("electricity_reading").getValue(String.class);
                    String limit = snapshot.child(p_number).child("limit").getValue(String.class);
                    String price = snapshot.child(p_number).child("price").getValue(String.class);


                    String spn = userDetails.get(SessionManager.KEY_PHONENUMBER);
                    String name = userDetails.get(SessionManager.KEY_FULLNAME);
                    String pin = userDetails.get(SessionManager.KEY_PIN);

                    sessionManager.createLoginSession(p_number, name, pin,readings_value,limit,price);

                    dashboar_l.setText(limit);
                    dashboard_p.setText(price);

                    display_r.setText(readings_value);

                    double r = Double.parseDouble(readings_value);
                    double v = Double.parseDouble(limit);
                    double t = r*100;
                    double t2 = t/v;
                    int it2 = (int) t2;
                    progressBar.setProgress(it2);
                    String sit1 = String.valueOf(it2);
                    String sit2 = sit1+"%";
                    p_percent.setText(sit2);

                    notification(readings_value,it2);

                    aunit = (int) r;
                    acunit =aunit-100;
                    if(aunit<=100)
                    {
                        aprice=0;
                        display_p.setText(String.valueOf(aprice));
                    }
                    else if(acunit>=1 && acunit<=199)
                    {
                        aprice= (int) (acunit*1.5);
                        display_p.setText(String.valueOf(aprice));
                    }
                    else if(acunit>=200 && acunit<=699)
                    {
                        acunit1=acunit-100;
                        aprice= ((100*2)+(acunit1*3));
                        display_p.setText(String.valueOf(aprice));
                    }
                    else if(acunit>=700)
                    {
                        acunit2= acunit-100;
                        acunit3=acunit2-300;
                        aprice= (int) ((100*3.5)+(300*4.6)+(acunit3*6.6));
                        display_p.setText(String.valueOf(aprice));
                    }

                } else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notification(String r,int p) {
        NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        if (i==1){
            if (p>=50) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n").setContentText(r).setSmallIcon(R.drawable.sm_icon).setAutoCancel(true).setContentText("You have completed 50% of your limit:" + r);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                managerCompat.notify(999, builder.build());
                i=2;
            }

        }
        else if (i==2){
            if (p>=75){

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n").setContentText(r).setSmallIcon(R.drawable.sm_icon).setAutoCancel(true).setContentText("You have completed 75% of your limit:" + r);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                    managerCompat.notify(999, builder.build());
                    i=3;
            }
        }
        else if (i==3){
            if (p>=90){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n").setContentText(r).setSmallIcon(R.drawable.sm_icon).setAutoCancel(true).setContentText("You have completed 90% of your limit:" + r);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                    managerCompat.notify(999, builder.build());
                    i=4;

            }
        }
        else if (i==4) {
            if (p == 100) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n").setContentText(r).setSmallIcon(R.drawable.sm_icon).setAutoCancel(true).setContentText("You have completed 100% of your limit:" + r);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(999, builder.build());
            i=5;
        }
        }

    }
}