package nori.m1nthing2322.joongang.activity.bap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import nori.m1nthing2322.joongang.R;
import nori.m1nthing2322.joongang.tool.BapTool;
import nori.m1nthing2322.joongang.tool.Preference;
import nori.m1nthing2322.joongang.tool.ProcessTask;
import nori.m1nthing2322.joongang.tool.Tools;

/**
 * 이 소스는 원작자: Mir(whdghks913)에 의해 생성되었으며,
 * 2차 수정자: NoriDev(noridevdroid@gmail.com)에 의해 수정되었습니다.
 *
 * 본 소스를 사용하고자 한다면, 이 주석을 삭제 또는 수정해서는 안됩니다.
 * 또한 앱 내부 및 스토어 등록 정보에서 다른 사람이 볼 수 있는 곳에 적어도 하나 이상의 위치에 위 저작자가 표시되어야 합니다.
 */

public class BapActivity extends AppCompatActivity {

    ListView mListView;
    BapAdapter mAdapter;

    Calendar mCalendar;
    int YEAR, MONTH, DAY;
    int DAY_OF_WEEK;

    BapDownloadTask mProcessTask;
    ProgressDialog mDialog;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bap);
        Toolbar mToolbar = findViewById(R.id.mToolbar);
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

        getCalendarInstance(true);
        mListView = findViewById(R.id.mListView);
        mAdapter = new BapAdapter(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                BapListData mData = mAdapter.getItem(position);
                String mShareBapMsg = String.format(getString(R.string.shareBap_message_msg),
                        mData.mCalender, mData.mLunch, mData.mDinner);
                withShare(mShareBapMsg);

                return true;
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCalendarInstance(true);
                getBapList(true);

                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton mFab = findViewById(R.id.mFab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalenderBap();
            }
        });

        getBapList(true);

        checkPermission();
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

    private void getCalendarInstance(boolean getInstance) {
        if (getInstance || (mCalendar == null))
            mCalendar = Calendar.getInstance();
        YEAR = mCalendar.get(Calendar.YEAR);
        MONTH = mCalendar.get(Calendar.MONTH);
        DAY = mCalendar.get(Calendar.DAY_OF_MONTH);
        DAY_OF_WEEK = mCalendar.get(Calendar.DAY_OF_WEEK);
    }

    private void getBapList(boolean isUpdate) {
        boolean isNetwork = Tools.isOnline(this);

        mAdapter.clearData();
        mAdapter.notifyDataSetChanged();

        getCalendarInstance(false);

        final Calendar mToday = Calendar.getInstance();
        final int TodayYear = mToday.get(Calendar.YEAR);
        final int TodayMonth = mToday.get(Calendar.MONTH);
        final int TodayDay = mToday.get(Calendar.DAY_OF_MONTH);

        // 이번주 월요일 날짜를 가져온다
        mCalendar.add(Calendar.DATE, 2 - DAY_OF_WEEK);

        for (int i = 0; i < 5; i++) {
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);

            BapTool.restoreBapDateClass mData =
                    BapTool.restoreBapData(this, year, month, day);

            if (mData.isBlankDay) {
                if (isUpdate && isNetwork) {
                    mDialog = new ProgressDialog(this);
                    mDialog.setIndeterminate(true);
                    mDialog.setMessage(getString(R.string.loading_title));
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();

                    mProcessTask = new BapDownloadTask(this);
                    mProcessTask.execute(year, month, day);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
                    builder.setTitle(R.string.no_network_title);
                    builder.setMessage(R.string.no_network_msg);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setCancelable(false);
                    builder.show();
                }
                return;
            }

            // if day equals today
            if ((year == TodayYear) && (month == TodayMonth) && (day == TodayDay)) {
                mAdapter.addItem(mData.Calender, mData.DayOfTheWeek, mData.Lunch, mData.Dinner, true);
            } else {
                mAdapter.addItem(mData.Calender, mData.DayOfTheWeek, mData.Lunch, mData.Dinner, false);
            }
            mCalendar.add(Calendar.DATE, 1);
        }

        mCalendar.set(YEAR, MONTH, DAY);
        mAdapter.notifyDataSetChanged();
        setCurrentItem();
    }

    private void setCurrentItem() {
        if (DAY_OF_WEEK > 1 && DAY_OF_WEEK < 7) {
            if (mAdapter.getCount() == 5)
                mListView.setSelection(DAY_OF_WEEK - 2);
        } else {
            mListView.setSelection(0);
        }
    }

    public void setCalenderBap() {
        getCalendarInstance(false);

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                mCalendar.set(year, month, day);
                getCalendarInstance(false);
                getBapList(true);
            }
        }, year, month, day, false);
        datePickerDialog.setYearRange(2006, 2030);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(getSupportFragmentManager(), "Tag");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calender) {
            setCalenderBap();
            return true;

        } else if (id == R.id.action_refresh) {
            boolean isNetwork = Tools.isOnline(this);

            if (isNetwork) {
                getCalendarInstance(false);

                mCalendar.add(Calendar.DATE, 2 - DAY_OF_WEEK);

                String mPrefLunchName = BapTool.getBapStringFormat(YEAR, MONTH, DAY, BapTool.TYPE_LUNCH);
                String mPrefDinnerName = BapTool.getBapStringFormat(YEAR, MONTH, DAY, BapTool.TYPE_DINNER);

                Preference mPref = new Preference(getApplicationContext(), BapTool.BAP_PREFERENCE_NAME);
                mPref.remove(mPrefLunchName);
                mPref.remove(mPrefDinnerName);

                getBapList(true);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
                builder.setTitle(R.string.no_network_title);
                builder.setMessage(R.string.no_network_msg);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setCancelable(false);
                builder.show();
            }
            return true;
        } else if (id == R.id.action_today) {
            getCalendarInstance(true);
            getBapList(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class BapDownloadTask extends ProcessTask {
        public BapDownloadTask(Context mContext) {
            super(mContext);
        }

        @Override
        public void onPreDownload() {
        }

        @Override
        public void onUpdate(int progress) {
            mDialog.setProgress(progress);
        }

        @Override
        public void onFinish(long result) {
            if (mDialog != null)
                mDialog.dismiss();
            if (result == -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BapActivity.this, R.style.AppCompatErrorAlertDialogStyle);
                builder.setTitle(R.string.meal_error_title);
                builder.setMessage(R.string.meal_error_message);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setCancelable(false);
                builder.show();
                return;
            }
            getBapList(false);

            if (mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void withShare(String mShareBapMsg) {
        Intent msg = new Intent(Intent.ACTION_SEND);
        msg.addCategory(Intent.CATEGORY_DEFAULT);
        msg.putExtra(Intent.EXTRA_TITLE, getString(R.string.shareBap_title));
        msg.putExtra(Intent.EXTRA_TEXT, mShareBapMsg);
        msg.setType("text/plain");
        startActivity(Intent.createChooser(msg, getString(R.string.shareBap_title)));
    }
}
