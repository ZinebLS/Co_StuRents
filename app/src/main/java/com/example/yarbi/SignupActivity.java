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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    Button signup;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText editTextEmail,editTextPassword,editTextPhone, editTextName, editTextConfirPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide navigation bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email_sign);
        editTextPassword = findViewById(R.id.pass_sign);
        editTextName = findViewById(R.id.nom);
        editTextPhone = findViewById(R.id.phone);
        editTextConfirPassword = findViewById(R.id.confirm_pass);
        progressBar = findViewById(R.id.progressBar);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String pass = editTextPassword.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String confirm_pass = editTextConfirPassword.getText().toString().trim();
                if (name.isEmpty()) {
                    editTextName.setError("Full name is required!");
                    editTextName.requestFocus();

                } else if (phone.isEmpty()) {
                    editTextPhone.setError("Phone number is required!");
                    editTextPhone.requestFocus();

                } else if (email.isEmpty()) {
                    editTextEmail.setError("Email is required!");
                    editTextEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please provide valid email!");
                    editTextEmail.requestFocus();

                } else if (pass.isEmpty()) {
                    editTextPassword.setError("Password is required!");
                    editTextPassword.requestFocus();

                } else if (pass.length() < 6) {
                    editTextPassword.setError("Min password length should be 6 characters!");
                    editTextPassword.requestFocus();
                } else if (confirm_pass.isEmpty()) {
                    editTextConfirPassword.setError("Confirm Password is required!");
                    editTextConfirPassword.requestFocus();

                } else if (confirm_pass.length() < 6) {
                    editTextConfirPassword.setError("Min password length should be 6 characters!");
                    editTextConfirPassword.requestFocus();
                } else if (!pass.equals(confirm_pass)) {
                    editTextConfirPassword.setError("Password Not match Both field");
                    editTextConfirPassword.requestFocus();
                } else
                 {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, pass).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User( name, phone,  email,  pass);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SignupActivity.this, "User has been registred successfully", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(SignupActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(SignupActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                    });
                }
            }
        });
    }
}