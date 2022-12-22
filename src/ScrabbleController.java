import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class ScrabbleController extends Component {
    private ScrabbleModel model;
    private ScrabbleView view;
    private String letter = null;
    private ArrayList<String> stuff;
    private List<Tile> readd;
    public ArrayList<Integer> coordinates;

    private Stack<File> stack;
    private Stack<File> stack2;

    private Stack<Integer> savecoord;
    private List<Coordinates> doubleWord;
    private List<Coordinates> doubleLetter;
    private List<Coordinates> tripleWord;
    private List<Coordinates> tripleLetter;
    private ArrayList<String> tPlaced;
    private boolean track_turn;
    private tilePlacer placer;

    private int cou = 0;
    private int c = 0;

    private int q = 0;
    private int drawn;
    private menuListener menu;
    private List<JButton> inactiveButtons;
    private List<JButton> onBoard;
    private String chosen_letter;
    private JButton save_Blank;
    private String s = "_";
    private int time_skip;
    private boolean was_blank = false;
    private List<JButton> perTurn;
    private JButton tileToPlace;
    private List<Player> p1;
    private int skipped;

    private ArrayList<File> Undo;
    private ArrayList<Tile> trac;
    private ArrayList<JButton> trac2;
    private File finalSave;
    private ArrayList<File> Redo;
    private ArrayList<File> RedoSave;
    private ArrayList<File> UndoSave;
    public ScrabbleController(ScrabbleModel model, ScrabbleView view, List<Coordinates> special2Coord,List<Coordinates> special2LCoord,List<Coordinates> special3Coord,List<Coordinates> special3LCoord, List<Player> p) {
        skipped=0;
        drawn=0;
        p1=p;
        stack = new Stack<>();
        stack2 = new Stack<>();
        savecoord = new Stack<>();
        trac = new ArrayList<>();
        trac2 = new ArrayList<>();
        doubleWord=special2Coord;
        doubleLetter=special2LCoord;
        tripleWord=special3Coord;
        tripleLetter=special3LCoord;
        this.model = model;
        this.view = view;
        Undo = new ArrayList<>();
        Redo = new ArrayList<>();
        RedoSave = new ArrayList<>();
        UndoSave = new ArrayList<>();
        onBoard = new ArrayList<>();
        perTurn = new ArrayList<>();
        placer = new tilePlacer();
        menu = new menuListener();
        this.view.addPlaceListener(placer);
        view.addMenuListener(menu);
        readd = new ArrayList<>();
        inactiveButtons =  new ArrayList<>();
        tileToPlace = new JButton();
        coordinates=new ArrayList<>();
        tPlaced=new ArrayList<>();
        save_Blank = new JButton();
        stuff = new ArrayList<>();
    }



    class menuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();
            if (item.getText() == "Save") {
                try {
                    String fileName = null;
                    String d = null;
                    JFileChooser c = new JFileChooser();
                    int x = c.showSaveDialog(ScrabbleController.this);
                    if (x == JFileChooser.APPROVE_OPTION) {
                        fileName = c.getSelectedFile().getName();
                        d = c.getCurrentDirectory().toString();
                    }
                    assert fileName != null;
                    File file = new File(d, fileName);
                    FileOutputStream output = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(output);
                    out.writeObject(model);
                    out.close();
                    output.close();
                    Tile[][] b = model.getBoard();
                    for (int i = 0; i < model.SIZE; i++) {
                        for (int j = 0; j < model.SIZE; j++) {
                            JButton but = new JButton(b[i][j].getLetter());
                            if (!but.getText().equals("_")) {
                                letter = but.getText();
                                ScrabbleEvent event = new ScrabbleEvent(model, letter, i, j);
                                view.update(event, "L");
                            }
                        }
                    }
                    long serialVersionUID = ObjectStreamClass.lookup(model.getClass()).getSerialVersionUID();
                    System.out.println("SAVE SUCCESS!");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (item.getText() == "Load") {
                try {
                    JButton[][] buttons = new JButton[15][15];
                    String fileName = null;
                    String d = null;
                    JFileChooser c = new JFileChooser();
                    int x = c.showOpenDialog(ScrabbleController.this);
                    if (x == JFileChooser.APPROVE_OPTION) {
                        fileName = c.getSelectedFile().getName();
                        d = c.getCurrentDirectory().toString();
                    }
                    view.clearBoard();
                    assert fileName != null;
                    File file = new File(d, fileName);
                    FileInputStream input = new FileInputStream(file);
                    ObjectInputStream in = new ObjectInputStream(input);
                    model = (ScrabbleModel) in.readObject();
                    in.close();
                    input.close();
//                    model.setBoard(newm.getBoard());
//                    Tile[][] test2 = model.getBoard();
                    Tile[][] b = model.getBoard();
                    for (int i = 0; i < model.SIZE; i++) {
                        for (int j = 0; j < model.SIZE; j++) {
                            JButton but = new JButton(b[i][j].getLetter());
                            if (!but.getText().equals("_")) {
                                letter = but.getText();
                                buttons[i][j] = but;
                                ScrabbleEvent event = new ScrabbleEvent(model, letter, i, j);
                                view.update(event, "L");
                            }
                        }
                    }
                    view.setModel(model);
                    view.addPlaceListener_update(placer);
                    System.out.println("LOAD SUCCESS!");

                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
    }

    /* This action listener controls the buttons on the board, each player's hand (tiles on their rack),
    the submit button, and the skip button */
    class tilePlacer implements ActionListener {
        int pressed=0;
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            s = button.getText();
            if(s==null){
                s = "_";
            }
            if(s.equals("*")){
                was_blank = true;
                JFrame f = new JFrame();
                chosen_letter = JOptionPane.showInputDialog(f,"Choose any valid letter (Worth 0 points)");

                if(chosen_letter.matches("[a-zA-Z]+")){
                    button.setText(chosen_letter.toUpperCase());
                }
                save_Blank.setText(chosen_letter.toUpperCase());

            }
            if(s == "Submit" && coordinates.size() > 0) { //We pressed the submit button

                stuff.clear();
                c = 0;
                Undo.clear();
                Redo.clear();
                stack.clear();
                stack2.clear();
                savecoord.clear();
                cou++;
                String xx = model.getPlayer().getName();
                if (model.isValidPlace()) {
                    if(model.getTurn() == true){
                        track_turn = true;
                    }
                    boolean ss = model.checkBoard(coordinates);

                    if (ss && pressed > 0) {
                        drawn=0;
                        readd.clear();
                        model.pointAdder(tPlaced, coordinates, doubleWord,doubleLetter,tripleWord,tripleLetter, chosen_letter);
                        try {
                            model.play(tPlaced);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        coordinates.clear();
                        tPlaced.clear();
                        time_skip=0;

                        String xy = model.getPlayer().getName();
                        //update player name and player hand
                        for (JButton x : onBoard) {
                            view.buttonDisable(x);
                        }
                        for (JButton x : perTurn) {
                            view.removeButton(x);
                        }
                        perTurn.clear();
                        model.changeTurn();
                        view.update_player();
                        view.addPlaceListener_update(placer);
                        try {
                            for(int i=0;i<model.AIAmount();i++){

                                if (model.play(model.AIWord())) {
                                    int count_c = 0;
                                    for (int g = 0; g < model.AIWord().size(); g++) {
                                        letter = model.AIWord().get(g);
                                        model.pointAdder(model.AIWord(), model.AICoord(), doubleWord,doubleLetter,tripleWord,tripleLetter, chosen_letter);

                                        ScrabbleEvent event = new ScrabbleEvent(model, letter, model.AICoord().get(count_c), model.AICoord().get(count_c + 1));
                                        view.update(event, "AI");
                                        view.buttonDisable(view.getbuttons()[model.AICoord().get(count_c)][model.AICoord().get(count_c + 1)]);
                                        //view.getbuttons()[model.AICoord().get(count_c)][model.AICoord().get(count_c + 1)].setBackground(new Color(88,19,94));
                                        view.addPlaceListener_update(placer);
                                        count_c = count_c + 2;
                                    }
                                    model.clear();
                                    view.update_player();
                                    view.addPlaceListener_update(placer);

                                    model.changeTurn();

                                }

                                else {
                                    //view.re_add_p1();
                                    model.clear();
                                    model.getPlayer().getHand().readdTile();
                                    coordinates.clear();
                                    tPlaced.clear();
                                    perTurn.clear();
                                    view.update_player();
                                    view.addPlaceListener_update(placer);
                                    view.update_player();
                                    model.changeTurn();
                                    view.addPlaceListener_update(placer);
                                }
                                view.update_player();
                                view.addPlaceListener_update(placer);

                            }

                        } catch (Exception ex) {

                            throw new RuntimeException(ex);

                        }
                    }

                    else {
                        model.removeWord(coordinates);
                        pressed=0;

                        coordinates.clear();
                        tPlaced.clear();

                        for(int i = 0; i < perTurn.size(); i++) {
                            tileToPlace = perTurn.get(i);
                            int y = view.getButtonX(perTurn.get(i));
                            int x = view.getButtonY(perTurn.get(i));
                            ScrabbleEvent event = new ScrabbleEvent(model,letter,y,x);
                            view.update(event, "P");
                            //g = g+2;
                            inactiveButtons.add(tileToPlace);
                        }

                        for(JButton b: inactiveButtons) {
                            if (was_blank && !b.isEnabled() && b.getText().equals(save_Blank.getText())) {
                                b.setText("*");
                                b.setEnabled(true);
                                b.setVisible(true);
                                was_blank = false;
                            }
                            else if (!b.isEnabled()) {
                                b.setEnabled(true);
                                b.setVisible(true);
                            }
                        }
                        onBoard.clear();
                        if(was_blank){
                            save_Blank.setText("*");
                        }
                        if(track_turn == true){
                            model.setTurn();
                            track_turn = false;
                        }
                        for(int i = 0; i < readd.size(); i++) {
                            if(model.getPlayerHand().getTiles().size() != 7) {
                                model.getPlayerHand().getTiles().add(readd.get(i));
                            }
                        }
                        readd.clear();
                        view.handList1.clear();
                        view.reAdd(model, placer);
                        view.buttonReset2();
                        //display that either the word is wrong or it did not connect to another word
                        JFrame f = new JFrame();
                        JOptionPane.showMessageDialog(f,"Not A Valid Word");
                    }
                }
                else {
                    model.removeWord(coordinates);
                    for(int i = 0; i < readd.size(); i++) {
                        model.getPlayerHand().getTiles().add(readd.get(i));
                    }
                    readd.clear();
                    view.handList1.clear();
                    view.reAdd(model, placer);
                    view.buttonReset();
                    // display that it was not in the middle
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f,"ERROR: First Tile Not In The Middle");
                }
                pressed=0;

            }

            if(s == "Draw"){
                if(drawn<1){
                    model.draw();
                    view.handList1.clear();
                    view.reAdd(model, placer);
                    view.setVisible();
                    drawn++;
                }else{

                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f,"You may only draw once per round");
                }



            }

            if(s == "Skip turn"){ //We pressed the skip turn button
                time_skip++;
                stuff.clear();
                if(time_skip == 2){
                    view.buttonDisableAll();
                    String winner=p1.get(0).getWinner(p1.get(1));
                    JFrame f = new JFrame();
                    if(winner.equals("")){
                        JOptionPane.showMessageDialog(f,"There is a TIE!");
                    }
                    else{
                        JOptionPane.showMessageDialog(f,"The Winner is "+winner);
                    }

                }
                model.changeTurn();
                model.removeWord(coordinates);
                for(int i = 0; i < perTurn.size(); i++) {
                    tileToPlace = perTurn.get(i);
                    int y = view.getButtonX(perTurn.get(i));
                    int x = view.getButtonY(perTurn.get(i));
                    ScrabbleEvent event = new ScrabbleEvent(model,letter,y,x);
                    view.update(event, "P");
                    //g = g+2;
                    inactiveButtons.add(tileToPlace);
                }

                for(JButton b: inactiveButtons) {
                    if (was_blank && !b.isEnabled() && b.getText().equals(save_Blank.getText())) {
                        b.setText("*");
                        b.setEnabled(true);
                        b.setVisible(true);
                        was_blank = false;
                    }
                    else if (!b.isEnabled()) {
                        b.setEnabled(true);
                        b.setVisible(true);
                    }
                }
                onBoard.clear();
                coordinates.clear();
                tPlaced.clear();
                view.update_player();
                drawn=0;

                if(skipped<2){
                    try {
                        for(int i=0;i<model.AIAmount();i++){
                            if(model.play(model.AIWord())){   //if player skips turn, the ai will play
                                int count_c=0;
                                for(int g = 0; g < model.AIWord().size();g++) {
                                    letter = model.AIWord().get(g);
                                    model.pointAdder(model.AIWord(), model.AICoord(), doubleWord,doubleLetter,tripleWord,tripleLetter, chosen_letter);

                                    ScrabbleEvent event = new ScrabbleEvent(model, letter, model.AICoord().get(count_c), model.AICoord().get(count_c+1));
                                    view.update(event, "AI");
                                    view.buttonDisable(view.getbuttons()[model.AICoord().get(count_c)][model.AICoord().get(count_c + 1)]);
                                    //view.getbuttons()[model.AICoord().get(count_c)][model.AICoord().get(count_c + 1)].setBackground(new Color(88,19,94));
                                    view.addPlaceListener_update(placer);
                                    view.update_player();
                                    count_c = count_c + 2;



                                }
                                model.clear();



                                model.changeTurn();
                                view.update_player();
                                view.addPlaceListener_update(placer);





                            }
                            else{
                                model.changeTurn();
                                view.update_player();
                                view.addPlaceListener_update(placer);

                            }





                        }




                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }


                }


            }

            if (view.handList1.contains(button) || view.handList2.contains(button)) {
                letter = button.getText(); //We pressed on one of the tiles on our hand
                tileToPlace = button;
            } else if (view.list.contains(button)) {
                if (letter != null && button.getText() == null || button.getText() == "") {//We pressed a button on the board after pressing a button on our hand
                    try {
                        File file = new File("saveUndo" + cou);
                        FileOutputStream output = new FileOutputStream(file);
                        ObjectOutputStream out = new ObjectOutputStream(output);
                        out.writeObject(new ScrabbleModelData(model.getBoard(), model.getPlayerHand()));
                        Undo.add(file);
                        stack.push(file);
                        out.close();
                        output.close();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    time_skip = 0;
                    int y = view.getButtonX(button);
                    int x = view.getButtonY(button);
                    model.placeBoard(y, x, new Tile(letter));

                    coordinates.add(y);
                    coordinates.add(x);
                    tPlaced.add(letter);

                    perTurn.add(button);

                    onBoard.add(button);
                    ScrabbleEvent event = new ScrabbleEvent(model, letter, y, x);
                    view.update(event, "P");
                    letter = null;
                    tileToPlace.setEnabled(false);
                    tileToPlace.setVisible(false);
                    inactiveButtons.add(tileToPlace);
                    for(int i =0; i<model.getPlayerHand().getTiles().size(); i++) {
                        if (model.getPlayerHand().getTiles().get(i).getLetter().equals(tileToPlace.getText())){
                            //if(stuff.contains(model.getPlayerHand().getTiles().get(i).getLetter())) {
                            stuff.add(model.getPlayerHand().getTiles().get(i).getLetter());
                            readd.add(model.getPlayerHand().getTiles().get(i));
                            model.getPlayerHand().getTiles().remove(i);
                            break;
                            //}
                        }
                    }
                    tileToPlace = null;
                    pressed++;
                    cou++;
                    try {
                        File file = new File("saveRedo" + cou);
                        FileOutputStream output = new FileOutputStream(file);
                        ObjectOutputStream out = new ObjectOutputStream(output);
                        out.writeObject(new ScrabbleModelData(model.getBoard(), model.getPlayerHand()));
                        Undo.add(file);
                        stack.push(file);
                        out.close();
                        output.close();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    cou++;
                }
                else if(letter == null && button.getText() != null){  //We pressed a button on the board after placing a letter on the board
                    for(JButton b: inactiveButtons){
                        if(b.getText() != null) {
                            if (b.getText().equals(button.getText())) {
                                if (!b.isEnabled()) {
                                    int y = view.getButtonX(button);
                                    int x = view.getButtonY(button);
                                    model.removeBoard(y, x);
                                    coordinates.remove(coordinates.size() - 1);
                                    coordinates.remove(coordinates.size() - 1);

                                    tPlaced.remove(s);

                                    ScrabbleEvent event = new ScrabbleEvent(model, letter, y, x);
                                    view.update(event, "P");
                                    if (was_blank) {
                                        b.setText("*");
                                        was_blank = false;
                                    }
                                    onBoard.remove(button);
                                    b.setEnabled(true);
                                    b.setVisible(true);
                                    if (pressed > 0) pressed--;
                                    break;


                                }
                            }
                        }
                    }
                }
            }
            if (s == "Redo") {
                if(stack2.size() > 0) {
                    ScrabbleModelData data;
                    view.buttonReset();
                    JButton[][] buttons = new JButton[15][15];
                    FileInputStream input = null;
                    try {
                        int x = savecoord.pop();
                        int y = savecoord.pop();
                        coordinates.add(x);
                        coordinates.add(y);
                        stack.push(stack2.peek());
                        stack2.pop();
                        stack.push(stack2.peek());
                        input = new FileInputStream(stack2.pop());
                        q++;
                        ObjectInputStream in = new ObjectInputStream(input);
                        data = (ScrabbleModelData) in.readObject();
                        in.close();
                        input.close();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    model.setBoard(data.getBoard());
                    model.setHand(data.getHand());
                    Tile[][] b = data.getBoard();
                    for (int i = 0; i < model.SIZE; i++) {
                        for (int j = 0; j < model.SIZE; j++) {
                            JButton but = new JButton(b[i][j].getLetter());
                            if (!but.getText().equals("_")) {
                                letter = but.getText();
                                //but.setFont(new Font("Arial", Font.BOLD,18));
                                buttons[i][j] = but;
                                ScrabbleEvent event = new ScrabbleEvent(model, letter, i, j);
                                view.update(event, "P");
                            }
                        }
                    }
                    onBoard.add(trac2.get(trac2.size() - 1));
                    trac2.remove(trac2.size() - 1);
                    tPlaced.add(trac.get(trac.size() - 1).getLetter());
                    readd.add(trac.get(trac.size() - 1));
                    trac.remove(trac.get(trac.size() - 1));
                    view.setUndoModel(model, placer);
                    view.addPlaceListener_update(placer);
                    for (int i = 0; i < tPlaced.size(); i++) {
                        System.out.println(tPlaced.get(i));
                    }
                    System.out.println("LOAD SUCCESS!");
                }
                else{
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f, "Nothing to Redo");
                }
            }

            if (s== "Undo") {
                if (stack.size() > 0) {
                    savecoord.push(coordinates.get(coordinates.size() - 1));
                    coordinates.remove(coordinates.size() - 1);
                    savecoord.push(coordinates.get(coordinates.size() - 1));
                    coordinates.remove(coordinates.size() - 1);

                    Undo();

                    if (readd.size() != 0) {
                        trac.add(readd.get(readd.size() - 1));
                        readd.remove(readd.size() - 1);
                    }
                    if (onBoard.size() != 0) {
                        trac2.add(onBoard.get(onBoard.size() - 1));
                        onBoard.remove(onBoard.size() - 1);
                    }
                    if (tPlaced.size() != 0) {
                        tPlaced.remove(tPlaced.get(tPlaced.size() - 1));
                    }
                    for (int i = 0; i < tPlaced.size(); i++) {
                        System.out.println(tPlaced.get(i));
                    }
                }
                else{
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f, "Nothing to Undo");
                }
            }
        }
    }

    public void Undo(){
        ScrabbleModelData data;
        view.buttonReset();
        JButton[][] buttons = new JButton[15][15];
        FileInputStream input = null;
        try {
            stack2.push(stack.peek());
            stack.pop();
            stack2.push(stack.peek());
            input = new FileInputStream(stack.pop());
            ObjectInputStream in = new ObjectInputStream(input);
            data = (ScrabbleModelData) in.readObject();
            in.close();
            input.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        model.setBoard(data.getBoard());
        model.setHand(data.getHand());
        Tile[][] b = model.getBoard();
        for (int i = 0; i < model.SIZE; i++) {
            for (int j = 0; j < model.SIZE; j++) {
                JButton but = new JButton(b[i][j].getLetter());
                if (!but.getText().equals("_")) {
                    but.addActionListener(placer);
                    letter = but.getText();
                    //.setFont(new Font("Arial", Font.BOLD,18));
                    buttons[i][j] = but;
                    //buttons[i][j].setEnabled(false);
                    ScrabbleEvent event = new ScrabbleEvent(model, letter, i, j);
                    view.update(event, "P");
                }
            }
        }
        view.setUndoModel(model,placer);
        view.addPlaceListener_update(placer);
        c++;
    }
}