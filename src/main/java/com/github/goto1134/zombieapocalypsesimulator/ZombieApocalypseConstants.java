package com.github.goto1134.zombieapocalypsesimulator;

import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.WalkerType;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 * Created by Andrew
 * on 01.12.2016.
 */
public class ZombieApocalypseConstants {
    public static final String ZOMBIE = WalkerType.ZOMBIE.toString();
    public static final String HUMAN = WalkerType.HUMAN.toString();
    public static final String WALKER = "WALKER";


    public static final DFAgentDescription ZOMBIE_AGENT_DESCRIPTION = new DFAgentDescription();
    public static final DFAgentDescription HUMAN_AGENT_DESCRIPTION = new DFAgentDescription();
    public static final String MAP_SIZE = "MapSize";
    public static final String COORDINATE = "Coordinate";
    public static final String CONTROLLER_AID = "CONTROLLER_AID";
    public static final String WALKER_TYPE = "type";
    public static final String HUMAN_STATE = "state";

    static {
        ServiceDescription zombieserviceDescription = new ServiceDescription();
        zombieserviceDescription.setType(ZOMBIE);
        ZOMBIE_AGENT_DESCRIPTION.addServices(zombieserviceDescription);

        ServiceDescription humanServiceDescription = new ServiceDescription();
        humanServiceDescription.setType(HUMAN);
        HUMAN_AGENT_DESCRIPTION.addServices(humanServiceDescription);
    }
}
