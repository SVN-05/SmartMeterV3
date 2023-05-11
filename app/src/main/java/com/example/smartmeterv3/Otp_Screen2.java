package com.example.smartmeterv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Otp_Screen2 extends AppCompatActivity {
    String  pnumberfromepn,o,ch;
    PinView otp;
    Button resend2,submit2;

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private String mVerificationId;
    private static final String TAG ="MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen2);

        pnumberfromepn = getIntent().getStringExtra("pnumbertootp");
        ch = getIntent().getStringExtra("checker");

        otp = findViewById(R.id.enter_otp2);

        o = otp.getText().toString();
        resend2 = findViewById(R.id.resendotp2);
        submit2 = findViewById(R.id.button_verify2);

        firebaseAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setTitle("Please Wait...");
        pd.setCanceledOnTouchOutside(false);
        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInwithPhoneauthcredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pd.dismiss();
                Toast.makeText(Otp_Screen2.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"OncodeSent:"+verificationId);

                mVerificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                Toast.makeText(Otp_Screen2.this, "Verification Code Sent...", Toast.LENGTH_SHORT).show();

            }
        };
        startphonenumberverification(pnumberfromepn);
        resend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(pnumberfromepn)){
                    Toast.makeText(Otp_Screen2.this,"Code will be resent, Please wait..,",Toast.LENGTH_SHORT).show();
                }
                else {
                    resendverificationcode(pnumberfromepn, forceResendingToken);
                }

            }
        });

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String so = otp.getText().toString().trim();
                if (TextUtils.isEmpty(so)){
                    Toast.makeText(Otp_Screen2.this, "Please Enter Verification Code...", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifyphonenumber(mVerificationId,so);
                }

            }
        });
    }

    private void startphonenumberverification(String pnumberfromepn) {
        pd.setMessage("Verifying Phone Number");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(pnumberfromepn)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mcallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyphonenumber(String mVerificationId, String o) {
        pd.setMessage("Verifying Code");
        pd.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,o);
        signInwithPhoneauthcredential(credential);
    }

    private void signInwithPhoneauthcredential(PhoneAuthCredential credential) {
        pd.setMessage("Logging In");

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        pd.dismiss();

                            String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                            Toast.makeText(Otp_Screen2.this, "Logged In As" + phone, Toast.LENGTH_SHORT).show();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                            Query checkuser = reference.orderByChild("phone_number").equalTo(pnumberfromepn);

                            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String pn = snapshot.child(pnumberfromepn).child("phone_number").getValue(String.class);

                                        if (Objects.equals(pn, pnumberfromepn)) {

                                            Intent intent = new Intent(getApplicationContext(), Change_pin.class);
                                            startActivity(intent);

                                        } else {


                                        }
                                    } else {

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Otp_Screen2.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void resendverificationcode(String pnumberfromepn, PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("Resending code...");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(pnumberfromepn)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mcallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void call_welcome(View view) {
        Intent intent = new Intent(Otp_Screen2.this,Login_Screen.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Otp_Screen2.this,Login_Screen.class);
        startActivity(intent);
    }
}