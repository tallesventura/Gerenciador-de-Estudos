package com.talles.android.daa_ti_tallesoliveira.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.talles.android.daa_ti_tallesoliveira.R;

public class LoginActivity extends AppCompatActivity {

    private Button btnSignIn;
    private Button btnSignUp;
    private EditText txtEmail;
    private EditText txtPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static final int SHORT_PASSWORD = 1;
    public static final int INVALID_PASSWORD = 2;
    public static final int INCORRECT_PASSWORD = 3;
    public static final int INCORRECT_EMAIL = 4;
    public static final int INVALID_EMAIL = 5;
    public static final int PASSWORD_OK = 6;
    public static final int EMAIL_OK = 7;



    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(LoginActivity.this,user.getEmail(),Toast.LENGTH_SHORT);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                final int flagEmail = validateEmail(email);
                final int flagPassword = validatePassword(password);

                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setTitle("Verificando credenciais");
                pd.setCancelable(false);
                pd.show();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                pd.dismiss();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());

                                    switch (flagEmail){
                                        case INVALID_EMAIL:
                                            Toast.makeText(LoginActivity.this, R.string.error_invalid_email,Toast.LENGTH_SHORT).show();
                                            break;
                                        case INCORRECT_EMAIL:
                                            Toast.makeText(LoginActivity.this, R.string.error_incorrect_email,Toast.LENGTH_SHORT).show();
                                            break;
                                    }

                                    switch (flagEmail){
                                        case INVALID_PASSWORD:
                                            Toast.makeText(LoginActivity.this, R.string.error_invalid_password,Toast.LENGTH_SHORT).show();
                                            break;
                                        case INCORRECT_PASSWORD:
                                            Toast.makeText(LoginActivity.this, R.string.error_incorrect_password,Toast.LENGTH_SHORT).show();
                                            break;
                                        case SHORT_PASSWORD:
                                            Toast.makeText(LoginActivity.this, R.string.error_short_password,Toast.LENGTH_SHORT).show();
                                            break;
                                    }

                                }else{
                                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(i);
                                }

                                // ...
                            }
                        });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private int validateEmail(String email){

        if(!email.contains("@") || !email.contains(".")){
            return INVALID_EMAIL;
        }else{
            return EMAIL_OK;
        }
    }

    public int validatePassword(String password){

        if(password.length() == 0 || password.contains(" ")){
            return  INVALID_PASSWORD;
        }else if(password.length() < 6){
            return SHORT_PASSWORD;
        }else{
            return PASSWORD_OK;
        }
    }
}
