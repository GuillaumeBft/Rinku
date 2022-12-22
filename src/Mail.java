import io.jbotsim.core.Node;

public class Mail{
    public Village sender, receiver;
    public String content;

    public Mail(Village sender, Village receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

}
