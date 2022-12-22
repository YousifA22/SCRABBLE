import java.io.Serializable;

public class ScrabbleModelData implements Serializable {
    private Tile[][] m;
    private Hand h;

    public ScrabbleModelData(Tile[][] b, Hand h){
        this.m = b;
        this.h = h;
    }

    public Hand getHand(){
        return h;
    }
    public Tile[][] getBoard(){
        return m;
    }
}
