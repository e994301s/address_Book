package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.Task.NetworkTask;
import com.android.address_book.R;
import com.android.address_book.User;
import com.android.method.SendMail;

import java.util.ArrayList;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 PW 찾기 화면                                   =======================
======================                               (결과창 Dialog)                                  =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class FindPWActivity extends AppCompatActivity {

    final static String TAG = "FindPWActivity";

    String user = "pakk7026@gmail.com"; // 보내는 계정의 id
    String password = "kyeongmi7"; // 보내는 계정의 pw

    EditText name, email;
    TextView check;
    String macIP, urlAddr, pw;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        macIP = "192.168.219.154";
        urlAddr = "http://" + macIP + ":8080/test/";

        name = findViewById(R.id.name_findPw);
        email = findViewById(R.id.email_findPw);
        check = findViewById(R.id.tv_fieldCheck_findPw);

        findViewById(R.id.backBtn_findPw).setOnClickListener(mClickListener);
        findViewById(R.id.btnEmailAuth_findPw).setOnClickListener(mClickListener);
        findViewById(R.id.btnPhoneAuth_findPw).setOnClickListener(mClickListener);

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.backBtn_findPw:
                    finish();
                    break;

                case R.id.btnEmailAuth_findPw:
                    emailFieldCheck();

                    break;

                case R.id.btnPhoneAuth_findPw:

                    break;

            }
        }
    };

    // field check
    private void emailFieldCheck(){
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        int count = 0;

        if(userName.length() == 0){
            check.setText("이름을 입력해주세요.");
        } else if (userEmail.length() == 0){
            check.setText("이메일을 입력해주세요.");
        } else {

            urlAddr = urlAddr + "user_query_all.jsp?name=" + userName +"&email=" + userEmail;
            users = connectSelectData(urlAddr);

            for(int i =0; i<users.size(); i++){
                if(userName.equals(users.get(i).getUserName()) && userEmail.equals(users.get(i).getUserEmail())){
                    pw = users.get(i).getUserPW();
                    count ++;
                }
            }

            Log.v(TAG, Integer.toString(count));

            if(count == 0) {
                check.setText("일치하는 정보가 없습니다. \n이름 또는 이메일을 다시 입력해주세요");
                name.setText("");
                email.setText("");

            } else {
                check.setText("");
                SendMail mailServer = new SendMail();

                String code = mailServer.sendSecurityCode(getApplicationContext(), email.getText().toString(), user, password);


                Intent intent = new Intent(FindPWActivity.this, EmailFindPWActivity.class);
                intent.putExtra("name", userName);
                intent.putExtra("user", user);
                intent.putExtra("password", password);
                intent.putExtra("pw", pw);
                intent.putExtra("codeAuth", code);
                finish();
                startActivity(intent);
            }
        }

    }

    // user Info 검색
    private ArrayList<User> connectSelectData(String urlAddr){
        ArrayList<User> user = null;

        try{
            NetworkTask selectNetworkTask = new NetworkTask(FindPWActivity.this, urlAddr, "select");
            Object obj = selectNetworkTask.execute().get();
            user = (ArrayList<User>) obj;

        } catch (Exception e){
            e.printStackTrace();

        }
        return user;
    }


}