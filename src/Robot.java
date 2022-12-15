import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Robot extends WaypointNode {
    Point spawn;
    Map<Point, Node> villages;
    List<Node> itinerary;
    List<Mail> backpack;

    public Robot(List<Node> itinerary, Controller controller){
        this.itinerary = itinerary;
        this.backpack = new ArrayList<>();
        this.villages = controller.villages;

        setIcon(Icons.ROBOT);
        setIconSize(16);
        setCommunicationRange(0);
        setSensingRange(0);
    }

    @Override
    public void onStart() {
        spawn = getLocation();
        super.setSpeed(5);
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

        //recuperer les courriers
        collectPostbox((Village) villages.get(getLocation()));
    }

    public void collectPostbox(Village village){
        backpack.addAll(village.getPostbox());
        village.clearPostBox();
    }

    @Override
    public String toString() {
        return super.toString() + ", in my backpack : " + backpack.size();
    }
}