package com.android.address_book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.address_book_Activity.FirstFragment;
import com.android.address_book_Activity.ViewPeopleActivity;

import java.util.ArrayList;

public class PeopleAdapter extends BaseAdapter {
    Context mContext = null;
    FirstFragment firstFragment = null;
    int layout = 0;
    ArrayList<People> data = null;
    LayoutInflater inflater = null;
    String urlImage;


    public PeopleAdapter(Context mContext, int layout, ArrayList<People> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getNo();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }

        WebView img_peopleImg = convertView.findViewById(R.id.imgPeople_custom);
        TextView tv_name = convertView.findViewById(R.id.tv_name_custom);
        ImageView img_favoirteImg = convertView.findViewById(R.id.imgFavorite_custom);
        ImageView img_emgImg = convertView.findViewById(R.id.imgEmg_custom);
        ImageView iv_viewPeople = convertView.findViewById(R.id.iv_viewPeople);

        WebSettings webSettings = img_peopleImg.getSettings();
//
        // 화면 비율
        webSettings.setUseWideViewPort(true);       // wide viewport를 사용하도록 설정
        webSettings.setLoadWithOverviewMode(true);  // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        img_peopleImg.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

//        iv_viewPeople.setBackgroundColor(0); //배경색
//
//        iv_viewPeople.setHorizontalScrollBarEnabled(false); //가로 스크롤
//        iv_viewPeople.setVerticalScrollBarEnabled(false);   //세로 스크롤
//
//        iv_viewPeople.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 스크롤 노출 타입
//        iv_viewPeople.setScrollbarFadingEnabled(false);
//
//        // 웹뷰 멀티 터치 가능하게 (줌기능)
//        webSettings.setBuiltInZoomControls(false);   // 줌 아이콘 사용
//        webSettings.setSupportZoom(false);


        if(data.get(position).getImage().equals("null")){
            urlImage = urlImage+"ic_defaultpeople.jpg";
            img_peopleImg.loadUrl(urlImage);
            img_peopleImg.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            img_peopleImg.setWebViewClient(new ViewPeopleActivity.WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용

            //img_peopleImg.setImageResource(R.drawable.ic_defaultpeople);

        } else {
//            img_peopleImg.setImageResource(Integer.parseInt(data.get(position).getImage()));

            urlImage = urlImage + data.get(position).getImage();
            img_peopleImg.loadUrl(urlImage);
            img_peopleImg.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            img_peopleImg.setWebViewClient(new ViewPeopleActivity.WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
        }
        tv_name.setText(data.get(position).getName());




        if(Integer.parseInt(data.get(position).getFavorite()) == 1 ) { // 즐겨찾기 적용되었을 때
            img_favoirteImg.setImageResource(R.drawable.ic_favorite);

        } else {
            img_favoirteImg.setImageResource(R.drawable.ic_nonfavorite);
        }

        if(Integer.parseInt(data.get(position).getEmergency()) == 1) { // 긴급연락처 적용되었을 때
            img_emgImg.setImageResource(R.drawable.ic_emg2);

        } else{
            img_emgImg.setImageResource(R.drawable.ic_nonemg2);
        }


//        TextView tv_no = convertView.findViewById(R.id.tv_no);
//        TextView tv_name = convertView.findViewById(R.id.tv_name);
//        TextView tv_tel = convertView.findViewById(R.id.tv_tel);
//        TextView tv_email = convertView.findViewById(R.id.tv_email);
//        TextView tv_relation = convertView.findViewById(R.id.tv_relation);
//        TextView tv_memo = convertView.findViewById(R.id.tv_memo);
//        TextView tv_image = convertView.findViewById(R.id.tv_image);
//
//        tv_no.setText("번호 : " + data.get(position).getNo());
//        tv_name.setText("이름 : " + data.get(position).getName());
//        tv_tel.setText("전화번호 : " + data.get(position).getTel());
//        tv_email.setText("이메일 : " + data.get(position).getEmail());
//        tv_relation.setText("관계 : " + data.get(position).getRelation());
//        tv_memo.setText("메모 : " + data.get(position).getMemo());
//        tv_image.setText("이미지 : " + data.get(position).getImage());

        if ((position % 2) == 1) {
            convertView.setBackgroundColor(0x50000000);
        } else {
            convertView.setBackgroundColor(0x50dddddd);
        }

        return convertView;
    }

    static class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
