import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.ui.JViewer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller implements CommandListener {
    public static final String START = "Start execution";
    public static final String BRUTEFORCE = "Set Bruteforce algo";
    public static final String RANDOM_INSERT = "Set Random Insertion algo";
    private Topology topology;
    static Map<Point, Node> villages;
    static Itinerary itinerary;

    private String selectedAlgo;

    public Controller() {
        topology = new Topology();
        topology.setDefaultNodeModel(Village.class);
        topology.setTimeUnit(100);

        selectedAlgo = "";

        villages = new HashMap<>();
        //addVillagesTestSpeed();
        addVillages();

        new JViewer(topology);
        topology.addCommandListener(this);
        topology.addCommand(RANDOM_INSERT);
        topology.addCommand(BRUTEFORCE);
    }

    @Override
    public void onCommand(String command) {
        if (command.equals(START)) {
            for (Node n : topology.getNodes()) {
                if (n instanceof Village) {
                    villages.put(n.getLocation(), n);
                }
            }
            computeItinerary(topology.getNodes(), selectedAlgo);
            addRobots();
        } else if (command.equals(BRUTEFORCE)) {
            selectedAlgo = BRUTEFORCE;
            System.out.println("Bruteforce optimal selection algorithm has been selected for the compute of the robot path.");
        } else if (command.equals(RANDOM_INSERT)) {
            selectedAlgo = RANDOM_INSERT;
            System.out.println("Random insertion algorithm has been selected for the compute of the robot path.");
        }
    }

    public static void computeItinerary(List<Node> nodes, String selectedAlgo){
        Algorithm algorithm = new Algorithm(nodes);
        switch (selectedAlgo) {
            case RANDOM_INSERT -> itinerary = algorithm.randomInsertion();
            case BRUTEFORCE -> itinerary = algorithm.bruteForce();
            default -> itinerary = algorithm.noAlgo();
        }
        System.out.println("Distance of the itinerary made by " + selectedAlgo + " is " + Algorithm.getDistance(itinerary.getSteps()));
    }

    public void addVillages(){
        topology.addNode(20, 200, new Village("Signy-Le-Petit"));
        topology.addNode(20, 70, new Village("Cussac"));
        topology.addNode(100, 200, new Village("Croissy"));
        topology.addNode(250, 50, new Village("Pessac"));
        topology.addNode(250, 250, new Village("Croatie"));
    }

    public void addVillagesTestSpeed(){
        topology.addNode(20, 100, new Village("Libourne"));
        topology.addNode(20, 200, new Village("Capri"));
        topology.addNode(20, 300, new Village("Grenoble"));
    }

    public void addRobots(){
        topology.addNode(100, 100, new Robot(itinerary));
        //topology.addNode(100, 100, new Robot(new Itinerary(itinerary.getSteps(), itinerary.getStart() + itinerary.getSize()/2)));
    }
}
