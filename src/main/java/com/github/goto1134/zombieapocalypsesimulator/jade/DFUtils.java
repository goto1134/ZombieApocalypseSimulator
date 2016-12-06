package com.github.goto1134.zombieapocalypsesimulator.jade;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
public class DFUtils {
    private static final Logger cat = LoggerFactory.getLogger(DFUtils.class);

    public static Stream<AID> getAllAgentsByType(Agent agent, DFAgentDescription agentDescription)
            throws FIPAException {
        return Arrays.stream(DFService.search(agent, agentDescription))
                .map(DFAgentDescription::getName)
                .peek(aid -> cat.info("found walkers : " + aid.getName()));
    }
}
