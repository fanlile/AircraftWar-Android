package edu.hitsz.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hitsz.R;
import edu.hitsz.dao.ScoreRecord;
import edu.hitsz.game.BaseGame;

public class TableActivity extends AppCompatActivity {
    private static final String TAG = "TableActivity";
    private ListView listView;
    private List<ScoreRecord> scoreRecords;
    private int rank;
    private String userName;
    private String date;
    private SimpleAdapter adapter;
    private ArrayList<Map<String,String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Button restart_btn = findViewById(R.id.reStart);

        listView = (ListView)findViewById(R.id.list);

        adapter = new SimpleAdapter(
                this,
                getData(),
                R.layout.listview_item,
                new String[]{"名次","玩家昵称","分数","日期"},
                new int[]{R.id.rank,R.id.user,R.id.score,R.id.date}
        );

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(itemDeleteListener);

        restart_btn.setOnClickListener(view -> {
            Intent intent = new Intent(TableActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private List<Map<String,String>> getData(){
        data = new ArrayList<Map<String,String>>();
        Map<String,String> map = new HashMap<String,String>();
        map.put("名次", '\t'+"名次");
        map.put("玩家昵称","玩家昵称");
        map.put("分数","分数");
        map.put("日期","日期");
        data.add(map);

        BaseGame.scoreDao.outputFile(TableActivity.this);
        BaseGame.scoreDao.sort();
        scoreRecords = BaseGame.scoreDao.getAllRecord();

        int i = 0;
        for (ScoreRecord scoreRecord : scoreRecords){
            i++;
            map = new HashMap<String,String>();
            map.put("名次", '\t'+String.valueOf(i));
            map.put("玩家昵称",scoreRecord.getUserName());
            map.put("分数",String.valueOf(scoreRecord.getScore()));
            map.put("日期",scoreRecord.getTime());
            data.add(map);
        }
        return data;
    }
    private final AdapterView.OnItemLongClickListener itemDeleteListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            // 长按列表项的时候弹出 确认删除对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
            builder.setMessage("确认删除?");

            // 点击对话框的 确认 按钮后的操作
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BaseGame.scoreDao.cleanFile(TableActivity.this);
                    scoreRecords.remove(position-1);
                    for(ScoreRecord scoreRecord : scoreRecords){
                        BaseGame.scoreDao.writeFile(TableActivity.this,scoreRecord.getUserName(),scoreRecord.getScore(),scoreRecord.getTime());
                    }

                    data.clear();

                    Map<String,String> map = new HashMap<String,String>();
                    map.put("名次", '\t'+"名次");
                    map.put("玩家昵称","玩家昵称");
                    map.put("分数","分数");
                    map.put("日期","日期");
                    data.add(map);

                    BaseGame.scoreDao.cleanScoreRecords();
                    BaseGame.scoreDao.outputFile(TableActivity.this);
                    BaseGame.scoreDao.sort();
                    scoreRecords = BaseGame.scoreDao.getAllRecord();

                    int i = 0;
                    for (ScoreRecord scoreRecord : scoreRecords){
                        i++;
                        map = new HashMap<String,String>();
                        map.put("名次", '\t'+String.valueOf(i));
                        map.put("玩家昵称",scoreRecord.getUserName());
                        map.put("分数",String.valueOf(scoreRecord.getScore()));
                        map.put("日期",scoreRecord.getTime());
                        data.add(map);
                    }

                    adapter.notifyDataSetChanged(); // 适配器数据刷新
                    Toast.makeText(getBaseContext(), "已删除第"+position+"条记录", Toast.LENGTH_SHORT).show();
                }
            });

            // 点击对话框的 取消 按钮后的操作
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 无操作
                }
            });

            builder.create().show();
            return false;
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

