package edu.hitsz.game;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.Random;

import edu.hitsz.ImageManager;
import edu.hitsz.aircraft.Enemy;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.Prop_Bomb;

public class EasyGame extends BaseGame{
    /**
     * 屏幕中出现的敌机最大数量
     */
    protected int enemyMaxNumber = 3;

    /**
     * 周期（ms）
     * 指示敌机子弹的发射
     */
    private int enemyCycleDuration = 1600;

    public EasyGame(Context context, Handler handler) {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND1_IMAGE;
    }

    @Override
    protected void newEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            Random r1 = new Random();
            Log.d("BaseGame","produceEnemy");
            // 生成随机数
            int i1 = r1.nextInt(6);
            // 生成精英敌机
            if(i1 == 4) {
            enemyFactory = new EliteEnemyFactory();
            }
            // 生成普通敌机
            else {
                enemyFactory = new MobEnemyFactory();
            }
            enemy = enemyFactory.createEnemy();
            enemyAircrafts.add(enemy);
            //将新生成的敌机加入爆炸道具的观察者列表中（如果有爆炸道具）
            for(BaseProp prop :baseProps){
                if(prop instanceof Prop_Bomb){
                    ((Prop_Bomb) prop).addSubscriber(enemy);
                }
            }
        }
    }
    @Override
    protected boolean enemyTimeCountAndNewCycleJudge() {
        enemyCycleTime += timeInterval;
        if (enemyCycleTime >= enemyCycleDuration && enemyCycleTime - timeInterval < enemyCycleTime) {
            // 跨越到新的周期
            enemyCycleTime %= enemyCycleDuration;
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected boolean difficultyTimeCountAndNewCycleJudge() {
        return false;
    }
    @Override
    protected void enemyShootAction() {
        for(Enemy enemyAircraft : enemyAircrafts) {
            List<AbstractBullet> bullets= enemyAircraft.shoot();
            enemyBullets.addAll(bullets);
            for(BaseProp prop :baseProps){
                if(prop instanceof Prop_Bomb){ ((Prop_Bomb) prop).addSubscriber(bullets); }
            }
        }
    }
}
