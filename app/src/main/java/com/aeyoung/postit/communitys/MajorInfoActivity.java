package com.aeyoung.postit.communitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MajorInfoActivity extends AppCompatActivity{

    Button computer;
    Button smart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_major);

        computer =  findViewById(R.id.computer);

        computer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MajorInfoActivity.this, CSActivity.class);
                startActivity(intent);
            }
        });

        smart =  findViewById(R.id.smart);

        smart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MajorInfoActivity.this, SmartITActivity.class);
                startActivity(intent);
            }
        });
    }
}