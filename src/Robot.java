import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

import java.util.List;
import java.util.stream.Collectors;

public class Robot extends WaypointNode {
    Point spawn;
    List<Node> villages;

    public Robot(){
        setIcon(Icons.ROBOT);
        setIconSize(16);
        setCommunicationRange(120);
        setSensingRange(30);
    }

    @Override
    public void onStart() {
        spawn = getLocation();
        super.setSpeed(2);
        villages = getTopology().getNodes().stream().filter(n -> n instanceof Village).map(n -> (Node) n).collect(Collectors.toList());
        System.out.println(villages.toString());
    }

    @Override
    public void onSensingIn(Node node) {
        // Le robot sent qqc...
    }

    @Override
    public void onMessage(Message message) {
        // Le robot a reçu un message...
    }

    @Override
    public void onArrival() {
        // Le robot est arrivé à sa destination
    }
}