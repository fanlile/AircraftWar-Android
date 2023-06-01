package edu.hitsz.game;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.Random;

import edu.hitsz.ImageManager;
import edu.hitsz.aircraft.Enemy;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.Prop_Bomb;

public class HardGame extends BaseGame{
    public HardGame(Context context, Handler handler) {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND3_IMAGE;
    }
    /**
     * boss机生成分数阈值
     */
    private final int boss_threshold = 700;
    /**
     * 记录boss机已经产生的数量，防止反复生成
     */
    private int count = 0;

    /**
     * 周期（ms）
     * 指示敌机子弹的发射
     */
    private int enemyCycleDuration;

    @Override
    protected void newEnemy() {
        int enemyMaxNumber = BaseGame.difficulty;
        if (enemyAircrafts.size() < enemyMaxNumber) {
            Random r1 = new Random();
            Log.d("BaseGame","produceEnemy");
            // 生成随机数
            int i1 = r1.nextInt(7);
            // 分数达到阈值，生成Boss敌机
            if(score/boss_threshold == (count+1)) {
                enemyFactory = new BossEnemyFactory();
                enemy = enemyFactory.createEnemy();
                // 每次召唤提升Boss机血量
                enemy.increaseMaxHp(enemy.getHp()*count/2);
                enemyAircrafts.add(enemy);
                bossVaild = true;
                bossVailded = true;
                count += 1;
            }
            // 生成精英敌机
            else if(i1 == 3){
                enemyFactory = new EliteEnemyFactory();
                enemy = enemyFactory.createEnemy();
                // 精英机血量随难度增加
                enemy.increaseMaxHp(enemy.getHp()*BaseGame.difficulty/10);
                enemyAircrafts.add(enemy);
            }
            // 生成普通敌机
            else{
                enemyFactory = new MobEnemyFactory();
                enemy = enemyFactory.createEnemy();
                // 普通机血量随时间增加
                enemy.increaseMaxHp(enemy.getHp()*BaseGame.difficulty/10);
                enemyAircrafts.add(enemy);
            }
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
        enemyCycleDuration = 2500 - difficulty * 110;
        if (enemyCycleDuration < 300) {enemyCycleDuration = 300;}
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
