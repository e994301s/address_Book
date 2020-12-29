package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Network;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.NetworkTask;
import com.android.address_book.R;
import com.android.address_book.User;

import java.util.ArrayList;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 My Page 화면                                     =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/


public class MypageActivity extends AppCompatActivity {

    EditText currentPW;
    String macIP, urlAddr;
    TextView field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        currentPW = findViewById(R.id.currentPW_changePW);
        field = findViewById(R.id.tv_fieldCheck_changePW);

        macIP = "192.168.0.81";
        urlAddr = "http://" + macIP + ":8080/test/";

        findViewById(R.id.backBtn_changePW).setOnClickListener(mClickListener);
        findViewById(R.id.btnNextChangePW_changePW).setOnClickListener(mClickListener);

    }


    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.backBtn_changePW:
                    finish();
                    break;

                case R.id.btnNextChangePW_changePW:
                    String pw = currentPW.getText().toString().trim();
                    checkPW(pw);
            }
        }
    };

    // 비밀번호 일치 여부 확인
    private void checkPW(String pw){
        int count = 0 ;

        if(pw.length() == 0){
            field.setText("비밀번호를 입력해주세요.");

        } else {
            urlAddr = urlAddr + "user_query_all.jsp";

            ArrayList<User> users = connectSelectData(urlAddr);

            for (int i=0; i<users.size(); i++){
                // 아이디 일치 여부 확인 하기
               if(pw.equals(users.get(i).getUserPW())){
                   count++;
                }
            }

            if (count == 0) {
                field.setText("비밀번호가 일치하지 않습니다.");
                Toast.makeText(MypageActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

            } else {
                field.setText("");
                Toast.makeText(MypageActivity.this, "비밀번호 변경을 위해 다음 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    // connection select
    private ArrayList<User> connectSelectData(String urlAddr){
        ArrayList<User> users = null;

        try {
            NetworkTask networkTask = new NetworkTask(MypageActivity.this, urlAddr, "select");
            Object obj = networkTask.execute().get();
            users = (ArrayList<User>) obj;

        } catch (Exception e){
            e.printStackTrace();
        }

        return users;
    }


}