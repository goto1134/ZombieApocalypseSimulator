package com.github.goto1134.zombieapocalypsesimulator.jade;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.ApocalypseOntology;
import jade.content.lang.leap.LEAPCodec;
import jade.lang.acl.ACLMessage;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
public class MessageUtils {
    public static ACLMessage prepareMessage(int performative) {
        ACLMessage message = new ACLMessage(performative);
        message = prepareMessage(message);
        return message;
    }

    private static ACLMessage prepareMessage(ACLMessage message) {
        message.setOntology(ApocalypseOntology.NAME);
        message.setLanguage(new LEAPCodec().getName());
        return message;
    }
}
