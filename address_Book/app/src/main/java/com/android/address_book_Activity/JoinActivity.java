package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.address_book.R;
import com.google.android.material.textfield.TextInputLayout;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 회원가입 화면                                   =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class JoinActivity extends AppCompatActivity {

    EditText inputLayoutEmail;
    Button backBtn, submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        TextInputLayout inputLayoutPW = findViewById(R.id.InputLayoutPw_join);
        TextInputLayout inputLayoutPWCheck = findViewById(R.id.InputLayoutPwCheck_join);

        inputLayoutPW.setPasswordVisibilityToggleEnabled(true);
        inputLayoutPWCheck.setPasswordVisibilityToggleEnabled(true);

        inputLayoutEmail = findViewById(R.id.email_join);

        inputLayoutEmail.addTextChangedListener(changeListener);
    }


    // email 입력란 text 변경 시 listener
    TextWatcher changeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateEdit(s);
        }
    };


    // email 형식 일치 확인
    private void validateEdit(Editable s){
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
            inputLayoutEmail.setError("이메일 형식으로 입력해주세요.");
        } else{
            inputLayoutEmail.setError(null);         //에러 메세지 제거
        }
    }

}