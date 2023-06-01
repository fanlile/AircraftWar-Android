package edu.hitsz.aircraft;


import java.util.LinkedList;
import java.util.List;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.game.BaseGame;
import edu.hitsz.prop.*;
import edu.hitsz.bullet.AbstractBullet;


/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends Enemy{

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
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
    public List<AbstractBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public int increaseScore(int score) {
        //普通敌机被击落加10分
        return score+10;
    }
    @Override
    public List<BaseProp> drop_prop() {
        //普通机不掉落道具
        return new LinkedList<>();
    }
    @Override
    public void bombActive(){
        // 避免无效加分
        if(isValid){
            decreaseHp(hp);
            BaseGame.score += 10;
        }
    }
}
