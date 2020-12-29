package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    String urlAddr = null;
    EditText loginId;
    EditText loginPw;
    Button loginBtn;
    String useremail, userpw, macIP;
    String urlAddrLoginCheck = null;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        macIP = "192.168.35.251";
        urlAddr = "http://" + macIP + ":8080/test/logincheck.jsp?";


        loginBtn = findViewById(R.id.login_btn);
        loginId = findViewById(R.id.login_id);
        loginPw = findViewById(R.id.login_pw);


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            useremail = loginId.getText().toString();
            userpw = loginPw.getText().toString();

            urlAddr = urlAddr + "useremail=" + useremail + "&userpw=" + userpw;


            count = loginCount();

            Log.v("여기",""+loginCount());

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


}//---------------------------
