package com.aeyoung.postit.communitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SmartITThirdActivity extends AppCompatActivity{

    @Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_smart_it_third);

    final String[] mid = {
            "		- 3학년 1학기 -",
            "트랙 트랙 공통 과목 3학점",
            "교선 일반교양 자유선택 5학점",
            "전필 모바일프로그래밍 3학점",
            "전필 아두이노IoT실습 2학점",
            "전선 모바일소프트웨어공학 2학점",
            "전선 IT실무영어2 2학점",
            "(전공) (현장실습) (4이상)",
            "		- 3학년 2학기 -",
            "트랙 트랙 공통 과목 3학점",
            "트랙 트랙 공통 과목 3학점",
            "교선 일반교양 자유선택 2학점",
            "전필 모바일운영체제 3학점",
            "전필 빅데이터통계 3학점",
            "전선 인공지능기초 3학점",
            "(전공) (현장실습) (4이상)"};

    ListView list = (ListView) findViewById(R.id.listView7);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, mid);
    list.setAdapter(adapter);

    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Toast.makeText(getApplicationContext(), mid[arg2],
                    Toast.LENGTH_SHORT);
        }
    });
}
}