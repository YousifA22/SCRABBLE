import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class UndoRedo implements Serializable {

    private int pointer;
    private ArrayList<File> historyList;

    public UndoRedo(){
        historyList = new ArrayList<>();
        pointer = -1;
    }

    public void addToHistory(File file){
        //If player played a move after undoing 1 or multiple times, the states after the point they undo'ed will be
        //deleted
        if(pointer < historyList.size() - 1){
            for(int i = historyList.size() - 1; i > pointer; i--){
                historyList.remove(pointer);
            }

        }
        historyList.add(file);
        pointer++;
    }

    public void undo(){
        if(pointer > 0) {
            pointer--;
        }
    }

    public void redo(){
        if(pointer < historyList.size() - 1){
            pointer++;
        }
    }


    public File getModelState(){
        return historyList.get(pointer);
    }
}
