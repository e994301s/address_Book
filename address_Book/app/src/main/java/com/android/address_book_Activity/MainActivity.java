package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.android.Task.CUDNetworkTask;
import com.android.Task.NetworkTask;
import com.android.address_book.R;


/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 로그인 화면                                     =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class MainActivity extends AppCompatActivity {
    private boolean saveLoginData;
    String urlAddr = null;
    EditText loginId;
    EditText loginPw;
    Button loginBtn;
    String useremail, userpw, macIP;
    String urlAddrLoginCheck = null;
    CheckBox savechb;
    int count = 0;
    private SharedPreferences appData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savechb = findViewById(R.id.save_chb);

        macIP = "192.168.2.14";

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);


        urlAddr = "http://" + macIP + ":8080/test/logincheck.jsp?";


        loginBtn = findViewById(R.id.login_btn);
        loginId = findViewById(R.id.login_id);
        loginPw = findViewById(R.id.login_pw);

        loginBtn.setOnClickListener(onClickListener);
        useremail = loginId.getText().toString();
        userpw = loginPw.getText().toString();

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        if (saveLoginData) {
            loginId.setText(useremail);
            loginPw.setText(userpw);
            savechb.setChecked(saveLoginData);
        }

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, JoinActivity.class);
            startActivity(intent);

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            save();
            useremail = loginId.getText().toString();
            userpw = loginPw.getText().toString();

            urlAddr = urlAddr + "useremail=" + useremail + "&userpw=" + userpw;


            count = loginCount();

            Log.v("여기", "" + loginCount());

            Log.v("아이디", "login : " + useremail + userpw);

            if (count == 1) {
                connectUpdateData();
                Toast.makeText(MainActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddressListActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "아이디와 비밀번호를 확인하세요!", Toast.LENGTH_SHORT).show();

            }


        }
    };


    private int loginCount() {
        try {
            NetworkTask networkTask = new NetworkTask(MainActivity.this, urlAddr, "loginCount");
            Object obj = networkTask.execute().get();

            count = (int) obj;
            Log.v("여기", "loginCount : " + count);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    private String connectUpdateData() {

        String result = null;
        try {
            CUDNetworkTask updnetworkTask = new CUDNetworkTask(MainActivity.this, urlAddr);
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            // - 수정 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = updnetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", savechb.isChecked());
        editor.putString("useremail", loginId.getText().toString().trim());
        editor.putString("userpw", loginPw.getText().toString().trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        useremail = appData.getString("ID", "");
        userpw = appData.getString("PWD", "");


    }



}//---------------------------
