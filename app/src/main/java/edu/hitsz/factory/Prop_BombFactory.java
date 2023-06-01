package edu.hitsz.factory;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.game.BaseGame;
import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.Prop_Bomb;

/**
 * @author fll
 */
public class Prop_BombFactory implements BasePropFactory {
    @Override
    public BaseProp createBaseProp(int x, int y){
        Prop_Bomb prop = new Prop_Bomb(x,y,0,6);
        for(Enemy enemy: BaseGame.enemyAircrafts){
            prop.addSubscriber(enemy);
        }
        for(AbstractBullet bullet :BaseGame.enemyBullets){
            prop.addSubscriber(bullet);
        }
        return prop;
    }
}
