package com.ikhokha.techcheck.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ikhokha.techcheck.R;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditTExt, passwordEditText;
    private Button loginButton;
    private FirebaseAuth firebaseAuthentication;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        firebaseAuthentication = FirebaseAuth.getInstance();

        //Initialize Authentication listener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //User is Signed in
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }else{
                    //User is signed out
                }
            }
        };

        usernameEditTExt = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.email_sign_in_button);
        progressBar = findViewById(R.id.login_progress);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        firebaseAuthentication.addAuthStateListener(authStateListener);
    }

    private void startLogin() {
        String emailStr = usernameEditTExt.getText().toString();
        String passwordStr = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr)) {

        }else{
            firebaseAuthentication.signInWithEmailAndPassword(emailStr, passwordStr)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(this.getClass().getName(), "signInWithEmail:success");
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(this.getClass().getName(), "signInWithEmail:failure", task.getException());

                            }
                            // ...
                        }
                    });
        }
    }
}
