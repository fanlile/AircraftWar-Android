package edu.hitsz.factory;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.Enemy;
import edu.hitsz.ImageManager;
import edu.hitsz.strategy.DirectShoot;

public class EliteEnemyFactory implements EnemyFactory {
    @Override
    public Enemy createEnemy(){
        Enemy eliteEnemy = new EliteEnemy(
                (int) (Math.random() * (MainActivity.screenWidth - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * MainActivity.screenHeight * 0.05),
                0,
                5,
                120);
        eliteEnemy.setShootStrategy(new DirectShoot());
        return eliteEnemy;
    }
}
