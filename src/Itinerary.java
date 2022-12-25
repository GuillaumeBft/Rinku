import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.List;

public class Itinerary {
    List<Point> steps;
    int start;
    Point spawn;

    public Itinerary(List<Point> steps, int start) {
        this(steps, start, steps.get(start));
    }

    public Itinerary(List<Point> steps, int start, Point spawn) {
        this.steps = steps;
        this.start = start;
        this.spawn = spawn;
    }

    public List<Point> getSteps() {
        return steps;
    }

    public int getStart() {
        return start;
    }

    public Point getSpawn() {
        return spawn;
    }

    public int getEnd() {
        return (start == 0) ? getSize()-1 : start-1;
    }

    public int getSize() {
        return steps.size();
    }

    public void addStep(Point step){
        steps.add(step);
    }

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append('[');
        for (Point p : steps) {
            strBuild.append("(x= " + p.getX() + ", y= " + p.getY() + "), ");
        }
        strBuild.delete(strBuild.length()-2, strBuild.length());
        strBuild.append(" | START at = " + start + " ]");
        return strBuild.toString();
    }
}
