import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.ArrayList;
import java.util.List;

public class Village extends Node {
    String name;
    List<Node> villages;
    List<Message> postbox;

    public Village(String name, List<Node> villages) {
        this.name = name;
        this.villages = villages;
        postbox = new ArrayList<>();
    }

    @Override
    public void onStart() {
        this.setIcon(System.getProperty("user.dir") + "/hutte.png");
        setIconSize(16);
        setCommunicationRange(0);
        setSensingRange(0);
    }

    @Override
    public void onClock() {
        if (Math.random() < 0.005) {
            newMessage();
        }
    }

    private void newMessage(){
        postbox.add(new Message("cc c nou"));
    }

    public void clearPostBox(){
        postbox.clear();
    }

    public List<Message> getPostbox() {
        return postbox;
    }

    @Override
    public String toString() {
        return super.toString() + " " + name + ", msg : " + postbox.size();
    }
}
