import java.io.Serializable;

public class Coordinates implements Serializable {
    private int x;
    private String type;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType(){
        return type;
    }

    private int y;
    public Coordinates(int x,int y) {
        this.y=y;
        this.x = x;
    }

    public Coordinates(int x, int y, String type){
        this.y=y;
        this.x = x;
        this.type = type;
    }
}