package com.example.smartmeterv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void call_ep(View view) {
        Intent intent = new Intent(getApplicationContext(),EnterPhoneNumber.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }
}