import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;
public  class wordGenerator implements Serializable {
    private Dictionary dictionary;
    private ArrayList<String> possibleWords;
    ArrayList<String> letters;

    /*
    Generates a word with the given hand, if there are already tiles to
    play with then a string s can be input.
     */
    public wordGenerator(Hand hand,String s) throws Exception {
        dictionary = new Dictionary();
        possibleWords = new ArrayList<>();
        letters = new ArrayList<>();
        if(s == null) {
            Hand hand2 = hand;
            hand2.readdTile();
            for (Tile tile : hand2.getTiles()) {
                letters.add(tile.getLetter());
            }
            char[] chars = letters.stream().collect(Collectors.joining()).toCharArray();
            for(int i = 0; i < 7; i++){
                checkWords(chars, i, new char[i], 0);
            }
        }
        else{
            String upperCase = s.toUpperCase();
            char[] split = upperCase.toCharArray();
            Hand hand2 = hand;
            hand2.readdTile();
            for (Tile tile : hand2.getTiles()) {
                letters.add(tile.getLetter());
            }
            char[] chars = letters.stream().collect(Collectors.joining()).toCharArray();
            StringBuilder sb = new StringBuilder();
            sb.append(split);
            sb.append(chars);
            char[] result = sb.toString().toCharArray();
            for(int i = 0; i < 7; i++){
                checkWords2(result, i, new char[i], 0, result);
            }
        }
    }
    public void checkWords2(char[] chars, int length, char[] wordBuilder, int position, char[] h) {
        char[] a = h;
        if (position == length) {
            String word = new String(wordBuilder);
            int count = 0;
            ArrayList<String>lettersToUse = new ArrayList<>();
            for (int x=0; x<a.length; x++) {
                lettersToUse.add(String.valueOf(a[x]));
            }
            char[] wordLetters = word.toCharArray();
            for(int z = 0; z<wordLetters.length;z++){
                if(lettersToUse.contains(String.valueOf(wordLetters[z]))){
                    count +=1;
                    lettersToUse.remove(String.valueOf(wordLetters[z]));
                }}
            if (dictionary.getDictionary().containsKey(word) && !possibleWords.contains(word) && word.length()>1
                    && count == word.length()) {
                possibleWords.add(word);
            }
            return;
        }
        for (int i = 0; i < chars.length; i++) {
            wordBuilder[position] = chars[i];
            checkWords2(chars, length, wordBuilder, position + 1,h);
        }
    }
    public void checkWords(char[] chars, int length, char[] wordBuilder, int position) {
        if (position == length) {
            String word = new String(wordBuilder);
            if (dictionary.getDictionary().containsKey(word) && !possibleWords.contains(word) && word.length()>1) {
                possibleWords.add(word);
            }
            return;
        }
        for (int i = 0; i < chars.length; i++) {
            wordBuilder[position] = chars[i];
            checkWords(chars, length, wordBuilder, position + 1);
        }
    }
    public ArrayList<String> getPossibleWords() {
        return possibleWords;
    }

    public ArrayList<String> getWordsStartingWith(String s) {
        char[] split = s.toCharArray();
        ArrayList<String> words = this.getPossibleWords();
        ArrayList<String> word = new ArrayList<>();
        word.add(s);
        ArrayList<String> wordsStarting = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            char[] chars = words.get(i).toCharArray();
            if (split.length == 1) {
                if (chars[0] == s.toUpperCase().charAt(0) &&
                        words.get(i).length() > 1) {
                    if (!word.contains(words.get(i))) {
                        wordsStarting.add(words.get(i));

                    }
                }
            }
            if (split.length == 2) {
                if (chars[0] == s.toUpperCase().charAt(0) && chars[1] == s.toUpperCase().charAt(1) &&
                        words.get(i).length() > 2) {
                    if (!word.contains(words.get(i))) {
                        wordsStarting.add(words.get(i));

                    }
                }
            }
            if (split.length == 3 && chars.length > 2) {
                if (chars[0] == s.toUpperCase().charAt(0) && chars[1] == s.toUpperCase().charAt(1)
                        && chars[2] == s.toUpperCase().charAt(2) && words.get(i).length() >= 3) {
                    if (!word.contains(words.get(i))) {
                        wordsStarting.add(words.get(i));

                    }
                }
            }
            if (split.length == 4 && chars.length > 3) {
                if (chars[0] == s.toUpperCase().charAt(0) && chars[1] == s.toUpperCase().charAt(1)
                        && chars[2] == s.toUpperCase().charAt(2) && chars[3] == s.toUpperCase().charAt(3) &&
                        words.get(i).length() >= 4) {
                    if (!word.contains(words.get(i))) {
                        wordsStarting.add(words.get(i));

                    }
                }
            }
            if (split.length == 5 && chars.length > 4) {
                if (chars[0] == s.toUpperCase().charAt(0) && chars[1] == s.toUpperCase().charAt(1)
                        && chars[2] == s.toUpperCase().charAt(2) && chars[3] == s.toUpperCase().charAt(3) &&
                        chars[4] == s.toUpperCase().charAt(4) && words.get(i).length() >= 4) {
                    if (!word.contains(words.get(i))) {
                        wordsStarting.add(words.get(i));

                    }
                }
            }
        }
        return wordsStarting;
    }
}