package com.example.ayushmittal.records;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class welcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        ObjectAnimator animator= ObjectAnimator.ofFloat(findViewById(R.id.anim),"translationY",50f);
        animator.setDuration(2000);
        animator.start();
        ObjectAnimator animator1=ObjectAnimator.ofFloat(findViewById(R.id.txt),"translationY",+50f);
        animator1.setDuration(2000);
        animator1.start();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 123);

        }
        else{
            start();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            Snackbar.make(getCurrentFocus(),"GRANT PERMISSION TO ACCES APP RESTART APPLICATION",Snackbar.LENGTH_LONG).show();

        }
        else{
            start();
        }


    }


    void start(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(FirebaseAuth.getInstance().getCurrentUser()== null){
                    i=new Intent(welcomeScreen.this,login.class);}
                else{
                    i=new Intent(welcomeScreen.this,home.class);
                }

                startActivity(i);
                finish();

            }
        },2500);

    }
}
