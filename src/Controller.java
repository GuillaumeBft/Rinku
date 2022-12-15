import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private Topology topology;
    Map<Point, Node> villages;
    Itinerary itinerary;

    public Controller() {
        topology = new Topology();
        topology.setDefaultNodeModel(WaypointNode.class);
        topology.setTimeUnit(100);

        villages = new HashMap<>();
        addDataVillages();
        computeItinerary();
        addDataRobots();

        topology.start();
        new JViewer(topology);
    }

    public void computeItinerary(){
        itinerary = new Itinerary(new ArrayList<>(), 0);
        for (Point p : villages.keySet()) {
            itinerary.getSteps().add(villages.get(p));
        }
    }

    public void addDataVillages(){
        Node v1 = new Village("Signy-Le-Petit", this);
        villages.put(new Point(20, 200), v1);
        Node v2 = new Village("Cussac", this);
        villages.put(new Point(20, 70), v2);
        Node v3 = new Village("Croissy", this);
        villages.put(new Point(100, 200), v3);
        Node v4 = new Village("Pessac", this);
        villages.put(new Point(250, 50), v4);

        for (Point p : villages.keySet()) {
            topology.addNode(p.getX(), p.getY(), villages.get(p));
        }
    }

    public void addDataRobots(){
        topology.addNode(100, 100, new Robot(itinerary, this));
        topology.addNode(100, 100, new Robot(new Itinerary(itinerary.getSteps(), itinerary.getStart() + itinerary.getSize()/2), this));
    }
}
