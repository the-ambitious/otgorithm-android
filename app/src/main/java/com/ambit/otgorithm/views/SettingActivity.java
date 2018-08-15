package com.ambit.otgorithm.views;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;

public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    Toolbar toolbar;
    TextView textViewToolbarTitle;

    public static boolean alarmIsNotChecked;
    public static int alram;

    private static final String TAG = "PreSettingsActivity";

    private static final String USE_USER_NAME = "key_useUserName";
    private static final String USER_NAME = "key_userName";
    private static final String USE_BACKGROUND_COLOR = "key_backgroundcolor";
    private static final String BACKGROUND_COLOR = "key_dialog_backgroundcolor";
    private static final String TEXT_COLOR = "key_textcolor";
    private static final String ALL_REMOVE_MEMO = "key_all_memo_clear";

    private static final String PUSH_ALARM = "push_alarm";

    public static Preference mPushAlarm;

    private PreferenceScreen screen;
    private CheckBoxPreference mUseUsername;
    private EditTextPreference mUsername;
    private ListPreference mbackgroundcolor;
    private ListPreference mTextcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);

        textViewToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        textViewToolbarTitle.setText("설정");
        textViewToolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewToolbarTitle.setTextColor(Color.WHITE);

        /*toolbar.setTitle("설정");
        toolbar.setTitleTextColor(Color.WHITE);*/
        //setGravity(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //
        addPreferencesFromResource(R.xml.pref_settings);

        // 차단친구 관리
        findPreference("blacklist").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                intent.putExtra("blacknwhite", "blacklist");
                startActivity(intent);
                return false;
            }
        });

        // 이용약관
        findPreference("terms").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
                intent.putExtra("description", "terms");
                startActivity(intent);
                return false;
            }
        });

        // 개인정보취급방침
        findPreference("privacy").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
                intent.putExtra("description", "privacy");
                startActivity(intent);
                return false;
            }
        });

        // 오픈 소스 라이브러리(url 수정 필요)
        findPreference("license").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
                intent.putExtra("description", "license");
                startActivity(intent);
                return false;
            }
        });


        screen = getPreferenceScreen();

        //인자로 전달되는 Key값을 가지는 Preference 항목의 인스턴스를 가져옴
        //굳이 여러곳에서 사용하지 않는 이상에는 이런식으로 객체화 시킬필요는 없는듯
        mUsername = (EditTextPreference) screen.findPreference(USER_NAME);
        mbackgroundcolor = (ListPreference) screen.findPreference(BACKGROUND_COLOR);
        mTextcolor = (ListPreference) screen.findPreference(TEXT_COLOR);
        mPushAlarm = screen.findPreference(PUSH_ALARM);

        findPreference("push_alarm").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                return false;
            }
        });
/*        mPushAlarm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // 푸시 알람 스위치 on/off를 알려주는 변수
//                alarmIsNotChecked = mPushAlarm.isChecked();


                *//*if (!alarmIsNotChecked) {
                    // 푸시 알람이 on일 경우
                    Toast.makeText(SettingActivity.this, "switch on", Toast.LENGTH_SHORT).show();
                } else {
                    // 푸시 알람이 off일 경우
                    Toast.makeText(SettingActivity.this, "switch off", Toast.LENGTH_SHORT).show();
                }
                *//*
                return true;
            }
        });*/
        //변화 이벤트가 일어났을 시 동작
/*        mUsername.setOnPreferenceChangeListener(this);
        mbackgroundcolor.setOnPreferenceChangeListener(this);
        mTextcolor.setOnPreferenceChangeListener(this);*/
    }

    @Override
    public void onResume(){
        super.onResume();

        updateSummary();
        Log.d(TAG,"onResume");
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        Log.i(TAG, "onPreferenceTreeClick");
        if(preference.getKey().equals("key_all_memo_clear")){
            Log.i(TAG, "key_all_memo_clear");
            //모든 메모 삭제 기능 넣을 것
//          showRemoveMemoDialog();
        }
        return false;
    }


    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.i(TAG, "preference : " + preference +", newValue : "+ newValue);

        String value = (String) newValue;
        if (preference == mUsername) {
            Log.i(TAG, "mUsername onPreferenceChange");
            mUsername.setSummary(value);
        } else if (preference == mbackgroundcolor) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            mbackgroundcolor.setSummary(index >= 0 ? listPreference.getEntries()[index]
                    : null);    // entries 값 대신 이에 해당하는 entryValues값 set
        } else if (preference == mTextcolor) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            mTextcolor.setSummary(index >= 0 ? listPreference.getEntries()[index]
                    : null);    // entries 값 대신 이에 해당하는 entryValues값 set
        }
        return true;
    }


    private void updateSummary(){
        //액티비티 실행 할 때 저장되어있는 summary값을 set
        //안 하면 안 뜸
/*        mUsername.setSummary(mUsername.getText());
        mbackgroundcolor.setSummary(mbackgroundcolor.getEntry());
        mTextcolor.setSummary(mTextcolor.getEntry());*/
        Log.d(TAG,"mbackgroundcolor="+mbackgroundcolor +", mUsername :" + mUsername);
    }

    /*    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.activity_setting, new LinearLayout(this), false);

        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar_basic);

        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);

        getWindow().setContentView(contentView);
    }*/

}
