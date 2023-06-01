package edu.hitsz.factory;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.Enemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.ImageManager;

public class MobEnemyFactory implements EnemyFactory {
    @Override
    public Enemy createEnemy(){
        return new MobEnemy(
                (int) (Math.random() * (MainActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                (int) (Math.random() * MainActivity.screenHeight * 0.2),
                0,
                6,
                30);
    }
}
