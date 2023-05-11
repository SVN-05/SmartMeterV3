package com.example.smartmeterv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smartmeterv3.HelperClass.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EnterPhoneNumber extends AppCompatActivity {
    TextInputLayout pn;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);
        pn = findViewById(R.id.phone_number);
        next = findViewById(R.id.send_pnumber);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = pn.getEditText().getText().toString().trim();
                String c = ("\\d{10}");
                if (check.matches(c)){
                    send_number_to_otp_Screen();
                }
                else{
                    pn.setError("Enter Valid Mobile Number");
                }
            }
        });


    }

    private void send_number_to_otp_Screen() {

        String spn = "+91"+pn.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkuser = reference.orderByChild("phone_number").equalTo(spn);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String sspn = "+91"+pn.getEditText().getText().toString().trim();
                    String p_numberfromdb = snapshot.child(sspn).child("phone_number").getValue(String.class);

                    if (Objects.equals(p_numberfromdb, spn)){

                        Intent intent = new Intent(getApplicationContext(), Otp_Screen3.class);
                        String sssspn = "+91"+pn.getEditText().getText().toString().trim();
                        intent.putExtra("pnumbertootp",sssspn);
                        startActivity(intent);

                    }
                    else {


                    }
                }
                else {
                    String sssspn = "+91"+pn.getEditText().getText().toString().trim();
                    Intent intent = new Intent(getApplicationContext(),OtpScreen.class);
                    intent.putExtra("pnumbertootp",sssspn);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}