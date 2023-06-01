package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.hitsz.R;
import edu.hitsz.dao.ScoreDao;
import edu.hitsz.dao.ScoreDaoImpl;
import edu.hitsz.game.BaseGame;

public class InputActivity extends AppCompatActivity {
    private static final String TAG = "InputActivity";
    private String userName;
    private String pathName;
    private int gameType=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Button save_btn = findViewById(R.id.save_btn);
        TextView scoreText = findViewById(R.id.scoreText);
        TextInputEditText usernameText = findViewById(R.id.usernameText);

        scoreText.setText(String.valueOf(BaseGame.score));

        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
        }

        if(gameType == 1){
            pathName = "easy.txt";
        }else if(gameType == 2){
            pathName = "simple.txt";
        }else{
            pathName = "difficult.txt";
        }

        save_btn.setOnClickListener(view -> {
            // 获取用户名称
            userName = String.valueOf(usernameText.getText());
            // 获取当前时间
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
            String formatterDateTime = formatter.format(date);

            BaseGame.scoreDao = new ScoreDaoImpl();
            // 设置路径
            BaseGame.scoreDao.setPathName(pathName);
            // 写入本地文件
            BaseGame.scoreDao.writeFile(InputActivity.this,userName,BaseGame.score,formatterDateTime);

            Intent intent = new Intent(InputActivity.this, TableActivity.class);
            startActivity(intent);
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
