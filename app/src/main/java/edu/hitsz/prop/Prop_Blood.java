package edu.hitsz.prop;

import static edu.hitsz.game.BaseGame.mysp;
import static edu.hitsz.game.BaseGame.soundPoolMap;

import edu.hitsz.aircraft.*;
import edu.hitsz.game.BaseGame;
//import edu.hitsz.application.MusicThread;

public class Prop_Blood extends BaseProp {
    protected int blood;
    public Prop_Blood(int locationX, int locationY, int speedX, int speedY, int blood) {
        super(locationX, locationY, speedX, speedY);
        this.blood = blood;
    }
    @Override
    public void active(HeroAircraft heroAircraft) {
        if (BaseGame.needMusic) {
            Runnable r4 = () -> {
                mysp.play((Integer) soundPoolMap.get("get_supply"),1,1,0,0,1.2f);
            };
            new Thread(r4,"4").start();
        }
        heroAircraft.increaseHp(blood);
    }
}
