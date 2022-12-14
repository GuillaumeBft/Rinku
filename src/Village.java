import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

import java.util.List;

public class Village extends Node {
    String name;
    List<Node> villages;

    public Village(String name, List<Node> villages) {
        this.name = name;
        this.villages = villages;
    }

    @Override
    public void onStart() {
        setIcon(Icons.SENSOR);
        setIconSize(16);
        setCommunicationRange(120);
        setSensingRange(60);
    }

    @Override
    public String toString() {
        return super.toString() + " " + name;
    }
}
