package com.android.address_book_Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.Task.CUDNetworkTask;
import com.android.Task.PeopleNetworkTask;
import com.android.address_book.People;
import com.android.address_book.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    Button btn_updatePeople, add_view;
    ImageButton btn_backToViewPeople, btn_remove;
    WebView editImage;
    TextView tv_editPeopleImage;
    EditText editName,editPhone, editPhone2, editPhone3, editPhone4,editEmail,editMemo;
    String phoneUpdate, urlUpdatePhonenumber, macIP;
    ArrayList<People> members;
    String urlImage;
    int imageCheck=0;
    String strUpdatePhone1=null;
    String strUpdatePhone2=null;
    String strUpdatePhone3=null;
    String strUpdatePhone4=null;
    String strUpdatePhoneMain=null;
    ArrayList<String> totalPhoneNo = new ArrayList<String>();
    int addPhoneCheck = 0;
    int phoneUpdateResult = 0;

    // 사진 올리고 내리기 위한 변수들
    private final int REQ_CODE_SELECT_IMAGE = 100;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    String imageName = null;
    private String f_ext = null;
    File tempSelectFile;

    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_people);

        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");


       // urlAddr = "http://" + IP + ":8080/address/people_query_Update.jsp";
        urlAddr = "http://" + macIP + ":8080/test/";

        urlImage = urlAddr;

        peopleno = intent.getStringExtra("peopleno");
        useremail = intent.getStringExtra("useremail");
//        phoneno = intent.getStringExtra("phoneno");
//        peoplename = intent.getStringExtra("peoplename");
//        peopleemail = intent.getStringExtra("peopleemail");
//        phonetel = intent.getStringExtra("phonetel");
//        peoplememo= intent.getStringExtra("peoplememo");


        urlAddr2 = urlAddr + "people_query_selectModify.jsp?email="+useremail+"&peopleno=" + peopleno;

        // Task 연결
        members = connectSelectedData(urlAddr2);

        // get Data // set Text
        phoneno = members.get(0).getPhoneno();


        // 사진 연결
        url = "http://"+macIP+":8080/test/multipartRequest.jsp";

        tv_editPeopleImage = findViewById(R.id.tv_editPeopleImage);
//        editImage.setOnClickListener(onClickListener);
        tv_editPeopleImage.setOnClickListener(onClickListener);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());



//        // get Data // set Text
        add_view = findViewById(R.id.addUpdateTelNoButton);

        peoplename = members.get(0).getName();
        editName = findViewById(R.id.edit_peopleName);
        editName.setText(peoplename);
//
        phonetel = members.get(0).getTel();
        editPhone = findViewById(R.id.edit_peoplePhone);
        editPhone.setText(phonetel.get(0));
//        editPhone.setText(phonetel);
//
        peopleemail = members.get(0).getEmail();
        editEmail = findViewById(R.id.edit_peopleEmail);
        editEmail.setText(peopleemail);
//
////        peoplerelation = members.get(0).getRelation();
////        view_relation = findViewById(R.id.view_relation);
////        view_relation.setText(peoplerelation);
//
        peoplememo = members.get(0).getMemo();
        editMemo = findViewById(R.id.edit_peopleMemo);
        editMemo.setText(peoplememo);
//
//        // Web View에 이미지 띄움
//        editImage = findViewById(R.id.iv_editPeopleImage);
//        editImage.getSettings().setJavaScriptEnabled(true);
//        imageCheck();
//        WebSettings webSettings = editImage.getSettings();
//
//        // 화면 비율
//        webSettings.setUseWideViewPort(true);       // wide viewport를 사용하도록 설정
//        webSettings.setLoadWithOverviewMode(true);  // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
//        //iv_viewPeople.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//
//        editImage.setBackgroundColor(0); //배경색
//
//        editImage.setHorizontalScrollBarEnabled(false); //가로 스크롤
//        editImage.setVerticalScrollBarEnabled(false);   //세로 스크롤
//
//        editImage.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 스크롤 노출 타입
//        editImage.setScrollbarFadingEnabled(false);
//
//        // 웹뷰 멀티 터치 가능하게 (줌기능)
//        webSettings.setBuiltInZoomControls(false);   // 줌 아이콘 사용
//        webSettings.setSupportZoom(false);



        tv_editPeopleImage = findViewById(R.id.tv_editPeopleImage);

        btn_backToViewPeople = findViewById(R.id.btn_backToViewPeople);
        btn_backToViewPeople.setImageResource(R.drawable.ic_back);
        btn_updatePeople = findViewById(R.id.btn_updatePeople);
        btn_remove = findViewById(R.id.btn_remove);
        btn_remove.setImageResource(R.drawable.ic_remove);

        btn_backToViewPeople.setOnClickListener(onClickListener);
        btn_updatePeople.setOnClickListener(onClickListener);
        btn_remove.setOnClickListener(onClickListener);
        add_view.setOnClickListener(onClickListener);

    } // onCreate end -------------------------------------------------------------------

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.addUpdateTelNoButton:
                    RegisterAddPhoneNumber n_layout = new RegisterAddPhoneNumber(getApplicationContext());
                    LinearLayout con = (LinearLayout)findViewById(R.id.update_phonenum_layout);
                    con.addView(n_layout);
                    add_view.setVisibility(View.INVISIBLE);
                    editPhone = findViewById(R.id.editPhone);
                    editPhone2 = findViewById(R.id.editPhone2);
                    editPhone3 = findViewById(R.id.editPhone3);
                    editPhone4 = findViewById(R.id.editPhone4);
                    addPhoneCheck =1;
                    break;
                case R.id.btn_backToViewPeople: // view로 돌아가기
                    //finish();
                    intent = new Intent(ModifyPeopleActivity.this, ViewPeopleActivity.class); //화면 이동시켜주기
                    intent.putExtra("macIP", macIP); //값 넘겨주기
                    intent.putExtra("peopleno", peopleno); //값 넘겨주기
                    intent.putExtra("phonetel", phonetel); //값 넘겨주기
                    startActivity(intent); //이동시킨 화면 시작
                    break;
                case R.id.btn_updatePeople: // update
                    // 순서 1. 네트워크 연결 및 이미지 서버에 전송, 이미지 이름 저장
                    if(imageCheck==1){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                doMultiPartRequest();
                            }
                        }).start();
                    }
                        Log.v(TAG, "image Name : "+ imageName);

                        // DB와 연결(NetworkTask)해서 정보 update
                    updatePeople(peopleno, peoplename, peopleemail, peoplerelation, peoplememo, peopleimage, phoneno, phonetel);




                    //updatePeople(peopleno, peoplename, peopleemail, peoplerelation, peoplememo, peopleimage, phoneno, phonetel);
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
                case R.id.tv_editPeopleImage:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
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
      //  urlAddr1 = urlAddr + "people_query_Update.jsp?" + "no="+peopleno+"&name="+peoplename+"&email="+peopleemail+"&relation="+peoplerelation+"&memo="+peoplememo+"&phoneno="+phoneno+"&phonetel="+phonetel;
       peoplename = editName.getText().toString();
       peopleemail = editEmail.getText().toString();
       peoplememo = editMemo.getText().toString();
        // 순서 4. peopleno랑 전화번호 정보 insert
        strUpdatePhoneMain = editPhone.getText().toString();
        totalPhoneNo.add(strUpdatePhoneMain);

        if(addPhoneCheck == 1) {
            if (editPhone.getText().toString().length() != 0) {
                strUpdatePhone1 = editPhone.getText().toString();
                totalPhoneNo.add(strUpdatePhone1);
            }
            if (editPhone2.getText().toString().length() != 0) {
                strUpdatePhone2 = editPhone2.getText().toString();
                totalPhoneNo.add(strUpdatePhone2);
            }
            if (editPhone3.getText().toString().length() != 0) {
                strUpdatePhone3 = editPhone3.getText().toString();
                totalPhoneNo.add(strUpdatePhone3);
            }
            if (editPhone4.getText().toString().length() != 0) {
                strUpdatePhone4 = editPhone4.getText().toString();
                totalPhoneNo.add(strUpdatePhone4);
            }
        }

        for (int i = 0; i<totalPhoneNo.size();i++){
            urlUpdatePhonenumber = "http://"+macIP+":8080/test/people_query_Update.jsp?";
            Log.v(TAG, "TelNo insert : "+totalPhoneNo.get(i));
            urlUpdatePhonenumber = urlUpdatePhonenumber+"people_peopleno="+peopleno+"&totalPhoneNo="+totalPhoneNo.get(i);
            connectUpdateData(urlUpdatePhonenumber);
            if(phoneUpdate.equals("1")){
                phoneUpdateResult++;
            }
        }

       // urlAddr1 = urlAddr + "people_query_Update.jsp?" + "no="+peopleno+"&name="+peoplename+"&email="+peopleemail+"&relation="+peoplerelation+"&memo="+peoplememo+"&phoneno="+phoneno.get(0)+"&phonetel="+phonetel.get(0);



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

        Intent intent = new Intent(ModifyPeopleActivity.this, ViewPeopleActivity.class); //화면 이동시켜주기
                    intent.putExtra("macIP", macIP); //값 넘겨주기
                    intent.putExtra("peopleno", peopleno); //값 넘겨주기
                    intent.putExtra("phonetel", phonetel); //값 넘겨주기
                    startActivity(intent); //이동시킨 화면 시작

//        if(result.equals("1")){
//            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
//
//        } else{
//            Toast.makeText(ModifyPeopleActivity.this, peoplename + "의 정보 삭제가 실패했습니다. \n같은 문제가 지속적으로 발생하면 고객센터에 문의주세요.", Toast.LENGTH_SHORT).show();
//
//        }

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
        if (members.get(0).getImage() == null) {
//            urlAddr1 = urlAddr + "people_query_all.jsp?peopleimage=" + peopleimage;
//            String result = connectCheckData(urlAddr1);
            urlImage = urlImage+"ic_defaultpeople.jpg";
            editImage.loadUrl(urlImage);
            editImage.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            editImage.setWebViewClient(new ViewPeopleActivity.WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용

//        } else if(peopleimage.length() != 0) {
            // } else if(peopleimage.equals("!=null")) {
        } else if(members.get(0).getImage() != null) {
//            urlAddr1 = urlAddr + "people_query_all.jsp?peopleimage=" + peopleimage;
//            String result = connectCheckData(urlAddr1);
            urlImage = urlImage + members.get(0).getImage();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    imageCheck=1;
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();
                    Log.v("test", String.valueOf(data.getData()));
                    //이미지를 비트맵형식으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                    //editImage.setImageBitmap(image_bitmap_copy);

                    // 파일 이름 및 경로 바꾸기(임시 저장)
                    String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                    imageName = date+"."+f_ext;
                    tempSelectFile = new File("/data/data/com.android.address_book/", imageName);
                    OutputStream out = new FileOutputStream(tempSelectFile);
                    image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    // 임시 파일 경로로 위의 img_path 재정의
                    img_path = "/data/data/com.android.address_book/"+imageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        // 확장자 명 저장
        f_ext = imgPath.substring(imgPath.length()-3, imgPath.length());
        Toast.makeText(ModifyPeopleActivity.this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
//        this.imageName = imgName;

        return imgPath;
    }//end of getImagePathToUri()
    //파일 변환
    private void doMultiPartRequest() {

        File f = new File(img_path);

        DoActualRequest(f);
    }

    //서버 보내기
    private void DoActualRequest(File file) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


} // end --------------------------------------------------------------------------------