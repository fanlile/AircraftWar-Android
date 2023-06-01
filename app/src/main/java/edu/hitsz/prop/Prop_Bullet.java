package edu.hitsz.prop;

//import edu.hitsz.application.MusicThread;
import static edu.hitsz.game.BaseGame.mysp;
import static edu.hitsz.game.BaseGame.soundPoolMap;

import edu.hitsz.game.BaseGame;
import edu.hitsz.strategy.DirectShoot;
import edu.hitsz.strategy.ScatterShoot;
import edu.hitsz.aircraft.HeroAircraft;

public class Prop_Bullet extends BaseProp {
    public Prop_Bullet(int locationX, int locationY, int speedX, int speedY) {
        super(locationX,locationY,speedX,speedY);
    }
    @Override
    public void active(HeroAircraft heroAircraft) {
        if (BaseGame.needMusic) {
            Runnable r3 = () -> {
                mysp.play((Integer) soundPoolMap.get("get_supply"),1,1,0,0,1.2f);
            };
            new Thread(r3,"3").start();
        }
        heroAircraft.setShootNum(3);
        heroAircraft.setShootStrategy(new ScatterShoot());
        Runnable r = () -> {
            try {
                Thread.sleep(5000); // 等待5秒钟
                heroAircraft.setShootNum(1);
                heroAircraft.setShootStrategy(new DirectShoot());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        // 启动线程
        new Thread(r, "线程 1").start();
    }
}
