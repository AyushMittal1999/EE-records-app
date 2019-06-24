package com.example.ayushmittal.records;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class login_fragment extends Fragment {
    private EditText usernmae,password;
    private Button button;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_login_fragment,container,false);
        // Inflate the layout for this
       usernmae=(EditText)v.findViewById(R.id.user_name);
        password=(EditText)v.findViewById(R.id.pass);
        button=(Button)v.findViewById(R.id.login);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setTag("Authenticating...");
                    button.setEnabled(false);

                isValid(usernmae.getText().toString(),password.getText().toString());


            }
        });


return v;
    }

    private void isValid(final String user, final String pass) {
        if (isPassValid(pass)) {
            mAuth = FirebaseAuth.getInstance();

            
            mAuth.signInWithEmailAndPassword(user.trim(), pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(getActivity(), "login succesful", Toast.LENGTH_SHORT).show();
                        changeactivity();
                        button.setEnabled(true);
                    } else {
                        // If sign in fails, display a message to the user.
                        progressBar.setVisibility(View.GONE);
                        button.setEnabled(true);
                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else {
            progressBar.setVisibility(View.GONE);
            button.setEnabled(true);
        }
    }

    private void changeactivity(){
        Intent i=new Intent(getActivity(),home.class);
        Toast.makeText(getActivity(),"Please Sync once ",Toast.LENGTH_SHORT).show();

        startActivity(i);
        getActivity().finish();

    }



    public boolean isPassValid(String p){
        if(p.length()<6){
            Toast.makeText(getActivity(),"Password must be greater than 5",Toast.LENGTH_SHORT).show();
            password.setFocusable(true);

            return false;
        }
        return true;
    }


}
