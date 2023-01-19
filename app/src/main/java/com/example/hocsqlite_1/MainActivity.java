package com.example.hocsqlite_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //khai báo các biến giao diện
    EditText edtmalop, edttenlop, edtsiso;
    Button btninsert, btndelete, btnupdate, btnquery;
    //khai báo về ListView;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtmalop = findViewById(R.id.edtmalop);
        edttenlop = findViewById(R.id.edttenlop);
        edtsiso = findViewById(R.id.edtsiso);
        btninsert = findViewById(R.id.btninsert);
        btndelete = findViewById(R.id.btndelete);
        btnupdate = findViewById(R.id.btnupdate);
        btnquery = findViewById(R.id.btnquery);
        //Tạo listview
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);
        //Tạo và mở cơ sở dữ liệu Sqlite
        mydatabase = openOrCreateDatabase("qlsinhiven.db", MODE_PRIVATE, null);
        //Tạo table để chứa dữ liệu
        try {

            String sql = "CREATE TABLE tbllop(malop TEXT primary key,tenlop TEXT, siso INTEGER)";
            mydatabase.execSQL(sql);
        }catch (Exception e) {
            Log.e("Error", "Table đã tồn tại");
        }
        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = edtmalop.getText().toString();
                String tenlop = edttenlop.getText().toString();
                int siso = Integer.parseInt(edtsiso.getText().toString());
                ContentValues myvalues = new ContentValues();
                myvalues.put("malop", malop);
                myvalues.put("tenlop", tenlop);
                myvalues.put("siso", siso);
                String msg = "";
                if(mydatabase.insert("tbllop", null, myvalues) == -1)
                {
                    msg = "Fail to Insert Record!";
                }else {
                    msg = "Insert record Sucessfully";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = edtmalop.getText().toString();
                int n = mydatabase.delete("tbllop", "malop = ?", new String[]{malop});
                String msg = "";
                if(n == 0) {
                    msg = "No record to update";
                }
                else {
                    msg = n + "record is deleted";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int siso = Integer.parseInt(edtsiso.getText().toString());
                String malop = edtmalop.getText().toString();
                ContentValues myvalue = new ContentValues();
                myvalue.put("siso", siso);
                myvalue.put("malop", malop);
                int n = mydatabase.update("tbllop", myvalue, "malop = ?", new String[]{malop});
                String msg = "";
                if(n == 0 ) {
                    msg = "No record to Update";
                }else {
                    msg = n+ "record is updated";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylist.clear();
                Cursor cursor = mydatabase.query("tbllop", null, null, null, null, null, null);
                cursor.moveToNext();
                String data = "";
                while (cursor.isAfterLast() == false) {

                    data = cursor.getString(0) +" -"+ cursor.getString(1)+" -"+cursor.getString(2);
                    cursor.moveToNext();
                    mylist.add(data);

                }
                cursor.close();
                myadapter.notifyDataSetChanged();
            }
        });
    }
}