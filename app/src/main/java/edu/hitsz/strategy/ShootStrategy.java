package edu.hitsz.strategy;

import edu.hitsz.bullet.AbstractBullet;

import java.util.List;

public interface ShootStrategy {
    /**
     *射击策略
     * @param locationX x轴坐标
     * @param locationY y轴坐标
     * @param speedY y轴方向速度
     * @param direction 飞行方向
     * @param power 子弹伤害
     * @param shootNum 每次发射子弹数量
     * @return 子弹集合
     */
    List<AbstractBullet> shoot(Class <? extends AbstractBullet > bulletClass,
                           int locationX,
                           int locationY,
                           int speedY,
                           int direction,
                           int power,
                           int shootNum
    );
}