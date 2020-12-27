package com.android.address_book_Activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import com.android.Task.NetworkTask;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 주소록 전체 화면                                 =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class AddressListActivity extends AppCompatActivity {
    final static String TAG = "AddressListActivity";
    String urlAddr = null;
    ArrayList<People> members;
    PeopleAdapter adapter;
    ListView listView;
    String macIP;

    LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        Intent intent = getIntent();


        listView = findViewById(R.id.lv_student);

        macIP = intent.getStringExtra("macIP");

        urlAddr = "http://"+macIP+":8080/test/people_query_all.jsp";

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(SelectAllActivity.this, UpdateActivity.class);
//
//                intent.putExtra("studentid", members.get(position).getCode());
//                intent.putExtra("macIP", macIP);
//                intent.putExtra("studentname", members.get(position).getName());
//                intent.putExtra("studentmajor", members.get(position).getDept());
//                intent.putExtra("studenttel", members.get(position).getPhone());
//
//                startActivity(intent);
//            }
//        });
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(SelectAllActivity.this, DeleteActivity.class);
//                intent.putExtra("macIP", macIP);
//                intent.putExtra("studentid", members.get(position).getCode());
//                intent.putExtra("studentname", members.get(position).getName());
//                intent.putExtra("studentmajor", members.get(position).getDept());
//                intent.putExtra("studenttel", members.get(position).getPhone());
//
//                startActivity(intent);
//                return true;
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        connectGetData();
        Log.v(TAG, "onResume()");

    }

    private void connectGetData(){
        try {
            NetworkTask networkTask = new NetworkTask(AddressListActivity.this, urlAddr);
            Object obj = networkTask.execute().get();
            members = (ArrayList<People>) obj;

            adapter = new PeopleAdapter(AddressListActivity.this, R.layout.content_address_list, members);
            listView.setAdapter(adapter);


        }catch (Exception e){
            e.printStackTrace();
        }
    }


}