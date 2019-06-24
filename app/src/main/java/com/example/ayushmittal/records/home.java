package com.example.ayushmittal.records;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.ayushmittal.records.adapter.recyclerAdapter;
import com.example.ayushmittal.records.database.dbhelper;
import com.example.ayushmittal.records.objectclass.equipment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class home extends AppCompatActivity {

    private TextView user;
    private CardView searchrange,addnewuser,entry;
    private Button ref;
    private ViewFlipper vf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        addnewuser=(CardView) findViewById(R.id.add_lab);
        entry=(CardView)findViewById(R.id.newrecord);
        searchrange=(CardView) findViewById(R.id.search);
        user=(TextView)findViewById(R.id.user);
        ref=(Button)findViewById(R.id.refresh);
        vf= (ViewFlipper)findViewById(R.id.vf);

        int [] a= {R.drawable.pic,R.drawable.pic1};


        for(int i =0;i<2;i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(a[i]);
            vf.addView(imageView);
            vf.setFlipInterval(4000);
            vf.setAutoStart(true);
            vf.setInAnimation(this,R.anim.fui_slide_in_right);
            vf.setOutAnimation(this,R.anim.fui_slide_out_left);
        }
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
        user.setText("You logged in as "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());}
        else{
            Intent intent=new Intent(home.this,login.class);
            startActivity(intent);
        }

        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(home.this,newentry.class);
                startActivity(i);
            }
        });

        searchrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(home.this,search.class);
                startActivity(i);
            }
        });


        addnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equalsIgnoreCase("ADMIN")) {
                Intent i=new Intent(home.this,addnewuser.class);
                startActivity(i);
                }
                else{
                   Toast.makeText(home.this,"You are not an admin",Toast.LENGTH_SHORT).show();
               }
            }
        });

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;}



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if(menuItemThatWasSelected==R.id.log_out) {

            dbhelper q=new dbhelper(this);
            q.droptable();
            q.close();

            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(home.this,login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void refresh(View view) {

        ref.setEnabled(false);
        dbhelper db=new dbhelper(home.this);
        db.droptable();
        db.createtable();
        db.close();

        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equalsIgnoreCase("admin")) {
            FirebaseDatabase.getInstance().getReference("labs").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String l = dataSnapshot.getKey();
                    updatedatabse(l);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else{
            updatedatabse(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
    }
    private void updatedatabse(String l) {
        Query q=FirebaseDatabase.getInstance().getReference("database").child(l).orderByChild("lab");

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.setEnabled(true);
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    equipment eq=snapshot.getValue(equipment.class);
                    dbhelper d=new dbhelper(home.this);
                    d.adddata(eq);
                    d.close();
                }
                Snackbar.make(findViewById(R.id.relativeLayout),"Succesfully Updated \n"+String.valueOf(dataSnapshot.getChildrenCount())+" Records Found ",Snackbar.LENGTH_LONG+Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}