import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;
/*
 * This class is part of the "Scrabble" application
 *
 * A "Tile" represents the tile that is used in the game.
 *
 * This class holds all relevant information concerning a tile, including the letter and its value.
 */
public class Tile implements Serializable {

    private Map<String, Integer> tiles = new HashMap<>();
    private final String letter;
    private final int value;

    /*
    Creates a tile with its associated value.
     */
    public Tile(String letter){
        getTile();
        this.letter=letter;
        this.value = tiles.get(letter);
    }

    /*
    Returns the letter of a tile.
     */
    public String getLetter(){
        return letter;
    }

    /*
    Returns the values of a tile.
     */
    public int getValue(){
        return value;
    }

    public void getTile() {
        tiles.put("_",0);
        Stream.of("*").forEach(ti -> tiles.put(ti, 1));
        Stream.of("A", "E", "I", "L", "N", "O", "R", "S", "T", "U").forEach(ti -> tiles.put(ti, 1));
        Stream.of("D", "G").forEach(ti -> tiles.put(ti, 2));
        Stream.of("B", "C", "M", "P").forEach(ti -> tiles.put(ti, 3));
        Stream.of("F", "H", "V", "W", "Y").forEach(ti -> tiles.put(ti, 4));
        Stream.of("K").forEach(ti -> tiles.put(ti, 5));
        Stream.of("J", "X").forEach(ti -> tiles.put(ti, 8));
        Stream.of("Q", "Z").forEach(ti -> tiles.put(ti, 10));
}
}
