package com.example.yarbi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView sign, forget;
    Button login;
    EditText email_login, pass_login;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide navigation bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email_login= findViewById(R.id.email_login);
        pass_login = findViewById(R.id.pass_login);
        progressBar = findViewById(R.id.progressBar);
        this.sign = findViewById(R.id.sign);
        sign.setOnClickListener(this);

        this.forget = findViewById(R.id.forget_pass);
        forget.setOnClickListener(this);

        this.login = findViewById(R.id.login);
        login.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign:
                startActivity(new Intent(this,SignupActivity.class));
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forget_pass:
                startActivity(new Intent(this,ForgetPasswordActivity.class));
                break;
        }

    }

    private void userLogin() {
        String email = email_login.getText().toString().trim();
        String pass = pass_login.getText().toString().trim();
    if (email.isEmpty()) {
        email_login.setError("Email is required!");
        email_login.requestFocus();
    }
    else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        email_login.setError("Please provide valid email!");
        email_login.requestFocus();

    } else if (pass.isEmpty()) {
        pass_login.setError("Password is required!");
        pass_login.requestFocus();

    } else if (pass.length() < 6) {
        pass_login.setError("Min password length should be 6 characters!");
        pass_login.requestFocus();
    }
    else {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "check your email!" , Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }
}


}