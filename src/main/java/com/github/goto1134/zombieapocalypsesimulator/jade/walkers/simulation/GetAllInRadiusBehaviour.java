package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
class GetAllInRadiusBehaviour extends AchieveREInitiator {
    private static ACLMessage message = new ACLMessage(ACLMessage.QUERY_REF);

    public GetAllInRadiusBehaviour(Agent a) {
        super(a, message);
    }


    @Override
    protected Vector prepareRequests(ACLMessage request) {
        // TODO: 05.12.2016 Запросить координаты всех участников противоположной команды в радиусе, определённом действием
        return super.prepareRequests(request);
    }

    @Override
    protected void handleAllResponses(Vector responses) {
        // TODO: 06.12.2016 Сохранить все результаты и передать в следующий шаг
        super.handleAllResponses(responses);
    }

    @Override
    public int onEnd() {
        // TODO: 06.12.2016 Вернуть тип следующего действия
        return super.onEnd();
    }
}
