package aeyong.first.csw;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity{ // 상속이 TabActivity이다. 주의할 것!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpecSong = tabHost.newTabSpec("Community").setIndicator("커뮤니티");
        tabSpecSong.setContent(R.id.tab1_title);
        tabHost.addTab(tabSpecSong);

        TabHost.TabSpec tabSpecArtist = tabHost.newTabSpec("Schedule").setIndicator("학사 일정");
        tabSpecArtist.setContent(R.id.tab2_title);
        tabHost.addTab(tabSpecArtist);

        TabHost.TabSpec tabSpecAlbum = tabHost.newTabSpec("App Notice").setIndicator("앱 공지");
        tabSpecAlbum.setContent(R.id.tab3_title);
        tabHost.addTab(tabSpecAlbum);

        tabHost.setCurrentTab(0);

    }
}