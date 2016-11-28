package com.talles.android.daa_ti_tallesoliveira.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.talles.android.daa_ti_tallesoliveira.R;

import static com.talles.android.daa_ti_tallesoliveira.activity.LoginActivity.EMAIL_OK;
import static com.talles.android.daa_ti_tallesoliveira.activity.LoginActivity.INCORRECT_EMAIL;
import static com.talles.android.daa_ti_tallesoliveira.activity.LoginActivity.INCORRECT_PASSWORD;
import static com.talles.android.daa_ti_tallesoliveira.activity.LoginActivity.INVALID_EMAIL;
import static com.talles.android.daa_ti_tallesoliveira.activity.LoginActivity.INVALID_PASSWORD;
import static com.talles.android.daa_ti_tallesoliveira.activity.LoginActivity.PASSWORD_OK;
import static com.talles.android.daa_ti_tallesoliveira.activity.LoginActivity.SHORT_PASSWORD;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private Button btnFinish;
    private EditText txtEmail;
    private EditText txtPassword;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnFinish = (Button) findViewById(R.id.sign_up_btnFinish);
        txtEmail = (EditText) findViewById(R.id.sign_up_email);
        txtPassword = (EditText) findViewById(R.id.sign_up_password);

        mAuth = FirebaseAuth.getInstance();



        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                final int flagEmail = validateEmail(email);
                final int flagPassword = validatePassword(password);

                final ProgressDialog pd = new ProgressDialog(SignUpActivity.this);
                pd.setTitle("Processando Cadastro.");
                pd.setCancelable(false);
                pd.show();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        pd.dismiss();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            switch (flagEmail){
                                case INVALID_PASSWORD:
                                    Toast.makeText(SignUpActivity.this, R.string.error_invalid_password,Toast.LENGTH_SHORT).show();
                                    break;
                                case INCORRECT_PASSWORD:
                                    Toast.makeText(SignUpActivity.this, R.string.error_incorrect_password,Toast.LENGTH_SHORT).show();
                                    break;
                                case SHORT_PASSWORD:
                                    Toast.makeText(SignUpActivity.this, R.string.error_short_password,Toast.LENGTH_SHORT).show();
                                    break;
                            }

                            switch (flagEmail){
                                case INVALID_EMAIL:
                                    Toast.makeText(SignUpActivity.this, R.string.error_invalid_email,Toast.LENGTH_SHORT).show();
                                    break;
                                case INCORRECT_EMAIL:
                                    Toast.makeText(SignUpActivity.this, R.string.error_incorrect_email,Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        }else{
                            Toast.makeText(SignUpActivity.this, R.string.sign_up_success,
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    }
                });
            }
        });

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
