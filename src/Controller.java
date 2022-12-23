import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller implements CommandListener {
    public static final String START = "Start execution";
    public static final String BRUTEFORCE = "Set Bruteforce algo";
    public static final String RANDOM_INSERT = "Set Random Insertion algo";
    public static final String VRP = "Set VRP algo";
    public static final String DISPLAY_MAXTIME = "Display max comm. times";
    public static final int NB_ROBOTS = 6;
    public static Topology topology;
    public static String selectedAlgo;

    static Map<Point, Village> villages;
    static List<Robot> robots;
    static List<Itinerary> itineraries;

    List<Point> emptyPoints;
    Itinerary emptyIt;

    public Controller() {
        topology = new Topology();
        topology.setDefaultNodeModel(Village.class);
        topology.setTimeUnit(100);

        selectedAlgo = "";
        itineraries = new ArrayList<>();

        villages = new HashMap<>();
        //addVillagesTest();
        addVillages();

        robots = new ArrayList<>();

        emptyPoints = new ArrayList<>();
        emptyIt = new Itinerary(emptyPoints, 0);

        new JViewer(topology);
        topology.addCommandListener(this);
        topology.removeCommand("Set communication range");
        topology.removeCommand("Set sensing range");
        topology.addCommand(DISPLAY_MAXTIME);
        topology.addCommand(RANDOM_INSERT);
        topology.addCommand(VRP);
        topology.addCommand(BRUTEFORCE);
    }

    @Override
    public void onCommand(String command) {
        switch (command) {
            case START :
                for (Node n : topology.getNodes()) {
                    if (n instanceof Village) {
                        villages.put(n.getLocation(), (Village) n);
                    }
                }
                computeItinerary(topology.getNodes(), selectedAlgo);
                addRobots();
                break;

            case DISPLAY_MAXTIME :
                for (Village v : villages.values()) {
                    System.out.println("Max times to deliver message from " + v.getName() + " to ");
                    for (Village v2 : villages.values().stream().filter(village -> !village.equals(v)).collect(Collectors.toList())) {
                        System.out.println("    " + v2.getName() + " : "
                                + v.getMaxCommunicationTime(v2) + " ticks");
                    }
                }
                break;

            case BRUTEFORCE :
                selectedAlgo = BRUTEFORCE;
                System.out.println("Bruteforce optimal selection algorithm has been selected for the compute of the robot path.");
                break;

            case RANDOM_INSERT :
                selectedAlgo = RANDOM_INSERT;
                System.out.println("Random insertion algorithm has been selected for the compute of the robot path.");
                break;

            case VRP :
                selectedAlgo = VRP;
                System.out.println("VRP algorithm has been selected for the compute of the robot path.");
                break;
        }
    }

    public static void computeItinerary(List<Node> nodes, String selectedAlgo){
        Algorithm algorithm = new Algorithm(nodes);
        switch (selectedAlgo) {
            case RANDOM_INSERT:
                itineraries.add(0, algorithm.randomInsertion());
                break;
            case BRUTEFORCE:
                itineraries.add(0, algorithm.bruteForce());
                break;
            case VRP:
                itineraries = algorithm.VRP(NB_ROBOTS);
                break;
            default:
                itineraries.add(0, algorithm.noAlgo());
                break;
        }
        System.out.println("Total distance of the itineraries made by " + selectedAlgo + " is "
                + Algorithm.getItinerariesTotalDistance(itineraries));
    }

    public void addVillages(){
        topology.addNode(20, 200, new Village("Signy-Le-Petit"));
        topology.addNode(20, 70, new Village("Cussac"));
        topology.addNode(100, 200, new Village("Croissy"));
        topology.addNode(250, 50, new Village("Pessac"));
        topology.addNode(250, 250, new Village("Croatie"));
        /*topology.addNode(25, 25, new Village("Gap"));
        topology.addNode(43, 324, new Village("Manhattan"));
        topology.addNode(320, 260, new Village("Bucarest"));
        topology.addNode(56, 125, new Village("Budapest"));
        topology.addNode(240, 66, new Village("Singapour"));
        topology.addNode(300, 130, new Village("Berlin"));*/
    }

    public void addVillagesTest(){
        topology.addNode(20, 100, new Village("Libourne"));
        topology.addNode(20, 200, new Village("Capri"));
        topology.addNode(20, 300, new Village("Grenoble"));
    }

    public void addRobots(){
        if (selectedAlgo.equals(VRP)) {
            for(int i = 0; i < NB_ROBOTS; i++){
                topology.addNode(Robot.SPAWN_POINT_X, Robot.SPAWN_POINT_Y, new Robot(new Itinerary(itineraries.get(i).getSteps(), 0)));
            }
        } else {
            for(int i = 0; i < NB_ROBOTS; i++){
                Itinerary itRobot = new Itinerary(itineraries.get(0).getSteps(), itineraries.get(0).getStart() + i);
                topology.addNode(Robot.SPAWN_POINT_X, Robot.SPAWN_POINT_Y, new Robot(itRobot));
            }
        }
    }
}
