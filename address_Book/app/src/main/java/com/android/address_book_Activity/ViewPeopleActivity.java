package com.android.address_book_Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.NetworkTask;
import com.android.Task.SQLite;
import com.android.address_book.R;

 /*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 연락처 보기 화면                                 =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class ViewPeopleActivity extends Activity {

    final static String TAG = "ViewPeopleActivity";
    String urlAddr = null;
    String IP; // MainActivity에서 넘겨줌
    String useremail, peoplename, peopleemail, peoplerelation, peoplememo, peopleimage, phonetel;
    int peopleno, phoneno, peoplefavorite, peopleemg;
    int result;
    Button backToList, btn_edit_addressView, btn_view_dial, btn_view_message = null;
    ImageView iv_viewPeople;
    ImageButton btn_view_favorite, btn_view_emergency;
    TextView view_name, view_phone, view_email, view_relation, view_memo;
    SQLite favorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_people);

        Intent intent = getIntent();
        IP = intent.getStringExtra("IP");
        //urlAddr = "http://" + IP + ":8080/address/people_query_all.jsp";
        urlAddr = "http://" + IP + ":8080/address/";

        peopleno = intent.getIntExtra("peopleno", 0);
        phoneno = intent.getIntExtra("phoneno", 0);
        peoplename = intent.getStringExtra("peoplename");
        peopleemail = intent.getStringExtra("peopleemail");
        useremail = intent.getStringExtra("useremail");
        peoplerelation = intent.getStringExtra("peoplerelation");
        peoplememo = intent.getStringExtra("peoplememo");
        peopleimage = intent.getStringExtra("peopleimage");
        phonetel = intent.getStringExtra("phonetel");
        peoplefavorite = intent.getIntExtra("peoplefavorite", 0);
        peopleemg = intent.getIntExtra("peopleemg", 0);


        // 클래스가 바뀌어도 memberinfo는 모든 클래스에 들어가 있어야 한다!
        // SQLite는 서버가 아닌 로컬DB이기 때문에 일일히 알려줘야 한다!
//        favorite = new SQLite(ViewPeopleActivity.this);

        backToList = findViewById(R.id.btn_backToList);
        btn_edit_addressView = findViewById(R.id.btn_edit_addressView);
        btn_view_dial = findViewById(R.id.btn_view_dial);
        btn_view_message = findViewById(R.id.btn_view_message);

        backToList.setOnClickListener(OnclickListener);
        btn_edit_addressView.setOnClickListener(OnclickListener);
        btn_view_dial.setOnClickListener(OnclickListener);
        btn_view_message.setOnClickListener(OnclickListener);


    } // onCreate 끝 -----------------------------------------------------------------------

    View.OnClickListener OnclickListener = new View.OnClickListener() {

        // DB 선언
        SQLiteDatabase DB;

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) { // List로 이동
                case R.id.btn_backToList:
                    intent = new Intent(ViewPeopleActivity.this, AddressListActivity.class); //화면 이동시켜주기
                    intent.putExtra("IP", IP); //값 넘겨주기
                    startActivity(intent); //이동시킨 화면 시작
                    break;
                case R.id.btn_edit_addressView: // 연락처 수정/삭제 페이지로 이동
                    intent = new Intent(ViewPeopleActivity.this, ModifyPeopleActivity.class); //화면 이동시켜주기
                    intent.putExtra("IP", IP); //값 넘겨주기

                    // List에서 받아온 파라미터 넘겨주기!!!!!!!!!!!!!!!
                    // peopleno & phoneno
                    intent.putExtra("peopleno", peopleno); //값 넘겨주기
                    intent.putExtra("peoplename", peoplename); //값 넘겨주기
                    intent.putExtra("peopleemail", peopleemail); //값 넘겨주기
                    intent.putExtra("peoplerelation", peoplerelation); //값 넘겨주기
                    intent.putExtra("peoplememo", peoplememo); //값 넘겨주기
                    intent.putExtra("peopleimage", peopleimage); //값 넘겨주기
                    intent.putExtra("phonetel", phonetel); //값 넘겨주기


                    startActivity(intent);
                    break;
                case R.id.btn_view_dial: // 지정된 전화로 이동
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-1234-5678"));
                    startActivity(intent);
                    break;
                case R.id.btn_view_message: // 문자로 이동
                    Uri uri = Uri.parse("smsto:" + phonetel); // 상대방 번호 연결 → 값 받아서 연결 추가
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", "The SMS text");
                    startActivity(it);
                    break;
                case R.id.btn_view_favorite: // 즐겨찾기 추가/해제
                    favoriteCheck();
                    break;
                case R.id.btn_view_emergency: // 긴급연락처 추가/해제
                    emergencyCheck(useremail, peopleno);
                    break;

            }
        }
    };

    // 즐겨찾기 1인지 0인지 판단
    public void favoriteCheck() {
        //int result = 0;
        // INTENT 받아온 값 추가
        //String query = "select count(peoplefavorite) where userinfo_useremial=" + userinfo_useremail + "and people_peopleno =" + people_peopleno;

         String urlAddr1 = "";
         //urlAddr1 = urlAddr + "people_query_SelectFavorite.jsp?usremail=" + useremail + "&peopleno=" + peopleno;

        if (peoplefavorite == 0) { // 0이라면 1로 세팅
            urlAddr1 = urlAddr + "people_query_Favorite.jsp?peoplefavorite=1&peopleno=" + peopleno;
            String result = connectCheckData(urlAddr1);
            peoplefavorite = 1;

            Toast.makeText(ViewPeopleActivity.this, peoplename + "이 즐겨찾기에 등록되었습니다.", Toast.LENGTH_SHORT).show();

        } else if(peoplefavorite == 1) { // 이미 있다면 0으로 세팅
            urlAddr1 = urlAddr + "people_query_Favorite.jsp?peoplefavorite=0&peopleno=" + peopleno;
            String result = connectCheckData(urlAddr1);
            peoplefavorite = 0;

            Toast.makeText(ViewPeopleActivity.this, peoplename + "이 즐겨찾기에서 해제되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

        //connection FavoriteCheck Data
        private String connectCheckData (String urlAddr1){
            String result = null;
            try {
                NetworkTask insertNetworkTask = new NetworkTask(ViewPeopleActivity.this, urlAddr, "favoriteCount");
                Object obj = insertNetworkTask.execute().get();
                result = (String) obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }


    // 긴급연락처 1인지 0인지 판단
    public void emergencyCheck(String useremail, int peopleno) {
        //int result = 0;
        // INTENT 받아온 값 추가
        //String query = "select count(peoplefavorite) where userinfo_useremial=" + userinfo_useremail + "and people_peopleno =" + people_peopleno;

        String urlAddr1 = "";
       // urlAddr1 = urlAddr + "people_query_SelectEmergency.jsp?usremail=" + useremail + "&peopleno=" + peopleno;


        if (peopleemg == 0) { // 0이라면 1로 세팅
            urlAddr1 = urlAddr + "people_query_Emergency.jsp?peopleemg=1&peopleno=" + peopleno;
            peopleemg = 1;
            String result = connectEmgCheckData(urlAddr1);

            Toast.makeText(ViewPeopleActivity.this, peoplename + "이 긴급연락처에 등록되었습니다.", Toast.LENGTH_SHORT).show();

        } else if(peopleemg ==1) { // 이미 있다면 0으로 세팅
            urlAddr1 = urlAddr + "people_query_Emergency.jsp?peopleemg=0&peopleno=" + peopleno;
            String result = connectEmgCheckData(urlAddr1);
            peopleemg = 0;

            Toast.makeText(ViewPeopleActivity.this, peoplename + "이 긴급연락처에서 해제되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //connection Emergency Check Data
    private String connectEmgCheckData (String urlAddr){
        String result = null;

        try {
            NetworkTask insertNetworkTask = new NetworkTask(ViewPeopleActivity.this, urlAddr, "emergencyCount");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }



} // 끝 ------------------------------------------------------------------------------------