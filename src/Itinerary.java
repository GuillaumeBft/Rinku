import io.jbotsim.core.Node;

import java.util.List;

public class Itinerary {
    private List<Node> steps;
    private int start;

    public Itinerary(List<Node> steps, int start) {
        this.steps = steps;
        this.start = start;
    }

    public List<Node> getSteps() {
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

    @Override
    public String toString() {
        return getSteps().toString();
    }
}
