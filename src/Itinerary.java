import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.List;

public class Itinerary {
    List<Point> steps;
    int start;

    public Itinerary(List<Point> steps, int start) {
        this.steps = steps;
        this.start = start;
    }

    public List<Point> getSteps() {
        return steps;
    }

    public int getStart() {
        return start;
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
