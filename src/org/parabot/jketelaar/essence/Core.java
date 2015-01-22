package org.parabot.jketelaar.essence;

import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.parabot.jketelaar.essence.strategies.Bank;
import org.parabot.jketelaar.essence.strategies.Mine;
import org.parabot.jketelaar.essence.strategies.Walk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JKetelaar
 */
@ScriptManifest(author = "JKetelaar",
        category = Category.MINING,
        description = "An essence miner meant for 2006scape, but should work with different servers.",
        name = "JKEssence",
        servers = { "Any 317" },
        version = 1.0)
public class Core extends Script {

    private final List<Strategy> strategies = new ArrayList<>();

    @Override
    public boolean onExecute() {
        strategies.add(new Walk());
        strategies.add(new Mine());
        strategies.add(new Bank());
        provide(strategies);
        return true;
    }
}
