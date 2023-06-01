package edu.hitsz.aircraft;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.bullet.*;
import edu.hitsz.game.BaseGame;
import edu.hitsz.prop.*;
import edu.hitsz.factory.BasePropFactory;
import edu.hitsz.factory.Prop_BloodFactory;
import edu.hitsz.factory.Prop_BombFactory;
import edu.hitsz.factory.Prop_BulletFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.*;
/**
 * 精英敌机
 * 可以射击
 * @author hitsz
 */
public class EliteEnemy extends Enemy {
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

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
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.screenHeight) {
            vanish();
        }
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
        //击落精英敌机加30分
        return score+30;
    }
    @Override
    public List<BaseProp> drop_prop() {
        List<BaseProp> props = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        Random r = new Random();
        int i = r.nextInt(10);
        if(i < 3){
            basepropFactory = new Prop_BloodFactory();
            baseProp = basepropFactory.createBaseProp(x,y);
            props.add(baseProp);
        }
        else if(i < 6){
            basepropFactory = new Prop_BombFactory();
            baseProp = basepropFactory.createBaseProp(x,y);
            props.add(baseProp);
        }
        else if(i < 9){
            basepropFactory = new Prop_BulletFactory();
            baseProp = basepropFactory.createBaseProp(x,y);
            props.add(baseProp);
        }
        else {
            System.out.println("不生成道具");
        }
        return props;
    }
    @Override
    public void bombActive(){
        // 避免无效加分
        if(isValid){
            decreaseHp(hp);
            BaseGame.score += 30;
        }
    }
}
