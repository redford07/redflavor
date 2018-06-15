package com.example.vip.redfalvor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class ServiceActivity extends AppCompatActivity {

    static int ReCnt=0;
    static int rotation=10;
    static int testbtncnt=1;
    public static String defaultip = "121.129.13.219"; //socket server ip
    public static String defaultport = "8885";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        LayoutInflater inflater = LayoutInflater.from(this);

        final Button snapshotbtn= (Button)findViewById(R.id.snapshotbtn);
        final Button recordbtn = (Button)findViewById(R.id.recordbtn);
        Button servoRbtn =(Button)findViewById(R.id.servorightbtn);
        Button servoLbtn = (Button)findViewById(R.id.servoleftbtn);
        final Button importbtn = (Button)findViewById(R.id.cameraimportbtn);

        Button uribtn = (Button)findViewById(R.id.uribtn);
        final Button defalturibtn = (Button)findViewById(R.id.defaulturibtn);
        final EditText testuri = (EditText)findViewById(R.id.testuri);
        final VideoView v = (VideoView)findViewById(R.id.videoView);

        //테스트용

        final Button ipchangebtn = (Button)findViewById(R.id.ipchangebtn);
        final Button portchangebtn = (Button)findViewById(R.id.portchangebtn);
        final Button defaultchangebtn = (Button)findViewById(R.id.defaultiport);
        final EditText ipchange = (EditText)findViewById(R.id.ipchange) ;
        final EditText portchange = (EditText)findViewById(R.id.portchange) ;
        final Button towebviewbtn = (Button)findViewById(R.id.towebviewbtn);
        Button testbtn = (Button)findViewById(R.id.testbtn);

        ipchangebtn.setVisibility(View.GONE);
        portchangebtn.setVisibility(View.GONE);
        defaultchangebtn.setVisibility(View.GONE);
        ipchange.setVisibility(View.GONE);
        portchange.setVisibility(View.GONE);
        towebviewbtn.setVisibility(View.GONE);

        final String DefaultUri = "rtsp://121.129.13.219:8554/h264";
        final String uri = DefaultUri;
        snapshotbtn.setEnabled(false);
        recordbtn.setEnabled(false);

        v.setVideoURI( Uri.parse(uri) );
        //v.setMediaController( new MediaController( this ) );
        v.requestFocus();
        v.start();

        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testbtncnt%2==0){
                    testbtncnt++;
                    ipchangebtn.setVisibility(View.GONE);
                    portchangebtn.setVisibility(View.GONE);
                    defaultchangebtn.setVisibility(View.GONE);
                    ipchange.setVisibility(View.GONE);
                    portchange.setVisibility(View.GONE);
                    towebviewbtn.setVisibility(View.GONE);
                }
                else{
                    testbtncnt++;
                    ipchangebtn.setVisibility(View.VISIBLE);
                    portchangebtn.setVisibility(View.VISIBLE);
                    defaultchangebtn.setVisibility(View.VISIBLE);
                    ipchange.setVisibility(View.VISIBLE);
                    portchange.setVisibility(View.VISIBLE);
                    towebviewbtn.setVisibility(View.VISIBLE);
                }


            }
        });



        portchangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultport= portchange.getText().toString();
                Toast.makeText(getApplicationContext(),"port changed ",Toast.LENGTH_SHORT).show();
            }
        });

        ipchangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultip = ipchange.getText().toString();
                Toast.makeText(getApplicationContext(),"ip changed ",Toast.LENGTH_SHORT).show();
            }
        });
        defaultchangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultip="121.129.13.219";
                defaultport="8885";
                Toast.makeText(getApplicationContext(),"ip port changed ",Toast.LENGTH_SHORT).show();
            }
        });
        towebviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), WebViewActivity.class);
                startActivity(intent1);
                finish();

            }
        });


        defalturibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.stopPlayback();
                v.setVideoURI(Uri.parse(DefaultUri));
                v.requestFocus();
                v.start();
            }
        });

        uribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.stopPlayback();
                v.setVideoURI(Uri.parse(testuri.getText().toString()));
                v.requestFocus();
                v.start();

            }
        });


        snapshotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"사진 저장완료",Toast.LENGTH_SHORT).show();
                MyClientTask myClientTask = new MyClientTask(defaultip, Integer.parseInt(defaultport), "capture");
                myClientTask.execute();
            }
        });
        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ;
                ReCnt++;
                if(ReCnt%2==0)
                {
                    recordbtn.setText("녹화시작");
                    Toast.makeText(getApplicationContext(),"영상이 저장되었습니다. ",Toast.LENGTH_SHORT).show();
                    MyClientTask myClientTask = new MyClientTask(defaultip, Integer.parseInt(defaultport), "end");
                    myClientTask.execute();
                }
                else
                {
                    recordbtn.setText("녹화중");
                    Toast.makeText(getApplicationContext(),"녹화시작 ",Toast.LENGTH_SHORT).show();
                    MyClientTask myClientTask = new MyClientTask(defaultip, Integer.parseInt(defaultport), "start");
                    myClientTask.execute();
                }

            }
        });

        importbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importbtn.setText("카메라임포트완료");
                importbtn.setEnabled(false);
                Toast.makeText(getApplicationContext(),"카메라를 사용할 수 있습니다. ",Toast.LENGTH_SHORT).show();
                snapshotbtn.setEnabled(true);
                recordbtn.setEnabled(true);
                MyClientTask myClientTask = new MyClientTask(defaultip, Integer.parseInt(defaultport), "import");
                myClientTask.execute();
            }
        });



      //  uniqueNTxt.setText(getIntent().getStringExtra(MainActivity.USER_ID)); //고유번호 출력
     //   pNameTxt.setText(getIntent().getStringExtra(MainActivity.NICKNAME));
        if(TextUtils.isEmpty(getIntent().getStringExtra(MainActivity.NICKNAME))==false)
        {
            String hello = getIntent().getStringExtra(MainActivity.NICKNAME) + "님 환영합니다";
            Toast.makeText(getApplicationContext(),hello,Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"환영합니다.",Toast.LENGTH_SHORT).show();
        }
        servoLbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN:
                        if(rotation<7){
                            Toast.makeText(getApplicationContext(),"더이상 이동이 불가능합니다. ",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //Toast.makeText(getApplicationContext(),"카메라 좌측이동 ",Toast.LENGTH_SHORT).show();
                            rotation--;
                            MyClientTask myClientTask = new MyClientTask(defaultip, Integer.parseInt(defaultport), "left");
                            myClientTask.execute();
                        }



                        break;
                }


                return false;
            }
        });

        servoRbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {



                    case MotionEvent.ACTION_DOWN:
                        if(rotation>13)
                        {
                            Toast.makeText(getApplicationContext(),"더이상 이동이 불가능합니다. ",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            rotation++;
                            //+Toast.makeText(getApplicationContext(),"카메라 우측이동 ",Toast.LENGTH_SHORT).show();
                            MyClientTask myClientTask = new MyClientTask(defaultip, Integer.parseInt(defaultport), "right");
                            myClientTask.execute();
                        }



                        break;
                }


                return false;
            }
        });


    }//endoncreate

@Override

public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.activity_menu, menu);

    return true;

}

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.setuptest:
                Intent intent3 = new Intent(getApplicationContext(), TestSetUpActivity.class);
                startActivity(intent3);
              //  finish();
                return true;

            case R.id.setup:
                Intent intent2 = new Intent(getApplicationContext(), SetUpActivity.class);
                startActivity(intent2);
              //  finish();
                return true;

            case R.id.appinfo:
                Intent intent4 = new Intent(getApplicationContext(), PreSettingsActivity.class);
                startActivity(intent4);
                //  finish();
                return true;




            default:

                return super.onOptionsItemSelected(item);

        }

    }

    //두번 연속으로 뒤로가기 누를 시 종료
    long pressTime;
    @Override
    public void onBackPressed(){

        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - pressTime;

        if(intervalTime <2000){
            super.onBackPressed();
            finishAffinity();
        }else{
            pressTime = currentTime;
            Toast.makeText(this,"한 번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }
    //재 로그인 요청
    private void redirectLoginActivity() {
        final Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";


        MyClientTask(String addr, int port, String message){
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            myMessage = myMessage.toString();
            try {
                socket = new Socket(dstAddress, dstPort);

                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
                response = "서버의 응답: " + response;

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //recieveText.setText(response);
            super.onPostExecute(result);
        }
    }



}