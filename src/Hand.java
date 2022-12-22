import java.io.Serializable;
import java.util.*;
/*
 * This class is part of the "Scrabble" application
 *
 * A "Hand" represents the rack that is used to hold the 7 tiles, per player, in the game.
 *
 * This class holds all relevant information concerning a rack and is manipulated based on the actions made by a player.
 */
public class Hand implements Serializable {
    private Bag bag;
    private final Random random = new Random();
    private List<Tile> tilesInHand;
    private int numOfTiles;

    /*
    Creates the hand with 7 random tiles to play with from the bag.
     */
    public Hand(Bag bag){
        tilesInHand = new ArrayList<>();
        this.bag = new Bag();
        numOfTiles = 7;
        for(int i = 0; i<7; i++) {
            int x = random.nextInt(bag.getSizeOfBag());
            tilesInHand.add(bag.getBag().get(x));
            bag.removeTile(bag.getBag().get(x));
        }
    }


    /*
    Removes tiles from the hand.
     */
    public void removeTile(Tile tile) {
        for(int i=0; i<tilesInHand.size();i++){
            Tile x1 = tilesInHand.get(i);
            if (x1.getLetter().equals(tile.getLetter())){
                tilesInHand.remove(i);
                break;
            }
        }
        numOfTiles--;
    }

    public void readdTile() {
        for (int i = 0; i < 7; i++) {
            if (tilesInHand.size() < 7) {
                int x = random.nextInt(bag.getSizeOfBag());
                tilesInHand.add(bag.getBag().get(x));
                bag.removeTile(bag.getBag().get(x));
                if(numOfTiles<7){
                    numOfTiles++;
                }

            }
        }
    }

    /*
    Returns all the tiles in the current hand.
     */
    public List<Tile> getTiles(){
            return tilesInHand;
    }

    /*
    Returns the number of tiles left in the current hand.
     */
    public int tilesLeft(){
            return numOfTiles;
    }
}
