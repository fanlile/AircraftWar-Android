package edu.hitsz.dao;

import android.content.Context;

import java.io.*;
import java.util.*;

/**
 * @author fll
 */
public class ScoreDaoImpl implements ScoreDao {
    private String pathName;
    public List<ScoreRecord> scoreRecords;
    public ScoreDaoImpl() {
        scoreRecords = new ArrayList<ScoreRecord>();
    }
    /**
     * 获取所有得分记录
     */
    @Override
    public List<ScoreRecord> getAllRecord() {
        return scoreRecords;
    }
    /**
     * 增加分数记录
     */
    @Override
    public void doAdd(ScoreRecord scoreRecord) {
        scoreRecords.add(scoreRecord);
    }
    /**
     * 按照score的高低，对List<ScoreRecord>进行排序
     */
    @Override
    public void sort() {
        Collections.sort(scoreRecords, (b,a)->{return a.getScore() - b.getScore();});
    }
    /**
     * 设置文件路径
     */
    @Override
    public void setPathName(String pathName){
        this.pathName = pathName;
    }

    /**
     * 清空scoreRecords
     */
    @Override
    public void cleanScoreRecords() {
        scoreRecords.clear();
    }

    /**
     * 清空所有游戏得分记录
     */
    @Override
    public void cleanFile(Context context){
        try {
            FileOutputStream fos = context.openFileOutput(pathName, Context.MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write("");

            bw.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     *将得分数据写入本地文件
     */
    @Override
    public void writeFile(Context context,String userName,int score,String formattedDateTime) {
        String score_ = Integer.toString(score);
        try {
            FileOutputStream fos = context.openFileOutput(pathName, Context.MODE_APPEND);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write(userName+"，"+score_+"，"+formattedDateTime);
            bw.newLine();

            bw.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    /**
     * 从文件中读出数据并输出
     */
    @Override
    public void outputFile(Context context) {
        //将存放在本地文件中的数据读入到程序中
        try {
            FileInputStream fis = context.openFileInput(pathName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("，");
                scoreRecords.add(new ScoreRecord(parts[0],Integer.parseInt(parts[1]),parts[2]));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}