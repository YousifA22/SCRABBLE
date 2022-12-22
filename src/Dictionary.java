import java.net.*;
import java.io.*;
import java.util.*;
/*
 * This class is part of the "Scrabble" application
 *
 * This class contains a dictionary of all the valid words that can be played in the game. A player can only play a word
 * if that word can be found in this dictionary.
 */
public class Dictionary implements Serializable {
    private Map<String,Integer> dictionary;
    /*
    Populates the dictionary based on the MIT word list of approved words. These will be the
    valid words that can be played in the game.
     */
    public Dictionary() throws Exception {
            dictionary = new HashMap<>();
            URL dict = new URL("https://www.mit.edu/~ecprice/wordlist.10000");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(dict.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                dictionary.put(inputLine.toUpperCase(),0);
            in.close();
        }
    public Map<String,Integer> getDictionary(){
        return dictionary;
    }
}
