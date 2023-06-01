package edu.hitsz.factory;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.BaseProp;

public interface BasePropFactory {
    BaseProp createBaseProp(int x, int y);
}
