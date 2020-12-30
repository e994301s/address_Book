package com.android.address_book_Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.Task.CUDNetworkTask;
import com.android.Task.NetworkTask;
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
======================                                 연락처 등록 화면                                 =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class RegisterPeopleActivity extends AppCompatActivity {

    String TAG = "Register";

    ImageView registerImage;
    Button add_view, registerButton, registerBookMarkButton, registerEmergencyPhoneNumber;
    EditText registerName, registerMainTelNo, registerAddPhoneNumber1, registerAddPhoneNumber2, registerAddPhoneNumber3, registerAddPhoneNumber4, registerEmail, registerComment;
    String strRegisterName=null;
    String strRegisterMainTelNo=null;
    String strRegisterAddPhoneNumber1=null;
    String strRegisterAddPhoneNumber2=null;
    String strRegisterAddPhoneNumber3=null;
    String strRegisterAddPhoneNumber4=null;
    String strRegisterEmail= null;
    String strRegisterComment= null;

    String macIP, urlAddPeople, urlGetNumber, urlAddPhoneNumber, urlAddstatus;

    String peopleNo, peopleInsertResult, phoneInsert, statusInsert;
    ArrayList<String> totalPhoneNo = new ArrayList<String>();

    int bookMark = 0;
    int emergencyStatus = 0;
    int phoneInsertResult = 0;

    // 사진 올리고 내리기 위한 변수들
    private final int REQ_CODE_SELECT_IMAGE = 100;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    private String imageName = null;
    private String f_ext = null;
    File tempSelectFile;
    String url = "http://192.168.0.128:8080/test/multipartRequest.jsp"; // URL 꼭 바꿔주기!!!!!!!!!!!!!!!!


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_people);


        add_view = findViewById(R.id.addTelNoButton);
        registerButton = findViewById(R.id.registerButton);
        registerImage = findViewById(R.id.registerImage);
        registerBookMarkButton = findViewById(R.id.registerBookMarkButton);
        registerEmergencyPhoneNumber = findViewById(R.id.registerEmergencyPhoneNumber);
        registerName = findViewById(R.id.registerName);
        registerMainTelNo = findViewById(R.id.registerMainTelNo);
        registerAddPhoneNumber1 = findViewById(R.id.registerAddPhoneNumber1);
        registerAddPhoneNumber2 = findViewById(R.id.registerAddPhoneNumber2);
        registerAddPhoneNumber3 = findViewById(R.id.registerAddPhoneNumber3);
        registerAddPhoneNumber4 = findViewById(R.id.registerAddPhoneNumber4);
        registerEmail = findViewById(R.id.registerEmail);
        registerComment = findViewById(R.id.registerComment);

        Intent intent = getIntent();
        // macIP = intent.getStringExtra("macIP");
        macIP = "192.168.0.128";
        urlAddPeople = "http://"+macIP+":8080/test/peopleInsert.jsp?";
        urlGetNumber = "http://"+macIP+":8080/test/getPeopleNo.jsp";
        urlAddPhoneNumber = "http://"+macIP+":8080/test/phoneInsert.jsp?";
        // urlAddstatus = "http://"+macIP+":8080/test/statusInsert.jsp?";

        add_view.setOnClickListener(mClickListener);
        registerButton.setOnClickListener(mClickListener);
        registerImage.setOnClickListener(mClickListener);
        registerBookMarkButton.setOnClickListener(mClickListener);
        registerEmergencyPhoneNumber.setOnClickListener(mClickListener);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            strRegisterName = registerName.getText().toString();
            strRegisterEmail = registerEmail.getText().toString();
            // 관계
            strRegisterComment = registerComment.getText().toString();


            switch (v.getId()){
                case R.id.addTelNoButton:
                    RegisterAddPhoneNumber n_layout = new RegisterAddPhoneNumber(getApplicationContext());
                    LinearLayout con = (LinearLayout)findViewById(R.id.add_PhoneNumber_layout);
                    con.addView(n_layout);
                    add_view.setVisibility(View.INVISIBLE);
                    break;

                case R.id.registerButton:

                    // 순서 1. 네트워크 연결 및 이미지 서버에 전송, 이미지 이름 저장
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            doMultiPartRequest();
                        }
                    });

                    // 순서 2. DB와 연결(NetworkTask)해서 정보 insert

                    urlAddPeople = urlAddPeople+"peoplename="+strRegisterName+"&peopleemail="+strRegisterEmail+"&peoplememo="+strRegisterComment+"&peopleimage="+imageName;
                    connectInsertData();
                    // 순서 3. insert 되서 생성된 peopleno 가져오기
                    connectGetData();
                    // 순서 4. peopleno랑 전화번호 정보 insert

                    if(registerMainTelNo.getText().toString().length()!=0){
                        strRegisterMainTelNo = registerMainTelNo.getText().toString();

                        totalPhoneNo.add(strRegisterName);
                    }
                    if(registerAddPhoneNumber1.getText().toString().length()!=0){
                        strRegisterAddPhoneNumber1 = registerAddPhoneNumber1.getText().toString();

                        totalPhoneNo.add(strRegisterAddPhoneNumber1);
                    }
                    if(registerAddPhoneNumber2.getText().toString().length()!=0){
                        strRegisterAddPhoneNumber2 = registerAddPhoneNumber2.getText().toString();

                        totalPhoneNo.add(strRegisterAddPhoneNumber2);
                    }
                    if(registerAddPhoneNumber3.getText().toString().length()!=0){
                        strRegisterAddPhoneNumber3 = registerAddPhoneNumber3.getText().toString();

                        totalPhoneNo.add(strRegisterAddPhoneNumber3);
                    }
                    if(registerAddPhoneNumber4.getText().toString().length()!=0){
                        strRegisterAddPhoneNumber4 = registerAddPhoneNumber4.getText().toString();

                        totalPhoneNo.add(strRegisterAddPhoneNumber4);
                    }
                    for (int i = 0; i<totalPhoneNo.size();i++){
                        urlAddPhoneNumber = urlAddPhoneNumber+"people_peopleno="+peopleNo+"&totalPhoneNo="+totalPhoneNo.get(i);
                        connectInsertPhoneNo();
                        if(phoneInsert.equals("1")){
                            phoneInsertResult++;
                        }
                    }
                    // 순서 5. 좋아요, 긴급연락처 추가
                        // urlAddstatus = urlAddstatus+"people_peopleno="+peopleNo+"&userinfo_useremail="+"&peopleemg="+emergencyStatus+"&peoplefavorite="+bookMark;
                    //connectInsertStatus()


                    if(peopleInsertResult.equals("1")&&phoneInsertResult==totalPhoneNo.size()&&statusInsert.equals("1")){
                        Toast.makeText(RegisterPeopleActivity.this, "입력이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterPeopleActivity.this, "관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.registerImage:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    break;

                case R.id.registerBookMarkButton:
                    if(bookMark == 0){
                        bookMark = 1;
                    }else if (bookMark ==1){
                        bookMark = 0;
                    }

                    break;

                case R.id.registerEmergencyPhoneNumber:
                    if(emergencyStatus == 0){
                        emergencyStatus = 1;
                    }else if (emergencyStatus ==1){
                        emergencyStatus = 0;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();
                    Log.v("test", String.valueOf(data.getData()));
                    //이미지를 비트맵형식으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                    registerImage.setImageBitmap(image_bitmap_copy);

                    // 파일 이름 및 경로 바꾸기(임시 저장)
                    String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                    imageName = date+"."+f_ext;
                    tempSelectFile = new File("/data/data/com.androidlec.imageupload/", imageName);
                    OutputStream out = new FileOutputStream(tempSelectFile);
                    image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    // 임시 파일 경로로 위의 img_path 재정의
                    img_path = "/data/data/com.androidlec.imageupload/"+imageName;
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
        Toast.makeText(RegisterPeopleActivity.this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
        this.imageName = imgName;

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
                       RequestBody.create(MediaType.parse("image/jpeg"), file))
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

    private void connectInsertData() {
        try {
            CUDNetworkTask insnetworkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlAddPeople, "Register");
            Object object = insnetworkTask.execute().get();
            peopleInsertResult = (String) object;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void connectGetData(){
        try {
            CUDNetworkTask networkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlGetNumber, "Register");
            Object obj = networkTask.execute().get();
            peopleNo = (String) obj;


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void connectInsertPhoneNo() {
        try {
            CUDNetworkTask insTelNonetworkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlAddPhoneNumber, "Register");
            Object object = insTelNonetworkTask.execute().get();
            phoneInsert =(String) object;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void connectInsertStatus() {
        try {
            CUDNetworkTask insTelNonetworkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlAddstatus, "Register");
            Object object = insTelNonetworkTask.execute().get();
            statusInsert =(String) object;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
