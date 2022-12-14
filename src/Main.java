import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(WaypointNode.class);
        tp.setTimeUnit(100);
        List<Node> villages = new ArrayList<>();

        villages.add(new Village("Signy-Le-Petit", villages));
        villages.add(new Village("Cussac", villages));
        villages.add(new Village("Croissy", villages));
        villages.add(new Village("Pessac", villages));

        tp.addNode(20, 200, villages.get(0));
        tp.addNode(20, 70, villages.get(1));
        tp.addNode(100, 200, villages.get(2));
        tp.addNode(250, 50, villages.get(3));

        List<Node> itinerary = computeItinerary(villages);

        tp.addNode(100, 100, new Robot(itinerary));
       // tp.addNode(100, 100, new Robot());

        new JViewer(tp);
        tp.start();
    }

    public static List<Node> computeItinerary(List<Node> villages){

        return villages;
    }
}