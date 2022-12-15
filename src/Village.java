import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Village extends Node {
    String name;
    Map<Point, Node> villages;
    List<Mail> postbox;

    public Village(String name, Controller controller) {
        this.name = name;
        this.villages = controller.villages;
        postbox = new ArrayList<>();
    }

    @Override
    public void onStart() {
        this.setIcon(System.getProperty("user.dir") + "/hutte.png");
        setIconSize(16);
        setCommunicationRange(0);
        setSensingRange(0);
    }

    @Override
    public void onClock() {
        if (Math.random() < 0.005) {
            newMail();
        }
    }

    private void newMail(){
        List<Node> potentialDestinations = villages.values().stream().filter(node -> !node.equals(this)).toList();
        //System.out.println("I am : " + name + ", dest : " + potentialDestinaions.toString());
        Random rand = new Random();
        Node randomElement = potentialDestinations.get(rand.nextInt(potentialDestinations.size()));

        postbox.add(new Mail(this, randomElement, "cc c nou"));
    }

    public void clearPostBox(){
        postbox.clear();
    }

    public List<Mail> getPostbox() {
        return postbox;
    }

    @Override
    public String toString() {
        return super.toString() + " " + name + ", msg : " + postbox.size();
    }
}
