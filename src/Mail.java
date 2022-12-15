import io.jbotsim.core.Node;

public class Mail{
    public Node sender, receiver;
    public String content;

    public Mail(Node sender, Node receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

}
