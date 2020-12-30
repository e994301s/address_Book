//package com.android.address_book_Activity;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.android.Task.CUDNetworkTask;
//import com.android.address_book.R;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
///*
//===========================================================================================================================
//===========================================================================================================================
//===========================================================================================================================
//======================                                                                              =======================
//======================                                                                              =======================
//======================                                 연락처 등록 화면                                 =======================
//======================                                                                              =======================
//======================                                                                              =======================
//===========================================================================================================================
//===========================================================================================================================
//===========================================================================================================================
//*/
//
//public class RegisterPeopleActivity extends AppCompatActivity {
//
//    ImageView registerImage;
//    Button add_view, registerButton, registerBookMarkButton, registerEmergencyPhoneNumber;
//    EditText registerName, registerMainTelNo, registerAddPhoneNumber1, registerAddPhoneNumber2, registerAddPhoneNumber3, registerAddPhoneNumber4, registerEmail, registerComment;
//    String strRegisterName, strRegisterMainTelNo, strRegisterAddPhoneNumber1, strRegisterAddPhoneNumber2, strRegisterAddPhoneNumber3, strRegisterAddPhoneNumber4, strRegisterEmail, strRegisterComment;
//    String macIP, urlAddPeople;
//
//    // 사진 올리고 내리기 위한 변수들
//    private final int REQ_CODE_SELECT_IMAGE = 100;
//    private String img_path = new String();
//    private Bitmap image_bitmap_copy = null;
//    private Bitmap image_bitmap = null;
//    private String imageName = null;
//    private String f_ext = null;
//    File tempSelectFile;
//    String url = "http://192.168.0.54:8080/test/multipartRequest.jsp"; // URL 꼭 바꿔주기!!!!!!!!!!!!!!!!
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register_people);
//
//
//        add_view = findViewById(R.id.addTelNoButton);
//        registerButton = findViewById(R.id.registerButton);
//        registerImage = findViewById(R.id.registerImage);
//        registerBookMarkButton = findViewById(R.id.registerBookMarkButton);
//        registerEmergencyPhoneNumber = findViewById(R.id.registerEmergencyPhoneNumber);
//        registerName = findViewById(R.id.registerName);
//        registerMainTelNo = findViewById(R.id.registerMainTelNo);
//        registerAddPhoneNumber1 = findViewById(R.id.registerAddPhoneNumber1);
//        registerAddPhoneNumber2 = findViewById(R.id.registerAddPhoneNumber2);
//        registerAddPhoneNumber3 = findViewById(R.id.registerAddPhoneNumber3);
//        registerAddPhoneNumber4 = findViewById(R.id.registerAddPhoneNumber4);
//        registerEmail = findViewById(R.id.registerEmail);
//        registerComment = findViewById(R.id.registerComment);
//
//        Intent intent = getIntent();
//        macIP = intent.getStringExtra("macIP");
//        urlAddPeople = "http://" + macIP + ":8080/test/studentInsert.jsp?";
//
//
//        add_view.setOnClickListener(mClickListener);
//        registerButton.setOnClickListener(mClickListener);
//        registerImage.setOnClickListener(mClickListener);
//        registerBookMarkButton.setOnClickListener(mClickListener);
//        registerEmergencyPhoneNumber.setOnClickListener(mClickListener);
//
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .permitDiskReads()
//                .permitDiskWrites()
//                .permitNetwork().build());
//
//    }
//
//    View.OnClickListener mClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.addTelNoButton:
//                    RegisterAddPhoneNumber n_layout = new RegisterAddPhoneNumber(getApplicationContext());
//                    LinearLayout con = (LinearLayout) findViewById(R.id.add_PhoneNumber_layout);
//                    con.addView(n_layout);
//                    add_view.setVisibility(View.INVISIBLE);
//                    break;
//
//                case R.id.registerButton:
//                    // 순서 1. 네트워크 연결 및 이미지 서버에 전송, 이미지 이름 저장
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            doMultiPartRequest();
//                        }
//                    });
//
//                    // 순서 2. DB와 연결(NetworkTask)해서 정보 insert
//                    strRegisterName = registerName.getText().toString();
//                    strRegisterEmail = registerEmail.getText().toString();
//                    // 관계
//                    strRegisterComment = registerComment.getText().toString();
//                    urlAddPeople = urlAddPeople + "peoplename=" + strRegisterName + "&peopleemail=" + strRegisterEmail + "&peoplerelation=" + sdept + "&peoplememo=" + strRegisterComment + "&peopleimage" + imageName;
//                    connectInsertData();
//                    // 순서 3. insert 되서 생성된 peopleno 가져오기
//                    // 순서 4. peopleno랑 전화번호 정보 insert
//
//                    break;
//                case R.id.registerImage:
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
//                    break;
//
//                case R.id.registerBookMarkButton:
//
//                    break;
//
//                case R.id.registerEmergencyPhoneNumber:
//
//                    break;
//            }
//        }
//    };
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == REQ_CODE_SELECT_IMAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                try {
//                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
//                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();
//                    Log.v("test", String.valueOf(data.getData()));
//                    //이미지를 비트맵형식으로 반환
//                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//
//                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
//                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
//                    registerImage.setImageBitmap(image_bitmap_copy);
//
//                    // 파일 이름 및 경로 바꾸기(임시 저장)
//                    String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
//                    imageName = date + "." + f_ext;
//                    tempSelectFile = new File("/data/data/com.androidlec.imageupload/", imageName);
//                    OutputStream out = new FileOutputStream(tempSelectFile);
//                    image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//
//                    // 임시 파일 경로로 위의 img_path 재정의
//                    img_path = "/data/data/com.androidlec.imageupload/" + imageName;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public String getImagePathToUri(Uri data) {
//        //사용자가 선택한 이미지의 정보를 받아옴
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(data, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//
//        //이미지의 경로 값
//        String imgPath = cursor.getString(column_index);
//        Log.d("test", imgPath);
//
//        //이미지의 이름 값
//        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
//
//        // 확장자 명 저장
//        f_ext = imgPath.substring(imgPath.length() - 3, imgPath.length());
//        Toast.makeText(RegisterPeopleActivity.this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
//        this.imageName = imgName;
//
//        return imgPath;
//    }//end of getImagePathToUri()
//
//    //파일 변환
//    private void doMultiPartRequest() {
//
//        File f = new File(img_path);
//
//        DoActualRequest(f);
//    }
//
//    //서버 보내기
//    private void DoActualRequest(File file) {
//        OkHttpClient client = new OkHttpClient();
//
//
//        RequestBody body = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("image", file.getName(),
//                        RequestBody.create(MediaType.parse("image/jpeg"), file))
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//        try {
//            Response response = client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void connectInsertData() {
//        try {
//            CUDNetworkTask insnetworkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlAddr);
//            insnetworkTask.execute().get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}