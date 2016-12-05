package com.github.goto1134.zombieapocalypsesimulator.jade.controller;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.MapOntology;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.WalkerAction;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Created by Andrew
 * on 01.12.2016.
 */
class PerformWalkerAction extends AchieveREInitiator {

    private static final Logger cat = LoggerFactory.getLogger(PerformWalkerAction.class);

    private static final ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);

    static {
        aclMessage.setOntology(MapOntology.NAME);
    }

    private final WalkerAction action;
    private final DFAgentDescription agentDescription;

    PerformWalkerAction(Agent a, WalkerAction action, DFAgentDescription agentDescription) {
        super(a, aclMessage);
        this.action = action;
        this.agentDescription = agentDescription;
    }

    @Override
    protected Vector prepareRequests(ACLMessage request) {
        try {
            return Arrays.stream(DFService.search(getAgent(), agentDescription))
                    .map(DFAgentDescription::getName)
                    .peek(aid -> cat.info("found walker : " + aid.getName()))
                    .map(this::getRequestMessage)
                    .collect(Collectors.toCollection(Vector::new));
        } catch (FIPAException e) {
            cat.error("", e);
        }
        return super.prepareRequests(request);
    }

    @Override
    protected void handleAllResponses(Vector responses) {
        cat.info("Responses: " + responses);
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        cat.info("Result " + resultNotifications);
    }

    private ACLMessage getRequestMessage(AID s) {
        ACLMessage outgoingRequest = new ACLMessage(ACLMessage.REQUEST);
        outgoingRequest.setOntology(MapOntology.NAME);
        ContentManager contentManager = getAgent().getContentManager();

        outgoingRequest.setLanguage(contentManager.getLanguageNames()[0]);
        try {
            contentManager.fillContent(outgoingRequest, action);
        } catch (Codec.CodecException | OntologyException e) {
            cat.error("", e);
        }
        outgoingRequest.addReceiver(s);
        outgoingRequest.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        return outgoingRequest;
    }
}
