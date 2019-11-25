package com.aeyoung.postit.communitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SmartITProcessActivity extends AppCompatActivity{

    Button grade5;
    Button grade6;
    Button grade7;
    Button grade8;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_it_edu_process);

        grade5 =  findViewById(R.id.grade5);

        grade5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartITProcessActivity.this, SmartITFirst.class);
                startActivity(intent);
            }
        });

        grade6 =  findViewById(R.id.grade6);

        grade6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartITProcessActivity.this, SmartITSecond.class);
                startActivity(intent);
            }
        });

        grade7 =  findViewById(R.id.grade7);

        grade7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartITProcessActivity.this, SmartITThirdActivity.class);
                startActivity(intent);
            }
        });

        grade8 =  findViewById(R.id.grade8);

        grade8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartITProcessActivity.this, SmartITFourthActivity.class);
                startActivity(intent);
            }
        });

    }
}