import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller implements CommandListener {
    public static final String START = "Start execution";
    private Topology topology;
    static Map<Point, Node> villages;
    static Itinerary itinerary;

    public Controller() {
        topology = new Topology();
        topology.setDefaultNodeModel(Village.class);
        topology.setTimeUnit(100);

        villages = new HashMap<>();
        addDataVillages();

        new JViewer(topology);
        topology.addCommandListener(this);
    }

    @Override
    public void onCommand(String command) {
        if (command.equals(START)) {
            for (Node n : topology.getNodes()) {
                if (n instanceof Village) {
                    villages.put(n.getLocation(), n);
                }
            }
            computeItinerary();
            addDataRobots();
        }
    }

    public static void computeItinerary(){
        List<Point> points = new ArrayList<>();
        for (Point p : villages.keySet()) {
            points.add(p);
        }
        Algorithm algorithm = new Algorithm(points);
        itinerary = algorithm.noAlgo();
    }

    public void addDataVillages(){
        topology.addNode(20, 200, new Village("Signy-Le-Petit"));
        topology.addNode(20, 70, new Village("Cussac"));
        topology.addNode(100, 200, new Village("Croissy"));
        topology.addNode(250, 50, new Village("Pessac"));
    }

    public void addDataRobots(){
        topology.addNode(100, 100, new Robot(itinerary));
        //topology.addNode(100, 100, new Robot(new Itinerary(itinerary.getSteps(), itinerary.getStart() + itinerary.getSize()/2)));
    }
}
