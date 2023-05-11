package com.example.smartmeterv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText phone_number,otp;
    TextView resend;
    Button submit,send;
    String p,o;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private String mVerificationId;
    private static final String TAG ="MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone_number = findViewById(R.id.phone);
        otp = findViewById(R.id.otp);
        p = phone_number.getText().toString().trim();
        o = otp.getText().toString().trim();

        resend = findViewById(R.id.resend);
        submit = findViewById(R.id.submit);
        send = findViewById(R.id.send);

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
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"OncodeSent:"+verificationId);

                mVerificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                Toast.makeText(MainActivity.this, "Verification Code Sent...", Toast.LENGTH_SHORT).show();

            }
        };
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(p)){
                    Toast.makeText(MainActivity.this,"Code will be resent, wait..,",Toast.LENGTH_SHORT).show();
                }
                else {
                    resendverificationcode(p, forceResendingToken);
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String so = otp.getText().toString().trim();
                if (TextUtils.isEmpty(so)){
                    Toast.makeText(MainActivity.this, "Please Enter Verification Code...", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifyphonenumber(mVerificationId,so);
                }

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sp = phone_number.getText().toString().trim();
                if (TextUtils.isEmpty(sp)){
                    Toast.makeText(MainActivity.this,"Please Enter Phone Number...",Toast.LENGTH_SHORT).show();
                }
                else {
                    startphonenumberverification(sp);
                }

            }
        });
    }

    private void startphonenumberverification(String p) {
        pd.setMessage("Verifying Phone Number");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(p)
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
                        Toast.makeText(MainActivity.this, "Logged In As"+phone, Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void resendverificationcode(String p, PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("Resending code...");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(p)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mcallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}