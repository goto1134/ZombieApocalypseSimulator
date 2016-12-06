package com.github.goto1134.zombieapocalypsesimulator.jade;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.ApocalypseOntology;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.DieAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.ShoutAction;
import jade.content.abs.AbsContentElement;
import jade.content.lang.Codec;
import jade.content.lang.leap.LEAPCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
public class MessageUtils {
    private static final Logger cat = LoggerFactory.getLogger(MessageUtils.class);
    public static final String ShoutActionContent = getContent(new ShoutAction());
    public static final String DieActionContent = getContent(new DieAction());

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

    public static String getContent(Object object) {
        Ontology instance = ApocalypseOntology.getInstance();
        try {
            return new String(new LEAPCodec().encode(instance, (AbsContentElement) instance.fromObject(object)));
        } catch (Codec.CodecException | OntologyException e) {
            cat.error("", e);
        }
        return null;
    }
}
