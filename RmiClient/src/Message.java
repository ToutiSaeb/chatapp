import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String pseudo;
    private String message;
    private Date time;

    public Message(String pseudo, String message, Date time) {
        this.pseudo = pseudo;
        this.message = message;
        this.time = time;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "pseudo='" + pseudo + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time.toString() +
                '}';
    }
}
