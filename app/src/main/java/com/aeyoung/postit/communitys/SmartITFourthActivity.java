package com.aeyoung.postit.communitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SmartITFourthActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_it_fourth);


            final String[] mid = {
                    "		- 4학년 1학기 -",
                    "자유선택 자유선택(교양및기타전공) 5학점",
                    "전선 창의적종합설계1 3학점",
                    "전선 컴퓨터구조실무 3학점",
                    "전선 실전IoT응용코딩 3학점",
                    "전선 인공지능응용3학점",
                    "(전공) (현장실습) (4이상)",
                    "		- 4학년 2학기 -",
                    "자유선택 자유선택(교양및기타전공) 5학점",
                    "전선 창의적종합설계2 3학점",
                    "전선 머신러닝 3학점",
                    "전선 시스템보안실무 3학점",
                    "전선 스마트데이터컴퓨팅실습3학점",
                    "(전공) (현장실습) (4이상)"};

            ListView list = (ListView) findViewById(R.id.listView8);

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