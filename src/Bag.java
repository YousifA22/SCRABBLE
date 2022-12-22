import java.util.*;
import java.io.*;

/*
 * This class is part of the "Scrabble" application
 *
 * A "Bag" represents the bag that used in the game. It holds a collection of tiles (from the Tile class) to be placed
 * in a players hand. It keeps track of all 98 tiles in the game and its letter distribution, where each letter has
 * a specific amount of tiles.
 */
public class Bag implements Serializable{
    private int sizeOfBag;
    private List<Tile> tilesInBag;

    private final Random random = new Random();
    /*
    Creates a bag of tiles based on a specified number to be played from. This is the bag
    all the players will randomly pull their tiles from.
     */
    public Bag() {
        tilesInBag = new ArrayList<>();
        try {
            FileInputStream myObj = new FileInputStream("letters.txt");
            Scanner scan = new Scanner(myObj);
            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                tilesInBag.add(new Tile(data));
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
                }
        sizeOfBag = tilesInBag.size();
    }

    /*
    Removes a specified tile from the bag.
     */
    public void removeTile(Tile t){

        tilesInBag.remove(t);

        sizeOfBag--;
    }

    /*
    Returns the size of the bag.
     */
    public int getSizeOfBag() {
        return sizeOfBag;
    }

    /*
    Returns the tiles in the bag.
     */
    public List<Tile> getBag(){
        return tilesInBag;
    }

}

