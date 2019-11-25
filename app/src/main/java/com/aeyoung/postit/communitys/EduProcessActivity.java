package com.aeyoung.postit.communitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EduProcessActivity extends AppCompatActivity{

    Button computereducation;
    Button smarteducation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs_edu_goal);

        computereducation =  findViewById(R.id.computereducation);

        computereducation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EduProcessActivity.this, CSProcessActivity.class);
                startActivity(intent);
            }
        });

        smarteducation =  findViewById(R.id.smarteducation);

        smarteducation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EduProcessActivity.this, SmartITProcessActivity.class);
                startActivity(intent);
            }
        });

    }
}