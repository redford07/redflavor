package com.example.vip.redfalvor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {
    SessionCallback callback;
    public static boolean klogin=false;
    public static final String NICKNAME = "nick";
    public static final String USER_ID = "id";
    public static final String PROFILE_IMG = "img";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText id = (EditText) findViewById(R.id.id);
        final EditText password = (EditText)findViewById(R.id.password);
        Button logbtn = (Button)findViewById(R.id.login);


        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().isEmpty()==true || password.getText().toString().isEmpty()==true)
                {
                 Toast.makeText(getApplicationContext(),"빈칸은 모두 채워주세요",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new JSONTask().execute("http://222.107.4.162:3824/member/login");
                }

            }
        });


        Button toregisterbtn =(Button)findViewById(R.id.registerbtn);
        Intent intent1 = getIntent();



        toregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);

            }
        });



        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Toast.makeText(getApplicationContext(),"로그아웃 되었습니다",Toast.LENGTH_LONG).show();
            }
        });

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                        //재로그인구현필요
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {



                    final String nickName = userProfile.getNickname();
                    final long userID = userProfile.getId();
                    final String pImage = userProfile.getProfileImagePath();
                    klogin =true;
                    //Toast.makeText(getApplicationContext(),"카카오톡 로그인성공",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    intent.putExtra(NICKNAME,nickName);
                    intent.putExtra(USER_ID,String.valueOf(userID));
                    intent.putExtra(PROFILE_IMG,pImage);
                    startActivity(intent);
                    finish();

                }
            });



        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때

            if(exception!=null) Logger.e(exception);
            Toast.makeText(getApplicationContext(),"세션 처리중입니다. 환영합니다 made by redflavor",Toast.LENGTH_SHORT).show();
        }
    }

    ///로그인을 위한 내부클래스
    public class JSONTask extends AsyncTask<String, String, String> {

        EditText eid = (EditText) findViewById(R.id.id);
        EditText epassword = (EditText)findViewById(R.id.password);
        Button elogbtn = (Button)findViewById(R.id.login);




        @Override
        public String doInBackground(String... urls) {

            try {
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("id", eid.getText().toString());
                jsonObject.accumulate("password",  epassword.getText().toString());



                HttpURLConnection con = null;
                BufferedReader reader = null;
                try{
                    //   URL url = new URL("http://203.249.39.167:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();


                    OutputStream outStream = con.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    return buffer.toString();
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //  final TextView tvData = (TextView) findViewById(R.id.testtext);
            //  tvData.setText(result);
            if(result.contains("false"))
            {
                Toast.makeText(getApplicationContext(),"아이디 혹은 비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
            }
            else if(result.contains("true"))
            {
                Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), ServiceActivity.class);
                startActivity(intent1);
                finish();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"정의되지 않은 값입니다. 관리자에게 문의하세요.",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }





}
