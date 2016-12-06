package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
class FightBehaviour  extends AchieveREInitiator{

    public static ACLMessage message = new ACLMessage(ACLMessage.QUERY_REF);
    public FightBehaviour(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg);
    }
}
