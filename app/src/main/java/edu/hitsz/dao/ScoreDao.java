package edu.hitsz.dao;

import java.util.List;
import android.content.Context;

public interface ScoreDao {
    List<ScoreRecord> getAllRecord();
    void doAdd(ScoreRecord scoreRecord);
    void sort();

    void setPathName(String pathName);

    void cleanScoreRecords();

    void cleanFile(Context context);
    void writeFile(Context context, String userName, int score, String formattedDateTime);
    void outputFile(Context context);
}
