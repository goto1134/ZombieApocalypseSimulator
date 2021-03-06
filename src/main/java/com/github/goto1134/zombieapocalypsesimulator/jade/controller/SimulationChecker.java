package com.github.goto1134.zombieapocalypsesimulator.jade.controller;

import com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Проверяет, нужно ли продолжать симуляцию
 * Created by Andrew
 * on 01.12.2016.
 */
class SimulationChecker extends OneShotBehaviour {
    private static final Logger cat = LoggerFactory.getLogger(SimulationChecker.class);
    private SimulationStep simulationStep;


    SimulationChecker(DataStore dataStore) {
        setDataStore(dataStore);
    }

    @Override
    public void action() {
        try {
            Agent agent = getAgent();
            int zombieCount = DFService.search(agent, ZombieApocalypseConstants.ZOMBIE_AGENT_DESCRIPTION).length;
            int humansCount = DFService.search(agent, ZombieApocalypseConstants.HUMAN_AGENT_DESCRIPTION).length;

            if (zombieCount == 0) {
                cat.info("!!!!!!!!!!!!!!!!!!!!Humanity survived!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            } else if (humansCount == 0) {
                cat.info("!!!!!!!!!!!!!!!!!!!!!!!Zombies won!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            } else {
                SimulationStep simulationStep = new SimulationStep(getAgent());
                simulationStep.setDataStore(getDataStore());
                getAgent().addBehaviour(simulationStep);
            }

        } catch (FIPAException e) {
            cat.error("", e);
        }
    }
}
