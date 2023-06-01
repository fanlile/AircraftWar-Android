package edu.hitsz.aircraft;

import edu.hitsz.prop.*;
import java.util.List;

/**
 * @author fll
 */
public abstract class Enemy extends AbstractAircraft{
    public Enemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY,hp);
    }

    /**
     * 积分增加
     * @param score 英雄机分数
     * @return 被击落后英雄机的分数
     */
    public abstract int increaseScore(int score);

    /**
     * 掉落道具
     * @return 道具集合
     */
    public abstract List<BaseProp> drop_prop();
}