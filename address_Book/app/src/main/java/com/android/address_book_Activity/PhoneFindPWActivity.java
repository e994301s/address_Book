package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.address_book.R;

public class PhoneFindPWActivity extends AppCompatActivity {

    final static String TAG = "PhoneFindPWActivity";

    TextView field;
    EditText phoneInput, codeNum;
    String name, pw, phone;
    String smsCode = createSMSCode();
    LinearLayout layoutSMS;

    /*카운트 다운 타이머에 관련된 필드*/

    TextView time_counter; //시간을 보여주는 TextView
    EditText emailAuth_number; //인증 번호를 입력 하는 칸
    Button emailAuth_btn; // 인증버튼
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_find_pw);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        pw = intent.getStringExtra("pw");
        phone = intent.getStringExtra("phone");

        phoneInput = findViewById(R.id.phone_phonefindPw);
        field = findViewById(R.id.tv_fieldCheck_phonefindPw);
        layoutSMS = findViewById(R.id.linearSMS_phonefindPw);
        codeNum = findViewById(R.id.sendNum_phonefindPw);

        findViewById(R.id.backBtn_phonefindPw).setOnClickListener(mClickListener);
        findViewById(R.id.btnSendMsg_phonefindPw).setOnClickListener(mClickListener);
        findViewById(R.id.btnFindPw_phonefindPw).setOnClickListener(mClickListener);

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.backBtn_phonefindPw:
                    finish();
                    break;

                case R.id.btnSendMsg_phonefindPw:
                    phoneCheck();
                    break;

                case R.id.btnFindPw_phonefindPw:
                    String code = codeNum.getText().toString().trim();
                    checkCode(code);
                    break;
            }
        }
    };

    private void phoneCheck(){
        String phoneIn = phoneInput.getText().toString().trim();
        field.setText("");

        if(phoneIn.length() == 0){
            field.setText("휴대폰 번호를 입력해주세요.");

        } else {
            field.setText("");
            if(phone.equals(phoneIn)){
                sendMessage(phone);
                countDownTimer();
                layoutSMS.setVisibility(View.VISIBLE);
                Toast.makeText(PhoneFindPWActivity.this, "문자 발송하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                field.setText("휴대폰 번호가 회원정보와 일치하지 않습니다.");
            }
        }

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

    // 인증코드 일치 여부 확인
    private void checkCode(String code){
        if(code.equals(smsCode)){
            Toast.makeText(PhoneFindPWActivity.this, "일치", Toast.LENGTH_SHORT).show();
            alertCheck();

        } else{
            codeNum.setText("");
            Toast.makeText(PhoneFindPWActivity.this, "인증코드 다시 입력해주세요.", Toast.LENGTH_SHORT).show();

        }
    }

    // pw 알람 발생
    private void alertCheck(){
        new androidx.appcompat.app.AlertDialog.Builder(PhoneFindPWActivity.this)
                .setTitle("알람")
                .setMessage( name + "님의 비밀번호는 \n" + pw + " 입니다.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false) // 버튼으로만 대화상자 닫기가 된다. (미작성 시 다른부분 눌러도 대화상자 닫힌다)
                .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 닫기 시 FindIDActivity 종료
                        PhoneFindPWActivity.this.finish();
                    }
                })  // 페이지 이동이 없으므로 null
                .show();
    }

    //카운트 다운 메소드
    public void countDownTimer() {

        time_counter = findViewById(R.id.time_phonefindPw);
        //줄어드는 시간을 나타내는 TextView


        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.

            }


            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료

                finish();

            }
        }.start();
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