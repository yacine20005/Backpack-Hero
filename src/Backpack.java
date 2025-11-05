import java.util.HashMap;
import java.util.Map;

public class Backpack {

    private final int width = 3;
    private final int height = 5;

    private final Map<Position, Item> items; // Here we declare items as map and not as HashMap to increase the flexibility if we want to change the implementation later

    public Backpack() {
        this.items = new HashMap<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<Position, Item> getItems() {
        return items;
    }
}
