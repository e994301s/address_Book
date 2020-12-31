package com.android.address_book_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.Task.GroupNetworkTask;
import com.android.Task.PeopleNetworkTask;
import com.android.address_book.Group;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    final static String TAG = "SelectAllActivity";
    String urlAddr = null;
    String urlAddr1 = null;
    String urlAddr4 = null;
    ArrayList<People> members;
    PeopleAdapter adapter;
    ListView listView;
    String macIP;
    String email;
    Button btnGroup1, btnGroup2, btnGroup3, btnGroup4;

    private ArrayList<Group> groups = null;
    private LinearLayout ll;
    private TextView[] tvs;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectGetData(urlAddr1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment는 Activity가 아니기때문에 리턴값과 레이아웃을 변수로 정해준다.
        View v = inflater.inflate(R.layout.fragment_second, container, false);

        // listView와 Ip, jsp를 불러온다
        listView = v.findViewById(R.id.lv_group);
        macIP = "192.168.219.154";
        email = "qkr@naver.com";
        urlAddr = "http://" + macIP + ":8080/test/";
        urlAddr1 = urlAddr + "group_people_query_all.jsp?email=qkr@naver.com";


        // 그룹에 관한 버튼 액션
//        btnGroup1 = v.findViewById(R.id.button1);
//        btnGroup2 = v.findViewById(R.id.button2);
//        btnGroup3 = v.findViewById(R.id.button3);
//
//        btnGroup1.setOnClickListener(onCLickListener);
//        btnGroup2.setOnClickListener(onCLickListener);
//        btnGroup3.setOnClickListener(onCLickListener);

        groups = connectSelectData(urlAddr1);

        ll = v.findViewById(R.id.ll_01_group);
        tvs = new TextView[groups.size()];

        for (int i=0; i<groups.size(); i++){
            // main에 보여주기 위해 기본 셋팅
            tvs[i] = new TextView(v.getContext());
            tvs[i].setText(groups.get(i).getGroupName());
            tvs[i].setId(i);
            ll.addView(tvs[i]);

        }


        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        connectGetData(urlAddr1);
        Log.v(TAG, "onResume()");

    }

    // NetworkTask에서 값을 가져오는 메소드
    private void connectGetData(String urlAddr) {
        try {
            PeopleNetworkTask peopleNetworkTask = new PeopleNetworkTask(getContext(), urlAddr);
            Object obj = peopleNetworkTask.execute().get();
            members = (ArrayList<People>) obj;
            Log.v("here", "" + members);
            adapter = new PeopleAdapter(getContext(), R.layout.people_custom_layout, members); // 아댑터에 값을 넣어준다.
            listView.setAdapter(adapter);  // 리스트뷰에 어탭터에 있는 값을 넣어준다.


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // group NetworkTask에서 값을 가져오는 메소드
    private ArrayList<Group> connectSelectData(String urlAddr) {
        try {
            GroupNetworkTask selectNetworkTask = new GroupNetworkTask(getContext(), urlAddr, "select");
            Object obj = selectNetworkTask.execute().get();
            groups = (ArrayList<Group>) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }

//    // 그룹에 대한 선택 ( 친구 , 가족 , 등등드으드응)
//    View.OnClickListener onCLickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//
//                case R.id.button1:
//                        urlAddr4 = "";
//                        String group1 = btnGroup1.getText().toString();
//
//                        urlAddr4 = urlAddr + "group_people_query_all.jsp?email=qkr@naver.com&group=" + group1;
////                        urlAddr4 = urlAddr + "group_people_query_all.jsp?email=" + email + "&group=" + group1;
//                        connectGetData(urlAddr4);
//                        break;
//
//                case R.id.button2:
//                    urlAddr4 = "";
//                    String group2 = btnGroup2.getText().toString();
//
//                    urlAddr4 = urlAddr + "group_people_query_all.jsp?email=qkr@naver.com&group=" + group2;
//                    connectGetData(urlAddr4);
//                    break;
//
//            }
//        }
//    };

}