package com.example.smartmeterv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
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

public class connect_iot extends AppCompatActivity {
    Button connect;
    String p_number,url_state;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_iot);

        connect = findViewById(R.id.csubmit);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isconnected(connect_iot.this)){
                    showcustomDialog();
                }
                else if (connect.getText().toString().equals("CONNECT")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.4.1/"));
                    startActivity(browserIntent);

                }
                else {
                    Intent intent = new Intent(getApplicationContext(),Login_Screen2.class);
                    startActivity(intent);
                }

            }
        });
    }

    private boolean isconnected(connect_iot connect_iot) {

        ConnectivityManager connectivityManager = (ConnectivityManager) connect_iot.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wificon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ((wificon!=null && wificon.isConnected())){
            return true;

        }
        else {
            return false;
        }
    }

    private void showcustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(connect_iot.this);
        builder.setMessage("Please connect to the WIFI to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(getApplicationContext(),connect_iot.class));
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
    }
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                getstatevalue();
            }
        }, delay);
        super.onResume();
    }
    private void getstatevalue() {

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> userDetails = sessionManager.getUserDetailsFromSessinon();

        p_number= userDetails.get(SessionManager.KEY_PHONENUMBER);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkuser = reference.orderByChild("phone_number").equalTo(p_number);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    url_state = snapshot.child(p_number).child("url_state").getValue(String.class);
                    if (url_state.equals("1")) {
                        connect.setText("FINISH");
                    }

                } else if (url_state.equals("0")) {
                    connect.setText("CONNECT");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}