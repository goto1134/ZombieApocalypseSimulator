package com.github.goto1134.zombieapocalypsesimulator.jade.controller;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.ApocalypseOntology;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.Coordinates;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.GenerateCoordinates;
import com.github.goto1134.zombieapocalypsesimulator.view.SimulationProperties;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static jade.lang.acl.MessageTemplate.*;

/**
 * Регистрирует заданное количество людей, после чего меняет поведение
 * Created by Andrew
 * on 03.12.2016.
 */
class RegistrarBehaviour extends AchieveREResponder {
    private static final Logger cat = LoggerFactory.getLogger(RegistrarBehaviour.class);
    private static final MessageTemplate TEMPLATE =
            and(
                    and(
                            MatchOntology(ApocalypseOntology.getInstance().getName()),
                            MatchPerformative(ACLMessage.QUERY_REF)),
                    MatchProtocol(FIPA_QUERY));
    private final SimulationProperties simulationProperties;
    private int responseNumbers = 0;
    private Random generator = new Random(System.currentTimeMillis());

    RegistrarBehaviour(Agent a, DataStore store) {
        super(a, TEMPLATE, store);
        simulationProperties = (SimulationProperties) store.get(ApocalypseController.SIMULATION_PROPERTIES_KEY);
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        if (responseNumbers >= simulationProperties.walkersNumber) {
            getAgent().removeBehaviour(this);
            throw new RefuseException("out of limit");
        }

        Agent agent = getAgent();
        ContentManager contentManager = agent.getContentManager();

        ContentElement contentElement;
        try {
            contentElement = contentManager.extractContent(request);
        } catch (Codec.CodecException | OntologyException e) {
            cat.error("", e);
            throw new NotUnderstoodException(request);
        }

        if (!(contentElement instanceof GenerateCoordinates)) {
            throw new RefuseException(request);
        }

        GenerateCoordinates generateCoordinates = GenerateCoordinates((GenerateCoordinates) contentElement);

        cat.info("request " + generateCoordinates);

        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            contentManager.fillContent(reply, generateCoordinates);
        } catch (Codec.CodecException | OntologyException e) {
            cat.error("", e);
            throw new RefuseException(request);
        }

        responseNumbers++;

        if (responseNumbers == simulationProperties.walkersNumber) {
//            agent.removeBehaviour(this);
            DataStore ds = new DataStore();
            ds.put(ApocalypseController.SIMULATION_PROPERTIES_KEY, simulationProperties);

            SimulationStarter simulationStarter = new SimulationStarter(getAgent(), ds);
            agent.addBehaviour(simulationStarter);
        }
        return reply;
    }

    /**
     * Заполняет все поля действия
     */
    private GenerateCoordinates GenerateCoordinates(GenerateCoordinates contentElement) {
        Coordinates coordinates = new Coordinates(generateNext(), generateNext());
        contentElement.setCoordinates(coordinates);
        contentElement.setMapSize(simulationProperties.mapSize);
        return contentElement;
    }

    /**
     * @return случайная составляющая координаты
     */
    private int generateNext() {
        return generator.nextInt(simulationProperties.mapSize);
    }
}
