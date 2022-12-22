import java.io.Serializable;
import java.util.*;

public class ScrabbleModel implements Serializable {

    public static final int SIZE = 15;
    private List<String> wordsCreated = new ArrayList<>();
    private Dictionary dictionary;
    private int count;
    private Tile[][] board;
    private int skipped;
    private Map<String, Integer> numbers = new HashMap<>();
    private List<String> allWords = new ArrayList<>();
    private String right = "";
    private String down = "";
    private boolean firstTurn;
    private List<ScrabbleView> views;
    private List<Integer> SpecialTiles;
    public int turn;
    private int skipCount;
    private Player player1;
    private Player player2;
    private List<Player> activePlayers;
    private List<AI_Player> AIPlayers;//to group the human player(s) and the ai_player(s)
    private List<Tile> allTilesOnBoard;
    private List<Tile> undoLetters;
    public boolean playerTurn;
    private Bag bag;

    public ScrabbleModel() throws Exception{
        AIPlayers=new ArrayList<>();
        turn =0;
        count = 0;
        SpecialTiles=new ArrayList<>();
        this.board = new Tile[SIZE][SIZE];
        for (char alpha = 'A'; alpha <= 'Z'; alpha++) {
            numbers.put(String.valueOf(alpha), count);
            count++;
        }
        bag=new Bag();
        player1 = new Player("Player1",bag);
        player2 = new Player("Player2",bag);
        playerTurn = true;
        firstTurn = true;
        dictionary = new Dictionary();
        views = new ArrayList<>();
        createBoard();
        skipped=0;
        allTilesOnBoard = new ArrayList<>();
        undoLetters = new ArrayList<>();
        //For later
        turn = 0;
        activePlayers = new ArrayList<>(); //this will allow us to affect all the players in the game (ex. changeturns)

    }

    public boolean play(ArrayList<String> letters) throws Exception {
        if (turn==0) {
            for (int i = 0; i < letters.size(); i++) {
                for (Tile t : player1.getHand().getTiles()) {
                    if (t.getLetter().equals(letters.get(i))) {
                        player1.placeTile(t);
                        break;
                    }
                }
            }
            activePlayers.get(turn).getHand().readdTile();

        } else {
            AI_Player ai_player = aiTurn();
            if (ai_player.play()) {
                ArrayList<String> letter = AIWord();
                for (int i = 0; i < letter.size(); i++) {
                    for (Tile t : ai_player.getHand().getTiles()) {

                        if (t.getLetter().equals(letter.get(i))) {
                            ai_player.placeTile(t);
                            break;
                        }
                    }
                }
                ai_player.getHand().readdTile();
                firstTurn=false;
                return true;
            }
            else {
                System.out.println(ai_player.getName()+" Skips Turn");
                return false;
            }
        }
        firstTurn=false;
        return true;

    }

    public void AIScrabble(int i) throws Exception {
        activePlayers.add(player1);
        this.addAIPlayer(i);

    }

    public int getNumPlayers(){
        return activePlayers.size();
    }


    public ArrayList<Player> getPlayers(){
        return (ArrayList<Player>) activePlayers;

    }

    public Player getPlayer(){
        return activePlayers.get(turn);
    }

    /* Keeps track of the tiles on board*/
    public void undoPlacedTile(){
        undoLetters.add(allTilesOnBoard.get(allTilesOnBoard.size() - 1));
        allTilesOnBoard.remove(allTilesOnBoard.size() - 1);

    }

    public List<Tile> getUndoLetters(){
        return undoLetters;
    }

    public void redoPlacedTile(){
        Tile oldTile = undoLetters.get(undoLetters.size() - 1);
        undoLetters.remove(undoLetters.size() - 1);
        allTilesOnBoard.add(oldTile);

    }

    public List<Tile> getTileOnBoard(){
        return allTilesOnBoard;
    }


    //this should be called at the start of the game where someone inputs the amount of ai players they want to play against
    //not final
    public void addAIPlayer(int i) throws Exception {
        if(i <= 3){
            for(int num_ai = 0; num_ai < i; num_ai++){
                String AI_name = "AI Player " + (num_ai + 1);
                AI_Player a=new AI_Player(AI_name, bag,this);
                activePlayers.add(a);
                AIPlayers.add(a);
            }
        }
    }

    public int getPlayerPoints(){
        return activePlayers.get(turn).getPoints();
    }

    public boolean isFinished() {
        //One player has played every tile on their rack, and no tiles remain in the bag (regardless of the tiles on the opponent's rack)
        return bag.getSizeOfBag() == 0 && (skipped > 1);
    }

    public int getTurnNum(){
        return turn;
    }

    public Hand getPlayerHand() {
        return activePlayers.get(turn).getHand();
    }


    public void addScrabbleView(ScrabbleView view){
        views.add(view);
    }


    public void placeBoard(int col,int row,Tile tile){
        board[col][row] = tile;
        for(ScrabbleView v: views){
            v.update(new ScrabbleEvent(this, tile.getLetter(), col, row), ""); //notifies view and updates it
        }
        allTilesOnBoard.add(tile);
    }
    public void removeBoard(int col,int row){
        board[col][row] = new Tile("_");
        for(ScrabbleView v: views){
            v.update(new ScrabbleEvent(this, board[col][row].getLetter(), col, row), ""); //notifies view and updates it
        }
    }

    public void clearWords(){
        wordsCreated.clear();
    }

    public void removeWord(ArrayList<Integer> ints){ // removes any tiles in the Arraylist of coordinates
        if(ints.size()>0) {
            for (int i = 0; i <= ints.size() / 2; i++) {

                removeBoard(ints.get(i), ints.get(i + 1));
                i++;
            }
        }
        clearWords();
    }

    public void changeTurn(){
        turn++;
        if(turn>AIPlayers.size()){
            turn =0;
        }
    }

    public AI_Player aiTurn(){
        if(turn>0){
            for(int i=0;i<=turn;i++){
                if(i==turn){
                    return AIPlayers.get(i-1);
                }
            }
        }
        return null;
    }

    public void createBoard()  {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new Tile("_");
            }
        }
    }

    public boolean isValidPlace() { // checks if the middle is played
        if (firstTurn) {
            if (board[7][7].getLetter().equals("_")) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Tile> StrToTile(String s) {
        ArrayList<Tile> T = new ArrayList<>();
        String[] arr = s.split("");
        for (String s1 : arr) {

            T.add(new Tile(s1));
        }
        return T;
    }

    public boolean down(int y, int x) {  //adds all the letters below a letter on the board

        if (y <= 14 && !board[y][x].getLetter().equals("_")) {
            down += board[y][x].getLetter();
            down(y + 1, x);

            return true;
        }
        return false;
    }

    public String up(int y, int x) {
        String s = "";
        if (y >= 0 && !board[y][x].getLetter().equals("_")) {//adds all the letters above a letter on the board
            s += up(y - 1, x);
            s += board[y][x].getLetter();
            return s;
        }
        return "";
    }

    public boolean right(int y, int x) {

        if (x < 14 && !board[y][x].getLetter().equals("_")) {  //adds all the letters to the right of a letter on the board
            right += board[y][x].getLetter();
            right(y, x + 1);
            return true;
        }
        return false;
    }

    public String left(int y, int x) {
        String s = "";
        if (x > 0 && !board[y][x].getLetter().equals("_")) {   //adds all the letters to the left of a letter on the board
            s += left(y, x - 1);
            s += board[y][x].getLetter();
            return s;

        }
        return "";
    }


    public boolean checkBoard(ArrayList<Integer> coord) {
        right = "";
        down="";
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!board[i][j].getLetter().equals("_")) {

                    String up = up(i - 1, j);
                    String left = left(i, j - 1);
                    if (up != "") {

                        if (down(i, j)) {
                            up += down;
                            if (!wordsCreated.contains(up) && up.length() > 1) {
                                wordsCreated.add(up);
                            }

                            down = "";


                        } else {
                            up += board[i][j].getLetter();
                            if (!wordsCreated.contains(up)  && up.length() > 1) {
                                wordsCreated.add(up);
                            }
                        }
                    } else if (up == "") {
                        if (down(i, j)) {

                            if (!wordsCreated.contains(down) && down.length() > 1) {
                                wordsCreated.add(down);
                            }

                            down = "";

                        }
                    }
                    if (left != "") {
                        left += board[i][j].getLetter();
                        if (right(i, j + 1)) {
                            left += right;
                            if (!wordsCreated.contains(left)&& left.length() > 1) {
                                wordsCreated.add(left);
                            }

                            right = "";

                        } else {
                            if (!wordsCreated.contains(left)&& left.length() > 1) {
                                wordsCreated.add(left);
                            }

                        }
                    } else if (left == "") {
                        if (right(i, j + 1)) {
                            String r = board[i][j].getLetter();

                            r += right;
                            if (!wordsCreated.contains(r) && r.length() > 1) {
                                wordsCreated.add(r);
                            }


                            right = "";

                        }
                    }
                }
            }
        }
        if(wordsCreated.size()>0){
            return wordCheck() && CheckConnected(coord);
        }

        return false;

    }

    public boolean CheckConnected(ArrayList<Integer> coord){  //checks if the word that is being submitting connects with any other word
        int c=0;
        if(firstTurn == false){
            for(int i=0;i<=coord.size()/2;i++){


                if(coord.get(i)<14 && !board[coord.get(i)-1][coord.get(i+1)].getLetter().equals("_") ){ // checks above
                    c++;
                }
                if(coord.get(i)>0 &&!board[coord.get(i)+1][coord.get(i+1)].getLetter().equals("_")){ // checks below
                    c++;
                }
                if(coord.get(i+1)<14 &&!board[coord.get(i)][coord.get(i+1)+1].getLetter().equals("_")){  //checks right side
                    c++;
                }if(coord.get(i+1)>0 &&!board[coord.get(i)][coord.get(i+1)-1].getLetter().equals("_")){ // checks left side
                    c++;
                }
                i++;

            }

            return c>( coord.size() -2);

        }
        firstTurn=false;

        return true;


    }


    public boolean wordCheck() {  //checks if the word on the board is a valid word
        if (wordsCreated.size() == 0) {
            return false;
        }
        for (String s : wordsCreated) {
            if (!dictionary.getDictionary().containsKey(s)) {
                return false;
            }

        }

        for(int i=0;i<wordsCreated.size();i++){
            if(wordsCreated.size()>1 && allWords.contains(wordsCreated.get(i))){
                wordsCreated.remove(wordsCreated.get(i));
                i--;
            }
            else if(!allWords.contains(wordsCreated.get(i))){
                allWords.add(wordsCreated.get(i));
            }
        }
        return true;
    }





    public void pointAdder(ArrayList<String > words,ArrayList<Integer> coord, List<Coordinates> Coord2, List<Coordinates> Coord2L, List<Coordinates> Coord3, List<Coordinates> Coord3L,String blankLetter) { // adds the points to the respective player
        int points = 0;
        boolean multiplied=false;
        ArrayList<Tile> tiles = new ArrayList<>();
        ArrayList<Coordinates> cTile=new ArrayList<>();

        for(int i=0;i<coord.size();i++){
            Coordinates c=new Coordinates(coord.get(i+1),coord.get(i));
            cTile.add(c);
            i++;
        }
        for (String tile : wordsCreated) {
            tiles.addAll(StrToTile(tile));
        }

        if(tiles.size()!=words.size()){
            for(int i=0;i<tiles.size();i++){
                for(int j=0;j<words.size();j++){
                    if(!tiles.get(i).getLetter().equals(words.get(j))){
                        points+=tiles.get(i).getValue(); //to add the points of the tiles that were already on the board
                    }

                }
            }
            tiles.clear();


            for (String tile : words) {
                tiles.addAll(StrToTile(tile));
            }

        }






        for (int k=0;k<tiles.size();k++) {
            multiplied=false;
            if(blankLetter!=null){
                if(!tiles.get(k).getLetter().equals(blankLetter.toUpperCase())){

                        for(int j=0;j<Coord2L.size();j++){
                            if(Coord2L.get(j).getX() == cTile.get(k).getX() &&Coord2L.get(j).getY() == cTile.get(k).getY()){
                                points += (tiles.get(k).getValue()*2);
                                multiplied=true;

                                break;
                            }
                        }





                    for(int j=0;j<Coord3L.size();j++){
                            if(Coord3L.get(j).getX() == cTile.get(k).getX() &&Coord3L.get(j).getY() == cTile.get(k).getY() && !multiplied){
                                points += (tiles.get(k).getValue()*2);
                                multiplied=true;
                                break;
                            }
                        }
                    if(!multiplied){
                        points+=tiles.get(k).getValue();

                    }




                }
            }
            else{

                for(int j=0;j<Coord2L.size();j++){
                    if(Coord2L.get(j).getX() == cTile.get(k).getX() &&Coord2L.get(j).getY() == cTile.get(k).getY()){
                        points += (tiles.get(k).getValue()*2);
                        multiplied=true;

                        break;
                    }
                }





                for(int j=0;j<Coord3L.size();j++){
                    if(Coord3L.get(j).getX() == cTile.get(k).getX() &&Coord3L.get(j).getY() == cTile.get(k).getY() && !multiplied){
                        points += (tiles.get(k).getValue()*2);
                        multiplied=true;
                        break;
                    }
                }
                if(!multiplied){
                    points+=tiles.get(k).getValue();

                }

            }

        }

        for (int i=0;i< Coord2.size();i++) {
            for(int j=0;j<cTile.size();j++){
                if(Coord2.get(i).getX() == cTile.get(j).getX() &&Coord2.get(i).getY() == cTile.get(j).getY()){
                    points*=2;
                }
            }
        }

        for (int i=0;i< Coord3.size();i++) {
            for(int j=0;j<cTile.size();j++){
                if(Coord3.get(i).getX() == cTile.get(j).getX() &&Coord3.get(i).getY() == cTile.get(j).getY()){
                    points*=3;
                }
            }
        }
        activePlayers.get(turn).addPoints(points);
        clearWords();
    }

    public Tile[][] getBoard(){
        return board;
    }

    public  ArrayList<String> allWordsCreated(){
        return (ArrayList<String>) allWords;
    }

    public boolean getTurn(){
        if(firstTurn==true) {
            return true;
        }
        return false;
    }

    public void setTurn(){
        if(firstTurn==false){
            firstTurn = true;
        }
    }

    public ArrayList<String> AIWord(){

        ArrayList<String> wordMade = new ArrayList<>();

        AI_Player ai_player =aiTurn();
        if(ai_player!=null){

            String[] Arr = ai_player.getWord().split("");
            for (String s : Arr) {
                wordMade.add(s);
            }
            return wordMade;
        }

        return null;
    }

    public ArrayList<Integer> AICoord(){

        AI_Player ai_player =aiTurn();
        return ai_player.getCord();
    }

    public void clear(){
        if(activePlayers.get(turn) instanceof AI_Player){
            AI_Player ai_player = (AI_Player) activePlayers.get(turn);
            ai_player.clear();
        }
    }

    public int AIAmount(){

        return AIPlayers.size();
    }
    public void hasSkipped(){
        skipCount++;
    }

    public void setBoard(Tile[][] b){
        board = b;
    }

    public void setHand(Hand h){
        player1.setHand(h);
    }

    public boolean isSkipTwice(){
        if(skipCount == 2){
            return true;
        }
        return false;
    }

    public void draw(){

        player1.draw();

    }
}