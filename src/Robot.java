import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Robot extends WaypointNode {
    Point spawn;
    Map<Point, Node> villages;
    Itinerary itinerary;
    List<Mail> backpack;

    public Robot(Itinerary itinerary){
        this.itinerary = itinerary;
        this.backpack = new ArrayList<>();
        this.villages = Controller.villages;

        setIcon(Icons.ROBOT);
        setIconSize(16);
        setCommunicationRange(0);
        setSensingRange(0);
    }

    @Override
    public void onStart() {
        spawn = getLocation();
        super.setSpeed(15);
        startVisitRound();
    }

    public void startVisitRound(){
        int cpt = 0;
        for (int i = itinerary.getStart();cpt < itinerary.getSize(); i++) {
            cpt++;
            i %= itinerary.getSize();
            addDestination(itinerary.getSteps().get(i).getLocation());
        }
    }

    @Override
    public void onArrival() {
        // Le robot est arrivé à sa destination
        if(getLocation().equals(itinerary.getSteps().get(itinerary.getEnd()).getLocation())){
            System.out.println("NEW ROUND AS ROBOT N°" + getID());
            startVisitRound();
        }

        Village current = (Village) villages.get(getLocation());

        //recuperer les courriers
        collectPostbox(current);
        //délivrer les courriers
        for (Mail m : backpack) {
            if (m.receiver.equals(current)) {
                send(current, new Message(m));
            }
        }
        backpack = backpack.stream().filter(mail -> !mail.receiver.equals(current)).collect(Collectors.toList());

    }

    public void collectPostbox(Village village){
        backpack.addAll(village.getPostbox());
        village.clearPostBox();
    }

    @Override
    public String toString() {
        return "Robot n°" + super.toString() + ", in my backpack : " + backpack.size() + " messages";
    }
}