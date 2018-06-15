package com.example.vip.redfalvor;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;



public class PreSettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    private static final String TAG = "PreSettingsActivity";

    private static final String USE_USER_NAME = "key_useUserName";
    private static final String USER_NAME = "key_userName";
    private static final String USE_BACKGROUND_COLOR = "key_backgroundcolor";
    private static final String BACKGROUND_COLOR = "key_dialog_backgroundcolor";
    private static final String TEXT_COLOR = "key_textcolor";
    private static final String ALL_REMOVE_MEMO = "key_all_memo_clear";

    private PreferenceScreen screen;
    private CheckBoxPreference mUseUsername;
    private EditTextPreference mUsername;
    private ListPreference mbackgroundcolor;
    private ListPreference mTextcolor;

  //  private ContentViewActivity contentview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        screen = getPreferenceScreen();

        //인자로 전달되는 Key값을 가지는 Preference 항목의 인스턴스를 가져옴
        //굳이 여러곳에서 사용하지 않는 이상에는 이런식으로 객체화 시킬필요는 없는듯
        mUsername = (EditTextPreference) screen.findPreference(USER_NAME);
        mbackgroundcolor = (ListPreference) screen.findPreference(BACKGROUND_COLOR);
        mTextcolor = (ListPreference) screen.findPreference(TEXT_COLOR);

        //변화 이벤트가 일어났을 시 동작
        mUsername.setOnPreferenceChangeListener(this);
        mbackgroundcolor.setOnPreferenceChangeListener(this);
        mTextcolor.setOnPreferenceChangeListener(this);
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
        if(preference == mUsername){
            Log.i(TAG, "mUsername onPreferenceChange");
            mUsername.setSummary(value);
        }else if(preference == mbackgroundcolor){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            mbackgroundcolor.setSummary(index >= 0 ? listPreference.getEntries()[index]
                    : null);    // entries 값 대신 이에 해당하는 entryValues값 set
        }else if(preference == mTextcolor){
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
        mUsername.setSummary(mUsername.getText());
        mbackgroundcolor.setSummary(mbackgroundcolor.getEntry());
        mTextcolor.setSummary(mTextcolor.getEntry());
        Log.d(TAG,"mbackgroundcolor="+mbackgroundcolor +", mUsername :" + mUsername);
    }
}
