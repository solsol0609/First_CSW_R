package nori.m1nthing2322.joongang.activity.timetable;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import itmir.tistory.com.spreadsheets.GoogleSheetTask;
import nori.m1nthing2322.joongang.R;
import nori.m1nthing2322.joongang.tool.Database;
import nori.m1nthing2322.joongang.tool.Preference;
import nori.m1nthing2322.joongang.tool.TimeTableTool;
import nori.m1nthing2322.joongang.tool.Tools;

/**
 * 이 소스는 원작자: Mir(whdghks913)에 의해 생성되었으며,
 * 2차 수정자: NoriDev(noridevdroid@gmail.com)에 의해 수정되었습니다.
 *
 * 본 소스를 사용하고자 한다면, 이 주석을 삭제 또는 수정해서는 안됩니다.
 * 또한 앱 내부 및 스토어 등록 정보에서 다른 사람이 볼 수 있는 곳에 적어도 하나 이상의 위치에 위 저작자가 표시되어야 합니다.
 */

public class TimeTableActivity extends AppCompatActivity {
    Preference mPref;
    ViewPager viewPager;

    private int timetableVer= 20180101;
    String xml;

    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        mPref = new Preference(getApplicationContext());
        int mGrade = mPref.getInt("myGrade", -1);
        int mClass = mPref.getInt("myClass", -1);

        Toolbar mToolbar = findViewById(R.id.mToolbar);
        if ((mGrade != -1) && (mClass != -1)) {
            mToolbar.setTitle(getString(R.string.title_activity_time_table));
            mToolbar.setSubtitle(String.format(getString(R.string.timetable_subtitle), mGrade, mClass));
        }
        setSupportActionBar(mToolbar);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        if ((mGrade == -1) || (mClass == -1)) {
            resetGrade();
            return;
        }

        if (!TimeTableTool.fileExists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle(R.string.no_time_table_db_title);
            builder.setMessage(R.string.no_time_table_db_message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downloadingDB();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();
            return;
        }

        viewPager = findViewById(R.id.mViewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = findViewById(R.id.mTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        FloatingActionButton mFab = findViewById(R.id.mFab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadingDB();
            }
        });

        checkPermission();
        setCurrentItem();
        timeTableUpdate();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 안드로이드 버전이 6.0 이상인 경우
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "정상적인 앱 구동을 위해 읽기/쓰기 권한 허용 필요", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 2);  // 마지막 인자는 체크해야 될 권한 갯수
            } else { }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //위 예시에서 requestPermission 메서드를 썼을시 , 마지막 매개변수에 0을 넣어 줬으므로, 매칭
        if (requestCode == 0) {
            // requestPermission의 두번째 매개변수는 배열이므로 아이템이 여러개 있을 수 있기 때문에 결과를 배열로 받는다.
            // 해당 예시는 요청 퍼미션이 한개 이므로 i=0 만 호출한다.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //해당 권한이 승낙된 경우
            } else {
                //해당 권한이 거절된 경우
                Toast.makeText(this , "권한사용을 동의해주셔야 이용이 가능합니다." , Toast.LENGTH_LONG ).show();
                finish();
            }
        }
    }

    private void timeTableUpdate() {
        pref = getSharedPreferences("pref", 0);  //변경하지 마시오
        edit = pref.edit();   //변경하지 마시오

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        StringBuilder sBuffer = new StringBuilder();
        try{//Start Try
            String urlAddr = "https://raw.githubusercontent.com/NoriDev/Project-School/master/version/Project_School_TimeTable.xml";
            URL url = new URL(urlAddr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if(conn != null){//Start if
                conn.setConnectTimeout(1000);
                //conn.setUseCaches(false);
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){//Start if
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    while(true){//Start While
                        String line = br.readLine();
                        if(line==null){//Start if
                            break;
                        }//end if
                        sBuffer.append(line);
                    }//end while
                    br.close();
                    conn.disconnect();
                }//end if
            }//end if
            xml = sBuffer.toString();
            CountDownTimer _timer = new CountDownTimer(1000, 1000){
                public void onTick(long millisUntilFinished)
                {}
                public void onFinish(){
                    if(Integer.parseInt(xml)==timetableVer){//new version
//                        Toast.makeText(getApplicationContext(), R.string.latest_version, Toast.LENGTH_SHORT).show();
                    } else if(Integer.parseInt(xml)>timetableVer){
                        //현재 버전보다 서버 버전이 높을때
                        if(pref.getInt("timetable_20180101",0)==0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TimeTableActivity.this);
                            builder.setTitle("시간표가 업데이트됨");
                            builder.setMessage("시간표가 업데이트 됨에 따라, 기존 시간표를 업데이트 하셔야 합니다.\n시간표를 업데이트 하시려면 \'확인\'을 눌러주십시오.\n\n- 이 알림은 시간표를 최신버전으로 업데이트 해야 사라집니다 -");
                            builder.setCancelable(false);
                            builder.setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.setPositiveButton(R.string.update_now, new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        downloadingDB();
                                        edit.putInt("timetable_20180101",1);
                                        edit.remove("timetable_20170201");  // 이전 변수를 지울 때 주석 제거
                                        edit.apply();
                                    }
                                });
                            builder.show();
                        }
                    } else {
                        //현재 버전보다 서버 버전이 낮을때
                    }
                }
            };
            _timer.start();
        }//end try
        catch (Exception e) {
            //네트워크가 올바르지 않을때
        }
    }

    private void setCurrentItem() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek > 1 && dayOfWeek < 7) {
            viewPager.setCurrentItem(dayOfWeek - 2);
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    private void resetGrade() {
        mPref.remove("myGrade");

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.action_setting_mygrade);
        builder.setItems(R.array.myGrade, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPref.putInt("myGrade", which + 1);
                resetClass();
            }
        });
        builder.show();
    }

    private void resetClass() {
        mPref.remove("myClass");

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.action_setting_myclass);
        builder.setItems(R.array.myClass, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPref.putInt("myClass", which + 1);
                startActivity(new Intent(getApplicationContext(), TimeTableActivity.class));
                Toast.makeText(getApplicationContext(), "학급 변경 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.show();
    }

    public void downloadingDB() {
        if (Tools.isOnline(getApplicationContext())) {
            downloadStart();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
            builder.setTitle(R.string.no_network_title);
            builder.setMessage(R.string.no_network_msg);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setCancelable(false);
            builder.show();
        }
    }

    private void downloadStart() {
        new File(TimeTableTool.mFilePath + TimeTableTool.TimeTableDBName).delete();
        DBDownloadTask mTask = new DBDownloadTask();
        mTask.execute(TimeTableTool.mGoogleSpreadSheetUrl);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter mAdapter = new Adapter(getSupportFragmentManager());

        for (int dayOfWeek = 0; dayOfWeek < 5; dayOfWeek++) {
            mAdapter.addFragment(TimeTableTool.mDisplayName[dayOfWeek], TimeTableFragment.getInstance(mPref.getInt("myGrade", -1), mPref.getInt("myClass", -1), dayOfWeek));
        }
        viewPager.setAdapter(mAdapter);
    }

    class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        public void addFragment(String mTitle, Fragment mFragment) {
            mFragments.add(mFragment);
            mFragmentTitles.add(mTitle);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    class DBDownloadTask extends GoogleSheetTask {
        private ProgressDialog mDialog;
        private Database mDatabase;
        private String[] columnFirstRow;

        @Override
        public void onPreDownload() {
            mDialog = new ProgressDialog(TimeTableActivity.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.loading_title));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

            mDatabase = new Database();
        }

        @Override
        public void onFinish(long result) {
            startActivity(new Intent(TimeTableActivity.this, TimeTableActivity.class));
            finish();

            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
            if (mDatabase != null)
                mDatabase.release();
        }

        @Override
        public void onRow(int startRowNumber, int position, String[] row) {
            if (startRowNumber == position) {
                columnFirstRow = row;

                StringBuilder Column = new StringBuilder();

                for (String column : row) {
                    Column.append(column);
                    Column.append(" text, ");
                }
                mDatabase.openOrCreateDatabase(TimeTableTool.mFilePath, TimeTableTool.TimeTableDBName, TimeTableTool.tableName, Column.substring(0, Column.length() - 2));
            } else {
                int length = row.length;
                for (int i = 0; i < length; i++) {
                    mDatabase.addData(columnFirstRow[i], row[i]);
                }
                mDatabase.commit(TimeTableTool.tableName);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset_mygrade) {
            resetGrade();
            return true;
        } else if (id == R.id.action_share_timetable) {
            shareTimeTable();
            return true;
        } else if (id == R.id.action_download_db) {
            downloadingDB();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareTimeTable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.action_share_day);
        builder.setItems(R.array.myWeek, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareTimeTable(which, mPref.getInt("myGrade", -1), mPref.getInt("myClass", -1));
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void shareTimeTable(int position, int mGrade, int mClass) {
        try {
            String mText = "";
            TimeTableTool.timeTableData mData = TimeTableTool.getTimeTableData(mGrade, mClass, position + 2);
            String[] subject = mData.subject;

            for (int period = 0; period < 7; period++) {
                mText += "\n" + (period + 1) + "교시 : " + subject[period];
            }

            String title = getString(R.string.action_share_timetable);
            Intent msg = new Intent(Intent.ACTION_SEND);
            msg.addCategory(Intent.CATEGORY_DEFAULT);
            msg.putExtra(Intent.EXTRA_TITLE, title);
            msg.putExtra(Intent.EXTRA_TEXT, String.format(
                    getString(R.string.action_share_timetable_msg),
                    TimeTableTool.mDisplayName[position], mText));
            msg.setType("text/plain");
            startActivity(Intent.createChooser(msg, title));

        } catch (Exception ex) {
            ex.printStackTrace();

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
            builder.setTitle(R.string.Unknown_error_title);
            builder.setMessage(R.string.Unknown_error_message);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setCancelable(false);
            builder.show();
        }
    }
}
