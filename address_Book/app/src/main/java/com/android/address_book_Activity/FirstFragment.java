package com.android.address_book_Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.Task.GroupNetworkTask;
import com.android.Task.PeopleNetworkTask;
import com.android.address_book.Group;
import com.android.address_book.GroupAdapter;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FirstFragment extends Fragment {

    final static String TAG = "First";
    String urlAddr = null;
    String urlAddr1 = null;
    String urlAddr2 = null;
    ArrayList<People> members;
    ArrayList<Group> groups;
    PeopleAdapter adapter;
    GroupAdapter groupAdapter;
    ListView listView, groupList;
    String macIP;
    String email;
    TextView textView;
    String peopleNum;
    HorizontalScrollView horizontalScrollView;

    private LinearLayout ll;
    private Button[] tvs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment는 Activity가 아니기때문에 리턴값과 레이아웃을 변수로 정해준다.
       View v = inflater.inflate(R.layout.fragment_first, container, false);

        // listView와 Ip, jsp를 불러온다
        listView = v.findViewById(R.id.lv_people);

//        groupList = v.findViewById(R.id.lv_group_frg);
        horizontalScrollView = v.findViewById(R.id.hsv_01_group);


        macIP = "192.168.0.81";
        email = "qkr@naver.com";
        urlAddr = "http://" + macIP + ":8080/test/";
        urlAddr1 = urlAddr + "people_query_all.jsp?email=qkr@naver.com";
        urlAddr2 = urlAddr + "group_query_all.jsp?email=qkr@naver.com";

        //////////////////////////////////////////////////////
        // 그룹별 horizontal 셋팅
        connectGetData(urlAddr1);
        groups = connectGroupGetData(urlAddr2);

        ll = v.findViewById(R.id.ll_01_group);
        tvs = new Button[groups.size() + 3];
        String[] groupDefault = {"가족", "친구", "회사"};

        for (int i=0; i<3; i++){
            // main에 보여주기 위해 기본 셋팅
            tvs[i] = new Button(getContext());
            tvs[i].setText(groupDefault[i]);
            tvs[i].setTextSize(15);
            tvs[i].getCompoundDrawablePadding();
            tvs[i].setId(i);
            ll.addView(tvs[i]);
            ll.setPadding(5,5,5,5);
            tvs[i].setOnClickListener(mClickListener);

        }

        for (int i=3; i<(groups.size()+3); i++){
            // main에 보여주기 위해 기본 셋팅
            tvs[i] = new Button(getContext());
            tvs[i].setText(groups.get(i-3).getGroupName());
            tvs[i].setTextSize(15);
            tvs[i].getCompoundDrawablePadding();
            tvs[i].setId(i);
            ll.addView(tvs[i]);
            ll.setPadding(5,5,5,5);
            tvs[i].setOnClickListener(mClickListener);

        }

        //////////////////////////////////////////////////////
        //////////////////////////////////////////////////////

        // 리스트 선택 리스너

        // 리스트 일반 클릭시
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewPeopleActivity.class);  // 원래는 회원정보로 가야한다 잠시 되는 곳 아무곳이나 보내놓음
                intent.putExtra("peopleno", members.get(position).getNo());
                intent.putExtra("useremail", members.get(position).getUseremail());
                startActivity(intent);
            }
        });


        // 리스트 길게 선택시
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                registerForContextMenu(listView); // 롱클릭시 이벤트와 메뉴 선
                return false;
            }
        });



        return v;

    }

    //////////////////////////////////////////////////////
    // 그룹별 horizontal 셋팅 - click 이벤트
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i=0; i<groups.size(); i++) {

            }

        }
    };


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) // 메뉴 레이아웃 선택
    {
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item)                                             // 메뉴 레이아웃에서 버튼 아이템 선택
    {
        String tel = "tel:" + peopleNum;
        switch(item.getItemId())
        {
            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+peopleNum));
                startActivity(intent);
                Toast.makeText(getActivity(), "전화 버튼 클릭됨", Toast.LENGTH_LONG).show();       // 전화 선택시
                return true;
            case R.id.message:
                Uri smsUri = Uri.parse("tel:" + "010");
                Intent smsIntent = new Intent(Intent.ACTION_VIEW); // 보내는 화면이 팝업됨

                smsIntent.putExtra("sms_body", "01086730811"); // 받는 번호
                smsIntent.setType("vnd.android-dir/mms-sms");
                startActivity(smsIntent);

                Toast.makeText(getActivity(), "문자 버튼 클릭됨", Toast.LENGTH_LONG).show();       // 문자 선택시
                return true;
            case R.id.kakao:
                Toast.makeText(getActivity(), "카톡 버튼 클릭됨", Toast.LENGTH_LONG).show();       // 카톡 선택시
                return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
//        connectGetData(urlAddr1);
//        connectGroupGetData(urlAddr2);
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

    // NetworkTask에서 값을 가져오는 메소드
    private ArrayList<Group> connectGroupGetData(String urlAddr) {
        try {
            GroupNetworkTask groupNetworkTask = new GroupNetworkTask(getContext(), urlAddr, "select");
            Object obj = groupNetworkTask.execute().get();
            groups = (ArrayList<Group>) obj;
            Log.v("here", "" + groups);
            groupAdapter = new GroupAdapter(getContext(), R.layout.group_custom_layout, groups); // 아댑터에 값을 넣어준다.
//            groupAdapter = new GroupAdapter(getContext(), R.layout.group_custom_layout, groups); // 아댑터에 값을 넣어준다.
            groupList.setAdapter(groupAdapter);  // 리스트뷰에 어탭터에 있는 값을 넣어준다.


        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }




}