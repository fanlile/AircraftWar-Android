package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.game.BaseGame;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.HardGame;
import edu.hitsz.game.MediumGame;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    private int gameType=0;
    private boolean needMusic = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG,"handleMessage");
                if (msg.what == 1) {
                    Toast.makeText(GameActivity.this,"GameOver",Toast.LENGTH_SHORT).show();
                }
            }
        };
        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
            needMusic = getIntent().getBooleanExtra("needMusic",false);
        }
        BaseGame basGameView;
        BaseGame.needMusic = this.needMusic;
        if(gameType == 1){
            BaseGame.difficulty = 0;
            basGameView = new EasyGame(this,handler);
        }else if(gameType == 2){
            BaseGame.difficulty = 1;
            basGameView = new MediumGame(this,handler);
        }else{
            BaseGame.difficulty = 4;
            basGameView = new HardGame(this,handler);
        }
        setContentView(basGameView);
        Runnable r = () -> {
            try {
                while (!basGameView.gameOverFlag){
                    Thread.sleep(1000); // 等待1秒钟
                }
                Intent intent = new Intent(GameActivity.this, InputActivity.class);
                intent.putExtra("gameType",gameType);
                startActivity(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        // 启动线程
        new Thread(r, "线程 1").start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}