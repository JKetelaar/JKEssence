package org.parabot.jketelaar.essence.strategies;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;

/**
 * @author JKetelaar
 */
public class Bank implements Strategy {

    @Override
    public boolean activate() {
        try {
            if (Inventory.getCount() >= 28) {
                if (org.rev317.min.api.methods.Bank.getBank() != null) {
                    if (org.rev317.min.api.methods.Bank.getBank().getLocation().distanceTo() <= 15) {
                        return true;
                    }
                }
            }
        }catch (NullPointerException | ArrayIndexOutOfBoundsException ignored){

        }
        return false;
    }

    @Override
    public void execute() {
        if (org.rev317.min.api.methods.Bank.getBank() != null && !org.rev317.min.api.methods.Bank.isOpen()){
            org.rev317.min.api.methods.Bank.open();
            Time.sleep(new SleepCondition() {
                @Override
                public boolean isValid() {
                    return org.rev317.min.api.methods.Bank.isOpen();
                }
            }, 5000);
        }
        if (org.rev317.min.api.methods.Bank.isOpen()){
            final int previous = Inventory.getCount();
            Menu.sendAction(431, 1436, 0, 5064);
            Time.sleep(150);
            Menu.sendAction(78, 7936, 0, 5064);
            Time.sleep(150);
            Menu.sendAction(431, 7936, 0, 5064);
            Time.sleep(new SleepCondition() {
                @Override
                public boolean isValid() {
                    return Inventory.getCount() != previous;
                }
            }, 1500);
            if (previous != Inventory.getCount()){
                org.rev317.min.api.methods.Bank.close();
            }
        }
    }
}
