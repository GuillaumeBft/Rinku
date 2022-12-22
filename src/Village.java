import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Village extends Node {
    String name;
    Map<Point, Village> villages;
    List<Mail> postbox = new ArrayList<>();


    // JBotSim a besoin d'un contructeur sans paramètres pour créer des noeuds à la souris
    public Village() {
        this("DefaultCity");
    }

    public Village(String name) {
        this.name = name;
        villages = Controller.villages;
        this.setIcon(System.getProperty("user.dir") + "/hutte.png");
        setIconSize(16);
        setCommunicationRange(0);
        setSensingRange(0);
    }

    @Override
    public void onStart() {
        if (name == "DefaultCity") {
            name += getID();
        }
    }

    @Override
    public void onClock() {
        if (Math.random() < 0.005) {
            newMail();
        }
    }

    private void newMail(){
        List<Village> potentialDestinations = villages.values().stream().filter(node -> !node.equals(this)).collect(Collectors.toList());
        Random rand = new Random();
        Village randomElement = potentialDestinations.get(rand.nextInt(potentialDestinations.size()));
        postbox.add(new Mail(this, randomElement, "Hello, my name is Brandom, I'm from " + name
                + " and I want to wizz my crush Randomia who lives in " + (randomElement).getName()));
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("Being " + name + " i got new message from " + ((Village)((Mail)message.getContent()).sender).name + " : " + ((Mail)message.getContent()).content);
    }

    public int getMaxCommunicationTime(Village village) {
        if (village.equals(this)) { return 0; }

        Itinerary itinerary = Controller.itineraries.get(0);
        List<Robot> robots = new ArrayList<>();
        for (Node n : Controller.topology.getNodes()){
            if (n instanceof Robot) {
                robots.add((Robot) n);
            }
        }
        double maxTime = Algorithm.getDistanceBetweenTwoFurthestRobots(itinerary.getSteps(), robots)
                + Algorithm.getRoundTotalDistance(itinerary.getSteps())
                - Algorithm.getDistanceThroughPath(itinerary.getSteps(), village, this);
        return (int) maxTime / Robot.SPEED;
    }

    public void clearPostBox(){
        postbox.clear();
    }

    public List<Mail> getPostbox() {
        return postbox;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return super.toString() + " " + name + ", msg : " + postbox.size();
    }
}
