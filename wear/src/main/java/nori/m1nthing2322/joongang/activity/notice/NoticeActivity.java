package nori.m1nthing2322.joongang.activity.notice;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

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

public class NoticeActivity extends AppCompatActivity {
    public static final String NoticeDBName = "Notice.db";
    public static final String NoticeTableName = "NoticeInfo";

    boolean isAdmin;

    ListView mListView;
    NoticeAdapter mAdapter;
    ProgressDialog mDialog;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setSubtitle(R.string.developer_title);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        isAdmin = new Preference(getApplicationContext()).getBoolean("userAdmin_1", false);

        mListView = findViewById(R.id.mListView);
        mAdapter = new NoticeAdapter(this);
        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showNoticeData();
            }
        });

        FloatingActionButton mFab = findViewById(R.id.mFab);
        if (isAdmin) {
            mFab.setVisibility(View.VISIBLE);
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdmin) {
                    startActivity(new Intent(getApplicationContext(), NoticeSendActivity.class).putExtra("userAdmin_1", true));
                } else {
                    Snackbar.make(view, R.string.user_info_require_message, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        checkPermission();
        showNoticeData();
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

    private void showNoticeData() {
        mAdapter.clearData();
        mAdapter.notifyDataSetChanged();

        if (Tools.isOnline(getApplicationContext())) {
            getNoticeDownloadTask mTask = new getNoticeDownloadTask();
            mTask.execute("https://docs.google.com/spreadsheets/d/1Cny4MK3_Y2m90QKUjLV0o_T1JhB0c1P37cAnD6giBj4/pubhtml?gid=1973119105&single=true");
        } else {
            offlineData();
            Toast.makeText(getApplicationContext(), "네트워크에 연결되어 있지않아,\n오프라인 모드로만 볼 수 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void offlineData() {
        if (new File(TimeTableTool.mFilePath + NoticeDBName).exists()) {
            showListViewDate();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
            builder.setTitle(R.string.no_network_title);
            builder.setMessage(R.string.no_network_msg);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }

        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    class getNoticeDownloadTask extends GoogleSheetTask {
        private Database mDatabase;
        private String[] columnFirstRow;

        @Override
        public void onPreDownload() {
            mDialog = new ProgressDialog(NoticeActivity.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.loading_title));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

            new File(TimeTableTool.mFilePath + NoticeDBName).delete();
            mDatabase = new Database();
        }

        @Override
        public void onFinish(long result) {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }

            if (mDatabase != null)
                mDatabase.release();

            showListViewDate();
        }

        @Override
        public void onRow(int startRowNumber, int position, String[] row) {
            if (startRowNumber == position) {
                columnFirstRow = row;

                StringBuilder Column = new StringBuilder();

                // remove deviceId
                for (int i = 0; i < row.length - 1; i++) {
                    Column.append(row[i]);
                    Column.append(" text, ");
                }

                mDatabase.openOrCreateDatabase(TimeTableTool.mFilePath, NoticeDBName, NoticeTableName, Column.substring(0, Column.length() - 2));
            } else {
                int length = row.length;
                for (int i = 0; i < length - 1; i++) {
                    mDatabase.addData(columnFirstRow[i], row[i]);
                }
                mDatabase.commit(NoticeTableName);
            }
        }
    }

    private void showListViewDate() {
        Database mDatabase = new Database();
        mDatabase.openDatabase(TimeTableTool.mFilePath, NoticeDBName);
        Cursor mCursor = mDatabase.getData(NoticeTableName, "*");

        for (int i = 0; i < mCursor.getCount(); i++) {
            mCursor.moveToNext();

            String date = mCursor.getString(1);
            String title = mCursor.getString(2);
            String message = mCursor.getString(3);

            mAdapter.addItem(title, message, date);
        }

        mAdapter.notifyDataSetChanged();

        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

}
