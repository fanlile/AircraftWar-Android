package edu.hitsz.prop;

import static edu.hitsz.game.BaseGame.mysp;
import static edu.hitsz.game.BaseGame.soundPoolMap;

import edu.hitsz.aircraft.*;
//import edu.hitsz.application.MusicThread;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.game.BaseGame;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fll
 */
public class Prop_Bomb extends BaseProp {
    private List<AbstractFlyingObject> subscribersList = new ArrayList<>();
    public Prop_Bomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    public void addSubscriber(AbstractFlyingObject subscriber){
        subscribersList.add(subscriber);
    }
    public void addSubscriber(List<AbstractBullet> bullets) { subscribersList.addAll(bullets); }
    public void removeSubscriber(AbstractFlyingObject subscriber){
        subscribersList.remove(subscriber);
    }
    @Override
    public void active(HeroAircraft heroAircraft) {
        if (BaseGame.needMusic) {
            Runnable r5 = () -> {
                mysp.play((Integer) soundPoolMap.get("bomb_explosion"),1,1,0,0,1.2f);
            };
            new Thread(r5,"5").start();
        }
        // 生效后通知所有观察者
        for(AbstractFlyingObject subscriber : subscribersList){
            subscriber.bombActive();
        }
    }
}
