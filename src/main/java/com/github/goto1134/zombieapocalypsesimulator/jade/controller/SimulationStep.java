package com.github.goto1134.zombieapocalypsesimulator.jade.controller;

import com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.FightAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.WalkAction;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;

/**
 * Created by Andrew
 * on 01.12.2016.
 */
class SimulationStep
        extends SequentialBehaviour {


    SimulationStep(Agent a) {
        super(a);
        init();
    }

    private void init() {
        addSubBehaviour(new PerformWalkerAction(getAgent(), new WalkAction(), ZombieApocalypseConstants.ZOMBIE_AGENT_DESCRIPTION));
        addSubBehaviour(new PerformWalkerAction(getAgent(), new FightAction(), ZombieApocalypseConstants.ZOMBIE_AGENT_DESCRIPTION));
        addSubBehaviour(new PerformWalkerAction(getAgent(), new WalkAction(), ZombieApocalypseConstants.HUMAN_AGENT_DESCRIPTION));
        addSubBehaviour(new PerformWalkerAction(getAgent(), new FightAction(), ZombieApocalypseConstants.HUMAN_AGENT_DESCRIPTION));
        addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                getAgent().addBehaviour(new SimulationChecker(getDataStore()));
            }
        });
    }
}
