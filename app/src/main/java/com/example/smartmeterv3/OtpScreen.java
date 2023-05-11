package com.example.smartmeterv3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class OtpScreen extends AppCompatActivity {
    String  pnumberfromepn,o,ch;
    PinView otp;
    Button resend,submit;

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private String mVerificationId;
    private static final String TAG ="MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        pnumberfromepn = getIntent().getStringExtra("pnumbertootp");
        ch = getIntent().getStringExtra("checker");

        otp = findViewById(R.id.enter_otp);

        o = otp.getText().toString();
        resend = findViewById(R.id.resendotp);
        submit = findViewById(R.id.button_verify);

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
                Toast.makeText(OtpScreen.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"OncodeSent:"+verificationId);

                mVerificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                Toast.makeText(OtpScreen.this, "Verification Code Sent...", Toast.LENGTH_SHORT).show();

            }
        };
        startphonenumberverification(pnumberfromepn);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(pnumberfromepn)){
                    Toast.makeText(OtpScreen.this,"Code will be resent, Please wait..,",Toast.LENGTH_SHORT).show();
                }
                else {
                    resendverificationcode(pnumberfromepn, forceResendingToken);
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String so = otp.getText().toString().trim();
                if (TextUtils.isEmpty(so)){
                    Toast.makeText(OtpScreen.this, "Please Enter Verification Code...", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(getApplicationContext(), SignupScreen.class);
                            intent.putExtra("pnumbertosignup", pnumberfromepn);
                            startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(OtpScreen.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(OtpScreen.this,Login_Screen.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OtpScreen.this,Login_Screen.class);
        startActivity(intent);
    }
}