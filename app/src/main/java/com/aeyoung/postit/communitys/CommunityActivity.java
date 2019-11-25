package com.aeyoung.postit.communitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class CommunityActivity extends AppCompatActivity {

    private long lastTimeBackPressed;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // 웹뷰
        webView = (WebView) findViewById(R.id.webView);
        webView.setHorizontalScrollBarEnabled(false); // 가로 스크롤 방지
        webView.setVerticalScrollBarEnabled(false); // 세로 스크롤 방지
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null); // 속도 향상
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null); // 속도 향상
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  // 캐시모드
        webView.setWebViewClient(new WebViewClient()); // 이걸 안해주면 새창이 뜸
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // width, height가 화면 크기와 맞지 않는 버그 해결
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // javascript의 window.opne 허용
        webView.getSettings().setJavaScriptEnabled(true); // 자바 스크립트 허용
        webView.getSettings().setUseWideViewPort(true); //meta태그의 viewport사용 가능
        webView.loadUrl("https://woosongcs.ml/");

    }

    // 백버튼 터치시 웹뷰페이지 뒤로 가기. 더이상 뒤로 갈곳이 없으면 연속 두번 터치시 종료
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();

        } else {
            if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
                finish();
                return;
            }
            lastTimeBackPressed = System.currentTimeMillis();
            Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }
}