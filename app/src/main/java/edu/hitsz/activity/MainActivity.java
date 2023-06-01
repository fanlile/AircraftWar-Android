package edu.hitsz.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static int screenWidth;
    public static int screenHeight;

    private int gameType=0;
    private boolean needMusic = false;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button medium_btn = findViewById(R.id.medium_btn);
        Button easy_btn = findViewById(R.id.easy_btn);
        Button hard_btn = findViewById(R.id.hard_btn);
        Button connect_btn = findViewById(R.id.connect_btn);
        CheckBox music_cb = findViewById(R.id.music_cb);

        getScreenHW();

        intent = new Intent(MainActivity.this, GameActivity.class);

        easy_btn.setOnClickListener(view -> {
            gameType =1;
            intent.putExtra("gameType",gameType);
            intent.putExtra("needMusic",needMusic);
            startActivity(intent);
        });

        medium_btn.setOnClickListener(view -> {
            gameType=2;
            intent.putExtra("gameType",gameType);
            intent.putExtra("needMusic",needMusic);
            startActivity(intent);
        });

        hard_btn.setOnClickListener(view -> {
            gameType =3;
            intent.putExtra("gameType",gameType);
            intent.putExtra("needMusic",needMusic);
            startActivity(intent);
        });
        connect_btn.setOnClickListener(view -> {
            Toast.makeText(getBaseContext(),"等待联机中", Toast.LENGTH_SHORT).show();
        });
        music_cb.setOnClickListener(view -> {
            needMusic = !needMusic;
        });
    }
    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        screenWidth= dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + screenWidth + " screenHeight : " + screenHeight);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}