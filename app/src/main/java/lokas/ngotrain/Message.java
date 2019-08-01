package lokas.ngotrain;

/**
 * Created by Bala on 28-09-2016.
 */
public class Message {
    private String id;
    private String message;

    Message(String id,String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return  message;
    }
}
