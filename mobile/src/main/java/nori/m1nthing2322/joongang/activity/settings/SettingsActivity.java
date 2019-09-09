package nori.m1nthing2322.joongang.activity.settings;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import nori.m1nthing2322.joongang.R;
import nori.m1nthing2322.joongang.autoupdate.updateAlarm;

/**
 * 이 소스는 원작자: Mir(whdghks913)에 의해 생성되었으며,
 * 2차 수정자: NoriDev(noridevdroid@gmail.com)에 의해 수정되었습니다.
 *
 * 본 소스를 사용하고자 한다면, 이 주석을 삭제 또는 수정해서는 안됩니다.
 * 또한 앱 내부 및 스토어 등록 정보에서 다른 사람이 볼 수 있는 곳에 적어도 하나 이상의 위치에 위 저작자가 표시되어야 합니다.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(R.id.container, new PrefsFragment()).commit();

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

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_settings);

/*            boolean isAdmin = getPreferenceManager().getSharedPreferences().getBoolean("userAdmin_1", false);
            if (isAdmin) {
                Preference proUpgrade = findPreference("proUpgrade");
                proUpgrade.setSummary(R.string.user_info_licensed);
                proUpgrade.setEnabled(false);
            } */

            setOnPreferenceClick(findPreference("infoAutoUpdate"));
            setOnPreferenceClick(findPreference("openSource"));
            setOnPreferenceClick(findPreference("project"));
            setOnPreferenceClick(findPreference("Changelog"));
			setOnPreferenceClick(findPreference("Developer"));
            //setOnPreferenceClick(findPreference("proUpgrade"));
            setOnPreferenceChange(findPreference("autoBapUpdate"));
            setOnPreferenceChange(findPreference("updateLife"));

            try {
                PackageManager packageManager = getActivity().getPackageManager();
                PackageInfo info = packageManager.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
                findPreference("appBuildName").setSummary(info.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void setOnPreferenceClick(Preference mPreference) {
            mPreference.setOnPreferenceClickListener(onPreferenceClickListener);
        }

        private Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                String getKey = preference.getKey();

                if ("openSource".equals(getKey)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(R.string.license_title);
                    builder.setMessage(R.string.license_msg);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setCancelable(false);
                    builder.show();
                } else if ("project".equals(getKey)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(R.string.project_title);
                    builder.setMessage(R.string.project_msg);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setCancelable(false);
                    builder.show();
                } else if ("Changelog".equals(getKey)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
// Stable Version
                    builder.setTitle(R.string.changelog_title);
                    builder.setMessage(R.string.changelog_msg);
// Beta Version
//                    builder.setTitle(R.string.changelog_title_beta);
//                    builder.setMessage(R.string.changelog_msg_beta);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setCancelable(false);
                    builder.show();
                } else if ("Developer".equals(getKey)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(R.string.developer_title);
                    builder.setMessage(R.string.developer_msg);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setCancelable(false);
                    builder.show();
                } else if ("infoAutoUpdate".equals(getKey)) {
                    showNotification();
/*                } else if ("proUpgrade".equals(getKey)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(R.string.user_info_class_up_title);
//                    builder.setMessage(R.string.no_network_msg);
                    // Set an EditText view to get user input
                    final EditText input = new EditText(getActivity());
                    builder.setView(input);

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = input.getText().toString();
                            SecurityXor securityXor = new SecurityXor();
                            if ("^fgmhgnAz".equals(securityXor.getSecurityXor(value, 777))) {
                                getPreferenceManager().getSharedPreferences().edit().putBoolean("userAdmin_1", true).commit();
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, null);
                    builder.show();
                } */
                }
                return true;
            }
        };

        private void setOnPreferenceChange(Preference mPreference) {
            mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);

            if (mPreference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) mPreference;
                int index = listPreference.findIndexOfValue(listPreference.getValue());
                mPreference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            } else if (mPreference instanceof EditTextPreference) {
                String values = ((EditTextPreference) mPreference).getText();
                if (values == null) values = "";
                onPreferenceChangeListener.onPreferenceChange(mPreference, values);
            }
        }

        private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();

                if (preference instanceof EditTextPreference) {
                    preference.setSummary(stringValue);

                } else if (preference instanceof ListPreference) {
                    /*
                     * ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를
                     * 적용하지 못한다 따라서 설정한 entries에서 String을 로딩하여 적용한다
                     */
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);

                    preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                    updateAlarm updateAlarm = new updateAlarm(getActivity());
                    updateAlarm.cancel();

                    if (index == 0) updateAlarm.autoUpdate();
                    else if (index == 1) updateAlarm.SaturdayUpdate();
                    else if (index == 2) updateAlarm.SundayUpdate();

                } else if (preference instanceof CheckBoxPreference) {
                    nori.m1nthing2322.joongang.tool.Preference mPref = new nori.m1nthing2322.joongang.tool.Preference(getActivity());

                    if (mPref.getBoolean("firstOfAutoUpdate", true)) {
                        mPref.putBoolean("firstOfAutoUpdate", false);
//                        showNotification();
                    }

                    if (!mPref.getBoolean("autoBapUpdate", false) && preference.isEnabled()) {
                        int updateLife = Integer.parseInt(mPref.getString("updateLife", "0"));

                        updateAlarm updateAlarm = new updateAlarm(getActivity());
                        if (updateLife == 1) updateAlarm.autoUpdate();
                        else if (updateLife == 0) updateAlarm.SaturdayUpdate();
                        else if (updateLife == -1) updateAlarm.SundayUpdate();

                    } else {
                        updateAlarm updateAlarm = new updateAlarm(getActivity());
                        updateAlarm.cancel();
                    }
                }
                return true;
            }
        };

        private void showNotification() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
            builder.setTitle(R.string.info_autoUpdate_title);
            builder.setCancelable(false);
            builder.setMessage(R.string.info_autoUpdate_msg);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }
}
