package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.address_book.R;
import com.navdrawer.SimpleSideDrawer;

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

    Button add_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_people);
        add_view = findViewById(R.id.addTelNoButton);

        add_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterAddPhoneNumber n_layout = new RegisterAddPhoneNumber(getApplicationContext());
                LinearLayout con = (LinearLayout)findViewById(R.id.add_PhoneNumber_layout);
                con.addView(n_layout);
                add_view.setVisibility(View.INVISIBLE);
            }
        });
    }
}
