package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.Task.NetworkTask;
import com.android.address_book.R;

import org.w3c.dom.Text;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 ID 찾기 화면                                   =======================
======================                               (결과창 Dialog)                                  =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class FindIDActivity extends AppCompatActivity {

    EditText phone, name;
    TextView fieldCheck;
    String macIP, urlAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

//        Intent intent = getIntent();
//        macIP = intent.getStringExtra("macIP");

        macIP = "192.168.219.154";
        urlAddr = "http://" + macIP + ":8080/test/";

        name = findViewById(R.id.name_findId);
        phone = findViewById(R.id.phone_findId);
        fieldCheck = findViewById(R.id.tv_fieldCheck_findId);

        findViewById(R.id.backBtn_findId).setOnClickListener(mClickListener);
        findViewById(R.id.btnSendMsg_findId).setOnClickListener(mClickListener);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.backBtn_findId:
                    finish();
                    break;

                case R.id.btnSendMsg_findId:
                    userInfoCheck();
                    break;
            }
        }
    };

    // user 정보 확인
    private void userInfoCheck(){

       String userName = name.getText().toString();
       String userPhone = phone.getText().toString();

       if(userName.trim().length() == 0){
            fieldCheck.setText("이름을 입력해주세요");
       } else if(userPhone.trim().length() == 0){
           fieldCheck.setText("휴대폰 번호을 입력해주세요");
       } else{
           urlAddr = urlAddr + "user_query_all.jsp?name=" + userName +"&phone=" + userPhone;
           connectSelectData(urlAddr);
       }

    }

    private void connectSelectData(String urlAddr){
        try{
            NetworkTask selectNetworkTask = new NetworkTask(FindIDActivity.this, urlAddr, "select");
            selectNetworkTask.execute().get();

        } catch (Exception e){
            e.printStackTrace();

        }
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

} //---------