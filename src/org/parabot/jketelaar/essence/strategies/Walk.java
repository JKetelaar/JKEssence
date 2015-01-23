package org.parabot.jketelaar.essence.strategies;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.parabot.jketelaar.essence.data.Constants;
import org.rev317.min.api.methods.Bank;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.Npc;
import org.rev317.min.api.wrappers.Tile;

import java.util.Random;

/**
 * @author JKetelaar
 */
public class Walk implements Strategy {

    private final Tile AFTER_TELEPORT = new Tile(2911, 4832);
    private final int AUBURY = 553;
    private final int TELEPORT = 2492;
    private final int CLOSED_DOOR = 1530;
    private final Tile MIDDLE = new Tile(3259, 3412);
    
    private Npc teleporter;

    @Override
    public boolean activate() {
        if (Npcs.getNearest(AUBURY) != null) {
            for (Npc npc : Npcs.getNearest(AUBURY)) {
                if (npc != null) {
                    teleporter = npc;
                    return true;
                }
            }
        }
        try{
            if (Bank.getBank().distanceTo() < 15 && Inventory.getCount() >= 28){
                return false;
            }
        }catch (NullPointerException | ArrayIndexOutOfBoundsException ignored){

        }
        return Inventory.getCount() >= 28 || AFTER_TELEPORT.distanceTo() < 5 || (Inventory.getCount() < 5 && AFTER_TELEPORT.distanceTo() > 25);
    }

    @Override
    public void execute() {
        if (Inventory.getCount() < 0) {
            if (teleporter != null && teleporter.getLocation().distanceTo() < 50) {
                if (!walkTo(teleporter.getLocation())){
                    if (SceneObjects.getNearest(CLOSED_DOOR) != null && SceneObjects.getNearest(CLOSED_DOOR).length >= 1){
                        SceneObjects.getNearest(CLOSED_DOOR)[0].interact(0);
                        walkTo(Bank.getBank().getLocation());
                    } else {
                        System.out.println("Help! I'm lost...");
                    }
                }
                teleporter.interact(3);
                Time.sleep(new SleepCondition() {
                    @Override
                    public boolean isValid() {
                        return AFTER_TELEPORT.distanceTo() < 5;
                    }
                }, 10000);
            }else if (MIDDLE.distanceTo() < 50){
                walkTo(MIDDLE);
                if (MIDDLE.distanceTo() > 5){
                    System.out.println("Help! I'm lost...");
                }
            }

            if (AFTER_TELEPORT.distanceTo() < 5) {
                Tile walk = Constants.ESSENCE_LOCATIONS[new Random().nextInt(4)];
                if (walkTo(walk)) {
                    System.out.println("I'm here, ready to mine");
                } else {
                    System.out.println("Help! I'm lost...");
                }
            }
        }else if (Inventory.getCount() >= 28){
            if (SceneObjects.getNearest(TELEPORT) != null){
                if (SceneObjects.getNearest(TELEPORT).length >= 1){
                    if (walkTo(SceneObjects.getNearest(TELEPORT)[0].getLocation())) {
                        System.out.println("I'm here, ready to teleport");

                        final Tile previous = Players.getMyPlayer().getLocation();
                        SceneObjects.getNearest(TELEPORT)[0].interact(0);
                        Time.sleep(new SleepCondition() {
                            @Override
                            public boolean isValid() {
                                return previous.distanceTo() > 5;
                            }
                        }, 3500);

                        final Tile previous2 = Players.getMyPlayer().getLocation();
                        if (previous.distanceTo() < 5){
                            SceneObjects.getNearest(TELEPORT)[0].interact(0);
                            Time.sleep(new SleepCondition() {
                                @Override
                                public boolean isValid() {
                                    return previous2.distanceTo() > 5;
                                }
                            }, 3500);
                            Time.sleep(1000, 1500);
                        }
                    } else {
                        System.out.println("Help! I'm lost...");
                    }
                }
            }

            if (SceneObjects.getNearest(CLOSED_DOOR) != null && SceneObjects.getNearest(CLOSED_DOOR).length >= 1){
                if (SceneObjects.getNearest(CLOSED_DOOR)[0].distanceTo() < 5) {
                    SceneObjects.getNearest(CLOSED_DOOR)[0].interact(0);
                }
            } else {
                System.out.println("Help! I'm lost...");
            }

            walkTo(MIDDLE);
            if (MIDDLE.distanceTo() > 5){
                System.out.println("Help! I'm lost...");
            }

            if (Bank.getBank() != null){
                if (!walkTo(Bank.getBank().getLocation())){
                    if (SceneObjects.getNearest(CLOSED_DOOR) != null && SceneObjects.getNearest(CLOSED_DOOR).length >= 1){
                        SceneObjects.getNearest(CLOSED_DOOR)[0].interact(0);
                        walkTo(Bank.getBank().getLocation());
                        if (Bank.getBank().getLocation().distanceTo() > 5){
                            System.out.println("Help! I'm lost...");
                        }
                    } else {
                        System.out.println("Help! I'm lost...");
                    }
                }
            }
        }else{
            System.out.println("Your inventory either has to be full or empty, not in the middle...");
        }
    }

    private boolean walkTo(Tile tile){
        int count = 0;
        while (tile.isReachable() && tile.distanceTo() > 5 && count < 10){
            tile.walkTo();
            Time.sleep(1500, 2000);
            count++;
        }
        final Tile previous = Players.getMyPlayer().getLocation();
        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return previous.distanceTo() > 0;
            }
        }, 5000);
        return count < 10;
    }
}
