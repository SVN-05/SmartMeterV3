package com.example.smartmeterv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.smartmeterv3.HelperClass.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class app_open_anim extends AppCompatActivity {
    Timer timer;

    SharedPreferences onboarding;

    String phone="",n="",p="",reading="0",limit="",price="",state;
    HashMap<String,String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_open_anim);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                onboarding = getSharedPreferences("onboardingscreen",MODE_PRIVATE);

                boolean isfirsttime = onboarding.getBoolean("firsttime",true);

                if (isfirsttime){

                    SharedPreferences.Editor editor = onboarding.edit();
                    editor.putBoolean("firsttime",false);
                    editor.commit();
                    loop2();

                    Intent intent = new Intent(app_open_anim.this, Onboarding_Screen.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    loop_Solution();
                }
            }
        },3000);

    }

    private void loop2() {
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.createLoginSession(phone, n, p,reading,limit,price);
    }

    private void loop_Solution() {
        SessionManager sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUserDetailsFromSessinon();

        phone= userDetails.get(SessionManager.KEY_PHONENUMBER);
        n = userDetails.get(SessionManager.KEY_FULLNAME);
        p = userDetails.get(SessionManager.KEY_PIN);

        Intent intent;
        if (phone.isEmpty()) {
           intent = new Intent(app_open_anim.this, Login_Screen.class);
        }
        else {
            intent = new Intent(getApplicationContext(), Login_Screen2.class);
        }
        startActivity(intent);
    }
}