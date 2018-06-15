package com.example.vip.redfalvor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

/**
 * Created by VIP on 2018-05-09.
 */

public class RegisterActivity extends Activity {

    public static final String NICKNAME = "nick";

    boolean jb =false;
    static boolean kakaologin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextView registertext= (TextView)findViewById(R.id.registertext);
        final EditText epassword = (EditText) findViewById(R.id.password);
        final EditText eid = (EditText) findViewById(R.id.insertid);
        final EditText epasscheck = (EditText) findViewById(R.id.passcheck);
        final EditText ename = (EditText) findViewById(R.id.name);
        final EditText egender = (EditText) findViewById(R.id.gender);
        final EditText eaddress = (EditText) findViewById(R.id.address);
        final EditText eemail = (EditText) findViewById(R.id.email);
        Button comfirmbtn = (Button) findViewById(R.id.comfirmbtn);
        Button jbcheck = (Button)findViewById(R.id.jbcheck);
        Button passcomfirmbtn =(Button)findViewById(R.id.passcomfirmbtn);
        passcomfirmbtn.setVisibility(View.GONE);



        final Intent intent = getIntent();
        //  uniqueNTxt.setText(getIntent().getStringExtra(MainActivity.USER_ID)); //고유번호 출력
        //   pNameTxt.setText(getIntent().getStringExtra(MainActivity.NICKNAME));
       final String usernick=getIntent().getStringExtra(MainActivity.NICKNAME);


        String hello = getIntent().getStringExtra(MainActivity.NICKNAME) + "님 추가적인 정보를 입력해주세요";
        if(TextUtils.isEmpty(getIntent().getStringExtra(MainActivity.USER_ID))==false)
        {
            Toast.makeText(getApplicationContext(),hello,Toast.LENGTH_SHORT).show();
            eid.setText(getIntent().getStringExtra(MainActivity.USER_ID));
            eid.setFocusable(false);
            eid.setClickable(false);

            ename.setText(getIntent().getStringExtra(MainActivity.NICKNAME));
            ename.setFocusable(false);
            ename.setClickable(false);

            comfirmbtn.setText("추가정보입력");
            registertext.setText("추가정보입력");
            passcomfirmbtn.setVisibility(View.VISIBLE);
            jbcheck.setVisibility(View.GONE);
            epasscheck.setVisibility(View.GONE);
            epassword.setVisibility(View.GONE);
            kakaologin=true;
        }

        passcomfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), ServiceActivity.class);
                intent1.putExtra(NICKNAME,getIntent().getStringExtra(MainActivity.NICKNAME));
                startActivity(intent1);
                finish();

            }
        });



        jbcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"작업중입니다..^^",Toast.LENGTH_SHORT).show();
            }
        });

            comfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String sid = eid.getText().toString();
                final String spassword = epassword.getText().toString();
                final String spasscheck = epasscheck.getText().toString();
                final String sname = ename.getText().toString();
                final String sgender = egender.getText().toString();
                final String saddress = eaddress.getText().toString();
                final String semail = eemail.getText().toString();


                if(kakaologin==false) {


                    if ((TextUtils.isEmpty(sid) == true || TextUtils.isEmpty(spassword) == true || TextUtils.isEmpty(spasscheck) == true || TextUtils.isEmpty(sname) == true || TextUtils.isEmpty(sgender) == true || TextUtils.isEmpty(semail) == true || TextUtils.isEmpty(saddress) == true)) {
                        Toast.makeText(getApplicationContext(), "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                    } else if (spassword.equals(spasscheck) == false) {
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        new JSONTask().execute("http://222.107.4.162:3824/member/add");

                    }
                }
                else if(kakaologin==true)
                {
                    if((TextUtils.isEmpty(sid) == true || TextUtils.isEmpty(sname) == true || TextUtils.isEmpty(sgender) == true || TextUtils.isEmpty(semail) == true || TextUtils.isEmpty(saddress) == true))
                    {
                        Toast.makeText(getApplicationContext(), "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new JSONTask().execute("http://222.107.4.162:3824/member/add");
                    }

                }
            }
        });

    }
        public class JSONTask extends AsyncTask<String, String, String> {

            final EditText epassword = (EditText) findViewById(R.id.password);
            final EditText eid = (EditText) findViewById(R.id.insertid);
            final EditText epasscheck = (EditText) findViewById(R.id.passcheck);
            final EditText ename = (EditText) findViewById(R.id.name);
            final EditText egender = (EditText) findViewById(R.id.gender);
            final EditText eaddress = (EditText) findViewById(R.id.address);
            final EditText eemail = (EditText) findViewById(R.id.email);

            @Override
            public String doInBackground(String... urls) {

                try {
                    JSONObject jsonObject = new JSONObject();
                    if(kakaologin==false)
                    {

                        jsonObject.accumulate("id", eid.getText().toString());
                        jsonObject.accumulate("password",  epassword.getText().toString());
                        jsonObject.accumulate("name",  ename.getText().toString());
                        jsonObject.accumulate("gender",  egender.getText().toString());
                        jsonObject.accumulate("address",  eaddress.getText().toString());
                        jsonObject.accumulate("email",  eemail.getText().toString());
                    }
                    else if(kakaologin==true)
                    {

                        jsonObject.accumulate("id", eid.getText().toString());
                        jsonObject.accumulate("password","kakao");
                        jsonObject.accumulate("name",  ename.getText().toString());
                        jsonObject.accumulate("gender",  egender.getText().toString());
                        jsonObject.accumulate("address",  eaddress.getText().toString());
                        jsonObject.accumulate("email",  eemail.getText().toString());
                    }


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

                        //서버로 보내기위해서 스트림 만듬
                        OutputStream outStream = con.getOutputStream();
                        //버퍼를 생성하고 넣음
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                        writer.write(jsonObject.toString());
                        writer.flush();
                        writer.close();//버퍼를 받아줌

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
                    if(kakaologin==true)
                    {
                        Toast.makeText(getApplicationContext(),"이미 가입되었습니다. 넘어가기를 눌러주세요",Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(),"중복된 아이디 입니다.",Toast.LENGTH_SHORT).show();

                }
                else if(result.contains("true"))
                {
                    Toast.makeText(getApplicationContext(),"회원가입 성공",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    intent1.putExtra(NICKNAME,getIntent().getStringExtra(MainActivity.NICKNAME));
                    startActivity(intent1);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"정의되지 않은 값입니다. 관리자에게 문의하세요.",Toast.LENGTH_SHORT).show();
                }
            }
        }

    }







