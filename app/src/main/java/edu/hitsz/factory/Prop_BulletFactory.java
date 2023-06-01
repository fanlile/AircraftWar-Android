package edu.hitsz.factory;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.Prop_Bullet;

public class Prop_BulletFactory implements BasePropFactory {
    @Override
    public BaseProp createBaseProp(int x, int y){
        return new Prop_Bullet(
                x,
                y,
                0,
                6
        );
    }
}
