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
    static final String START = "Start execution";
    static final String BRUTEFORCE = "Set Bruteforce algo";
    static final String RANDOM_INSERT = "Set Random Insertion algo";
    static final String NEAREST_NEIGHBOR = "Set Nearest Neighbor algo";
    static final String VRP = "Set VRP algo";
    static final String DISPLAY_MAXTIME = "Display max comm. times";
    static final String ADD_ONE_ROBOT = "Add one robot";
    static final String REMOVE_ONE_ROBOT = "Remove one robot";

    static int nbRobots = 3;
    static Topology topology;
    static String selectedAlgo;
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
        topology.addCommand(ADD_ONE_ROBOT);
        topology.addCommand(REMOVE_ONE_ROBOT);
        topology.addCommand(DISPLAY_MAXTIME);
        topology.addCommand(RANDOM_INSERT);
        topology.addCommand(NEAREST_NEIGHBOR);
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

            case ADD_ONE_ROBOT:
                nbRobots++;
                System.out.println("Added a new robot, now there are " + nbRobots + " robots.");
                break;

            case REMOVE_ONE_ROBOT:
                nbRobots--;
                if (nbRobots < 1) { nbRobots = 1; }
                System.out.println("Removed a robot, now there are " + nbRobots + " robots.");
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

            case NEAREST_NEIGHBOR :
                selectedAlgo = NEAREST_NEIGHBOR;
                System.out.println("Nearest neighbor algorithm has been selected for the compute of the robot path.");
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

            case NEAREST_NEIGHBOR:
                itineraries.add(0, algorithm.nearestNeighbor());
                break;

            case VRP:
                itineraries = algorithm.VRP(nbRobots);
                break;
            default:
                itineraries.add(0, algorithm.noAlgo());
                break;
        }
        System.out.println("Total distance of the itineraries made by " + selectedAlgo + " is "
                + Algorithm.getItinerariesTotalDistance(itineraries));
    }

    public void addVillages(){
        topology.addNode(120, 275, new Village("Signy-Le-Petit"));
        topology.addNode(100, 70, new Village("Cussac"));
        topology.addNode(150, 200, new Village("Croissy"));
        topology.addNode(450, 50, new Village("Pessac"));
        topology.addNode(400, 300, new Village("Croatie"));
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

    public void addRobots() {
        Robot robotAdded;
        int shift = itineraries.get(0).getSteps().size() / nbRobots;

        for(int i = 0; i < nbRobots; i++){
            if (selectedAlgo.equals(VRP)) {
                robotAdded =  new Robot(new Itinerary(itineraries.get(i).getSteps(), 0));
                topology.addNode(Robot.SPAWN_POINT_X, Robot.SPAWN_POINT_Y, robotAdded);
            } else {
                // Shift the starting village according to the number of villages and robots
                Itinerary it = new Itinerary(itineraries.get(0).getSteps(),
                        (i * shift) % (itineraries.get(0).getSteps().size()));
                robotAdded = new Robot(it);
                topology.addNode(Robot.SPAWN_POINT_X, Robot.SPAWN_POINT_Y, robotAdded);
            }
            robots.add(robotAdded);
        }
    }
}
