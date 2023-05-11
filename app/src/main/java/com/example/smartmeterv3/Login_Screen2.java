package com.example.smartmeterv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.smartmeterv3.HelperClass.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class Login_Screen2 extends AppCompatActivity {
    String p_number, ulpin, ullpin;
    Button signin;
    PinView umpin;
    TextView fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen2);

        fp = findViewById(R.id.fpass);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

        p_number= userDetails.get(SessionManager.KEY_PHONENUMBER);


        signin = findViewById(R.id.verify_user);
        umpin = findViewById(R.id.llpass);

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Otp_Screen2.class);
                intent.putExtra("pnumbertootp",p_number);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ulpin = umpin.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                Query checkuser = reference.orderByChild("phone_number").equalTo(p_number);

                checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ullpin = umpin.getText().toString();
                            String pinfromdb = snapshot.child(p_number).child("pin").getValue(String.class);
                            String name = snapshot.child(p_number).child("name").getValue(String.class);
                            String pin = snapshot.child(p_number).child("pin").getValue(String.class);
                            String readings_value = snapshot.child(p_number).child("electricity_reading").getValue(String.class);
                            String limit_value = snapshot.child(p_number).child("limit").getValue(String.class);
                            String price = snapshot.child(p_number).child("price").getValue(String.class);

                            if (Objects.equals(pinfromdb, ulpin)) {
                                sessionManager.createLoginSession(p_number, name, pin,readings_value,limit_value,price);

                                Intent intent = new Intent(getApplicationContext(), Dashboard_Screen.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(Login_Screen2.this, "LPIN IS INCORRECT", Toast.LENGTH_LONG).show();
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }
}