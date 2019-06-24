package com.example.ayushmittal.records;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class addnewuser extends AppCompatActivity {

    private EditText labname, username, password;
    private Button button;
    private String  admin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewuser);

        labname = (EditText)findViewById(R.id.editlab);
        password = (EditText)findViewById(R.id.editpass);
        username = (EditText)findViewById(R.id.editemail);
        button = (Button) findViewById(R.id.button3);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        admin=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                createuser();

            }
        });

    }

        void createuser() {
            if(isPassValid(password.getText().toString())) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updatedisplayname();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(addnewuser.this, "Unable to create user", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
            else{
                progressBar.setVisibility(View.GONE);

            }
        }

   public void updatedisplayname(){


             FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();


            if(user.getEmail().equalsIgnoreCase(username.getText().toString())){
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(labname.getText().toString().toUpperCase()).build();
                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(addnewuser.this,"User  details created",Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("labs").child(labname.getText().toString().toUpperCase()).setValue("");
                            FirebaseAuth.getInstance().signOut();
                            checkadmin();
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(addnewuser.this,"Unable to create user with display name",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
            else{
                Toast.makeText(addnewuser.this,"Some error Occured",Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(addnewuser.this,user.getEmail(),Toast.LENGTH_SHORT).show();


        }

    public void checkadmin() {

    final AlertDialog.Builder builder=new AlertDialog.Builder(addnewuser.this);
    builder.setTitle("Enter admin Password");
    View view= LayoutInflater.from(addnewuser.this).inflate(R.layout.builderissue,null);
        EditText starting = (EditText) view.findViewById(R.id.name);starting.setText(admin);starting.setEnabled(false);
         EditText ending = (EditText) view.findViewById(R.id.details);ending.setHint("Passwrod admin");ending.setTransformationMethod(new PasswordTransformationMethod());
        final EditText ending1 = ending;

        TextView txt1 = view.findViewById(R.id.textView);  txt1.setText("Email");        //Builder text views
        TextView txt2= view.findViewById(R.id.textView2);txt2.setText("Password");

        builder.setView(view);

        builder.setPositiveButton("Login ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                if(isPassValid(ending1.getText().toString())) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(admin, ending1.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(addnewuser.this, "Currently you are admin ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                AlertDialog dialog1 = builder.create();
                                dialog1.show();
                                Toast.makeText(addnewuser.this, "Enter correct password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    progressBar.setVisibility(View.GONE);


                }
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

public boolean isPassValid(String p){
        if(p.length()<6){
            Toast.makeText(addnewuser.this,"Password must be greater than 5",Toast.LENGTH_SHORT).show();

            return false;
        }
    return true;
    }


}
