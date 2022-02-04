package com.example.yarbi;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity {
    //ImageView flesh;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide navigation bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }
}