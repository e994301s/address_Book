# 사소한(사람들의 소중한 주소록)
제작자 : 김대환, 박경미, 박인영, 송예진, 유민규


## build.gradle에 필요한 라이브러리

dependencies {

    implementation files('lib/activation.jar')
    implementation files('lib/additionnal.jar')
    implementation files('lib/mail.jar')
    
    사진을 서버에 올리기 위한 라이브러리
    implementation 'com.squareup.okhttp3:okhttp:4.10.0-RC1'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'me.relex:circleindicator:2.1.4'
    
}


## Manifest에 필요한 권한들

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    

## Tomcat을 연결하기 위한 XML

안드로이드 스튜디오를 통해 address_Book을 실행시키면 app/res/xml/network_security_config.jsp 파일을 통해 Tomcat과 연결을 할 수 있다.


## DB(MYSQL) 연결

MySQL Connector Download Link: [MySQL Connector][Connector]

[Connector]: https://dev.mysql.com/downloads/connector/j/8.0.html



## JSP 폴더 내 자료

JSP 폴더 내의 자료는 톰켓 서버 경로(/webapps/ROOT/test) 안에 'jsp파일'들을 넣어준다.


## JSP와 DB를 연결하기 위한 JSP내 사용자 환경에 맞는 소스 변경 요소들

String url_mysql = "jdbc:mysql://localhost/__데이터베이스 스키마 이름__?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
String id_mysql = "**아이디**";
String pw_mysql = "**암호**";

