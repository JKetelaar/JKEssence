package org.parabot.jketelaar.essence.strategies;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.SceneObject;

/**
 * @author JKetelaar
 */
public class Mine implements Strategy {

    private final int ROCK_ID = 2491;
    private SceneObject rock;

    @Override
    public boolean activate() {
        if (SceneObjects.getNearest(ROCK_ID) != null && Inventory.getCount() < 28){
            for (SceneObject sceneObject : SceneObjects.getNearest(ROCK_ID)){
                rock = sceneObject;
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {
        if (rock != null) {
            final int count = Inventory.getCount();
            rock.interact(0);
            Time.sleep(new SleepCondition() {
                @Override
                public boolean isValid() {
                    return Inventory.getCount() != count;
                }
            }, 7500);
        }
    }
}
