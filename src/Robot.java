import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Robot extends WaypointNode {
    Point spawn;
    List<Node> villages;
    List<Node> itinerary;
    List<Message> backpack;

    public Robot(List<Node> itinerary){
        this.itinerary = itinerary;
        this.backpack = new ArrayList<>();
        setIcon(Icons.ROBOT);
        setIconSize(16);
        setCommunicationRange(0);
        setSensingRange(0);
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
    public void onArrival() {
        // Le robot est arrivé à sa destination
        if(getLocation().equals(itinerary.get(itinerary.size()-1).getLocation())){
            startVisitRound();
        }
    }
}