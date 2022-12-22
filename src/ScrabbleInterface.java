import java.io.Serializable;

public interface ScrabbleInterface {
    void update(ScrabbleEvent event, String player);

    void update_player();
}
