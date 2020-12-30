package com.android.address_book_Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.CUDNetworkTask;
import com.android.address_book.R;

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
    String urlAddr = null;
    String peoplename, peopleemail, peoplerelation, peoplememo, peopleimage, phonetel;
    int peopleno, phoneno;
    Button btn_updatePeople;
    ImageButton btn_backToViewPeople, btn_remove;
    ImageView editImage;
    TextView tv_editPeopleImage;
    EditText editName,editPhone,editEmail,editMemo;
    String no, name, email, relation, memo, IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_people);

        Intent intent = getIntent();
        IP = intent.getStringExtra("IP");
        Log.v(TAG, IP);

       // urlAddr = "http://" + IP + ":8080/address/people_query_Update.jsp";
        urlAddr = "http://" + IP + ":8080/test/";
        Log.v(TAG, urlAddr);

        peopleno = intent.getIntExtra("peopleno",0);
        peoplename = intent.getStringExtra("peoplename");
        peopleemail = intent.getStringExtra("peopleemail");
        peoplerelation = intent.getStringExtra("peoplerelation");
        peoplememo = intent.getStringExtra("peoplememo");
        peopleimage = intent.getStringExtra("peopleimage");
        phoneno = intent.getIntExtra("phoneno",0);
        phonetel = intent.getStringExtra("phonetel");

        editName = findViewById(R.id.edit_peopleName);
        editPhone = findViewById(R.id.edit_peoplePhone);
        editEmail = findViewById(R.id.edit_peopleEmail);
        editMemo = findViewById(R.id.edit_peopleMemo);
        editImage = findViewById(R.id.iv_editPeopleImage);
        tv_editPeopleImage = findViewById(R.id.tv_editPeopleImage);

        editName.setText(peoplename);
        editPhone.setText(phonetel);
        editEmail.setText(peopleemail);
        editMemo.setText(peoplememo);
      //  editImage.setImageBitmap(peopleimage);

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
                    intent = new Intent(ModifyPeopleActivity.this, ViewPeopleActivity.class); //화면 이동시켜주기
                    intent.putExtra("IP", IP); //값 넘겨주기
                    intent.putExtra("peopleno", peopleno); //값 넘겨주기
                    intent.putExtra("phonetel", phonetel); //값 넘겨주기
                    startActivity(intent); //이동시킨 화면 시작
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
    private void updatePeople(int peopleno, String peoplename, String peopleemail, String peoplerelation, String peoplememo, String peopleimage, int phoneno, String phonetel){
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
    private void deletePeople(int peopleno, int phoneno){
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
} // end --------------------------------------------------------------------------------