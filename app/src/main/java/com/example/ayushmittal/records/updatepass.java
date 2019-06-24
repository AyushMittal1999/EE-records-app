package com.example.ayushmittal.records;

import android.app.ProgressDialog;
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

public class updatepass extends Fragment {

    private EditText usernmae, password, newpass;
    private Button button;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_updatepass, container, false);
        usernmae = (EditText) v.findViewById(R.id.user_name);
        password = (EditText) v.findViewById(R.id.pass);
        newpass= (EditText) v.findViewById(R.id.newpass);
        button = (Button) v.findViewById(R.id.updatepass);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setTag("Authenticating...");

               if(isPassValid(password.getText().toString())&&isPassValid(newpass.getText().toString())){

                   isValid(usernmae.getText().toString(),password.getText().toString(),newpass.getText().toString());
               }


            }
        });


        return v;
    }

    private void isValid(final String user, final String pass, final String newpass) {

            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                Toast.makeText(getActivity(),"Password Updated succesfully",Toast.LENGTH_SHORT).show();
                                changeactivity();
                                progressBar.setVisibility(View.GONE);}

                                else{
                                    Toast.makeText(getActivity(),"Unable to update password",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });

                    } else {
                        // If sign in fails, display a message to the user.
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

    private void changeactivity(){
        Intent i=new Intent(getActivity(),home.class);
        Toast.makeText(getActivity(),"Please refresh once ",Toast.LENGTH_SHORT).show();

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