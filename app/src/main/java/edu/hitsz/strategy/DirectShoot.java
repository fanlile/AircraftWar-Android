package edu.hitsz.strategy;

import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class DirectShoot implements ShootStrategy {
    @Override
    public List<AbstractBullet> shoot(Class <? extends AbstractBullet > bulletClass,int locationX,int locationY,int speedY,int direction,int power,int shootNum) {
        List<AbstractBullet> res = new LinkedList<>();
        int x = locationX;
        int y = locationY + direction*2;
        int speedX = 0;
        AbstractBullet bullet;
        if(bulletClass.equals(HeroBullet.class)){
            speedY = speedY + direction*12;
            for(int i=0; i<shootNum; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                bullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
                res.add(bullet);
            }
        }
        else {
            speedY = speedY + direction*3;
            for(int i=0; i<shootNum; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
                res.add(bullet);
            }
        }
        return res;
    }
}
