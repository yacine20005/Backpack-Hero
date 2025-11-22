package fr.uge.backpackhero.model.level;

public record Position(int x, int y) {

    public boolean checkBounds(int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
