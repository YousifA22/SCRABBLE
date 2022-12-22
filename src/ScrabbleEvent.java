import java.io.Serializable;
import java.util.EventObject;

public class ScrabbleEvent extends EventObject implements Serializable {

    private String letter;
    private int x;
    private int y;


    public ScrabbleEvent(ScrabbleModel model, String letter, int x, int y){
        super(model);
        this.letter = letter;
        this.x = x;
        this.y = y;
    }

    public String getLetter() {
        return letter;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}


