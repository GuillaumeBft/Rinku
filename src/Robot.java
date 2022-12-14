import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

import java.util.List;
import java.util.stream.Collectors;

public class Robot extends WaypointNode {
    Point spawn;
    List<Node> villages;
    List<Node> itinerary;

    public Robot(List<Node> itinerary){
        this.itinerary = itinerary;
        setIcon(Icons.ROBOT);
        setIconSize(16);
        setCommunicationRange(120);
        setSensingRange(30);
    }

    @Override
    public void onStart() {
        spawn = getLocation();
        super.setSpeed(15);
        villages = getTopology().getNodes().stream().filter(n -> n instanceof Village).map(n -> (Node) n).collect(Collectors.toList());
        System.out.println(villages.toString());
        startVisitRound();
    }

    public void startVisitRound(){
        for(Node n : itinerary){
            addDestination(n.getLocation());
        }
    }

    @Override
    public void onSensingIn(Node node) {
        // Le robot sent qqc...
    }

    @Override
    public void onMessage(Message message) {
        // Le robot a reçu un message...
    }

    @Override
    public void onArrival() {
        // Le robot est arrivé à sa destination
        if(getLocation().equals(itinerary.get(itinerary.size()-1).getLocation())){
            startVisitRound();
        }
    }
}