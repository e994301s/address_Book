package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.NetworkTask;
import com.android.address_book.R;
import com.android.address_book.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

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

    final static String TAG = "FindIDActivity";

    LinearLayout layoutSMS;
    EditText phone, name, codeNum;
    TextView fieldCheck;
    String macIP, urlAddr, userEmail;
    ArrayList<User> users;
    //String SMSContents = "1234";
    String smsCode = createSMSCode();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            Log.d(TAG, "=== sms전송을 위한 퍼미션 확인 ===" );

            // For device above MarshMallow
            boolean permission = getWritePermission();
            if(permission) {
                // If permission Already Granted
                // Send You SMS here
                Log.d(TAG, "=== 퍼미션 허용 ===" );
            }
        }
        else{
            // Send Your SMS. You don't need Run time permission
            Log.d(TAG, "=== 퍼미션 필요 없는 버전임 ===" );
        }


//        Intent intent = getIntent();
//        macIP = intent.getStringExtra("macIP");

        macIP = "192.168.219.154";
        urlAddr = "http://" + macIP + ":8080/test/";

        name = findViewById(R.id.name_findId);
        phone = findViewById(R.id.phone_findId);
        fieldCheck = findViewById(R.id.tv_fieldCheck_findId);
        layoutSMS = findViewById(R.id.linearSMS_findId);
        codeNum = findViewById(R.id.sendNum_findId);

        findViewById(R.id.backBtn_findId).setOnClickListener(mClickListener);
        findViewById(R.id.btnSendMsg_findId).setOnClickListener(mClickListener);
        findViewById(R.id.btnFindId_findId).setOnClickListener(mClickListener);

    }

    // 문자 인증
    public boolean getWritePermission(){
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 10);
        }
        return hasPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 10: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Permission is Granted
                    // Send Your SMS here
                }
            }
        }
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

                case R.id.btnFindId_findId:
                    String code = codeNum.getText().toString();
                    checkCode(code);
                    break;
            }
        }
    };

    // user 정보 확인
    private void userInfoCheck(){
        int count = 0;

       String userName = name.getText().toString().trim();
       String userPhone = phone.getText().toString().trim();

       if(userName.length() == 0){
            fieldCheck.setText("이름을 입력해주세요");
       } else if(userPhone.length() == 0){
           fieldCheck.setText("휴대폰 번호을 입력해주세요");
       } else{
           urlAddr = urlAddr + "user_query_all.jsp?name=" + userName +"&phone=" + userPhone;
           users = connectSelectData(urlAddr);

           for(int i =0; i<users.size(); i++){
               if(userName.equals(users.get(i).getUserName()) && userPhone.equals(users.get(i).getUserPhone())){
                   userEmail = users.get(i).getUserEmail();
                   count ++;
               }
           }
               Log.v(TAG, Integer.toString(count));
           if(count == 0){
               fieldCheck.setText("일치하는 정보가 없습니다. \n이름 또는 휴대폰 번호를 다시 입력해주세요");
               name.setText("");
               phone.setText("");
           } else{
               //sendSMS(userPhone, "[1234] 발송");
               sendMessage(userPhone);
               fieldCheck.setText("");
               layoutSMS.setVisibility(View.VISIBLE);
               Toast.makeText(FindIDActivity.this, "문자 발송하였습니다.", Toast.LENGTH_SHORT).show();

           }
       }

    }

    // user Info 검색
    private ArrayList<User> connectSelectData(String urlAddr){
        ArrayList<User> user = null;

        try{
            NetworkTask selectNetworkTask = new NetworkTask(FindIDActivity.this, urlAddr, "select");
            Object obj = selectNetworkTask.execute().get();
            user = (ArrayList<User>) obj;

        } catch (Exception e){
            e.printStackTrace();

        }
        return user;
    }


    // 문자 발송
    private void sendMessage(String phoneNo){
        try {
            Log.d(TAG, "=== 문자 전송 시작 ===" );

            //전송
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, "[주소록]의 인증번호는 "+getSMSCode() +"입니다.", null, null); //SMSContents앞서 전역변수로 입력한, 번호 [랜덤숫자 생성] 포스팅의 메서드를 활용하여 넣으면, 랜덤으로 숫자가 보내진다.
            //
            Log.d(TAG, "=== 문자 전송 완료 ===" );

            //countDownTimer(); [카운트다운 시간재기]포스팅에서 확인할 수 있다.


        } catch (Exception e) {
            Log.d(TAG, "=== 문자 전송 실패 === 에러코드 e : "+e );
            e.printStackTrace();

//            sendCan=false;
//            Log.d(TAG, "=== sendCan === :" +sendCan );
        }
    }


    // 문자 랜덤 코드
    public String getSMSCode() {
        return smsCode;
    } //생성된 인증코드 반환

    private String createSMSCode() { // 인증코드 생성
        String[] str = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String newCode = new String();

        for (int x = 0; x < 6; x++) {
            int random = (int) (Math.random() * str.length);
            newCode += str[random];
        }

        return newCode;
    }

    private void checkCode(String code){
        if(code.equals(smsCode)){
            Toast.makeText(FindIDActivity.this, "일치", Toast.LENGTH_SHORT).show();
            alertCheck();
            finish();

        } else{
            codeNum.setText("");
            Toast.makeText(FindIDActivity.this, "인증코드 다시 입력해주세요.", Toast.LENGTH_SHORT).show();

        }
    }

    // id 알람 발생
    private void alertCheck(){
        new androidx.appcompat.app.AlertDialog.Builder(FindIDActivity.this)
                .setTitle("알람")
                .setMessage( name.getText().toString().trim() + "님의 Email은 \n" + userEmail + " 입니다.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false) // 버튼으로만 대화상자 닫기가 된다. (미작성 시 다른부분 눌러도 대화상자 닫힌다)
                .setPositiveButton("닫기", null)  // 페이지 이동이 없으므로 null
                .show();
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