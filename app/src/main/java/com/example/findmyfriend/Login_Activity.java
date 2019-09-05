package com.example.findmyfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Login_Activity extends AppCompatActivity {

    CountryCodePicker ccp;
    String phn_no,country_code,TAG = "log in page",user_phn_no,mVerificationId;
    EditText phn_no_text,editText_code;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    ConstraintLayout verification_layout,confirmation_dlg;
    Animation animation_topDown,animation_downtotop;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    boolean exist = false;
    Intent intent_sign_up,intent_map;
    TextView textView_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        animation_topDown = AnimationUtils.loadAnimation(this,R.anim.toptodown);
        animation_downtotop = AnimationUtils.loadAnimation(this,R.anim.downtotop);
        editText_code = findViewById(R.id.EditText5);
        textView_error = findViewById(R.id.textView3);
        intent_sign_up = new Intent(this, SignUp_activity.class);
        confirmation_dlg = findViewById(R.id.confirmation_dlg);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ccp = findViewById(R.id.spinner);
        phn_no_text = findViewById(R.id.editText);
        verification_layout = findViewById(R.id.verification_layout_id);
        country_code = ccp.getSelectedCountryCode();
        intent_map = new Intent(this,MapsActivity.class);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //verification finished
                //Toast.makeText(getApplicationContext(),"Verification Completed!",Toast.LENGTH_LONG).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
                startActivity(intent_map);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: "+e.getMessage());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Toast.makeText(getApplicationContext(),"Invalid Phone number!",Toast.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Toast.makeText(getApplicationContext(),"Something went wrong! :(",Toast.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                if (exist) {
                    Toast.makeText(getApplicationContext(), "Verification Code sent!", Toast.LENGTH_LONG).show();
                }
                else {

                }
                //update UI
                // [END_EXCLUDE]
            }

        };


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void next(View view){
        phn_no = phn_no_text.getText().toString().trim();
        if (phn_no != null && !phn_no.isEmpty()) {
            if (country_code.charAt(country_code.length() - 1) == phn_no.charAt(0)) {
                Log.d(TAG, "next: deleted " + phn_no.charAt(0));
                phn_no.replace(String.valueOf(phn_no.charAt(0)), "");
                Log.d(TAG, "next: deleted " + phn_no.charAt(0));

            }
            user_phn_no = "+"+country_code + phn_no;
            intent_sign_up.putExtra("phone_no",user_phn_no);
            Log.d(TAG, "next: phone no " + phn_no);
            Log.d(TAG, "next: country code " + country_code);
            Log.d(TAG, "next: " + user_phn_no);
            databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(user_phn_no)){
                        exist = true;
                    }
                    else {
                        exist = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
                    return;
                }
            });


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    user_phn_no,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks

            if(!exist) {
                confirmation_dlg.setVisibility(View.VISIBLE);
                // [END start_phone_auth]
            }
            else {
                verification_layout.setAnimation(animation_topDown);
                //phone no exist in database
            }

        }
        else {
            //if phone no is empty
            textView_error.setText("Phone No is EMPTY!");
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //startActivity(intent_sign_up);
                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            // [END_EXCLUDE]
                            Toast.makeText(getApplicationContext(),"Verification Completed!",Toast.LENGTH_LONG).show();
                            //startActivity(intent_map);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                //mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                                Toast.makeText(getApplicationContext(),"Something stopped verification process!",Toast.LENGTH_SHORT).show();
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    public void back_login(View view){
        confirmation_dlg.setVisibility(View.GONE);
    }
    public void go_next(View view){
        confirmation_dlg.setVisibility(View.GONE);
        verification_layout.setVisibility(View.VISIBLE);
        verification_layout.setAnimation(animation_topDown);


    }

    public void done_login(View view){
        String code = editText_code.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
        if(!exist) {
            startActivity(intent_sign_up);
        }
        else {
            startActivity(intent_map);
        }
    }

    public void change_phone_no(View view){
        verification_layout.setAnimation(animation_downtotop);
        verification_layout.setVisibility(View.GONE);
    }

    public void resend_code(View view){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                user_phn_no,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                mResendToken);
    }
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        //Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();

    }
}
