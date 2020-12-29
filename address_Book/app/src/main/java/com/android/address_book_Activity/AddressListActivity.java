package com.android.address_book_Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;


import com.android.Task.PeopleNetworkTask;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
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

    final static String TAG = "SelectAllActivity";
    String urlAddr = null;
    String urlAddr1 = null;
    String urlAddr2 = null;
    String urlAddr3 = null;
    String urlAddr4 = null;
    ArrayList<People> searchArr;
    ArrayList<People> members;
    PeopleAdapter adapter;
    ListView listView;
    String macIP;
    Button btnGroup1, btnGroup2, btnGroup3, btnGroup4;

    EditText search_EdT;

    private BottomNavigationView mBottomNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Intent intent = getIntent();
        listView = findViewById(R.id.lv_student);
//        macIP = intent.getStringExtra("macIP");
        macIP = "192.168.219.154";
        urlAddr = "http://" + macIP + ":8080/test/";
        btnGroup1 = findViewById(R.id.button1);
        btnGroup2 = findViewById(R.id.button2);
        btnGroup3 = findViewById(R.id.button3);

        btnGroup1.setOnClickListener(onCLickListener);
        btnGroup2.setOnClickListener(onCLickListener);
        btnGroup3.setOnClickListener(onCLickListener);


        search_EdT = findViewById(R.id.search_ET);


        search_EdT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = search_EdT.getText().toString();
                search(text);
            }
        });

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // BottomNavigate(menuItem.getItemId());
                switch (menuItem.getItemId()){
                    case R.id.navigation_1:
                        urlAddr2 = urlAddr + "people_query_all2.jsp";
                        connectGetData(urlAddr2);
                        break;

                    case R.id.navigation_2:
                        urlAddr4="";
                        String group1 = btnGroup1.getText().toString();
                        urlAddr4 = urlAddr + "group_people_query_all.jsp?email=qkr@naver.com&group=" + group1;
                        connectGetData(urlAddr4);
                        break;

                    case R.id.navigation_3:
                        urlAddr3 = urlAddr + "favorite_people_query_all.jsp";
                        connectGetData(urlAddr3);
                }


                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_1);
        mBottomNV.setSelectedItemId(R.id.navigation_2);
        mBottomNV.setSelectedItemId(R.id.navigation_3);

    }
    @Override
    protected void onResume() {
        super.onResume();
        urlAddr1 = urlAddr + "people_query_all2.jsp";
        connectGetData(urlAddr1);
        searchArr = new ArrayList<People>();
        searchArr.addAll(members);
        Log.v(TAG, "onResume()");

    }

    // NetworkTask에서 값을 가져오는 메소드
    private void connectGetData(String urlAddr) {
        try {
            PeopleNetworkTask peopleNetworkTask = new PeopleNetworkTask(AddressListActivity.this, urlAddr);
            Object obj = peopleNetworkTask.execute().get();
            members = (ArrayList<People>) obj;
            Log.v("here", "" + members);
            adapter = new PeopleAdapter(AddressListActivity.this, R.layout.people_custom_layout, members); // 아댑터에 값을 넣어준다.
            listView.setAdapter(adapter);  // 리스트뷰에 어탭터에 있는 값을 넣어준다.


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 검색을 해주는 메소드
    public void search(String charText) {
        members.clear();    // 멤버에 있는 값을 초기화
        if (charText.length() == 0) {
            members.addAll(searchArr);   // 가져온 값을 비어있는 members에 넣는다
        } else {
            for (int i = 0; i < searchArr.size(); i++) {        // 검색할 내용만큼  검색을 한다
                if (searchArr.get(i).getName().contains(charText)) {
                    members.add(searchArr.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();  // 검색할때 매번 리셋해주는 역할을 한다.
    }

//    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
//        String tag = String.valueOf(id);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
//        if (currentFragment != null) {
//            fragmentTransaction.hide(currentFragment);
//        }
//
//        Fragment fragment = fragmentManager.findFragmentByTag(tag);
//        if (fragment == null) {
//            if (id == R.id.navigation_1) {
//                fragment = new FragmentPage1();
//
//            } else if (id == R.id.navigation_2) {
//
//                fragment = new FragmentPage2();
//            } else {
//                fragment = new FragmentPage3();
//            }
//
//            fragmentTransaction.add(R.id., fragment, tag);
//        } else {
//            fragmentTransaction.show(fragment);content_layout
//        }
//
//        fragmentTransaction.setPrimaryNavigationFragment(fragment);
//        fragmentTransaction.setReorderingAllowed(true);
//        fragmentTransaction.commitNow();
//}
//
//
    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.navigation_1) {
                urlAddr2 = urlAddr + "people_query_all2.jsp";
                connectGetData(urlAddr2);

            } else if (id == R.id.navigation_2) {
                String urlAddr5 = urlAddr + "group_people_query_all.jsp";
                connectGetData(urlAddr4);

            } else if (id == R.id.navigation_3){
                urlAddr3 = urlAddr + "favorite_people_query_all.jsp";
                connectGetData(urlAddr3);
            }

            //fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
           // fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }

    View.OnClickListener onCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button1:
                        urlAddr4 = "";
                        String group1 = btnGroup1.getText().toString();

                        urlAddr4 = urlAddr + "group_people_query_all.jsp?email=qkr@naver.com&group=" + group1;
//                        urlAddr4 = urlAddr + "group_people_query_all.jsp?email=" + email + "&group=" + group1;
                        connectGetData(urlAddr4);
                        break;

                case R.id.button2:
                    urlAddr4 = "";
                    String group2 = btnGroup2.getText().toString();

                    urlAddr4 = urlAddr + "group_people_query_all.jsp?email=qkr@naver.com&group=" + group2;
                    connectGetData(urlAddr4);
                    break;

            }
        }
    };

}