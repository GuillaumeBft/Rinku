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
        new Controller();
        //produceAverageMaxTimesStats();
    }

    public static void produceAverageMaxTimesStats() {
        Controller c;
        System.out.println("number of robots ; noAlgo ; Bruteforce ; RandomInsertion ; NearestNeighbor");
        for (int i = 1; i < 13; i++) {
            System.out.print(i + " ; ");

            c = new Controller(i, "");
            System.out.print(c.averageMaxTime + " ; ");

            c = new Controller(i, Controller.BRUTEFORCE);
            System.out.print(c.averageMaxTime + " ; ");

            c = new Controller(i, Controller.RANDOM_INSERT);
            System.out.print(c.averageMaxTime  + " ; ");

            c = new Controller(i, Controller.NEAREST_NEIGHBOR);
            System.out.println(c.averageMaxTime);
        }
        System.exit(0);
    }

}