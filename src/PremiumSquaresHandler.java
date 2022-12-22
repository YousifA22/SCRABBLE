import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class PremiumSquaresHandler extends DefaultHandler {

    private String type;
    private static final String SQUARE = "square";

    private static final String DOUBLE = "doubleL";

    private static final String DOUBLEW = "doublew";

    private static final String TRIPLE = "tripleL";

    private static final String TRIPLEW = "triplew";
    private static final String X = "x";
    private static final String Y = "y";

    private int x;
    private int y;
    private ArrayList<Coordinates> premiumSquareCoordinates;

    boolean xCoordinate = false;
    boolean yCoordinate = false;


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(xCoordinate){
            String xAsString = new String(ch, start, length);
            x = Integer.parseInt(xAsString);
        }
        if(yCoordinate){
            String yAsString = new String(ch, start, length);
            y = Integer.parseInt(yAsString);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        premiumSquareCoordinates = new ArrayList<>();
    }


    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch(qName){
            case X:
                xCoordinate = true;
            case Y:
                yCoordinate = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch(qName){
            case SQUARE:
                Coordinates coordinate = new Coordinates(x, y, "square");
                premiumSquareCoordinates.add(coordinate);
                type = SQUARE;
                break;
            case DOUBLE:
                coordinate = new Coordinates(x, y, "doubleL");
                premiumSquareCoordinates.add(coordinate);
                break;
            case DOUBLEW:
                coordinate = new Coordinates(x, y, "doubleW");
                premiumSquareCoordinates.add(coordinate);
                break;
            case TRIPLE:
                coordinate = new Coordinates(x, y, "tripleL");
                premiumSquareCoordinates.add(coordinate);
                break;
            case TRIPLEW:
                coordinate = new Coordinates(x, y, "tripleW");
                premiumSquareCoordinates.add(coordinate);
                break;
            case X:
                xCoordinate = false;
            case Y:
                yCoordinate = false;
        }
    }

    public String getType(){
        return type;
    }

    public ArrayList<Coordinates> getPremiumSquareCoordinates() {
        return premiumSquareCoordinates;
    }
}