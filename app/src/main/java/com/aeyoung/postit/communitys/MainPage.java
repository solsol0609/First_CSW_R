package com.aeyoung.postit.communitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by YooJongHyeok on 2017-07-13.
 */

public class MainPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainPage.this,WelcomeActivity.class);
        startActivity(intent);
    }
}