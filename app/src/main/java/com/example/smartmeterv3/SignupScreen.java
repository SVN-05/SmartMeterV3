package com.example.smartmeterv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chaos.view.PinView;
import com.example.smartmeterv3.HelperClass.SessionManager;
import com.example.smartmeterv3.HelperClass.UserHelperClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupScreen extends AppCompatActivity {
    String sphone_number,pin,cpin,name;
    PinView pass,cpass;
    Button ps;
    TextInputLayout fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        sphone_number = getIntent().getStringExtra("pnumbertosignup");

        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
        fname = findViewById(R.id.enter_fname);
        ps = findViewById(R.id.psubmit);

        pin = pass.getText().toString();
        cpin = cpass.getText().toString();

        ps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.equals(cpin)){

                    name = fname.getEditText().getText().toString();
                    pin = pass.getText().toString();

                    Intent intent = new Intent(getApplicationContext(),Enter_Limit_Screen.class);
                    intent.putExtra("name_to_limit",name);
                    intent.putExtra("pin_to_limit",pin);
                    intent.putExtra("pnumber_to_limit",sphone_number);
                    startActivity(intent);
                }

                else{
                    pass.setError("New Pin is not same as in Confirmed Pin");
                    cpass.setError("Confirmed Pin is not same as in New Pin");
                }
            }
        });

    }

}