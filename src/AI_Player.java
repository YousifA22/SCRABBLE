import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AI_Player extends Player implements Serializable {
    private ScrabbleModel m;
    private wordGenerator wordGen;
    Tile[][] board;
    String wordMade;
    ArrayList<Integer> coord= new ArrayList<>();
    private ArrayList<Coordinates> cord = new ArrayList<>();
    List<String> pwords1= new ArrayList<>();
    List<String> pwords= new ArrayList<>();
    public AI_Player(String name, Bag bag, ScrabbleModel model){
        super(name, bag);
        m=model;
        wordMade=" ";
    }
    private Boolean centerCheck() throws Exception {
            if(board[7][7].getLetter().equals("_")) {
                int y = 7;
                int x = 7;
                wordGen = new wordGenerator(super.getHand(),null);
                String[] ARR = wordGen.getPossibleWords().get(wordGen.getPossibleWords().size() - 1).split("");
                for (String c : ARR) {
                    wordMade+=c;
                    coord.add(y);
                    coord.add(x);
                    m.placeBoard(y, x, new Tile(c));
                    x++;
                }
                m.checkBoard(coord);
                return true;
            }
            return false;
    }
    public boolean mDown1(List<String> pLetters,int y, int x, String wordn,int i) throws Exception {
        if(pwords1.size()==0){
            wordGen = new wordGenerator(super.getHand(),wordn);
            pwords1=wordGen.getWordsStartingWith(wordn);
        }
        if(pwords1.size() >0){
            for (String s : pwords1) {
                int q=0;
                String[] Arr = s.split("");

                for (String c : Arr) {
                    if(q>= Arr.length-1){
                        pLetters.add(c);
                        wordMade+=c;
                    }q++;
                }
                for (int p = 0; p < pLetters.size(); p++) {
                    y++;
                    if (15 > y &&board[y][x].getLetter().equals("_")) {
                        coord.add(y);
                        coord.add(x);
                        m.placeBoard(y, x, new Tile(pLetters.get(p)));
                        if (p == pLetters.size() - 1) {
                            if (m.checkBoard(coord)) {
                                pLetters.clear();
                                return true;
                            } else {
                                m.removeWord( coord);
                                clear();
                                pLetters.clear();
                                y = i;


                                break;
                            }
                        }
                    } else if (15 <= y || !board[y][x].getLetter().equals("_")) {
                        m.removeWord(coord);
                        clear();
                        pLetters.clear();

                        y = i;
                        break;
                    }
                }
            }
        }
        return false;
    }

    private boolean mRight1(List<String> pLetters,int y, int x,String left,int j) throws Exception{
        if(pwords.size()==0){
            wordGen = new wordGenerator(super.getHand(),left);
            pwords=wordGen.getWordsStartingWith(left);
        }
        if(pwords.size()>0 ){
            for (String s : pwords) {
                int q=0;
                String[] Arr = s.split("");

                for (String c : Arr) {
                    if(q>= Arr.length-1){
                        pLetters.add(c);
                        wordMade+=c;
                    }q++;
                }
                for (int p = 0; p < pLetters.size(); p++) {
                    x++;
                    if (15 > x && board[y][x].getLetter().equals("_")) {
                        coord.add(y);
                        coord.add(x);
                        m.placeBoard(y, x, new Tile(pLetters.get(p)));
                        if (p == pLetters.size() - 1) {
                            if (m.checkBoard(coord)) {
                                pLetters.clear();

                                return true;
                            } else {
                                m.removeWord(coord);
                                coord.clear();
                                cord.clear();
                                pLetters.clear();
                                x = j;

                                wordMade = "";
                                break;
                            }
                        }
                    } else if (15 <= x || !board[y][x].getLetter().equals("_")) {
                        m.removeWord(coord);
                        clear();
                        pLetters.clear();

                        x = j;
                        break;
                    }
                }
            }
        }
        return false;
    }

    public boolean play() throws Exception {
        clear();
        board= m.getBoard();
        List<String> pLetters=new ArrayList<>();
        if(centerCheck()){
            return true;
        }
        for(int i=0;i<m.SIZE;i++){
            for(int j=0;j<m.SIZE;j++){
                clear();
                pwords1.clear();
                pwords.clear();
                int y=i;
                int x=j;
                if (!board[i][j].getLetter().equals("_")){
                    String up=m.up(i-1,j);
                    String left= m.left(i,j-1)+board[i][j].getLetter();

                    String wordn = up+board[i][j].getLetter();
                    if(up.equals("")) {
                        clear();
                        if(!m.down(i+1,j)){
                            if(mDown1(pLetters, y,  x,  wordn, i)){
                                return true;
                            }
                        }
                        clear();
                        if(!m.right(i, j+1)) {
                            if(mRight1( pLetters, y,  x, left, j)){
                                return true;
                            }
                        }
                    }
                    else{
                        clear();
                        if(!m.right(i, j+1)) {
                            if(mRight1( pLetters, y,  x, left, j)){
                                return true;
                            }
                        }
                        clear();
                        if(!m.down(i+1,j)){
                            if(mDown1( pLetters, y,  x,  wordn,i)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        clear();
        return false;
    }
    public String getWord(){
        return wordMade;
    }

    public ArrayList<Integer> getCord(){
        return coord;
    }

    public void clear(){
        wordMade="";
        coord.clear();
    }
}
