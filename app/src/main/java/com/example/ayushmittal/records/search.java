package com.example.ayushmittal.records;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayushmittal.records.adapter.recyclerAdapter;
import com.example.ayushmittal.records.database.dbhelper;
import com.example.ayushmittal.records.objectclass.equipment;
import com.example.ayushmittal.records.objectclass.filter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class search extends AppCompatActivity {

    private recyclerAdapter adapter;
    private Spinner spinner;
    private LinearLayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton,pdfbutton;
    private ArrayAdapter<CharSequence> arrayAdapter;
    private ArrayList<equipment> al;
    private ArrayList<filter> queryparameter;
    private EditText start;
    private ArrayList<String> lablist;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("Search");
        floatingActionButton = (FloatingActionButton) findViewById(R.id.search);
        pdfbutton=(FloatingActionButton)findViewById(R.id.pdfbutton);
        pdfbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createpdf(v);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        al = new ArrayList<equipment>();
        lablist = new ArrayList<String>();
        start = (EditText) findViewById(R.id.start);
        queryparameter = new ArrayList<filter>();


        //Spinner
        spinner = (Spinner) findViewById(R.id.spinner);


        arrayAdapter = ArrayAdapter.createFromResource(search.this, R.array.field, R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_view);
        spinner.setAdapter(arrayAdapter);

/*
if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equalsIgnoreCase("admin")){

    FirebaseDatabase.getInstance().getReference("labs").addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String dat=dataSnapshot.getKey();
            lablist.add(dat);

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
else {
    lablist.add(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
}


*/

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    String temp = parent.getItemAtPosition(position).toString();

                    editTextHint(temp);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(search.this, "Select A Option", Toast.LENGTH_SHORT).show();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutManager = new LinearLayoutManager(search.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                al.clear();
                progressBar.setVisibility(View.VISIBLE);
            /*    for(int n=0;n<lablist.size();++n){
                 databaseReference= FirebaseDatabase.getInstance().getReference("database").child(lablist.get(n));

                 Query q1;;
                 if(queryparameter.get(0).getType().equalsIgnoreCase("cost")){
                  q1=databaseReference.orderByChild(queryparameter.get(0).getType().toLowerCase()).startAt(Float.parseFloat(queryparameter.get(0).getS())).endAt(Float.parseFloat(queryparameter.get(0).getE()));}


                 else{
                  q1=databaseReference.orderByChild(queryparameter.get(0).getType().toLowerCase()).startAt(queryparameter.get(0).getS()).endAt(queryparameter.get(0).getE());}

                 q1.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                         progressBar.setVisibility(View.GONE);

                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {



                           if(checkdata(snapshot)){

                               equipment ob = snapshot.getValue(equipment.class);
                               al.add(ob);

                               adapter = new recyclerAdapter(al);
                               recyclerView.setAdapter(adapter);
                           }

                         }
                     }
                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });

            }*/


                dbhelper dq = new dbhelper(search.this);
                //Cursor c = dq.getdata();
                Cursor c=dq.getfilterdata(queryparameter);
                while (c!=null&&c.moveToNext()) {

                    equipment obj = new equipment();
                    obj.setLab(c.getString(c.getColumnIndex("lab")));
                    obj.setName(c.getString(c.getColumnIndex("name")));
                    obj.setSerialStart(c.getString(c.getColumnIndex("serial_start")));
                    obj.setSerialEnd(c.getString(c.getColumnIndex("serial_end")));

                    obj.setDop(c.getString(c.getColumnIndex("dop")));
                    obj.setConsignor(c.getString(c.getColumnIndex("consignor")));
                    obj.setBillnumber(c.getString(c.getColumnIndex("billnumber")));
                    obj.setCost(c.getFloat(c.getColumnIndex("cost")));
                    obj.setQtyreceipt(c.getInt(c.getColumnIndex("qtyreceipt")));
                    obj.setQtyStock(c.getInt(c.getColumnIndex("qtystock")));
                    obj.setQtyissued(c.getInt(c.getColumnIndex("qtyissued")));

                    obj.setCheckedby(c.getString(c.getColumnIndex("checkedby")));
                    obj.setDescription(c.getString(c.getColumnIndex("description")));

                    {
                        al.add(obj);

                        adapter = new recyclerAdapter(al);
                        recyclerView.setAdapter(adapter);
                    }

                }
                if(c!=null&&(!c.isClosed()))
                c.close();
                dq.close();
                progressBar.setVisibility(View.GONE);
                Snackbar.make(getCurrentFocus(),"Total "+String.valueOf(al.size())+" Records found",Snackbar.LENGTH_INDEFINITE).show();

            }
        });


    }


    private boolean checkdata(equipment value) {

        for (int i = 0; i < queryparameter.size(); ++i) {
            // equipment value=snapshot.getValue(equipment.class);

            if (queryparameter.get(i).getType().equalsIgnoreCase("name")) {
                if (!queryparameter.get(i).getS().equalsIgnoreCase(value.getName())) {
                    return false;
                }
            } else if (queryparameter.get(i).getType().equalsIgnoreCase("lab")) {
                if (!queryparameter.get(i).getS().equalsIgnoreCase(value.getLab())) {
                    return false;
                }
            } else if (queryparameter.get(i).getType().equalsIgnoreCase("billnumber")) {
                if (!queryparameter.get(i).getS().equalsIgnoreCase(value.getBillnumber())) {
                    return false;
                }
            } else if (queryparameter.get(i).getType().equalsIgnoreCase("dop")) {


                try {
                    Date d1 = new SimpleDateFormat("yyyy/MM/dd").parse(value.getDop());
                    Date d2 = new SimpleDateFormat("yyyy/MM/dd").parse(queryparameter.get(i).getS());
                    Date d3 = new SimpleDateFormat("yyyy/MM/dd").parse(queryparameter.get(i).getE());

                    if (d1.compareTo(d2) == 0 || d1.compareTo(d3) == 0 || (d1.before(d2) && d3.after(d1))) {

                    } else
                        return false;


                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(search.this, "some error occured", Toast.LENGTH_SHORT).show();
                }


            } else if (queryparameter.get(i).getType().equalsIgnoreCase("cost")) {
                if (Float.parseFloat(queryparameter.get(i).getS()) >= (value.getCost()) && Float.parseFloat(queryparameter.get(i).getE()) <= (value.getCost())) {
                    return false;
                }
            }
        }
        return true;

    }




 /*   private boolean checkdata(DataSnapshot snapshot) {

        for(int i=1;i<queryparameter.size();++i){
            equipment value=snapshot.getValue(equipment.class);

            if(queryparameter.get(i).getType().equalsIgnoreCase("name")){
                if(!queryparameter.get(i).getS().equalsIgnoreCase(value.getName())){
                    return false;
                }
            }

            else if(queryparameter.get(i).getType().equalsIgnoreCase("billnumber")){
                if(!queryparameter.get(i).getS().equalsIgnoreCase(value.getBillnumber())){
                    return false;
                }
            }
            else if(queryparameter.get(i).getType().equalsIgnoreCase("dop")) {


                try {
                    Date d1 = new SimpleDateFormat("yyyy/MM/dd").parse(value.getDop());
                    Date d2 = new SimpleDateFormat("yyyy/MM/dd").parse(queryparameter.get(i).getS());
                    Date d3 = new SimpleDateFormat("yyyy/MM/dd").parse(queryparameter.get(i).getE());

                    if(d1.compareTo(d2)==0||d1.compareTo(d3)==0||(d1.before(d2)&&d3.after(d1))){

                    }
                    else
                        return false;


                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(search.this, "some error occured", Toast.LENGTH_SHORT).show();
                }


            }

          else if(queryparameter.get(i).getType().equalsIgnoreCase("cost")){
                if(Float.parseFloat(queryparameter.get(i).getS())>=(value.getCost())&&Float.parseFloat(queryparameter.get(i).getE())<=(value.getCost())){
                    return false;
              }
          }
        }
        return true;

    }*/


    private void editTextHint(final String temp) {

        AlertDialog.Builder builder = new AlertDialog.Builder(search.this);
        builder.setTitle("Enter values");
        View view = LayoutInflater.from(search.this).inflate(R.layout.builderissue, null);
        EditText starting = (EditText) view.findViewById(R.id.name);
        EditText ending = (EditText) view.findViewById(R.id.details);
        TextView txt1 = view.findViewById(R.id.textView);
        txt1.setText("Enter Starting value");        //Builder text views
        TextView txt2 = view.findViewById(R.id.textView2);
        final EditText starting1 = starting;
        final EditText ending1 = ending;
        ending.setEnabled(false);
        builder.setView(view);
        if (temp.equalsIgnoreCase("cost")) {
            starting.setHint("In Rs");
            ending.setEnabled(true);
            txt2.setText("Enter Ending Value");
            ending.setHint("In Rs");
        } else if (temp.equalsIgnoreCase("dop")) {
            starting.setHint("yyyy/mm/dd");
            ending.setEnabled(true);
            txt2.setText("Enter Ending Value");
            ending.setHint("To  (yyyy/mm/dd)");
        } else {
            starting.setHint("Enter value");
            txt2.setVisibility(View.GONE);
            ending.setVisibility(View.GONE);
        }

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filter o = new filter();
                o.setType(temp);
                o.setS(starting1.getText().toString());
                start.setText(start.getText() + "\n" + o.getType() + " ( " + o.getS());


                if (temp.equalsIgnoreCase("cost") || temp.equalsIgnoreCase("dop")) {
                    o.setE(ending1.getText().toString());
                    start.setText(start.getText() + " - " + o.getE());
                } else {
                    o.setE(starting1.getText().toString());
                }


                queryparameter.add(o);
                start.setText(start.getText() + " )");

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                this.finishAffinity();


        }
        onBackPressed();
        finish();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }


    public void filter(DataSnapshot snapshot, final int i) {
        Query q;


        if (queryparameter.get(i).getType().equalsIgnoreCase("dop")) {
            q = FirebaseDatabase.getInstance().getReference("database").orderByChild(queryparameter.get(i).getType().toLowerCase()).startAt(queryparameter.get(i).getS()).endAt(queryparameter.get(i).getE());
        } else if (queryparameter.get(i).getType().equalsIgnoreCase("cost")) {
            q = FirebaseDatabase.getInstance().getReference("database").orderByChild(queryparameter.get(i).getType().toLowerCase()).startAt(Float.parseFloat(queryparameter.get(i).getS())).endAt(Float.parseFloat(queryparameter.get(i).getE()));
        } else {
            q = FirebaseDatabase.getInstance().getReference("database").orderByChild(queryparameter.get(i).getType().toLowerCase()).equalTo(queryparameter.get(i).getS());
        }

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (i == queryparameter.size() - 1) {


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        equipment ob = ds.getValue(equipment.class);
                        al.add(ob);

                        adapter = new recyclerAdapter(al);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    void createpdf(View view) {

try{
        int gap = 85;

        PdfDocument document = new PdfDocument();
        int pageheight = 700;
        for (int r = 0; r < al.size(); r += 10) {


            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(950, pageheight, r / 10).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Paint pnt = new Paint();
            pnt.setTextSize(24);
            pnt.setColor(Color.BLACK);


            canvas.drawLine(10, 40, 45 + 10 * gap, 40, pnt);       //first horizontal line

            canvas.drawText("EE LAB RECORDS ", 300, 30, pnt);
            pnt.setTextSize(14);


            int vertrical = 25;

            try {
                int y = 70;
                for (int i = r; i < Math.min(r + 10, al.size()); ++i) {
                    int verterror = -3;
                    y += vertrical;

                    canvas.drawLine(10, y - 13, 45 + 10 * gap, y - 13, pnt);

                    canvas.drawText(String.valueOf(i + 1), 11, y, pnt);
                    int line = 0;
                    for (int q = 0; q < (al.get(i).getName().length()); ) {

                        int m = breakstring(al.get(i).getName(), q, q + 15);

                        canvas.drawText(al.get(i).getName(), q, Math.min(m, al.get(i).getName().length()), 47, y + line * vertrical, pnt);
                        line++;

                        q = m;
                        verterror++;
                    }
                    line = 0;
                    for (int q = 0; q <= (al.get(i).getLab().length()) / 15; q++) {
                        canvas.drawText(al.get(i).getLab(), 15 * q, Math.min(15 + 15 * q, al.get(i).getLab().length()), 47 + gap, y + q * vertrical, pnt);
                        verterror++;
                    }
                    canvas.drawText(String.valueOf(al.get(i).getQtyStock()), 47 + 2 * gap, y, pnt);
                    canvas.drawText((String.valueOf(al.get(i).getQtyreceipt())), 47 + 3 * gap, y, pnt);
                    canvas.drawText((String.valueOf(al.get(i).getCost())), 47 + 4 * gap, y, pnt);
                    canvas.drawText((String.valueOf(al.get(i).getBillnumber())), 47 + 5 * gap, y, pnt);
                    canvas.drawText(String.valueOf(al.get(i).getCheckedby()), 47 + 6 * gap, y, pnt);
                    line = 0;

                    for (int q = 0; q < (al.get(i).getDescription().length()); ) {

                        int m = breakstring(al.get(i).getDescription(), q, q + 30);

                        canvas.drawText(al.get(i).getDescription(), q, Math.min(m, al.get(i).getDescription().length()), 47 + 7 * gap, y + line * vertrical, pnt);
                        line++;

                        q = m;
                        verterror++;
                    }

                    y += verterror * vertrical;


                }
                pageheight = y - 13 + vertrical;

                canvas.drawLine(10, y - 13 + vertrical, 45 + 10 * gap, y - 13 + vertrical, pnt);       //horizontal line after each record

            } catch (Exception e) {
                Toast.makeText(this, "No records available to be printed", Toast.LENGTH_SHORT).show();
                //    Log.e("ere",e.getMessage());
            }


            canvas.drawLine(10, 0, 10, pageheight, pnt);
            canvas.drawLine(45, 0, 45, pageheight, pnt);

            canvas.drawText("S. No", 11, 60, pnt);
            canvas.drawText("Name", 47, 60, pnt);


            canvas.drawLine(45 + gap, 0, 45 + gap, pageheight, pnt);
            canvas.drawText("Lab", 47 + gap, 60, pnt);
            canvas.drawLine(45 + 2 * gap, 0, 45 + 2 * gap, pageheight, pnt);
            canvas.drawText("Qty Stock", 47 + 2 * gap, 60, pnt);
            canvas.drawLine(45 + 3 * gap, 0, 45 + 3 * gap, pageheight, pnt);
            canvas.drawText("qty receipt", 47 + 3 * gap, 60, pnt);
            canvas.drawLine(45 + 4 * gap, 40, 45 + 4 * gap, pageheight, pnt);
            canvas.drawText("cost", 47 + 4 * gap, 60, pnt);
            canvas.drawLine(45 + 5 * gap, 40, 45 + 5 * gap, pageheight, pnt);
            canvas.drawText("Bill No", 47 + 5 * gap, 60, pnt);
            canvas.drawLine(45 + 6 * gap, 40, 45 + 6 * gap, pageheight, pnt);
            canvas.drawText("check by", 47 + 6 * gap, 60, pnt);
            canvas.drawLine(45 + 7 * gap, 0, 45 + 7 * gap, pageheight, pnt);
            canvas.drawText("Description", 47 + 7 * gap, 60, pnt);
            canvas.drawLine(45 + 10 * gap, 0, 45 + 10 * gap, pageheight, pnt);


            document.finishPage(page);
        }
        SimpleDateFormat d = new SimpleDateFormat("hh:mm:ss");
        String name = d.format(Calendar.getInstance().getTime());
        File filedir = new File(Environment.getExternalStorageDirectory() + "/reports/");
        filedir.mkdir();
        final File file = new File(filedir, "/report" + name + ".pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            final Snackbar snackbar = Snackbar.make(getCurrentFocus(), "PDF Generated at " + file.getPath(), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OPEN", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(Intent.ACTION_VIEW);
                    if (file.exists()) {
                        in.setDataAndType(FileProvider.getUriForFile(search.this,getApplicationContext().getPackageName()+".provider",file), "application/pdf");
                        in.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try {
                            startActivity(in);
                        } catch (Exception e) {
                           Toast.makeText(search.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });
            snackbar.show();

        } catch (Exception e) {
            Toast.makeText(this, "Unable to save pdf in Device", Toast.LENGTH_LONG).show();
        }

        document.close();
    }catch (Exception e){
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }
    }

    public int breakstring(String name, int q, int i) {
        int end = i;
        if (i >= name.length())
            return name.length();
        while ((name.charAt(i) != ' ' && name.charAt(i) != '\n') && i > q) {

            i--;
        }
        if (i != q)
            return i;
        else
            return end;
    }


    }

