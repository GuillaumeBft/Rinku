import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

import java.util.ArrayList;
import java.util.List;

public class Village extends Node {
    String name;
    List<Node> villages;
    List<Message> mailbox;

    public Village(String name, List<Node> villages) {
        this.name = name;
        this.villages = villages;
        mailbox = new ArrayList<>();
    }

    @Override
    public void onStart() {
        this.setIcon(System.getProperty("user.dir") + "/hutte.png");
        setIconSize(16);
        setCommunicationRange(120);
        setSensingRange(60);
    }

    @Override
    public void onClock() {
        if (Math.random() < 0.005) {
            newMessage();
        }
    }

    public void newMessage(){
        mailbox.add(new Message("cc c nou"));
    }

    @Override
    public String toString() {
        return super.toString() + " " + name + ", msg : " + mailbox.size();
    }
}
