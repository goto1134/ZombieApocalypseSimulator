package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.Coordinates;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.WalkerPosition;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.DataStoreUtils;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.WalkerType;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;

/**
 * Created by Andrew
 * on 05.12.2016.
 */
class WalkBehaviour extends OneShotBehaviour {

    private static final Logger cat = LoggerFactory.getLogger(WalkBehaviour.class);
    private int mapSize = 0;

    @Override
    public void action() {
        DataStore dataStore = getDataStore();
        if (mapSize == 0) {
            mapSize = DataStoreUtils.getMapSize(dataStore);
        }

        List<Coordinates> coordinateList = DataStoreUtils.getCoordinateList(dataStore)
                .stream()
                .map(WalkerPosition::getCoordinates)
                .collect(Collectors.toList());
        WalkerType walkerType = DataStoreUtils.getWalkerType(dataStore);
        Coordinates coordinates = DataStoreUtils.getCoordinates(dataStore);
        Coordinates newCoordinates;
        if (walkerType.equals(WalkerType.HUMAN)) {
            newCoordinates = getMostSafePlace(coordinateList, coordinates);
        } else {
            newCoordinates = getNearestHuman(coordinateList, coordinates);
        }

        DataStoreUtils.putCoordinates(dataStore, newCoordinates);

        ACLMessage receivedMessage = DataStoreUtils.getReceivedMessage(dataStore);
        ACLMessage reply = receivedMessage.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        DataStoreUtils.putRespondMessage(dataStore, reply);
        cat.info("walked to " + newCoordinates);
    }

    private Coordinates getMostSafePlace(List<Coordinates> enemyCoordinates, Coordinates myCoordinates) {
        Random random = new Random(currentTimeMillis());
        Coordinates coordinates = new Coordinates(myCoordinates.getX() + random.nextInt(5) - 2,
                myCoordinates.getY() + random.nextInt(5) - 2);

        // TODO: 06.12.2016 Сгенерировать новые координаты
        return getRandomCoordinates(myCoordinates);
    }

    private Coordinates getNearestHuman(List<Coordinates> coordinateList, Coordinates coordinates) {
        Optional<Coordinates> min = coordinateList.stream()
                .min((o1, o2) -> (o1.distance(coordinates) - o2.distance(coordinates)));
        if (min.isPresent()) {
            return min.get();
        }
        return getRandomCoordinates(coordinates);

        // TODO: 06.12.2016 Сгенерировать новые координаты
    }

    private Coordinates getRandomCoordinates(Coordinates myCoordinates) {
        Coordinates newCoordinates;
        do {
            newCoordinates = myCoordinates.randomInRadius(2);
        }
        while (newCoordinates.getX() > mapSize || newCoordinates.getY() > mapSize);
        cat.info("coords generated");
        return newCoordinates;
    }
}
