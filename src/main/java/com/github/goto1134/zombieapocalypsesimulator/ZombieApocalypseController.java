package com.github.goto1134.zombieapocalypsesimulator;

import com.github.goto1134.zombieapocalypsesimulator.jade.controller.ApocalypseController;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.Walker;
import com.github.goto1134.zombieapocalypsesimulator.view.SimulationProperties;
import com.github.goto1134.zombieapocalypsesimulator.view.ZombieApocalypseWindow;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Andrew
 * on 01.12.2016.
 */
public class ZombieApocalypseController {
    private static final Logger cat = LoggerFactory.getLogger(ZombieApocalypseController.class);

    public static void main(String[] strings) {
        new ZombieApocalypseController();
    }

    private final Runtime instance = Runtime.instance();
    private final ZombieApocalypseWindow window = new ZombieApocalypseWindow();
    private final AgentContainer agentContainer;
    private Set<AgentController> controllers = new HashSet<>();


    private ZombieApocalypseController() {
        Profile profile = new ProfileImpl();
        agentContainer = instance.createMainContainer(profile);

        window.setStartListener(this::startSimulation);
        window.setStopListener(this::stopSimulation);
        window.setVisible(true);
    }

    private void startSimulation() {
        stopSimulation();
        SimulationProperties simulationProperties = window.getSimulationProperties();
        controllers = Stream.iterate(1, n -> n + 1)
                .limit(simulationProperties.walkersNumber)
                .map(n -> "walker" + n)
                .map(this::createWalker)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        try {
            controllers.add(agentContainer.createNewAgent("controller",
                    ApocalypseController.class.getName(),
                    new Object[]{simulationProperties}));
            controllers.forEach(this::startAgent);
        } catch (StaleProxyException e) {
            cat.error("", e);
        }


    }

    private void stopSimulation() {
        controllers.forEach(this::killAllAgents);
    }

    private void startAgent(AgentController agentController) {
        try {
            agentController.start();
        } catch (StaleProxyException e) {
            cat.error("", e);
        }
    }

    private AgentController createWalker(String s) {
        try {
            return agentContainer.createNewAgent(s, Walker.class.getName(), new Object[]{});
        } catch (StaleProxyException e) {
            cat.error("", e);
        }
        return null;
    }

    private void killAllAgents(AgentController agentController) {
        try {
            agentController.kill();
        } catch (StaleProxyException e) {
            cat.error("", e);
        }
    }
}
