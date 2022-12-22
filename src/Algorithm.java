import io.jbotsim.core.Point;
import io.jbotsim.core.Node;

import java.util.*;

public class Algorithm {

    protected List<Point> points;
    protected List<Node> nodes;
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
        // Il faut au moins 5 points pour utiliser cet algo
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

    static public double getDistanceBetweenTwoFurthestRobots(List<Point> steps, List<Robot> robots) {
        if (robots.size() == 1) {
            return getRoundTotalDistance(steps);
        }
        int size = robots.size();
        double max = 0;
        double currDistance;
        for (int i = 0; i < size; i++) {
            Robot nextRobot = robots.get((i + 1) % size);
            Village currStart = (Village) Controller.topology.getNodes().get(robots.get(i).getItinerary().getStart());
            Village nextStart = (Village) Controller.topology.getNodes().get(nextRobot.getItinerary().getStart());
            currDistance = getDistanceThroughPath(steps, currStart, nextStart);
            if (currDistance > max) {
                max = currDistance;
            }
        }
        return max;
    }


    public List<Itinerary> VRP(int nbRobots) {
        List<Itinerary> itineraries = new ArrayList<>();
        int nbPointsForRobot = points.size() / nbRobots; //+1 pour un robot s'il y a un reste
        Point spawnRobot = new Point(Robot.SPAWN_POINT_X, Robot.SPAWN_POINT_Y);

        //V1 juste repartition des points dans l'ordre
        Itinerary itGeneral = new Itinerary(points, 0);
        for(int i = 0; i < nbRobots; i++){
            List<Point> itRobotSteps = new ArrayList<>(itGeneral.getSteps()
                    .subList(i * nbPointsForRobot, i * nbPointsForRobot + nbPointsForRobot));
            Itinerary itRobot = new Itinerary(itRobotSteps, 0);
            itRobot.addStep(spawnRobot);
            itineraries.add(itRobot);
        }


        return itineraries;
    }
}