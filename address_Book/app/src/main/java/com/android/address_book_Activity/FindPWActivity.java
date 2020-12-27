package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.address_book.EmailFindPWActivity;
import com.android.address_book.R;

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

    EditText name, email;
    TextView check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

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
        if(name.getText().toString().trim().length() == 0){
            check.setText("이름을 입력해주세요.");
        } else if (email.getText().toString().trim().length() == 0){
            check.setText("이메일을 입력해주세요.");
        } else {
            Intent intent = new Intent(FindPWActivity.this, EmailFindPWActivity.class);
            intent.putExtra("name", name.getText().toString().trim());
            intent.putExtra("email", email.getText().toString().trim());
            finish();
            startActivity(intent);
        }
    }

}