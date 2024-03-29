import io.jbotsim.core.Point;
import io.jbotsim.core.Node;

import javax.naming.ldap.Control;
import java.util.*;

public class Algorithm {

    List<Point> points;
    List<Node> nodes;
    Map<Integer, Point> locations;

    public Algorithm(List<Node> nodes) {
        points = new ArrayList<>();
        locations = new HashMap<>();
        for (Node n : nodes) {
            points.add(n.getLocation());
            locations.put(n.getID(), n.getLocation());
        }
        this.nodes = nodes;
    }

    public Itinerary noAlgo() {
        return new Itinerary(points, 0);
    }

    public Itinerary nearestNeighbor() {
        List<Point> steps = new ArrayList<>();
        Point curr = points.get(0);
        points.remove(0);
        steps.add(curr);
        double minDist = Double.MAX_VALUE;
        int minIndex = -1;

        while (!points.isEmpty()) {
            for (Point v : points) {
                double dist = curr.distance(v);
                if (dist < minDist) {
                    minDist = dist;
                    minIndex = points.indexOf(v);
                }
            }

            steps.add(points.get(minIndex));
            curr = points.get(minIndex);
            points.remove(minIndex);
            minDist = Double.MAX_VALUE;
        }

        return new Itinerary(steps, 0);
    }

    public Itinerary randomInsertion() {
        // This algorithm needs at least 5 villages to works
        if (points.size() < 5) {
            System.out.println("[WARNING] Random Insertion needs at least 5 villages to works");
            System.out.println("    => Random Insertion is replaced by Bruteforce for this execution");
            return bruteForce();
        }

        List<Point> steps = new ArrayList<>();
        steps.add(points.get(0));
        points.remove(0);
        steps.add(points.get(1));
        points.remove(1);
        steps.add(points.get(2));
        points.remove(2);

        Random rand = new Random();

        while (!points.isEmpty()) {
            Point pointToAdd = points.get(rand.nextInt(points.size()));

            double elongation = Double.MAX_VALUE;
            Point selectedPoint = null;
            for (Point p : steps) {
                Point next = steps.get(steps.indexOf(p) % (steps.size() - 1) + 1);
                double initDistance = p.distance(next);
                double newDistance = p.distance(pointToAdd) + pointToAdd.distance(next);
                if (newDistance - initDistance < elongation) {
                    elongation = newDistance - initDistance;
                    selectedPoint = p;
                }
            }

            int nextIndex = steps.indexOf(selectedPoint) % (steps.size() - 1) + 1;
            steps.add(nextIndex, pointToAdd);
            points.remove(pointToAdd);
        }
        return new Itinerary(steps, 0);
    }

    public Itinerary bruteForce() {
        StringBuilder firstPermutation = new StringBuilder();
        for (Node n : nodes) {
            firstPermutation.append(n.getID());
        }
        List<String> permutations = findAllpermutations(firstPermutation.toString());
        double min = Double.MAX_VALUE;
        List<Point> bestSteps = permutationToSteps(firstPermutation.toString());
        for (String p : permutations) {
            List<Point> steps = permutationToSteps(p);
            double distance = getRoundTotalDistance(steps);
            if (distance < min) {
                min = distance;
                bestSteps = steps;
            }
        }
        return new Itinerary(bestSteps, 0);
    }

    public static List<String> findAllpermutations(String str) {
        List<String> permutations = new ArrayList<>();
        permutation("", str, permutations);
        return permutations;
    }

    private static void permutation(String prefix, String str, List<String> permutations) {
        int n = str.length();
        if (n == 0) permutations.add(prefix);
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n), permutations);
        }
    }

    public List<Point> permutationToSteps(String permutation) {
        List<Point> steps = new ArrayList<>();
        for (char charId : permutation.toCharArray()) {
            int id = Character.getNumericValue(charId);
            steps.add(locations.get(id));
        }
        return steps;
    }

    static public double getRoundTotalDistance(List<Point> steps) {
        double distance = 0;
        int size = steps.size();
        for (int i = 0; i < size; i++) {
            Point next = steps.get((i + 1) % size);
            distance += steps.get(i).distance(next);
        }
        return distance;
    }

    static public double getItinerariesTotalDistance(List<Itinerary> itineraries) {
        double distance = 0;
        for (Itinerary i : itineraries) {
            distance += getRoundTotalDistance(i.getSteps());
        }
        return distance;
    }

    static public double getDistanceThroughPath(List<Point> steps, Village start, Village end) {
        double distance = 0;
        int size = steps.size();
        int startIndex = steps.indexOf(start.getLocation());
        int endIndex = steps.indexOf(end.getLocation());
        int i = startIndex;
        Point next = steps.get((i + 1) % size);
        do {
            distance += steps.get(i).distance(next);
            i = (i + 1) % size;
            next = steps.get((i + 1) % size);
        } while (i != endIndex);

        return distance;
    }

    static public Itinerary itineraryAfterShiftingDistanceThroughPath(List<Point> steps, Point start, double distance) {
        boolean noNeedShift = distance == 0;
        int size = steps.size();
        Point curr = null;
        Point next = null;
        int startIndexVillage = -1;
        for (int i = steps.indexOf(start); distance >= 0; i++) {
            curr = steps.get(i);
            next = steps.get((i + 1) % size);
            distance -= curr.distance(next);
            startIndexVillage = (i + 1) % size;
        }

        if (noNeedShift) { return new Itinerary(steps, startIndexVillage); }

        double segmentDistance = curr.distance(next);
        double distanceThroughSegment = segmentDistance + distance;
        double ratio = distanceThroughSegment / segmentDistance;
        Point spawn = new Point((1 - ratio) * curr.getX() + ratio * next.getX(),
                (1 - ratio) * curr.getY() + ratio * next.getY());

        return new Itinerary(steps, startIndexVillage, spawn);
    }


    public List<Itinerary> VRP(int nbRobots) {
        List<Itinerary> itineraries = new ArrayList<>();
        int nbPointsForRobot = points.size() / nbRobots;
        int resMod = points.size() % nbRobots;

        Point spawnRobot = new Point(Robot.SPAWN_POINT_X, Robot.SPAWN_POINT_Y);

        //Just distribution of points in order
        //Itinerary itGeneral = new Itinerary(points, 0);
        //Using a real algo
        Itinerary itGeneral = nearestNeighbor();

        List<Point> stepsItGeneral = itGeneral.getSteps();
        List<Point> itRobotSteps;

        for(int i = 0; i < nbRobots; i++){
            if(resMod != 0){
                itRobotSteps = new ArrayList<>(stepsItGeneral.subList(0, nbPointsForRobot + 1));
                resMod--;
            } else {
                itRobotSteps = new ArrayList<>(stepsItGeneral.subList(0, nbPointsForRobot));
            }
            Itinerary itRobot = new Itinerary(itRobotSteps, 0);
            itRobot.addStep(spawnRobot);
            itineraries.add(itRobot);

            stepsItGeneral.removeAll(itRobotSteps);
        }

        return itineraries;
    }
}