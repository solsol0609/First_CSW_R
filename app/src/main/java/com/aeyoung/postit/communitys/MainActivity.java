package com.aeyoung.postit.communitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

     Button first;
     Button two;
     Button four, five;
     Button six,seven;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        first =  findViewById(R.id.first);

        first.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, GreetingActivity.class);
                        startActivity(intent);
                        }
                });
//끝
        two =  findViewById(R.id.two);

        two.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MajorInfoActivity.class);
                        startActivity(intent);
                }
        });

    four =  findViewById(R.id.four);

    four.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, EduProcessActivity.class);
            startActivity(intent);
        }
    });

        five =  findViewById(R.id.five);

        five.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                        startActivity(intent);
                }
        });

        six =  findViewById(R.id.six);

        six.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, CommunityActivity.class);
                        startActivity(intent);
                }
        });

        seven =  findViewById(R.id.seven);

        seven.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, EndActivity.class);
                        startActivity(intent);
                }
        });

        }

    //뒤로가기 버튼을 두번 연속으로 눌러야 종료되게끔 하는 메소드
    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로가기 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }
}