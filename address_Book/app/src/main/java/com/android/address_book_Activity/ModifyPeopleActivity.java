package com.android.address_book_Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.CUDNetworkTask;
import com.android.Task.PeopleNetworkTask;
import com.android.address_book.People;
import com.android.address_book.R;

import java.util.ArrayList;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 연락처 수정/삭제 화면                                     =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/


public class ModifyPeopleActivity extends Activity {

    final static String TAG = "Update";
    String urlAddr, urlAddr2 = null;
    String peopleno;
    String useremail;
    String peoplename;
    String peopleemail;
    String peoplerelation;
    String peoplememo;
    String peopleimage;
    ArrayList<String> phonetel;
    int phoneno;
    Spinner edit_spinner_relation;
    Button btn_updatePeople;
    ImageButton btn_backToViewPeople, btn_remove;
    WebView editImage;
    TextView tv_editPeopleImage;
    EditText editName,editPhone,editEmail,editMemo;
    String no, name, email, relation, memo, IP;
    ArrayList<People> members;
    String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_people);

        Intent intent = getIntent();
//        IP = intent.getStringExtra("IP");
//        Log.v(TAG, IP);

       // urlAddr = "http://" + IP + ":8080/address/people_query_Update.jsp";
        urlAddr = "http://192.168.0.96:8080/test/";
        urlImage = urlAddr;
        Log.v(TAG, urlAddr);

        peopleno = intent.getStringExtra("peopleno");
        useremail = intent.getStringExtra("useremail");
        phoneno = intent.getIntExtra("phoneno", 0);
        urlAddr2 = "http://192.168.0.96:8080/test/people_query_selected.jsp?email="+useremail+"&peopleno=" + peopleno;

        // Task 연결
        members = connectSelectedData(urlAddr2);


        // get Data // set Text
        peoplename = members.get(0).getName();
        editName = findViewById(R.id.edit_peopleName);
        editName.setText(peoplename);

        phonetel = members.get(0).getTel();
        editPhone = findViewById(R.id.edit_peoplePhone);
        editPhone.setText((CharSequence) phonetel);

        peopleemail = members.get(0).getEmail();
        editEmail = findViewById(R.id.edit_peopleEmail);
        editEmail.setText(peopleemail);

//        peoplerelation = members.get(0).getRelation();
//        view_relation = findViewById(R.id.view_relation);
//        view_relation.setText(peoplerelation);

        peoplememo = members.get(0).getMemo();
        editMemo = findViewById(R.id.edit_peopleMemo);
        editMemo.setText(peoplememo);

        // Web View에 이미지 띄움
        editImage = findViewById(R.id.iv_editPeopleImage);
        editImage.getSettings().setJavaScriptEnabled(true);
        imageCheck();


        tv_editPeopleImage = findViewById(R.id.tv_editPeopleImage);

        btn_backToViewPeople = findViewById(R.id.btn_backToViewPeople);
        btn_backToViewPeople.setImageResource(R.drawable.ic_back);
        btn_updatePeople = findViewById(R.id.btn_updatePeople);
        btn_remove = findViewById(R.id.btn_remove);
        btn_remove.setImageResource(R.drawable.ic_remove);

        btn_backToViewPeople.setOnClickListener(onClickListener);
        btn_updatePeople.setOnClickListener(onClickListener);
        btn_remove.setOnClickListener(onClickListener);

    } // onCreate end -------------------------------------------------------------------

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btn_backToViewPeople: // view로 돌아가기
                    finish();
//                    intent = new Intent(ModifyPeopleActivity.this, ViewPeopleActivity.class); //화면 이동시켜주기
//                    intent.putExtra("IP", IP); //값 넘겨주기
//                    intent.putExtra("peopleno", peopleno); //값 넘겨주기
//                    intent.putExtra("phonetel", phonetel); //값 넘겨주기
//                    startActivity(intent); //이동시킨 화면 시작
                    break;
                case R.id.btn_updatePeople: // update
//                    urlAddr = "http://" + IP + ":8080/address/people_query_Update.jsp?";
//                    urlAddr = urlAddr +"no="+peopleno+"&name="+peoplename+"&email="+peopleemail+"&relation="+peoplerelation+"&memo"+peoplememo+"&phoneno"+phoneno+"&phonetel"+phonetel;
//                    connectUpdateData(urlAddr);
                    updatePeople(peopleno, peoplename, peopleemail, peoplerelation, peoplememo, peopleimage, phoneno, phonetel);
                    break;
                case R.id.btn_remove: // delete
                    deletePeople(peopleno, phoneno);
//                    urlAddr = "http://" + IP + ":8080/address/people_query_Delete.jsp?";
//                    urlAddr = urlAddr + "no=" + peopleno+ "&phoneno" + phoneno;
//                    connectDeleteData(urlAddr);
                    break;
            }
        }
    };


    // people Update data 송부
    private void updatePeople(String peopleno, String peoplename, String peopleemail, String peoplerelation, String peoplememo, String peopleimage, int phoneno, ArrayList<String> phonetel){
        String urlAddr1 = "";
        urlAddr1 = urlAddr + "people_query_Update.jsp?" + "no="+peopleno+"&name="+peoplename+"&email="+peopleemail+"&relation="+peoplerelation+"&memo"+peoplememo+"&phoneno"+phoneno+"&phonetel"+phonetel;

        String result = connectUpdateData(urlAddr1);

        if(result.equals("1")){
            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();

        } else{
            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보 수정이 실패했습니다. \n같은 문제가 지속적으로 발생하면 고객센터에 문의주세요.", Toast.LENGTH_SHORT).show();
        }
        //finish();

    } // people Delete data 송부
    private void deletePeople(String peopleno, int phoneno){
        String urlAddr1 = "";
        urlAddr1 = urlAddr + "people_query_Delete.jsp?" + "no=" + peopleno+ "&phoneno=" + phoneno;

        String result = connectDeleteData(urlAddr1);

        if(result.equals("1")){
            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();

        } else{
            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보 수정이 실패했습니다. \n같은 문제가 지속적으로 발생하면 고객센터에 문의주세요.", Toast.LENGTH_SHORT).show();

        }

        //finish();

    }

    // connection Update people
    private String connectUpdateData(String urlAddr){
        String result = null;

        try {
            CUDNetworkTask updateNetworkTask = new CUDNetworkTask(ModifyPeopleActivity.this,urlAddr, "modifyPeople");
            Object obj =  updateNetworkTask.execute().get();
            result = (String) obj;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // connection Delete people
    private String connectDeleteData(String urlAddr) {
        String result = null;

        try {
            CUDNetworkTask delNetworkTask = new CUDNetworkTask(ModifyPeopleActivity.this, urlAddr);
            Object obj =  delNetworkTask.execute().get();
            result = (String) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void imageCheck() {
        // INTENT 받아온 값 추가
        //String query = "select count(peoplefavorite) where userinfo_useremial=" + userinfo_useremail + "and people_peopleno =" + people_peopleno;

        String urlAddr1 = "";
        //urlAddr1 = urlAddr + "people_query_SelectFavorite.jsp?usremail=" + useremail + "&peopleno=" + peopleno;

        // if (peopleimage.length() == 0) {
//        if (peopleimage.equals("null")) {
        if (peopleimage == null) {
//            urlAddr1 = urlAddr + "people_query_all.jsp?peopleimage=" + peopleimage;
//            String result = connectCheckData(urlAddr1);
            urlImage = urlImage+"ic_defaultpeople.png";
            editImage.loadUrl(urlImage);
            editImage.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            editImage.setWebViewClient(new ViewPeopleActivity.WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용

//        } else if(peopleimage.length() != 0) {
            // } else if(peopleimage.equals("!=null")) {
        } else if(peopleimage != null) {
//            urlAddr1 = urlAddr + "people_query_all.jsp?peopleimage=" + peopleimage;
//            String result = connectCheckData(urlAddr1);
            urlImage = urlImage + peopleimage;
            editImage.loadUrl(urlImage);
            editImage.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            editImage.setWebViewClient(new ViewPeopleActivity.WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
        }
    }
    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    // obj members 가져오기
    private ArrayList<People> connectSelectedData(String urlAddr2) {

        try {
            PeopleNetworkTask peopleNetworkTask = new PeopleNetworkTask(ModifyPeopleActivity.this, urlAddr2);

            Object obj = peopleNetworkTask.execute().get();

            // members에 obj를 줄거야! type은 Arraylist!
            members = (ArrayList<People>) obj;

        } catch (Exception e){
            e.printStackTrace();
        }
        return members;
    }
} // end --------------------------------------------------------------------------------