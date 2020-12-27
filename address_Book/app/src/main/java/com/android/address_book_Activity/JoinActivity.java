package com.android.address_book_Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.NetworkTask;
import com.android.address_book.R;
import com.android.address_book.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

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

    final static String TAG = "JoinActivity";

    EditText email, name, pw, pwCheck, phone;
    TextView pwCheckMsg;
    String macIP, urlAddr;
    String emailInput = null;
    int btnCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

//        Intent intent = getIntent();
//        macIP = intent.getStringExtra("macIP");
        macIP = "192.168.219.154";
        urlAddr = "http://" + macIP + ":8080/test/";

        TextInputLayout inputLayoutPW = findViewById(R.id.InputLayoutPw_join);
        TextInputLayout inputLayoutPWCheck = findViewById(R.id.InputLayoutPwCheck_join);

        inputLayoutPW.setPasswordVisibilityToggleEnabled(true);
        inputLayoutPWCheck.setPasswordVisibilityToggleEnabled(true);

        email = findViewById(R.id.email_join);
        name = findViewById(R.id.name_join);
        pw = findViewById(R.id.pw_join);
        pwCheck = findViewById(R.id.pwCheck_join);
        phone = findViewById(R.id.phone_join);
        pwCheckMsg = findViewById(R.id.tv_pwCheckMsg_join);

        findViewById(R.id.backBtn_join).setOnClickListener(mClickListener);
        findViewById(R.id.btnEmailCheck_join).setOnClickListener(mClickListener);
        findViewById(R.id.submitBtn_join).setOnClickListener(mClickListener);

        pwCheck.addTextChangedListener(changeListener1);
        email.addTextChangedListener(changeListener);
    }

    // button 클릭 시
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                // backButton 클릭 시 화면 JoinActivity 종료
                case R.id.backBtn_join:
                    finish();
                    break;

                // email 중복 체크
                case R.id.btnEmailCheck_join:
                    emailInput = email.getText().toString().trim();
                    emailCheck(emailInput);

                    break;

                // 완료 버튼 클릭 시
                case R.id.submitBtn_join:
                    checkField();
                    break;
            }

        }
    };

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
            // email 입력 시
            if(email.getText().toString().trim().length() != 0){
                validateEdit(s);
            }
        }
    };

    // pw 입력란 text 변경 시 listener
    TextWatcher changeListener1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // pwcheck 입력 시 일치 여부 message
            if(pwCheck.getText().toString().trim().length() != 0){
                if((pwCheck.getText().toString().trim()).equals(pw.getText().toString().trim())){
                    pwCheckMsg.setTextColor(getResources().getColor(R.color.blue));
                    pwCheckMsg.setText("비밀번호 일치");

                } else {
                    pwCheckMsg.setTextColor(getResources().getColor(R.color.red));
                    pwCheckMsg.setText("비밀번호 불일치");
                }
            }
        }
    };


    // email 형식 일치 확인
    private void validateEdit(Editable s){
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
            email.setError("이메일 형식으로 입력해주세요.");
        } else{
            email.setError(null);         //에러 메세지 제거
        }
    }

    // email 중복 체크
    private void emailCheck(String emailInput){
        int count = 0;

        if (emailInput.length() == 0) {
            Toast.makeText(JoinActivity.this, "Email 입력해주세요.", Toast.LENGTH_SHORT).show();

        } else {
            String urlAddr2 = "";
            urlAddr2 = urlAddr + "user_query_all.jsp?email=" + emailInput;

            Log.v(TAG, "email : " + emailInput);

            ArrayList<User> result = connectSelectData(urlAddr2);

            for(int i =0; i<result.size(); i++){
                if(emailInput.equals(result.get(i).getUserEmail())){
                    count ++;
                }
            }

            if (count == 0) {
                Toast.makeText(JoinActivity.this, "Email 사용이 가능합니다.", Toast.LENGTH_SHORT).show();
                btnCheck = 1;
            } else {
                Toast.makeText(JoinActivity.this, "동일한 Email이 존재합니다.", Toast.LENGTH_SHORT).show();
                btnCheck = 0;
            }
        }

    }


    // 입력란 field check
    private void checkField(){
        if(name.getText().toString().trim().length() == 0){
            alertCheck("이름을");
            name.setFocusableInTouchMode(true);
            name.requestFocus();

        } else if(email.getText().toString().trim().length() == 0){
            alertCheck("이메일을");
            email.setFocusableInTouchMode(true);
            email.requestFocus();

        } else if(pw.getText().toString().trim().length() == 0){
            alertCheck("비밀번호를");
            pw.setFocusableInTouchMode(true);
            pw.requestFocus();

        } else if(pwCheck.getText().toString().trim().length() == 0){
            alertCheck("비밀번호 확인을");
            pwCheck.setFocusableInTouchMode(true);
            pwCheck.requestFocus();

        } else if(phone.getText().toString().trim().length() == 0){
            alertCheck("전화번호를");
            phone.setFocusableInTouchMode(true);
            phone.requestFocus();

        } else{
            String userName = name.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPW = pw.getText().toString().trim();
            String userPhone = phone.getText().toString().trim();

            if(btnCheck == 1) {
                if ((pwCheck.getText().toString().trim()).equals(pw.getText().toString().trim())) {
                    insertUser(userName, userEmail, userPW, userPhone);

                } else {
                    pwCheck.setText("");
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다. \n다시 확인해주세요.", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(JoinActivity.this, "Email 중복 채크 해주세요.", Toast.LENGTH_SHORT).show();

            }

        }
    }

    // 미입력 시 알람 발생
    private void alertCheck(String field){
        new AlertDialog.Builder(JoinActivity.this)
                .setTitle("알람")
                .setMessage(field + " 입력해주세요.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false) // 버튼으로만 대화상자 닫기가 된다. (미작성 시 다른부분 눌러도 대화상자 닫힌다)
                .setPositiveButton("닫기", null)  // 페이지 이동이 없으므로 null
                .show();
    }

    // user 입력 data 송부
    private void insertUser(String userName, String userEmail, String userPW, String userPhone){
        String urlAddr1 = "";
        urlAddr1 = urlAddr + "userInfoInsert.jsp?name=" + userName + "&email=" + userEmail + "&pw=" + userPW + "&phone=" + userPhone;

        String result = connectInsertData(urlAddr1);

        if(result.equals("1")){
            Toast.makeText(JoinActivity.this, userName + "님 회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

        } else{
            Toast.makeText(JoinActivity.this, userName + "님 회원가입 실패하였습니다.", Toast.LENGTH_SHORT).show();

        }

        //finish();

    }

    //connection Insert
    private String connectInsertData(String urlAddr){
        String result = null;

        try{
            NetworkTask insertNetworkTask = new NetworkTask(JoinActivity.this, urlAddr, "insert");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e){
            e.printStackTrace();

        }
        return result;
    }

    //connection Select
    private ArrayList<User> connectSelectData(String urlAddr){
        ArrayList<User> result1 = null;

        try{
            NetworkTask selectNetworkTask = new NetworkTask(JoinActivity.this, urlAddr, "select");
            Object obj = selectNetworkTask.execute().get();
            result1 = (ArrayList<User>) obj;

        } catch (Exception e){
            e.printStackTrace();

        }
        return result1;
    }

    // 화면 touch 시 키보드 숨기
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}