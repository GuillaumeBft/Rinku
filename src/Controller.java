import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private Topology topology;
    List<Node> villages;
    List<Node> itinerary;

    public Controller() {
        topology = new Topology();
        topology.setDefaultNodeModel(WaypointNode.class);
        topology.setTimeUnit(100);

        villages = new ArrayList<>();
        itinerary = computeItinerary(villages);
        addData();
        
        topology.start();
        new JViewer(topology);
    }

    public List<Node> computeItinerary(List<Node> villages){
        return villages;
    }

    public void addData(){
        //VILLAGES
        villages.add(new Village("Signy-Le-Petit", villages));
        villages.add(new Village("Cussac", villages));
        villages.add(new Village("Croissy", villages));
        villages.add(new Village("Pessac", villages));

        topology.addNode(20, 200, villages.get(0));
        topology.addNode(20, 70, villages.get(1));
        topology.addNode(100, 200, villages.get(2));
        topology.addNode(250, 50, villages.get(3));

        //ROBOTS
        topology.addNode(100, 100, new Robot(itinerary));
        // tp.addNode(100, 100, new Robot());
    }
}
