package com.example.ayushmittal.records.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.example.ayushmittal.records.objectclass.equipment;
import com.example.ayushmittal.records.objectclass.filter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class dbhelper extends SQLiteOpenHelper {

    Context context;

    public dbhelper(Context context) {
        super(context, "data", null, 1);
        this.context = context;
        SQLiteDatabase db = this.getReadableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table t1( s_no integer primary key autoincrement,lab text," +
                "name text," +
                "serial_start text," +
                "serial_end text," +
                "dop text," +
                "consignor text," +
                "billnumber text," +
                "qtyreceipt integer," +
                "qtyissued integer," +
                "qtystock integer," +
                "cost real," +
                "checkedby text," +
                "description text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "t1");
        onCreate(db);

    }

    public void adddata(equipment equipment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lab", equipment.getLab());
        values.put("name", equipment.getName());
        values.put("serial_start", equipment.getSerialStart());
        values.put("serial_end", equipment.getSerialEnd());
        values.put("dop", equipment.getDop());
        values.put("consignor", equipment.getConsignor());
        values.put("billnumber", equipment.getBillnumber());
        values.put("qtyreceipt", equipment.getQtyreceipt());
        values.put("qtyissued", equipment.getQtyissued());
        values.put("qtystock", equipment.getQtyStock());
        values.put("cost", equipment.getCost());
        values.put("checkedby", equipment.getCheckedby());
        values.put("description", equipment.getDescription());

        db.insert("t1", null, values);
    }

    public void droptable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + "t1");

    }

    public void createtable() {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("create table t1( s_no integer primary key autoincrement,lab text," +
                "name text," +
                "serial_start text," +
                "serial_end text," +
                "dop text," +
                "consignor text," +
                "billnumber text," +
                "qtyreceipt integer," +
                "qtyissued integer," +
                "qtystock integer," +
                "cost real," +
                "checkedby text," +
                "description text)");

    }

    public Cursor getdata() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM t1", null);
        return c;
    }

    public Cursor getfilterdata(ArrayList<filter> queryparameter) {

        String q;
        SQLiteDatabase db = getReadableDatabase();

            String[] args = new String[7];

            args[5] = "1800/01/01";
            SimpleDateFormat d = new SimpleDateFormat("yyyy/mm/dd");
            args[6] = d.format(Calendar.getInstance().getTime());
            args[2] = "0";
            args[3] = String.valueOf(Integer.MAX_VALUE);
            args[0] = args[1] = args[4] = "%";

            q = "SELECT * FROM t1 WHERE NAME like ? AND lab LIKE ? and cost between ? and ? and billnumber LIKE ? and dop>=? and dop<=? order by lab limit 50";


            for (int i = 0; i < queryparameter.size(); ++i) {
                // equipment value=snapshot.getValue(equipment.class);

                if (queryparameter.get(i).getType().equalsIgnoreCase("name")) {
                    args[0] += queryparameter.get(i).getS();
                    args[0] += "%";
                } else if (queryparameter.get(i).getType().equalsIgnoreCase("lab")) {
                    args[1] += queryparameter.get(i).getS();
                    args[1] += "%";

                } else if (queryparameter.get(i).getType().equalsIgnoreCase("billnumber")) {
                    args[4] += queryparameter.get(i).getS();
                    args[4] += "%";
                } else if (queryparameter.get(i).getType().equalsIgnoreCase("cost")) {
                    args[2] = queryparameter.get(i).getS();
                    args[3] = queryparameter.get(i).getE();

                } else if (queryparameter.get(i).getType().equalsIgnoreCase("dop")) {

                    args[5] = queryparameter.get(i).getS();
                    args[6] = queryparameter.get(i).getE();
                }

            }
        Cursor c = null;
        try {


              c = db.rawQuery(q, args);

              return c;
          }catch (Exception e){

            Toast.makeText(context,"Try refresh ",Snackbar.LENGTH_LONG).show();
        }

    return c;
    }
}
