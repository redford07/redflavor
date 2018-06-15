package com.example.vip.redfalvor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by VIP on 2018-05-24.
 */

public class TestSetUpActivity extends Activity {

    public static String defaultip = "121.129.13.219"; //socket server ip
    public static String defaultport = "8885";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testsetup);

        Button ipchangebtn = (Button)findViewById(R.id.ipchangebtn);
        Button portchangebtn = (Button)findViewById(R.id.portchangebtn);
        Button defaultchangebtn = (Button)findViewById(R.id.defaultiport);
        final EditText ipchange = (EditText)findViewById(R.id.ipchange) ;
        final EditText portchange = (EditText)findViewById(R.id.portchange) ;
        final Button towebviewbtn = (Button)findViewById(R.id.towebviewbtn);



        final String DefaultUri = "rtsp://121.129.13.219:8554/h264";



        final String uri = DefaultUri;






        portchangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultport= portchange.getText().toString();
                Toast.makeText(getApplicationContext(),"포트변경완료 ",Toast.LENGTH_SHORT).show();
            }
        });

        ipchangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultip = ipchange.getText().toString();
                Toast.makeText(getApplicationContext(),"아이피변경완료 ",Toast.LENGTH_SHORT).show();
            }
        });
        defaultchangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultip="121.129.13.219";
                defaultport="8885";
                Toast.makeText(getApplicationContext(),"아이피 포트 변경완료 ",Toast.LENGTH_SHORT).show();
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

    }
}
