package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.Task.PeopleNetworkTask;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;

import java.util.ArrayList;

public class ThirdFragment extends Fragment {
    final static String TAG = "SelectAllActivity";
    String urlAddr = null;
    String urlAddr1 = null;
    String urlAddr2 = null;
    String urlAddr3 = null;
    String urlAddr4 = null;
    ArrayList<People> searchArr;
    ArrayList<People> members;
    PeopleAdapter adapter;
    ListView listView;
    String macIP;
    String email;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third, container, false);
        // Inflate the layout for this fragment

        listView = v.findViewById(R.id.lv_favorite);
//        adapter = new PeopleAdapter(getContext(), R.layout.people_custom_layout, members);
<<<<<<< HEAD
        macIP = "192.168.35.157";
        email = "con@naver.com";
        urlAddr = "http://" + macIP + ":8080/test/";
//        listView.setAdapter(adapter);  // 리스트뷰에 어탭터에 있는 값을 넣어준다.
        urlAddr1 = urlAddr + "favorite_people_query_all.jsp?email=con@naver.com";
=======


        email = getArguments().getString("useremail");
        macIP = getArguments().getString("macIP");

        urlAddr = "http://" + macIP + ":8080/test/";
//        listView.setAdapter(adapter);  // 리스트뷰에 어탭터에 있는 값을 넣어준다.
        urlAddr1 = urlAddr + "favorite_people_query_all.jsp?email=" + email;


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewPeopleActivity.class);  // 원래는 회원정보로 가야한다 잠시 되는 곳 아무곳이나 보내놓음
                intent.putExtra("peopleno", members.get(position).getNo());
                intent.putExtra("useremail", members.get(position).getUseremail());
                intent.putExtra("phonetel", members.get(position).getTel());
                intent.putExtra("macIP", macIP);

>>>>>>> 82c1e1c28ab013faea6c550e56034d31bbc3fd49

                startActivity(intent);
            }
        });
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

}