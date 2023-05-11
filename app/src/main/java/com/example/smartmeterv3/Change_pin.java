package com.example.smartmeterv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.smartmeterv3.HelperClass.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Change_pin extends AppCompatActivity {

    PinView cpass,ccpass;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        cpass = findViewById(R.id.change_pass);
        ccpass = findViewById(R.id.change_cpass);

    }

    public void call_login2_screen(View view) {
        String sp = cpass.getText().toString();
        String sp2 = ccpass.getText().toString();
        if (sp.equals(sp2)) {

            SessionManager sessionManager = new SessionManager(this);
            HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

            String phone= userDetails.get(SessionManager.KEY_PHONENUMBER);

            reference.child(phone).child("pin").setValue(cpass.getText().toString());

            Intent intent = new Intent(getApplicationContext(),Login_Screen2.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(Change_pin.this, "The new pin and the confirm pin are not same", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Change_pin.this,Login_Screen2.class);
        startActivity(intent);
        finish();
    }
}