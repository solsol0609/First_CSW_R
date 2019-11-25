package com.aeyoung.postit.communitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SmartITSecond extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_it_second);

        final String[] mid = {
                "		- 2학년 1학기 -",
                "교필 English/Japanese/Chinese for World Citizenship3(택1) 2학점",
                "교필핵심교양(교필) 2학점",
                "교선 일반교양 자유선택 2학점",
                "트랙 트랙 공통 과목 3학점",
                "전필 컴퓨터네트워크 3학점",
                "전필 객체지향프로그래밍 3학점",
                "전선 자료구조실습 2학점",
                "		- 2학년 2학기 -",
                "교필 English/Japanese/Chinese for World Citizenship4(택1) 2학점",
                "교선 일반교양 자유선택 2학점",
                "트랙 트랙 공통 과목 3학점",
                "트랙 구조적프로그래밍 (*) 3학점",
                "전필 데이터베이스 3학점",
                "전필 JAVA프로그래밍 2학점",
                "전선 IT실무영어1 2학점"};

        ListView list = (ListView) findViewById(R.id.listView6);

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