package com.aeyoung.postit.communitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SmartITFirst extends AppCompatActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_it_first);

        final String[] mid = {
                "		- 1학년 1학기 -",
                "교필 English/Japanese/Chinese for World Citizenship1(택1) 2학점",
                "교필 컴퓨팅사고와프로그래밍 3학점",
                "교필 논리와웹소프트웨어 3학점",
                "핵심교양(교필) 2학점",
                "교필 Sol-Sup1 2학점",
                "교선 일반교양 자유선택 2학점",
                "전필 컴퓨터프로그래밍 3학점",
                "전선 웹프로그래밍 3학점",
                "전선 정보통신기술 2학점",
                "		- 1학년 여름학기 -",
                "전필 프로그래밍프로젝트 3학점",
                "		- 1학년 2학기 -",
                "교필 English/Japanese/Chinese for World Citizenship2(택1) 2학점",
                "교필논리와웹소프트웨어 3학점",
                "교필 핵심교양(교필) 2학점",
                "교선 일반교양 자유선택 2학점",
                "전필 C프로그래밍 3학점",
                "전선 모바일앱실습 3학점",
                "전선운영체제실습 2학점"};

        ListView list = (ListView) findViewById(R.id.listView5);

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