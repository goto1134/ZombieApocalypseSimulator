package com.github.goto1134.zombieapocalypsesimulator.jade.walkers;

import com.github.goto1134.zombieapocalypsesimulator.jade.MessageUtils;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.DieAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.BecomeZombieBehaviour;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jade.lang.acl.MessageTemplate.*;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
class DieShoutActionPerformer extends SimpleAchieveREResponder {

    private static final Logger cat = LoggerFactory.getLogger(DieShoutActionPerformer.class);
    private static MessageTemplate template =
            and(
                    MatchPerformative(ACLMessage.REQUEST),
                    or(
                            MatchContent(MessageUtils.DieActionContent),
                            MatchContent(MessageUtils.ShoutActionContent)));
    private DataStore dataStore;

    public DieShoutActionPerformer(Agent a, DataStore dataStore) {
        super(a, template);
        this.dataStore = dataStore;
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request)
            throws NotUnderstoodException, RefuseException {
        ContentElement contentElement;
        try {
            contentElement = getAgent().getContentManager().extractContent(request);
        } catch (Codec.CodecException | OntologyException e) {
            throw new NotUnderstoodException(request);
        }

        if (contentElement instanceof DieAction) {
            cat.info("-------DIE RECEIVED--------");
            if (DataStoreUtils.getWalkerType(dataStore).equals(WalkerType.HUMAN)) {
                BecomeZombieBehaviour becomeZombieBehaviour = new BecomeZombieBehaviour();
                becomeZombieBehaviour.setDataStore(dataStore);
                getAgent().addBehaviour(becomeZombieBehaviour);
            } else {
                // TODO: 06.12.2016 Удалить агента
            }
            cat.info("died");
        } else {
            if (DataStoreUtils.getHumanState(dataStore).equals(HumanState.CALM)) {
                DataStoreUtils.putHumanState(dataStore, HumanState.SCARED);
            }
        }

        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        return reply;
    }
}
