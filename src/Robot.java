import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Robot extends WaypointNode {
    final static int SPEED = 10;
    public final static int SPAWN_POINT_X = 300;
    public final static int SPAWN_POINT_Y = 200;
    static int cptRobotsAtSpawn = 0;
    Map<Point, Village> villages;
    Itinerary itinerary;
    List<Mail> backpack;
    int time;
    boolean isWaiting;

    public Robot(Itinerary itinerary){
        this.itinerary = itinerary;
        this.backpack = new ArrayList<>();
        this.villages = Controller.villages;
        this.isWaiting = false;

        setIcon(Icons.ROBOT);
        setIconSize(16);
        setCommunicationRange(0);
        setSensingRange(0);
    }

    @Override
    public void onStart() {
        super.setSpeed(SPEED);
        startVisitRound();
        System.out.println("Robot n°" + getID() + " my itinerary is : " + itinerary.toString());
    }

    public void startVisitRound(){
        int cpt = 0;
        for (int i = itinerary.getStart();cpt < itinerary.getSize(); i++) {
            cpt++;
            i %= itinerary.getSize();
            addDestination(itinerary.getSteps().get(i));
        }
        time = getTime();
    }

    @Override
    public void onClock() {
        if(isWaiting){
            int nbRobots = Controller.topology.getNodes().stream().filter(n -> n instanceof Robot).collect(Collectors.toList()).size();
            if(cptRobotsAtSpawn % nbRobots == 0){
                isWaiting = false;
                //effectuer echanges de messages
                giveMailToRobots();
                //repartir pour un tour
                startVisitRound();
            }
        } else {
            super.onClock();
        }
    }

    @Override
    public void onArrival() {
        // Le robot est arrivé à sa dernière destination
        if(getLocation().equals(itinerary.getSteps().get(itinerary.getEnd()))){
            System.out.println("Robot n°" + getID() + " : I've made a complete round in " + (getTime() - time));

            if (Controller.selectedAlgo.equals(Controller.VRP)) {
                //Si VRP alors dernier point == spawn donc attente des autres
                if(getLocation().equals(new Point(Robot.SPAWN_POINT_X, Robot.SPAWN_POINT_Y))) {
                    cptRobotsAtSpawn++;
                    isWaiting = true;
                    destinations.clear();
                }
            } else { // L'algo sélectionné n'est pas VRP
                startVisitRound();
            }
        } else { // Le robot n'est pas arrivé à sa dernière destination
            Village current = villages.get(getLocation());

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
    }

    public void collectPostbox(Village village){
        backpack.addAll(village.getPostbox());
        village.clearPostBox();
    }

    public void giveMailToRobots(){
        /**
         * Pour chaque autre robot
         *      on recupere la liste des villages qu'il va voir
         *      pour tout nos messages dans le backpack
         *          on ajoute au robot les messages qui ont comme destination un village de la liste
         *          on retire le message de notre cote
         */

        for(Robot r: Controller.robots){
            //ne pas regarder nous meme
            if(!r.equals(this)){
                List<Village> villagesInIt = new ArrayList<>();
                for(Point p : r.getItinerary().getSteps()){
                   villagesInIt.add(Controller.villages.get(p));
                }

                List<Mail> mailsToMove = new ArrayList<>();
                for(Mail m : backpack){
                    if(villagesInIt.contains(m.receiver)){
                        mailsToMove.add(m);
                    }
                }
                r.backpack.addAll(mailsToMove);
                this.backpack.removeAll(mailsToMove);
            }
        }
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    @Override
    public String toString() {
        return "Robot n°" + super.toString() + ", in my backpack : " + backpack.size() + " messages";
    }
}