/*
 * This class is part of the "Scrabble" application
 *
 * A "Player" represents one of the players in the game.
 *
 * This class holds all information concerning a player. It is used to manipulate a players action and store relevant
 * information unique to a player as the game advances.
 */

import java.io.Serializable;
import java.util.*;
import static java.util.Collections.sort;

public class Player implements Serializable {
    private Hand hand;
    private String name;
    private int points;
    private Bag bag;

    /*
    Creates a player with a name and hand to hold tiles. Also starts the player with 0 points for the game.
     */
    public Player(String name,Bag bag){
        hand = new Hand(bag);
        this.name = name;
        points = 0;
        this.bag=bag;
    }

    /*
    Returns the players current hand of tiles.
     */
    public Hand getHand() {
        return hand;
    }

    /*
    Returns the players name.
     */
    public String getName() {
        return name;
    }

    /*
    Returns the players points in the current game.
     */
    public int getPoints() {
        return points;
    }

    /*
    Player places a tile and that tile is removed from the players hand.
     */
    public void placeTile(Tile tile) {
        hand.removeTile(tile);
    }

    /*
    Adds points to a player based on an inputted amount.
     */
    public void addPoints(int amount){
            points+=amount;
    }

    /*
    Prints the players name, current points and the tiles in there hand that they are holding.
     */
    public String toString(){
        return ("Player: "+ this.getName() + "\n Current points : " + this.getPoints());

    }

    /*
    Finds the winner based on earned points and returns that winner.
     */
    public String getWinner(Player p) {
        //Calculating total points including the reduction of their total points by the sum of their unused letters
        int player1Total = this.getPoints();
        int player2Total = p.getPoints();
        int extraPoints = 0;

        for(Tile tile : this.getHand().getTiles()){
            if(player2Total>0)player1Total -= 1 ;
        }
        for(Tile tile : p.getHand().getTiles()){
            if(player2Total>0)player2Total -= 1;
        }
        //if a player has used all of their letters (known as "going out" or "playing out"),
        // the sum of all other players' unused letters is added to that player's score.
        if(this.getHand().tilesLeft() == 0){
            for(Tile tile : p.getHand().getTiles()){
                extraPoints += tile.getValue() ;
            }
            player1Total += extraPoints;
            extraPoints=0;
        }else if(p.getHand().tilesLeft() == 0){
            for(Tile tile : this.getHand().getTiles()){
                extraPoints += tile.getValue() ;
            }
            player2Total += extraPoints;
        }
        if(player1Total > player2Total){
            return this.getName();
        }
        else if(player1Total == player2Total) {
            return "";
        }
        return p.getName();

    }
    public void setHand(Hand h){
        hand = h;
    }

    public void draw(){
        int size=hand.tilesLeft();
        for(int i=0;i<size;i++){
            this.placeTile(hand.getTiles().get(0));
        }
        hand.readdTile();
    }
}
