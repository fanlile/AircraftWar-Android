package edu.hitsz.factory;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.Enemy;
import edu.hitsz.ImageManager;
import edu.hitsz.strategy.ScatterShoot;

public class BossEnemyFactory implements EnemyFactory {
    @Override
    public Enemy createEnemy(){
        Enemy bossEnemy = new BossEnemy(
                (int) (Math.random() * (MainActivity.screenWidth - ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                115,
                5,
                0,
                400);
        bossEnemy.setShootStrategy(new ScatterShoot());
        return bossEnemy;
    }
}
