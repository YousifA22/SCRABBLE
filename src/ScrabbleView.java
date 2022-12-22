import org.xml.sax.SAXException;

import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;

public class  ScrabbleView extends JFrame implements ScrabbleInterface, Serializable {
    private JFrame frameMain;
    private JPanel panelBoard;
    private JPanel panelTitle;
    private JPanel panelMain;
    private JPanel turnPanel;
    private JLabel playerInfo;
    private ImageIcon value0;
    private ImageIcon value;
    private ImageIcon value2;
    private ImageIcon value3;
    private ImageIcon value4;
    private ImageIcon value5;
    private ImageIcon value8;
    private ImageIcon value10;
    public static ScrabbleModel model_game;
    private JMenuBar menuBar;
    private JLabel playerInfo2;
    private JLabel playerHand;
    private JLabel jlabel;
    private JLabel wordsMadeTitle;
    private Hand hand;
    public static List<Player> players;
    private JPanel handPanel;
    private JPanel panelWordsMade;
    private JPanel submitSkipCombine;

    private JPanel panelTileVlaues;
    private JButton special1,special2,special3,special4;
    private DefaultListModel<String> listModel;
    private JList<String> wordsMade;
    public List<JButton> list;
    public List<JButton> not_Bonuslist;
    public List<JButton> Bonuslist;
    public List<JButton> handList1;
    public List<JButton> handList2;
    public List<JButton> handList3;
    public List<JButton> handList4;
    public List<List> allHands;
    private JButton submitButton;
    private JButton[][] buttons;
    private static JButton[][] buttonSave;
    private JButton skipButton;
    public List<Coordinates> coordinatesDouble;
    public List<Coordinates> coordinatesTriple;
    public List<Coordinates> coordinatesLDouble;
    public List<Coordinates> coordinatesLTriple;
    private List<Integer> size;
    private int numAIs = 0;
    private JMenuBar menubar;
    private JMenu menu;
    private JMenuItem save;
    private JMenuItem load;
    private boolean customBoard;
    private JButton undo,redo,draw;
    static PremiumSquaresHandler premiumSquaresHandler = new PremiumSquaresHandler();

    private static final long serialVersionUID = 1;

    public ScrabbleView() throws Exception {
        String[] buttons2 = {"VS AI", "Quit"};
        int selected = JOptionPane.showOptionDialog(null, "Choose How You Want to Play", "Scrabble",
                JOptionPane.DEFAULT_OPTION, 3, null, buttons2, buttons2[1]);


        if (selected == 0) {
            numAIs = Integer.parseInt(JOptionPane.showInputDialog("How many AIs would you like to play against?\nMAXIMUM 3"));
            while (numAIs > 3) {
                JOptionPane.showMessageDialog(this, "Invalid number of AIs",
                        "ERROR", JOptionPane.WARNING_MESSAGE);
                numAIs = Integer.parseInt(JOptionPane.showInputDialog("How many AIs would you like to play against?\nMAXIMUM 3"));
            }
        }
        if (selected == 1) {
            System.exit(0);
        }

        String[] boardSelectionChoice = {"Default", "Custom"};
        int boardSelection = JOptionPane.showOptionDialog(null, "Would you like to use the regular board or your custom board", "Scrabble",
                JOptionPane.DEFAULT_OPTION, 3, null, boardSelectionChoice, boardSelectionChoice[1]);
        if(boardSelection == 0){
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse("default.xml", premiumSquaresHandler);
            customBoard = true;
        } else if(boardSelection == 1){
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse("premiumsquares.xml", premiumSquaresHandler);
            customBoard = true;
        }

        size=new ArrayList<>();
        createIcon();
        buttonSave = new JButton[15][15];
        frameMain = new JFrame();
        frameMain.setLayout(new FlowLayout());
        list = new ArrayList<>();
        not_Bonuslist = new ArrayList<>();
        menu = new JMenu("Menu");
        menu.setBackground(new Color(80,114,122));
        menu.setForeground(Color.WHITE);
        menubar = new JMenuBar();
        menubar.setBackground(Color.DARK_GRAY);
        menubar.setForeground(Color.WHITE);
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        undo = new JButton("Undo");
        redo = new JButton("Redo");
        draw= new JButton("Draw");
        menu.add(save);
        menu.add(load);
        menubar.add(menu);
        Bonuslist = new ArrayList<>();
        handList1 = new ArrayList<>();
        handList2 = new ArrayList<>();
        handList3 = new ArrayList<>();
        handList4 = new ArrayList<>();
        allHands = new ArrayList<>();
        panelMain = new JPanel();
        panelWordsMade = new JPanel();
        panelBoard = new JPanel();
        panelTitle = new JPanel();
        turnPanel = new JPanel();
        handPanel = new JPanel();
        panelTileVlaues = new JPanel();
        special1=new JButton();
        special2 = new JButton();
        special3 = new JButton();
        special4 =new JButton();
        submitSkipCombine = new JPanel();
        menuBar = new JMenuBar();
        turnPanel.setLayout(new BorderLayout());
        panelWordsMade.setLayout(new GridLayout(1,1));
        panelMain.setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        wordsMade = new JList<>(listModel);
        model_game = new ScrabbleModel();
        model_game.addScrabbleView(this);
        coordinatesDouble = new ArrayList<>();
        coordinatesLDouble = new ArrayList<>();
        coordinatesTriple = new ArrayList<>();
        coordinatesLTriple = new ArrayList<>();
        buttons = new JButton[ScrabbleModel.SIZE][ScrabbleModel.SIZE];
        GridLayout grid = new GridLayout(15,15);
        panelBoard.setLayout(grid);
        //Setting up the board and buttons
        players = model_game.getPlayers();
        model_game.createBoard();
        allHands.add(handList1);
        allHands.add(handList2);
        allHands.add(handList3);
        allHands.add(handList4);
        model_game.AIScrabble(numAIs);


        for(int i = 0; i < model_game.getNumPlayers(); i++){
            size.add(i);
        }

        /* Creates the board*/
        List<Coordinates> premiumSquareCoord = premiumSquaresHandler.getPremiumSquareCoordinates();
        int count1 = 0;
        Tile[][] c = model_game.getBoard();
        if(customBoard) {
            for (int i = 0; i < ScrabbleModel.SIZE; i++) {
                for (int j = 0; j < ScrabbleModel.SIZE; j++) {
                    int index = 0;
                    while (index < premiumSquareCoord.size()) {
                        Coordinates coord = premiumSquareCoord.get(index);
                        if (j == coord.getX() && i == coord.getY() && coord.getType().equals("doubleL") ) {
                            buttons[i][j] = new JButton(c[i][j].getLetter());
                            buttons[i][j].setText(null);
                            buttons[i][j].setBackground(new Color(155,220,237));
                            buttons[i][j].setBorderPainted(true);
                            Coordinates cord = new Coordinates(j, i);
                            coordinatesLDouble.add(cord);
                            Bonuslist.add(buttons[i][j]);
                            break;
                        }
                        if (j == coord.getX() && i == coord.getY() && coord.getType().equals("doubleW") ) {
                            buttons[i][j] = new JButton(c[i][j].getLetter());
                            buttons[i][j].setText(null);
                            buttons[i][j].setBackground(new Color(212, 119, 152));
                            buttons[i][j].setBorderPainted(true);
                            Coordinates cord = new Coordinates(j, i);
                            coordinatesDouble.add(cord);
                            Bonuslist.add(buttons[i][j]);
                            break;
                        }
                        if (j == coord.getX() && i == coord.getY() && coord.getType().equals("tripleL") ) {
                            buttons[i][j] = new JButton(c[i][j].getLetter());
                            buttons[i][j].setText(null);
                            buttons[i][j].setBackground(new Color(6, 128, 157));
                            buttons[i][j].setBorderPainted(true);
                            Coordinates cord = new Coordinates(j, i);
                            coordinatesLTriple.add(cord);
                            Bonuslist.add(buttons[i][j]);
                            break;
                        }
                        if (j == coord.getX() && i == coord.getY() && coord.getType().equals("tripleW") ) {
                            buttons[i][j] = new JButton(c[i][j].getLetter());
                            buttons[i][j].setText(null);
                            buttons[i][j].setBackground(new Color(143, 0, 0));
                            buttons[i][j].setBorderPainted(true);
                            Coordinates cord = new Coordinates(j, i);
                            coordinatesTriple.add(cord);
                            Bonuslist.add(buttons[i][j]);
                            break;
                        }
                        else if (index == premiumSquareCoord.size() - 1) {
                            buttons[i][j] = new JButton(c[i][j].getLetter());
                            buttons[i][j].setText(null);
                            buttons[i][j].setBorderPainted(false);
                            not_Bonuslist.add(buttons[i][j]);
                        }
                        index++;
                    }

                    list.add(buttons[i][j]);
                    count1 = count1 + 1;
                }
            }
        }
        buttonSave = buttons;
        for(JButton x5:list){
            panelBoard.add(x5);
        }


        /* Design of the board */
        int count = 0;
        boolean flag = true;
        for(int i = 0; i < list.size(); i++){
            if(count % 2 != 0 && list.get(i).isBorderPainted() == false){
                list.get(i).setBackground(Color.WHITE);
                list.get(i).setForeground(Color.darkGray);
                list.get(i).setBorderPainted(true);
            }
            else if (count % 2 == 0 && list.get(i).isBorderPainted() == false){
                list.get(i).setBackground(Color.WHITE);
                list.get(i).setForeground(Color.BLACK);
                list.get(i).setBorderPainted(true);
            }
            count += 1;
        }

        /* Different colour for the button in the middle for more clarity */
        buttons[7][7].setBackground(Color.DARK_GRAY);

        /* Initializes the players hand and score*/
        InitializePlayerButton();
        HandDisplay();

        /* Design of the frame */
        jlabel = new JLabel("SCRABBLE");
        jlabel.setForeground(Color.WHITE);
        panelTitle.setBackground(Color.darkGray);
        jlabel.setFont(new Font("Calibri", Font.BOLD + Font.ITALIC, 55));
        panelTitle.add(jlabel);

        /* Design of the each player's information */
        playerInfo = new JLabel("Current Player: " + model_game.getPlayer().getName()); //Just testing
        playerInfo.setForeground(Color.WHITE);
        playerInfo.setOpaque(true);
        playerInfo.setBackground(Color.darkGray);
        playerInfo.setFont(new Font("Calibri", Font.BOLD + Font.ITALIC, 20));

        playerInfo2 = new JLabel("Current Player Score: " +  model_game.getPlayerPoints()); //Just testing
        playerInfo2.setForeground(Color.WHITE);
        playerInfo2.setOpaque(true);
        playerInfo2.setBackground(Color.darkGray);
        playerInfo2.setFont(new Font("Calibri", Font.BOLD + Font.ITALIC, 20));

        /* Design of the title indicating the player's hand */
        playerHand = new JLabel("Current hand:" + model_game.getPlayerHand());
        playerHand.setForeground(Color.WHITE);
        playerHand.setOpaque(true);
        playerHand.setBackground(Color.WHITE);
        playerHand.setFont(new Font("Calibri", Font.BOLD + Font.ITALIC, 12));

        /* Submit button that will allow you to submit the word placed on the board */
        submitButton = new JButton("Submit");
        submitButton.setOpaque(true);
        submitButton.setBackground(new Color(80,114,122));
        submitButton.setFont(new Font("Calibri", Font.BOLD, 17));
        submitButton.setForeground(Color.WHITE);

        undo.setOpaque(true);
        undo.setBackground(new Color(80,114,122));
        undo.setFont(new Font("Calibri", Font.BOLD, 17));
        undo.setForeground(Color.WHITE);

        redo.setOpaque(true);
        redo.setBackground(new Color(80,114,122));
        redo.setFont(new Font("Calibri", Font.BOLD, 17));
        redo.setForeground(Color.WHITE);


        /* Skip button that will allow you to pass your turn to the other play */
        skipButton = new JButton("Skip turn");
        skipButton.setOpaque(true);
        skipButton.setForeground(Color.WHITE);
        skipButton.setBackground(new Color(80,114,122));
        skipButton.setFont(new Font("Calibri", Font.BOLD, 17));


        draw.setOpaque(true);
        draw.setForeground(Color.WHITE);
        draw.setBackground(new Color(80,114,122));
        draw.setFont(new Font("Calibri", Font.BOLD, 17));
        //panelWordsMade.add(wordsMadeTitle, BorderLayout.NORTH);

        for(String s : model_game.allWordsCreated()){
            listModel.addElement(s);
        }

        /* Specifications of the panels */
        panelBoard.setOpaque(true);
        panelBoard.setBackground(Color.GRAY);

        submitSkipCombine.setLayout(new GridLayout());
        submitSkipCombine.setBackground(Color.darkGray);
        submitSkipCombine.add(submitButton);
        submitSkipCombine.add(skipButton);
        submitSkipCombine.add(draw);
        submitSkipCombine.add(undo);
        submitSkipCombine.add(redo);

        turnPanel.add(playerInfo,BorderLayout.NORTH);
        turnPanel.add(playerInfo2,BorderLayout.CENTER);
        turnPanel.add(handPanel, BorderLayout.PAGE_END);
        turnPanel.add(submitSkipCombine, BorderLayout.EAST);

        special1.setOpaque(true);
        special1.setPreferredSize(new Dimension(120,50));
        special1.setBackground(new Color(212,119,152));
        special1.setForeground(new Color(128, 17, 60));
        special1.setFont(new Font("Calibri", Font.BOLD, 15));
        special1.setText("<html>DOUBLE<br />WORD</html>");

        special2.setOpaque(true);
        special2.setPreferredSize(new Dimension(120,50));
        special2.setBackground(new Color(143, 0, 0));
        special2.setForeground(new Color(212,119,152));
        special2.setFont(new Font("Calibri", Font.BOLD, 15));
        special2.setText("<html>TRIPLE<br />WORD</html>");

        special4.setOpaque(true);
        special4.setPreferredSize(new Dimension(120,50));
        special4.setBackground(new Color(6, 128, 157));
        special4.setForeground(new Color(155,220,237));
        special4.setFont(new Font("Calibri", Font.BOLD, 15));
        special4.setText("<html>TRIPLE<br />LETTER</html>");

        special3.setOpaque(true);
        special3.setPreferredSize(new Dimension(120,50));
        special3.setBackground(new Color(155,220,237));
        special3.setForeground(new Color(6, 128, 157));
        special3.setFont(new Font("Calibri", Font.BOLD, 15));
        special3.setText("<html>DOUBLE<br />LETTER</html>");


        /* Design of the section that will display all the valid words played over the course of the game */
        wordsMadeTitle = new JLabel("WORDS PLAYED");
        wordsMadeTitle.setFont(new Font("Calibri", Font.BOLD, 15));
        wordsMadeTitle.setForeground(Color.WHITE);
        wordsMade.setBackground(Color.DARK_GRAY);
        wordsMade.setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(wordsMade);
        scroll.setLayout(new ScrollPaneLayout());

        TitledBorder border = new TitledBorder("WORDS MADE");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);

        panelWordsMade.setBorder(border);
        panelWordsMade.add(scroll);
        panelWordsMade.setPreferredSize(new Dimension(120,120));
        //panelWordsMade.add(wordsMade,BorderLayout.EAST);
        panelWordsMade.setOpaque(true);
        panelWordsMade.setBackground(Color.LIGHT_GRAY);

        panelTileVlaues.setLayout(new GridLayout(4,1));
        panelTileVlaues.add(special1);
        panelTileVlaues.add(special2);
        panelTileVlaues.add(special3);
        panelTileVlaues.add(special4);
        panelTileVlaues.setBackground(Color.GRAY);
        panelTileVlaues.setPreferredSize(new Dimension(75,50));

        panelMain.setPreferredSize(new Dimension(700,700));
        panelMain.add(turnPanel, BorderLayout.SOUTH);
        panelMain.add(panelTitle, BorderLayout.NORTH);
        panelMain.add(panelBoard, BorderLayout.CENTER);
        panelMain.add(panelWordsMade,BorderLayout.EAST);
        panelMain.add(panelTileVlaues,BorderLayout.WEST);

        frameMain.setJMenuBar(menubar);
        frameMain.add(panelMain);
        frameMain.getContentPane().setBackground(Color.black);
        frameMain.setBackground(Color.GRAY);
        frameMain.setSize(400, 400);
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMain.setTitle("Scrabble");
        frameMain.setResizable(true);
        frameMain.setLocationRelativeTo(null);
        frameMain.pack();
        frameMain.setVisible(true);
        this.setResizable(false);
        new ScrabbleController(model_game, this, this.coordinatesDouble,this.coordinatesLDouble,this.coordinatesTriple,this.coordinatesLTriple,players);
        initialSave();
    }

    public void initialSave(){
        try {
            File file = new File("InitialSave");
            FileOutputStream output = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(this.buttons);
            out.close();
            output.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /* Initializes the panels that will display the player's information and their hand */
    public void InitializePlayerButton(){
        handPanel.setBackground(Color.DARK_GRAY);
        handPanel.setForeground(Color.DARK_GRAY);
        handPanel.setLayout(new GridLayout(1,7));
        for(int i = 0; i < model_game.getNumPlayers() ; i++) {
            hand = players.get(i).getHand();
            for (Tile tile : hand.getTiles()) {
                JButton handTile = new JButton(tile.getLetter());
                handTile.setFont(new Font("Arial", Font.BOLD, 28));
                handTile.setBackground(Color.WHITE);
                handTile.setForeground(Color.BLACK);
                handTile.setPreferredSize(new Dimension(40, 40));

                if (tile.getValue() == 1) {
                    handTile.setIcon(value);
                }
                if (tile.getValue() == 2) {
                    handTile.setIcon(value2);
                }
                if (tile.getValue() == 3) {
                    handTile.setIcon(value3);
                }
                if (tile.getValue() == 4) {
                    handTile.setIcon(value4);
                }
                if (tile.getValue() == 5) {
                    handTile.setIcon(value5);
                }
                if (tile.getValue() == 8) {
                    handTile.setIcon(value8);
                }
                if (tile.getValue() == 10) {
                    handTile.setIcon(value10);
                }
                if (tile.getLetter().equals("*")) {
                    handTile.setIcon(value0);
                }
                handTile.setVerticalAlignment(SwingConstants.CENTER);
                handTile.setHorizontalTextPosition(SwingConstants.LEFT);
                allHands.get(i).add(handTile);
            }
        }
    }

    /* Displays the player's information and their hand */
    public void HandDisplay() {
        hand = model_game.getPlayerHand(); //this is just to see if a hand will be displayed
        for(int i = 0 ; i < model_game.getNumPlayers(); i++) {
            if (allHands.get(i).size() < 7 && size.get(i) < 7) {
                re_add_p2();
                size.add(i, 7);
            }
        }
        re_add_p1();
        List<Button> handList = allHands.get(0);
        for (int x = 0; x < handList.size(); x++) {
            handPanel.add(handList.get(x));
        }
    }

    public void reAdd(ScrabbleModel a, ActionListener e){
        Hand handd = a.getPlayerHand();
        handPanel.removeAll();
        handList1.clear();
        for (Tile tile : handd.getTiles()) {
            JButton handTile = new JButton(tile.getLetter());
            handTile.setFont(new Font("Arial", Font.BOLD, 28));
            handTile.setBackground(Color.WHITE);
            handTile.setForeground(Color.DARK_GRAY);
            handTile.setPreferredSize(new Dimension(40, 40));

            if (tile.getValue() == 1) {
                handTile.setIcon(value);
            }
            if (tile.getValue() == 2) {
                handTile.setIcon(value2);
            }
            if (tile.getValue() == 3) {
                handTile.setIcon(value3);
            }
            if (tile.getValue() == 4) {
                handTile.setIcon(value4);
            }
            if (tile.getValue() == 5) {
                handTile.setIcon(value5);
            }
            if (tile.getValue() == 8) {
                handTile.setIcon(value8);
            }
            if (tile.getValue() == 10) {
                handTile.setIcon(value10);
            }
            if (tile.getLetter().equals("*")) {
                handTile.setIcon(value0);
            }
            handTile.setVerticalAlignment(SwingConstants.CENTER);
            handTile.setHorizontalTextPosition(SwingConstants.LEFT);
            handTile.addActionListener(e);
            handList1.add(handTile);
            handPanel.add(handTile);
        }
    }

    public void setVisible(){
        handPanel.setVisible(false);
        handPanel.setVisible(true);
    }

    /* Remove the buttons from the player's hand that were used to create a word on the board */
    public void removeButton(JButton b){
        String a = model_game.getPlayer().getName();
        int turn = model_game.getTurnNum();
        List<JButton> handList = allHands.get(turn);
        for (int i = 0; i < handList.size(); i++) {
            if (handList.get(i).getText().equals(b.getText())) {
                handList.remove(i);

                if (players.get(0).getHand().getTiles().get(i).getLetter().equals(b.getText())){
                    players.get(0).getHand().getTiles().remove(i);
                }
                int currentSize = size.get(turn);
                currentSize--;
                size.add(turn, currentSize);
            }
        }
    }


    /* Updates the view with the new look of the board */
    @Override
    public void update(ScrabbleEvent event, String name) {
        Border newBorder = new LineBorder(Color.GRAY, 1);
        String letter = event.getLetter();
        if(letter != "_" && name.equals("AI")) {
            buttons[event.getX()][event.getY()].setText(letter);
            buttons[event.getX()][event.getY()].setBorder(newBorder);
            //buttons[event.getX()][event.getY()].setBackground(new Color(82,81,81));
            buttons[event.getX()][event.getY()].setFont(new Font("Arial", Font.BOLD, 18));
            buttons[event.getX()][event.getY()].setForeground(Color.GRAY);
        }
        if(letter != "_" && name.equals("P")) {
            buttons[event.getX()][event.getY()].setText(letter);
            buttons[event.getX()][event.getY()].setBorder(newBorder);
            buttons[event.getX()][event.getY()].setFont(new Font("Arial", Font.BOLD, 18));
            buttons[event.getX()][event.getY()].setForeground(Color.GRAY);
        }

        if(letter != "_" && name.equals("L")) {
            buttons[event.getX()][event.getY()].setText(letter);
            buttons[event.getX()][event.getY()].setBorder(newBorder);
            buttons[event.getX()][event.getY()].setBackground(new Color(82,81,81));
            buttons[event.getX()][event.getY()].setFont(new Font("Arial", Font.BOLD, 18));
            buttons[event.getX()][event.getY()].setForeground(Color.GRAY);
            buttons[event.getX()][event.getY()].setEnabled(false);
        }

    }

    /* Updates the view with the new look of the player's information */
    @Override
    public void update_player(){
        //model_game.changeTurn();
        String y2 = model_game.getPlayer().getName();
        playerInfo.setText("Current Player: " + y2);
        playerInfo2.setText("Current Player Score:  " +  model_game.getPlayerPoints());
        for(String s : model_game.allWordsCreated()){
            if(!listModel.contains(s)) {
                listModel.addElement(s);
            }
        }
        handPanel.removeAll();
        HandDisplay();
        handPanel.setEnabled(false);
        handPanel.setEnabled(true);
    }

    /* Listeners for each button in the frame */
    void addPlaceListener(ActionListener listenPlaceTile){
        /* Listener for each player's hand */
        for(List<JButton> handList : allHands) {
            for (JButton button : handList) {
                button.addActionListener(listenPlaceTile);
            }
        }
        /* Listener for each button on the board */
        for(JButton button: list){
            button.addActionListener(listenPlaceTile);
        }
        /* Listener for the submit and skip button */
        submitButton.addActionListener(listenPlaceTile);
        skipButton.addActionListener(listenPlaceTile);
        undo.addActionListener(listenPlaceTile);
        redo.addActionListener(listenPlaceTile);
        draw.addActionListener(listenPlaceTile);
    }

    void addMenuListener(ActionListener e){
        save.addActionListener(e);


        load.addActionListener(e);
    }

    /* Listeners for each button on each player's hand */
    void addPlaceListener_update(ActionListener listenPlaceTile) {
        for(List<JButton> handList : allHands) {
            for (JButton button : handList) {
                button.addActionListener(listenPlaceTile);
            }
        }
    }

    /* Returns the X coordinate of a button
     *
     * @param a button on the board
     *
     * @return the button's X coordinate
     */
    public int getButtonX(JButton button){
        for (int i = 0; i < ScrabbleModel.SIZE; i++) {
            for (int j = 0; j < ScrabbleModel.SIZE; j++) {
                if(buttons[i][j].equals(button))
                    return i;
            }
        }
        return 0;
    }

    /* Returns the Y coordinate of a button
     *
     * @param a button on the board
     *
     * @return the button's Y coordinate
     */
    public int getButtonY(JButton button){
        for (int i = 0; i < ScrabbleModel.SIZE; i++) {
            for (int j = 0; j < ScrabbleModel.SIZE; j++) {
                if(buttons[i][j].equals(button))
                    return j;
            }
        }
        return 0;
    }

    /* Disables a button
     *
     * @param the button that will be disabled
     */
    public void buttonDisable(JButton b) {
        b.setBackground(new Color(82,81,81));
        b.setFont(new Font("Arial", Font.BOLD,18));
        b.setEnabled(false);
    }

    public JButton[][] getbuttons(){
        return buttons;
    }

    public void buttonDisableAll() {
        for (int i = 0; i < ScrabbleModel.SIZE; i++) {
            for (int j = 0; j < ScrabbleModel.SIZE; j++) {
                buttons[i][j].setBackground(new Color(82, 81, 81));
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 15));
                buttons[i][j].setEnabled(false);
            }
        }

    }

    public void buttonReset(){
        for (int i = 0; i < ScrabbleModel.SIZE; i++) {
            for (int j = 0; j < ScrabbleModel.SIZE; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    /* Adds the new tiles on player1's hand after they have placed a word */
    public void re_add_p1() {
        Hand a =  players.get(0).getHand();
        a.readdTile();
        handList1.clear();
        for (Tile tile : a.getTiles()) {
            JButton handTile = new JButton(tile.getLetter());
            handTile.setFont(new Font("Arial", Font.BOLD, 28));
            handTile.setBackground(Color.WHITE);
            handTile.setForeground(Color.DARK_GRAY);
            handTile.setPreferredSize(new Dimension(40, 40));

            if(tile.getValue() == 1){
                handTile.setIcon(value);
            }
            if(tile.getValue() == 2){
                handTile.setIcon(value2);
            }
            if(tile.getValue() == 3){
                handTile.setIcon(value3);
            }
            if(tile.getValue() == 4){
                handTile.setIcon(value4);
            }
            if(tile.getValue() == 5){
                handTile.setIcon(value5);
            }
            if(tile.getValue() == 8){
                handTile.setIcon(value8);
            }
            if(tile.getValue() == 10){
                handTile.setIcon(value10);
            }
            if(tile.getLetter().equals("*")){
                handTile.setIcon(value0);
            }
            handTile.setVerticalAlignment(SwingConstants.CENTER);
            handTile.setHorizontalTextPosition(SwingConstants.LEFT);
            handList1.add(handTile);

        }
    }


    /* Adds the new tiles on player2's hand after they have placed a word */
    public void re_add_p2() {
        int turn = model_game.getTurnNum();
        Hand n =  players.get(turn).getHand();
        n.readdTile();
        allHands.get(turn).clear();
        for (Tile tile : n.getTiles()) {
            JButton handTile = new JButton(tile.getLetter());
            handTile.setFont(new Font("Arial", Font.BOLD, 28));
            handTile.setBackground(Color.WHITE);
            handTile.setForeground(Color.DARK_GRAY);
            handTile.setPreferredSize(new Dimension(40, 40));

            if(tile.getValue() == 1){
                handTile.setIcon(value);
            }
            if(tile.getValue() == 2){
                handTile.setIcon(value2);
            }
            if(tile.getValue() == 3){
                handTile.setIcon(value3);
            }
            if(tile.getValue() == 4){
                handTile.setIcon(value4);
            }
            if(tile.getValue() == 5){
                handTile.setIcon(value5);
            }
            if(tile.getValue() == 8){
                handTile.setIcon(value8);
            }
            if(tile.getValue() == 10){
                handTile.setIcon(value10);
            }
            if(tile.getLetter().equals("*")){
                handTile.setIcon(value0);
            }
            handTile.setVerticalAlignment(SwingConstants.CENTER);
            handTile.setHorizontalTextPosition(SwingConstants.LEFT);
            allHands.get(turn).add(handTile);
        }
    }

    public void createIcon(){

        ImageIcon img0 = new ImageIcon("0icon.PNG");
        Image getimg0 = img0.getImage();
        Image imgscale0 = getimg0.getScaledInstance(15,15, Image.SCALE_DEFAULT);
        value0 = new ImageIcon(imgscale0);

        ImageIcon img = new ImageIcon("1icon.PNG");
        Image getimg = img.getImage();
        Image imgscale = getimg.getScaledInstance(15,15, Image.SCALE_DEFAULT);
        value = new ImageIcon(imgscale);

        ImageIcon img2 = new ImageIcon("2icon.PNG");
        Image getimg2 = img2.getImage();
        Image imgscale2 = getimg2.getScaledInstance(15,15, Image.SCALE_DEFAULT);
        value2 = new ImageIcon(imgscale2);

        ImageIcon img3 = new ImageIcon("3icon.PNG");
        Image getimg3 = img3.getImage();
        Image imgscale3 = getimg3.getScaledInstance(15,15, Image.SCALE_DEFAULT);
        value3 = new ImageIcon(imgscale3);

        ImageIcon img4 = new ImageIcon("4icon.PNG");
        Image getimg4 = img4.getImage();
        Image imgscale4 = getimg4.getScaledInstance(15,15, Image.SCALE_DEFAULT);
        value4 = new ImageIcon(imgscale4);

        ImageIcon img5 = new ImageIcon("5icon.PNG");
        Image getimg5 = img5.getImage();
        Image imgscale5 = getimg5.getScaledInstance(15,15, Image.SCALE_DEFAULT);
        value5 = new ImageIcon(imgscale5);

        ImageIcon img6 = new ImageIcon("8icon.PNG");
        Image getimg6 = img6.getImage();
        Image imgscale6 = getimg6.getScaledInstance(15,15, Image.SCALE_DEFAULT);
        value8 = new ImageIcon(imgscale6);

        ImageIcon img7 = new ImageIcon("10icon.PNG");
        Image getimg7 = img7.getImage();
        Image imgscale7 = getimg7.getScaledInstance(15,15, Image.SCALE_DEFAULT);
        value10 = new ImageIcon(imgscale7);
    }

    public void setModel(ScrabbleModel m){
        wordsMade.removeAll();
        listModel.clear();
        model_game = m;
        for(String s : model_game.allWordsCreated()){
            listModel.addElement(s);
        }
        players = model_game.getPlayers();
        HandDisplay();
        update_player();
    }

    public void clearBoard() throws ParserConfigurationException, SAXException, IOException {
        try {
            File file = new File("InitialSave");
            FileInputStream input = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(input);
            JButton[][] but = (JButton[][]) in.readObject();
            in.close();
            input.close();
            for (int i = 0; i < ScrabbleModel.SIZE; i++) {
                for (int j = 0; j < ScrabbleModel.SIZE; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setBackground(but[i][j].getBackground());
                    buttons[i][j].setEnabled(true);
                }
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void buttonReset2(){
        for (int i = 0; i < ScrabbleModel.SIZE; i++) {
            for (int j = 0; j < ScrabbleModel.SIZE; j++) {
                if(buttons[i][j].isEnabled() && buttons[i][j].getText() != ""){
                    buttons[i][j].setText("");
//                    buttons[i][j].setForeground(Color.darkGray);
//                    buttons[i][j].setBorderPainted(true);
//                    buttons[i][j].setEnabled(true);
                }
            }
        }
    }

    public void setUndoModel(ScrabbleModel m, ActionListener e){
        model_game = m;
        players = model_game.getPlayers();
        reAdd(m, e);
        //update_player();
    }

    public static void main(String[] args) throws Exception {
        ScrabbleView view = new ScrabbleView();


    }
}