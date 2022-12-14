import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

public class Village extends Node {
    String name;

    public Village(String name) {
        super();
        this.name = name;
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
