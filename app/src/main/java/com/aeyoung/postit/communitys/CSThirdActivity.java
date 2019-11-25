package com.aeyoung.postit.communitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CSThirdActivity extends AppCompatActivity{

    @Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cs_third);

    final String[] mid = {
            "		- 3학년 1학기 -",
            "트랙(부전공) 트랙 공통 과목 3학점",
            "교선 일반교양 자유선택 5학점",
            "전필 임베디드시스템 3학점",
            "전필 정보보안 2학점",
            "전선 JAVA응용프로그래 2학점",
            "전선IT실무영어2 2학점",
            "(전공) (현장실습) (4이상)",
            "		- 3학년 2학기 -",
            "트랙(부전공) 트랙 공통 과목 3학점",
            "트랙(부전공) 유비쿼터스컴퓨팅(*) 3학점",
            "교선 일반교양 자유선택 2학점",
            "전필 유무선네트워크보안 3학점",
            "전필 객체지향응용프로그래밍 3학점",
            "전선 소프트웨어공학 3학점",
            "(전공) (현장실습) (4이상)"};

    ListView list = (ListView) findViewById(R.id.listView3);

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