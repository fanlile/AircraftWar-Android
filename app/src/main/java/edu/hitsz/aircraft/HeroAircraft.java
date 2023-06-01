package edu.hitsz.aircraft;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.DirectShoot;

/**
 * 英雄飞机，游戏玩家操控，遵循单例模式（singleton)
 * 【单例模式】
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    /**
     * 子弹伤害
     */
    private int power = 20;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = -1;

/*
        volatile 修饰，
        singleton = new Singleton() 可以拆解为3步：
        1、分配对象内存(给singleton分配内存)
        2、调用构造器方法，执行初始化（调用 Singleton 的构造函数来初始化成员变量）。
        3、将对象引用赋值给变量(执行完这步 singleton 就为非 null 了)。
        若发生重排序，假设 A 线程执行了 1 和 3 ，还没有执行 2，B 线程来到判断 NULL，B 线程就会直接返回还没初始化的 instance 了。
        volatile 可以避免重排序。
    */
    /** 英雄机对象单例 */
    private volatile static HeroAircraft heroAircraft;

    /**
     * 单例模式：私有化构造方法
     */
    private HeroAircraft() {
        super(MainActivity.screenWidth / 2, MainActivity.screenHeight - ImageManager.HERO_IMAGE.getHeight(),
                0, -5, 1000);
        this.shootNum = 1;
        this.power = 30;
        this.direction = -1;
        this.rate = 1.5;
    }


    /**
     * 通过单例模式获得初始化英雄机
     * 【单例模式：双重校验锁方法】
     * @return 英雄机单例
     */
    public static HeroAircraft getHeroAircraft(){
        if (heroAircraft == null) {
            synchronized (HeroAircraft.class) {
                if (heroAircraft == null) {
                    heroAircraft = new HeroAircraft();
                    heroAircraft.shootStrategy = new DirectShoot();
                }
            }
        }
        heroAircraft.isValid = true;
        heroAircraft.setHp(1000);
        heroAircraft.setLocation(MainActivity.screenWidth / 2, MainActivity.screenHeight - ImageManager.HERO_IMAGE.getHeight());
        return heroAircraft;
    }

    public void setShootNum(int shootNum){
        this.shootNum = shootNum;
    }

    public void increaseHp(int blood) {
        if(hp+blood >= maxHp) {
            hp = maxHp;
        }
        else {
            hp += blood;
        }
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    public  List<AbstractBullet> shoot() {
        return shootStrategy.shoot(HeroBullet.class,locationX,locationY,speedY,direction,power,shootNum);
    }
}
