package com.android.address_book_Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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
    ArrayList<Integer> phoneno;
//    int phoneno;
    Spinner edit_spinner_relation;
    Button btn_updatePeople;
    ImageButton btn_backToViewPeople, btn_remove;
    WebView editImage;
    TextView tv_editPeopleImage;
    EditText editName,editPhone,editEmail,editMemo;
    String no, name, email, relation, memo, macIP;
    ArrayList<People> members;
    String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_people);

        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        Log.v(TAG, macIP);

       // urlAddr = "http://" + IP + ":8080/address/people_query_Update.jsp";
        urlAddr = "http://" + macIP + ":8080/test/";
        urlImage = urlAddr;
        Log.v(TAG, urlAddr);

        peopleno = intent.getStringExtra("peopleno");
        useremail = intent.getStringExtra("useremail");

        urlAddr2 = urlAddr + "people_query_all_no.jsp?email="+useremail+"&peopleno=" + peopleno;
        // Task 연결
        members = connectSelectedData(urlAddr2);

        // get Data // set Text
        phoneno = members.get(0).getPhoneno();





        // get Data // set Text
        peoplename = members.get(0).getName();
        editName = findViewById(R.id.edit_peopleName);
        editName.setText(peoplename);

        phonetel = members.get(0).getTel();
        editPhone = findViewById(R.id.edit_peoplePhone);
        editPhone.setText(phonetel.get(0));

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
        WebSettings webSettings = editImage.getSettings();

        // 화면 비율
        webSettings.setUseWideViewPort(true);       // wide viewport를 사용하도록 설정
        webSettings.setLoadWithOverviewMode(true);  // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        //iv_viewPeople.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        editImage.setBackgroundColor(0); //배경색

        editImage.setHorizontalScrollBarEnabled(false); //가로 스크롤
        editImage.setVerticalScrollBarEnabled(false);   //세로 스크롤

        editImage.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 스크롤 노출 타입
        editImage.setScrollbarFadingEnabled(false);

        // 웹뷰 멀티 터치 가능하게 (줌기능)
        webSettings.setBuiltInZoomControls(false);   // 줌 아이콘 사용
        webSettings.setSupportZoom(false);



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
                    peoplename=editName.getText().toString();
                    peopleemail=editEmail.getText().toString();
                    peoplememo=editMemo.getText().toString();
                    phonetel.set(0, editPhone.getText().toString());

                    updatePeople(peopleno, peoplename, peopleemail, peoplerelation, peoplememo, peopleimage, phoneno, phonetel);
                    break;
                case R.id.btn_remove: // delete
                             new AlertDialog.Builder(ModifyPeopleActivity.this)
                            .setIcon(R.drawable.ic_launcher_background)
                            .setMessage("정말 삭제하시겠습니까?\n삭제한 정보는 복구가 불가능합니다.")
                            .setPositiveButton("취소", null)
                            .setNegativeButton("삭제", mclick)
                            .show();
                    //deletePeople(peopleno, phoneno);
//                    urlAddr = "http://" + IP + ":8080/address/people_query_Delete.jsp?";
//                    urlAddr = urlAddr + "no=" + peopleno+ "&phoneno" + phoneno;
//                    connectDeleteData(urlAddr);
                    break;
            }
        }
    };

    DialogInterface.OnClickListener mclick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            deletePeople(peopleno, phoneno);
        }
    };

    // people Update data 송부
    private void updatePeople(String peopleno, String peoplename, String peopleemail, String peoplerelation, String peoplememo, String peopleimage, ArrayList<Integer> phoneno, ArrayList<String> phonetel){
        String urlAddr1 = "";
        urlAddr1 = urlAddr + "people_query_Update.jsp?" + "no="+peopleno+"&name="+peoplename+"&email="+peopleemail+"&relation="+peoplerelation+"&memo="+peoplememo+"&phoneno="+phoneno.get(0)+"&phonetel="+phonetel.get(0);
        connectUpdateData(urlAddr1);


//        if(result.equals("1")){
//            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
//
//        } else{
//            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보 수정이 실패했습니다. \n같은 문제가 지속적으로 발생하면 고객센터에 문의주세요.", Toast.LENGTH_SHORT).show();
//        }
        //finish();

    } // people Delete data 송부
    private void deletePeople(String peopleno, ArrayList<Integer> phoneno){

        String urlAddr1 = "";
        String urlAddr3 = "";
        urlAddr1 = urlAddr + "people_query_Delete1.jsp?peopleno=" + peopleno+ "&phoneno=" + phoneno;
        connectDeleteData(urlAddr1);
        urlAddr3 = urlAddr + "people_query_Delete2.jsp?peopleno=" + peopleno+ "&phoneno=" + phoneno;
        connectDeleteData(urlAddr3);

//        if(result.equals("1")){
//            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
//
//        } else{
//            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보 삭제가 실패했습니다. \n같은 문제가 지속적으로 발생하면 고객센터에 문의주세요.", Toast.LENGTH_SHORT).show();
//
//        }

        finish();

    }

    // connection Update people
    private void connectUpdateData(String urlAddr){
        try {
            PeopleNetworkTask updateNetworkTask = new PeopleNetworkTask(ModifyPeopleActivity.this,urlAddr);
            updateNetworkTask.execute().get();

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    // connection Delete people
    private void connectDeleteData(String urlAddr) {
//        String result = null;

        try {
            PeopleNetworkTask delNetworkTask = new PeopleNetworkTask(ModifyPeopleActivity.this, urlAddr);
            delNetworkTask.execute().get();
           // result = (String) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  return result;
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
            urlImage = urlImage+"ic_defaultpeople.jpg";
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