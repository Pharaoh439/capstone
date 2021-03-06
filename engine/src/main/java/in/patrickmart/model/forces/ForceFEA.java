package in.patrickmart.model.forces;

import in.patrickmart.model.Entity;
import in.patrickmart.model.Vector2D;

public class ForceFEA extends Force {

    public ForceFEA(Entity destination){
        super(null, destination);
    }
    /**
     * force comes from directly above each object
     */
    public Vector2D calculatePosition(){
        return destination.getPosition();
    }

    /**
     * What direction is this force being applied in?
     */
    public Vector2D calculateDirection(){
        return new Vector2D(0, -9.8);
    }

    /**
     * How much force is being applied?
     */
    public double calculateNewtons(){
        return 9.8 * destination.getMass();
    }
}
