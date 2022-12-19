import io.jbotsim.core.Point;
import io.jbotsim.core.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Algorithm {

    protected List<Point> points;
    protected List<Node> nodes;

    public Algorithm(List<Node> nodes) {
        List<Point> points = new ArrayList<>();
        for (Node n : nodes) {
            points.add(n.getLocation());
        }
        this.points = points;
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
        List<List<Node>> permutations = findAllpermutations(nodes);
        System.out.println(permutations);
        return null;
    }

    public static List<List<Node>> findAllpermutations(List<Node> str) {
        List<List<Node>> permutations = new ArrayList<>();
        permutation(new ArrayList<Node>(), str, permutations);
        return permutations;
    }

    private static void permutation(List<Node> prefix, List<Node> str, List<List<Node>> permutations) {
        int n = str.size();
        if (n == 0) permutations.add(prefix);
        else {
            for (int i = 0; i < n; i++) {
                List<Node> newPrefix = new ArrayList<>(prefix);
                prefix.add(str.get(i));
                List<Node> newStr = new ArrayList<>(str.subList(0, i));
                newStr.addAll(str.subList(i+1, n));
                permutation(newPrefix, newStr, permutations);
            }
        }
    }

    static public double getDistance(List<Point> steps) {
        double distance = 0;
        for (Point p : steps) {
            Point next = steps.get(steps.indexOf(p) % (steps.size() - 1) + 1);
            distance += p.distance(next);
        }
        return distance;
    }
}