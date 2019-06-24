package com.example.ayushmittal.records;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayushmittal.records.objectclass.equipment;
import com.example.ayushmittal.records.objectclass.transferequip;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class newentry extends AppCompatActivity {


    private EditText serialStart;
    private EditText serialEnd;
    private EditText consignor;
    private EditText billnumber;
    private EditText qtyReceipt;
    private EditText qtyissued;
    private EditText qtyStock;

    private EditText name;
    private EditText price;
    private EditText description;
    private EditText checkedby;

    private EditText dop;
    private EditText lab;
    private Button submit;
    private Button save;
    private DatabaseReference databaseReference;
    private String reference;
    private ArrayList<String> labtranslist;
    private transferequip trans[];
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry);

        labtranslist=new ArrayList<String>();
        trans=new transferequip[100];

        serialStart=(EditText)findViewById(R.id.edituid);
        serialEnd=(EditText)findViewById(R.id.edituid2);
        consignor=(EditText)findViewById(R.id.editconsignor);
        billnumber=(EditText)findViewById(R.id.editbill);
        qtyReceipt=(EditText)findViewById(R.id.editqtyreceipt);
        qtyissued=(EditText)findViewById(R.id.editqtyissue);
        qtyStock=(EditText)findViewById(R.id.editqtystock);qtyStock.setEnabled(false);
        description=(EditText)findViewById(R.id.editdesc);
        checkedby=(EditText)findViewById(R.id.editcheck);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        name=(EditText)findViewById(R.id.editname);
        price=(EditText)findViewById(R.id.editprice);
        dop=(EditText)findViewById(R.id.editdop);
        lab=(EditText)findViewById(R.id.editlab);
        String user=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if(!user.equalsIgnoreCase("admin")){
        lab.setText(user);
        lab.setEnabled(false);}
        submit=(Button)findViewById(R.id.add);
        save=(Button)findViewById(R.id.save);save.setEnabled(false);
        databaseReference= FirebaseDatabase.getInstance().getReference("database").child(lab.getText().toString());




       Snackbar.make(findViewById(R.id.mre),"Fill All Fields",Snackbar.LENGTH_INDEFINITE).show();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checknull()==true) {
                    int i = 1;
                    reference = "";
                    int issued = Integer.valueOf(qtyissued.getText().toString());
                    for (; i <= issued; i++) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(newentry.this);
                        builder.setTitle("Enter issue details " + String.valueOf(i));
                        View view = LayoutInflater.from(newentry.this).inflate(R.layout.builderissue, null);
                        final EditText name = (EditText) view.findViewById(R.id.name);
                        final EditText details = (EditText) view.findViewById(R.id.details);
                        final TextView labtrans=(TextView) view.findViewById(R.id.labtrans);
                        final EditText labe=(EditText)view.findViewById(R.id.labe);
                        labe.setVisibility(View.VISIBLE);
                        labtrans.setVisibility(View.VISIBLE);

                        builder.setView(view);builder.setCancelable(false);
                        final int finalI = i;
                        builder.setCancelable(false);
                        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference += name.getText().toString()+ " ";
                                if(!labe.getText().toString().equals(""))
                                {labtranslist.add(labe.getText().toString().toUpperCase());
                                trans[labtranslist.size()-1]=new transferequip();
                                trans[labtranslist.size()-1].setSerialend(name.getText().toString());
                                trans[labtranslist.size()-1].setSerialstart(name.getText().toString());}
                                else {
                                    Toast.makeText(newentry.this,"You did not enterd lab name in dialog",Toast.LENGTH_SHORT).show();
                                }
                                reference+= "transferred to "+labe.getText().toString();
                                reference += details.getText().toString() + "\n";


                                if (finalI == 1)
                                    save.setEnabled(true);


                            }
                        });
                        AlertDialog dialog = builder.create();

                        dialog.show();

                    }
                    if (issued == 0)
                        save.setEnabled(true);


                }
                else {
                    Toast.makeText(newentry.this, "Fill All the fields ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                equipment obj = new equipment();
                obj.setCost(Float.parseFloat(price.getText().toString()));
                price.setText("");
                obj.setDop(dop.getText().toString());
                dop.setText("");

                obj.setName(name.getText().toString());
                name.setText("");

                obj.setBillnumber(billnumber.getText().toString());billnumber.setText("");

                obj.setCheckedby(checkedby.getText().toString());checkedby.setText("");
                obj.setConsignor(consignor.getText().toString());consignor.setText("");



                for(int i=0;i<labtranslist.size();++i){

                    obj.setLab(labtranslist.get(i).toUpperCase());
                    obj.setSerialStart(trans[i].getSerialstart());
                    obj.setSerialEnd(trans[i].getSerialend());
                    obj.setQtyissued(0);
                    obj.setQtyStock(1);
                    obj.setQtyreceipt(1);
                    obj.setDescription("transferred from "+lab.getText().toString());

                    FirebaseDatabase.getInstance().getReference("database").child(obj.getLab()).push().setValue(obj);



                }
                obj.setSerialStart(serialStart.getText().toString());
                serialStart.setText("");
                obj.setSerialEnd(serialEnd.getText().toString());
                serialEnd.setText("");
                obj.setQtyreceipt(Integer.valueOf(qtyReceipt.getText().toString()));
                obj.setQtyissued(Integer.valueOf(qtyissued.getText().toString()));qtyReceipt.setText("");qtyissued.setText("");
                obj.setQtyStock(obj.getQtyreceipt() - obj.getQtyissued());qtyStock.setText("");
                obj.setLab(lab.getText().toString().toUpperCase());
                lab.setText("");
                obj.setDescription(reference+"\n");
                obj.setDescription(obj.getDescription()+description.getText().toString() + "\n");
                description.setText("");


                FirebaseDatabase.getInstance().getReference("database").child(obj.getLab()).push().setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(newentry.this, "updated", Toast.LENGTH_SHORT).show();
                    }
                });







            }
        });





    }

    public boolean checknull() {

        if(serialEnd.getText().toString().equalsIgnoreCase(""))
            return false;
        if(serialStart.getText().toString().equalsIgnoreCase(""))
            return false;
        if(name.getText().toString().equalsIgnoreCase(""))
            return false;
        if(lab.getText().toString().equalsIgnoreCase(""))
            return false;
        if(dop.getText().toString().equalsIgnoreCase(""))
            return false;
        if(consignor.getText().toString().equalsIgnoreCase(""))
            return false;
        if(billnumber.getText().toString().equalsIgnoreCase(""))
            return false;
        if(qtyReceipt.getText().toString().equalsIgnoreCase(""))
            return false;
        if(qtyissued.getText().toString().equalsIgnoreCase(""))
            return false;
        if(checkedby.getText().toString().equalsIgnoreCase(""))
            return false;
        if(price.getText().toString().equalsIgnoreCase(""))
            return false;


        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2,menu);
        return true;}



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if(menuItemThatWasSelected==R.id.BACK) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            }
            else
            {
                Intent i= new Intent(newentry.this,home.class);
                startActivity(i);
            }
        }
        else if(menuItemThatWasSelected==R.id.clear)
        {
            Intent i= new Intent(newentry.this,newentry.class);
            startActivity(i);}
        else if(menuItemThatWasSelected==R.id.exit)
        {
            this.finishAffinity();
        }

        return super.onOptionsItemSelected(item);
    }



}
