package com.example.vip.redfalvor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by VIP on 2018-05-08.
 */

public class SplashActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(1000);

        }catch (InterruptedException e){
            e.printStackTrace();
        }

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
