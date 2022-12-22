import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ScrabbleModelTest {
    /* Test to see if a players hand receives random new letters after placing their tiles */
    @Test
    public void handGetsNewTilesAfterPlay() throws Exception {
        ScrabbleModel model = new ScrabbleModel();
        model.AIScrabble(2);
        model.getPlayer().getHand().getTiles().clear();
        model.getPlayer().getHand().getTiles().add(new Tile("A"));
        model.getPlayer().getHand().getTiles().add(new Tile("E"));
        model.getPlayer().getHand().getTiles().add(new Tile("T"));
        model.getPlayer().getHand().getTiles().add(new Tile("V"));
        model.getPlayer().getHand().getTiles().add(new Tile("W"));
        model.getPlayer().getHand().getTiles().add(new Tile("T"));
        model.getPlayer().getHand().getTiles().add(new Tile("V"));
        ArrayList<String> letters = new ArrayList<>();
        letters.add("E");
        letters.add("A");
        letters.add("T");

        ArrayList<String> originalHand = new ArrayList<>();
        for(Tile tile: model.getPlayer().getHand().getTiles()){
            originalHand.add(tile.getLetter());
        }
        model.play(letters);
        model.turn = 0;
        ArrayList<String> newHand = new ArrayList<>();
        for(Tile tile: model.getPlayer().getHand().getTiles()){
            newHand.add(tile.getLetter());
        }
        assertNotEquals(originalHand, newHand);
    }

    /* Test to see if player gets right amount of points if one of their tiles land on a premium square */
    @Test
    public void playerGetsRightAmountOfPointsWithPremiumSquares() throws Exception{
        ScrabbleModel model = new ScrabbleModel();
        ScrabbleView view = new ScrabbleView();
        model.AIScrabble(1);
        model.getPlayer().getHand().getTiles().clear();
        model.getPlayer().getHand().getTiles().add(new Tile("T"));
        model.getPlayer().getHand().getTiles().add(new Tile("E"));
        model.getPlayer().getHand().getTiles().add(new Tile("A"));
        model.getPlayer().getHand().getTiles().add(new Tile("H"));
        model.getPlayer().getHand().getTiles().add(new Tile("B"));
        model.getPlayer().getHand().getTiles().add(new Tile("R"));
        model.getPlayer().getHand().getTiles().add(new Tile("V"));
        ArrayList<String> letters = new ArrayList<>();
        letters.add("E");
        letters.add("A");
        letters.add("R");
        letters.add("T");
        letters.add("H");

        ArrayList<Integer> tileCoordinates = new ArrayList<>(Arrays.asList(6, 7, 6, 8, 6, 9, 6, 10, 6, 11));;
        model.placeBoard(6 , 7, new Tile("E"));
        model.placeBoard(6 , 8, new Tile("A"));
        model.placeBoard(6 , 9, new Tile("R"));
        model.placeBoard(6 , 10, new Tile("T"));
        model.placeBoard(6 , 11, new Tile("H"));
        model.checkBoard(tileCoordinates);
        model.CheckConnected(tileCoordinates);
        model.pointAdder(letters, tileCoordinates, view.coordinatesDouble, view.coordinatesLDouble, view.coordinatesTriple, view.coordinatesLTriple, null);
        model.play(letters);
        model.turn = 0;

        assertEquals(9, model.getPlayerPoints());
    }


    /*Test to see if players get the right amount of points when placing tiles to make their word */
    @Test
    public void playerGetsRightAmountOfPoints() throws Exception{
        ScrabbleModel model = new ScrabbleModel();
        ScrabbleView view = new ScrabbleView();
        model.AIScrabble(2);
        model.getPlayer().getHand().getTiles().clear();
        model.getPlayer().getHand().getTiles().add(new Tile("T"));
        model.getPlayer().getHand().getTiles().add(new Tile("E"));
        model.getPlayer().getHand().getTiles().add(new Tile("A"));
        model.getPlayer().getHand().getTiles().add(new Tile("H"));
        model.getPlayer().getHand().getTiles().add(new Tile("B"));
        model.getPlayer().getHand().getTiles().add(new Tile("R"));
        model.getPlayer().getHand().getTiles().add(new Tile("V"));
        ArrayList<String> letters = new ArrayList<>();
        letters.add("E");
        letters.add("A");
        letters.add("R");
        letters.add("T");
        letters.add("H");

        ArrayList<Integer> tileCoordinates = new ArrayList<>(Arrays.asList(7, 7, 7, 8, 7, 9, 7, 10, 7, 11));;
        model.placeBoard(7 , 7, new Tile("E"));
        model.placeBoard(7 , 8, new Tile("A"));
        model.placeBoard(7 , 9, new Tile("R"));
        model.placeBoard(7 , 10, new Tile("T"));
        model.placeBoard(7 , 11, new Tile("H"));
        model.checkBoard(tileCoordinates);
        model.CheckConnected(tileCoordinates);
        model.pointAdder(letters, tileCoordinates, view.coordinatesDouble, view.coordinatesLDouble, view.coordinatesTriple, view.coordinatesLTriple, null);
        model.play(letters);
        model.turn = 0;
        int points = model.getPlayerPoints();

        assertEquals(24, points);
    }

    /* Test to see if the right player starts the game */
    @Test
    public void initialTurnIsHumanPlayer() throws Exception {
        ScrabbleModel model = new ScrabbleModel();
        model.AIScrabble(2);
        assertEquals(0, model.getTurnNum());
    }

    /* Test to see if the game ends when two skips were made consecutively */
    @Test
    public void gameEndsWhenSkippedTwice() throws Exception{
        ScrabbleModel model = new ScrabbleModel();
        model.AIScrabble(2);
        model.hasSkipped();
        model.hasSkipped();
        boolean gameOver = model.isSkipTwice();

        assertTrue(gameOver);

    }

    @Test
    public void undoPlacedTiles() throws Exception{
        ScrabbleModel model = new ScrabbleModel();
        ScrabbleView view = new ScrabbleView();
        model.AIScrabble(2);
        model.getPlayer().getHand().getTiles().clear();
        model.getPlayer().getHand().getTiles().add(new Tile("T"));
        model.getPlayer().getHand().getTiles().add(new Tile("E"));
        model.getPlayer().getHand().getTiles().add(new Tile("A"));
        model.getPlayer().getHand().getTiles().add(new Tile("H"));
        model.getPlayer().getHand().getTiles().add(new Tile("B"));
        model.getPlayer().getHand().getTiles().add(new Tile("R"));
        model.getPlayer().getHand().getTiles().add(new Tile("V"));
        ArrayList<String> letters = new ArrayList<>();
        letters.add("E");
        letters.add("A");
        letters.add("R");
        letters.add("T");
        letters.add("H");

        ArrayList<Integer> tileCoordinates = new ArrayList<>(Arrays.asList(7, 7, 7, 8, 7, 9, 7, 10, 7, 11));;
        model.placeBoard(7 , 7, new Tile("E"));
        model.placeBoard(7 , 8, new Tile("A"));
        model.placeBoard(7 , 9, new Tile("R"));
        model.placeBoard(7 , 10, new Tile("T"));
        model.placeBoard(7 , 11, new Tile("H"));
        model.checkBoard(tileCoordinates);
        model.CheckConnected(tileCoordinates);
        model.pointAdder(letters, tileCoordinates, view.coordinatesDouble, view.coordinatesLDouble, view.coordinatesTriple, view.coordinatesLTriple, null);
        model.turn = 0;

        model.undoPlacedTile();
        int lastUndoElement = model.getUndoLetters().size() - 1;
        Tile tile = model.getUndoLetters().get(lastUndoElement);
        String tileLetter = tile.getLetter();

        assertEquals(4, model.getTileOnBoard().size());
        assertEquals("H", tileLetter);


    }

    @Test
    public void redoPlacedTiles() throws Exception {
        ScrabbleModel model = new ScrabbleModel();
        ScrabbleView view = new ScrabbleView();
        model.AIScrabble(2);
        model.getPlayer().getHand().getTiles().clear();
        model.getPlayer().getHand().getTiles().add(new Tile("T"));
        model.getPlayer().getHand().getTiles().add(new Tile("E"));
        model.getPlayer().getHand().getTiles().add(new Tile("A"));
        model.getPlayer().getHand().getTiles().add(new Tile("H"));
        model.getPlayer().getHand().getTiles().add(new Tile("B"));
        model.getPlayer().getHand().getTiles().add(new Tile("R"));
        model.getPlayer().getHand().getTiles().add(new Tile("V"));
        ArrayList<String> letters = new ArrayList<>();
        letters.add("E");
        letters.add("A");
        letters.add("R");
        letters.add("T");
        letters.add("H");

        ArrayList<Integer> tileCoordinates = new ArrayList<>(Arrays.asList(7, 7, 7, 8, 7, 9, 7, 10, 7, 11));

        model.placeBoard(7, 7, new Tile("E"));
        model.placeBoard(7, 8, new Tile("A"));
        model.placeBoard(7, 9, new Tile("R"));
        model.placeBoard(7, 10, new Tile("T"));
        model.placeBoard(7, 11, new Tile("H"));
        model.checkBoard(tileCoordinates);
        model.CheckConnected(tileCoordinates);
        model.pointAdder(letters, tileCoordinates, view.coordinatesDouble, view.coordinatesLDouble, view.coordinatesTriple, view.coordinatesLTriple, null);
        model.turn = 0;

        model.undoPlacedTile();
        model.redoPlacedTile();
        int lastUndoElement = model.getTileOnBoard().size() - 1;
        Tile tile = model.getTileOnBoard().get(lastUndoElement);
        String tileLetter = tile.getLetter();

        assertEquals(5, model.getTileOnBoard().size());
        assertEquals("H", tileLetter);

    }
}