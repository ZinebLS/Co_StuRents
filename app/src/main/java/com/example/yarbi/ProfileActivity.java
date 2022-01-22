package com.example.yarbi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    CardView card, logout;
    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide navigation bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        this.card = findViewById(R.id.card_home);
        card.setOnClickListener(this);

        this.logout = findViewById(R.id.card_logout);
        logout.setOnClickListener(this);
        //
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView fullNameTextView = findViewById(R.id.full_name);
        final EditText nameEditText = findViewById(R.id.name);
        final EditText phoneEditText = findViewById(R.id.num);
        final EditText emailEditText = findViewById(R.id.email);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              User userProfile = snapshot.getValue(User.class);

              if(userProfile != null)
              {
                  String fullName = userProfile.name;
                  String phone = userProfile.phone;
                  String email = userProfile.email;

                  fullNameTextView.setText(fullName);
                  nameEditText.setText(fullName);
                  phoneEditText.setText(phone);
                  emailEditText.setText(email);


              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.card_home:
                    startActivity(new Intent(this,HomeActivity.class));
                    break;
                case R.id.card_logout:
                    logout();
                    break;
            }

        }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,LoginActivity.class));
    }
}