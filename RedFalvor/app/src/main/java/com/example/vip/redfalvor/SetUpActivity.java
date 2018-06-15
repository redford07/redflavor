package com.example.vip.redfalvor;



import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.View;
import android.widget.Button;


public class SetUpActivity extends Activity implements View.OnClickListener{

    private Button Button1, Button2;
    private Fragment fr = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Button1 = (Button)findViewById(R.id.button1);
        Button2 = (Button)findViewById(R.id.button2);

        Button1.setOnClickListener(this);
        Button2.setOnClickListener(this);


        if(findViewById(R.id.fragment_container) != null){

            if(savedInstanceState != null) return;

            SetUpAuthActivity firstFragment = new SetUpAuthActivity();
            firstFragment.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();

        }
    }


    public void onClick(View v){

        switch (v.getId()) {
            case R.id.button1:
                fr= new SetUpAuthActivity();
                selectFragment(fr);
                break;

            case R.id.button2:
                fr= new SetUpInfoActivity();
                selectFragment(fr);
                break;
        }

    }

    public void selectFragment(Fragment fr){

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fr);

        //활성화 했을 때 뷰가 겹쳐져서 올라감. 뒤로 누르면 앱 종료가 아니라 이전 fragment가 나옴
        //사용자가 BACK키를 눌러서 프래그먼트가 복구되면 프래그먼트는 다시 시작하게 된다.
        //트랜잭션을 백스택에 추가하지 않았다면 프래그먼트는 제거or교체될 때 파괴 된다.
        //fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }

}
