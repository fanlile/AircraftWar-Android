package edu.hitsz.aircraft;

import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.game.BaseGame;
import edu.hitsz.prop.*;
import edu.hitsz.factory.BasePropFactory;
import edu.hitsz.factory.Prop_BloodFactory;
import edu.hitsz.factory.Prop_BombFactory;
import edu.hitsz.factory.Prop_BulletFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author fll
 */
public class BossEnemy extends Enemy{
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 3;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;
    private BasePropFactory basepropFactory;
    private BaseProp baseProp;
    /**
     * @param locationX 精英敌机位置x坐标
     * @param locationY 精英敌机位置y坐标
     * @param speedX 精英敌机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 精英敌机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    生命值
     */
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }
    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<AbstractBullet> shoot() {
        return shootStrategy.shoot(EnemyBullet.class,locationX,locationY,speedY,direction,power,shootNum);
    }

    @Override
    public int increaseScore(int score) {
        //击落Boss敌机加200分
        return score+200;
    }
    @Override
    public List<BaseProp> drop_prop() {
        List<BaseProp> props = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        Random r = new Random();

        for(int i = -1; i < 2 ;i++) {
            int m = r.nextInt(3);
            if(m == 0){
                basepropFactory = new Prop_BloodFactory();
                baseProp = basepropFactory.createBaseProp(x+45*i,y);
                props.add(baseProp);
            }
            else if(m == 1){
                basepropFactory = new Prop_BombFactory();
                baseProp = basepropFactory.createBaseProp(x+45*i,y);
                props.add(baseProp);
            }
            else {
                basepropFactory = new Prop_BulletFactory();
                baseProp = basepropFactory.createBaseProp(x+45*i,y);
                props.add(baseProp);
            }
        }
        return props;
    }
    @Override
    public void bombActive(){
        decreaseHp(100);
        if(!isValid){
            BaseGame.score += 200;
        }
    }
}
