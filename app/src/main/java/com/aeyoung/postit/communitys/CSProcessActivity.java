package com.aeyoung.postit.communitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CSProcessActivity extends AppCompatActivity{

    Button grade1;
    Button grade2;
    Button grade3;
    Button grade4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs_process);

        grade1 =  findViewById(R.id.grade1);

        grade1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CSProcessActivity.this, CSFirstActivity.class);
                startActivity(intent);
            }
        });

        grade2 =  findViewById(R.id.grade2);

        grade2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CSProcessActivity.this, CSSecondActivity.class);
                startActivity(intent);
            }
        });

        grade3 =  findViewById(R.id.grade3);

        grade3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CSProcessActivity.this, CSThirdActivity.class);
                startActivity(intent);
            }
        });

        grade4 =  findViewById(R.id.grade4);

        grade4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CSProcessActivity.this, CSFourthActivity.class);
                startActivity(intent);
            }
        });

    }
}