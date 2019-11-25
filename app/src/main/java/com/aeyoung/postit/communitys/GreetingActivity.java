package com.aeyoung.postit.communitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class GreetingActivity extends AppCompatActivity {

    Button csintro;
    Button smartintro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        csintro = findViewById(R.id.csintro);

        csintro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GreetingActivity.this, GreetingCSActivity.class);
                startActivity(intent);
            }
        });

        smartintro = findViewById(R.id.smartintro);

        smartintro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GreetingActivity.this, GreetingSmartActivity.class);
                startActivity(intent);
            }
        });

    }
}