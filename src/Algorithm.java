import io.jbotsim.core.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Algorithm {

    protected List<Point> points;

    public Algorithm(List<Point> points) {
        this.points = points;
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

    public List<Point> twoApprox() {
        List<Point> itinerary = new ArrayList<>();
        List<Node<Point>> mst = Prim();
        Node<Point> root = mst.get(0);
        boolean[] isVisited = new boolean[points.size()];
        Node<Point> current = root;
        points.remove(root.getData());

        dfsTwoApprox(current, isVisited);

        return itinerary;
    }

    private void dfsTwoApprox(Node<Point> current, boolean[] isVisited) {

    }

    public List<Node<Point>> Prim() {
        // not tested
        List<Node<Point>> mst = new ArrayList<>();
        List<Point> usedPoints = new ArrayList<>();
        Point initial = points.get(0);
        points.remove(initial);
        mst.add(new Node(initial, new Node(initial)));

        while (!points.isEmpty()) {
            double minDist = Double.MAX_VALUE;
            Node<Point> selectedNode = null;
            Point selectedPoint = null;
            for (Node<Point> node : mst) {
                for (Point p : points) {
                    if (node.getData().distance(p) < minDist) {
                        minDist = node.getData().distance(p);
                        selectedNode = node;
                        selectedPoint = p;
                    }
                }
            }
            selectedNode.addChild(selectedPoint);
            points.remove(selectedPoint);
        }

        return mst;
    }

    static public double getItineraryDistance(Itinerary itinerary) {
        List<Point> steps = itinerary.getSteps();
        double distance = 0;
        for (Point p : steps) {
            Point next = steps.get(steps.indexOf(p) % (steps.size() - 1) + 1);
            distance += p.distance(next);
        }
        return distance;
    }
}