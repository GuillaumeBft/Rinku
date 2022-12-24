import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

public class Mail {
    Village sender, receiver;
    String content;
    int time;

    public Mail(Village sender, Village receiver, String content, int time) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
    }

}
