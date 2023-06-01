package edu.hitsz.factory;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.Prop_Blood;

public class Prop_BloodFactory implements BasePropFactory {
    @Override
    public BaseProp createBaseProp(int x, int y){
        return new Prop_Blood(
                x,
                y,
                0,
                6,
                100
        );
    }
}
