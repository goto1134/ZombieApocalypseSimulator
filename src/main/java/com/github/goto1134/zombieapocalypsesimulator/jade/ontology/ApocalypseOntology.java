package com.github.goto1134.zombieapocalypsesimulator.jade.ontology;

import jade.content.onto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew
 * on 28.11.2016.
 */
public class ApocalypseOntology extends BeanOntology {
    private static final Logger cat = LoggerFactory.getLogger(ApocalypseOntology.class);
    public static final String NAME = ApocalypseOntology.class.getName();
    private static Ontology theInstance = new ApocalypseOntology();

    public static Ontology getInstance() {
        return theInstance;
    }

    private ApocalypseOntology() {
        super(NAME, BasicOntology.getInstance());
        try {
            add("com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data");
        } catch (BeanOntologyException e) {
            cat.error("Error adding beans to ontology", e);
        }
    }

    public static void main(String[] args) throws Exception {
        Ontology onto = getInstance();
        OntologyUtils.exploreOntology(onto);
    }
}
