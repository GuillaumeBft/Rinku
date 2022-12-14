import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(WaypointNode.class);
        tp.setTimeUnit(100);

        tp.addNode(20, 200, new Village("Signy-Le-Petit"));
        tp.addNode(100, 200, new Village("Cussac"));
        tp.addNode(200, 70, new Village("Croissy"));
        tp.addNode(250, 250, new Village("Pessac"));

        tp.addNode(100, 100, new Robot());
        tp.addNode(100, 100, new Robot());

        new JViewer(tp);
        tp.start();
    }
}