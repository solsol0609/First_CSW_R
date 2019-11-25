package com.aeyoung.postit.communitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity<button1, id> extends AppCompatActivity {
    
    Button button1;
    private Intent subactivity;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent( MainActivity.this.subactivity);
                startActivity(intent);
            }
        });
    }
}
