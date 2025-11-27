package fr.uge.backpackhero.engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.github.forax.zen.ApplicationContext;

import fr.uge.backpackhero.model.Hero; 
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;
import fr.uge.backpackhero.model.level.Room;
import fr.uge.backpackhero.model.level.RoomType;

public class View {

    public static final int TILE_SIZE = 100;
    public static final int BACKPACK_WIDTH_IN_TILES = 3; 
    public static final int BACKPACK_PIXEL_WIDTH = BACKPACK_WIDTH_IN_TILES * TILE_SIZE;

    static void draw(ApplicationContext context, Floor floor, Position heroPos, Hero hero) {
        context.renderFrame(screen -> {
            
            var screenInfo = context.getScreenInfo();
            var width = (int) screenInfo.width();
            var height = (int) screenInfo.height();

            clearScreen(screen, width, height); 
            drawBackpack(screen, hero, height); 
            drawDungeon(screen, floor);
            drawHero(screen, heroPos);
        });
    }

    private static void clearScreen(Graphics2D screen, int width, int height) {
        screen.setColor(Color.BLACK);
        screen.fillRect(0, 0, width, height);
    }
    
    private static void drawOneItemName(Graphics2D screen, Item item, Position pos) {
        if (item == null || pos == null) return;

        String name = item.getName();
        if (name == null) return;

        int x = pos.x() * TILE_SIZE;
        int y = (pos.y() * TILE_SIZE) + TILE_SIZE; 

        screen.setColor(Color.WHITE);
        screen.drawString(name, x + 5, y + 20);
    }

    private static void drawBackpack(Graphics2D screen, Hero hero, int screenHeight) {
        screen.setColor(new Color(20, 20, 20)); 
        screen.fillRect(0, 0, BACKPACK_PIXEL_WIDTH, screenHeight);

        screen.setColor(Color.WHITE);
        screen.drawString("Backpack", 10, 20);

        var backpack = hero.getBackpack();
        int backpackHeightInTiles = backpack.getHeight();

        drawBackpackGrid(screen, backpackHeightInTiles);

        backpack.getItems().forEach((position, item) -> {
            drawOneItemName(screen, item, position);
        });
    }

    private static void drawBackpackGrid(Graphics2D screen, int heightInTiles) {
        screen.setColor(Color.GRAY);
        screen.setStroke(new BasicStroke(1)); 

        for (int y = 0; y < heightInTiles; y++) {
            for (int x = 0; x < BACKPACK_WIDTH_IN_TILES; x++) { 
                screen.drawRect(
                    x * TILE_SIZE, 
                    (y * TILE_SIZE) + (TILE_SIZE), 
                    TILE_SIZE, 
                    TILE_SIZE
                );
            }
        }
    }

    private static void drawDungeon(Graphics2D screen, Floor floor) {
        for (int y = 0; y < floor.getHeight(); y++) { 
            for (int x = 0; x < floor.getWidth(); x++) { 
                Room room = floor.getRoom(new Position(x, y));
                if (room != null) {
                    drawOneRoom(screen, room, x, y, BACKPACK_PIXEL_WIDTH);
                }
            }
        }
    }

    private static void drawOneRoom(Graphics2D screen, Room room, int x, int y, int xOffset) {
        var rect = new Rectangle2D.Float(
            (x * TILE_SIZE) + xOffset, 
            y * TILE_SIZE, 
            TILE_SIZE, 
            TILE_SIZE
        );

        screen.setColor(getColorForRoom(room.getType()));
        screen.fill(rect);

        screen.setColor(Color.WHITE);
        screen.draw(rect);
        
        screen.drawString(
            room.getType().getSymbol(), 
            (x * TILE_SIZE) + xOffset + 15, 
            y * TILE_SIZE + 25
        );
    }

    private static void drawHero(Graphics2D screen, Position heroPos) {
        if (heroPos == null) return;

        var heroRect = new Rectangle2D.Float(
            (heroPos.x() * TILE_SIZE) + BACKPACK_PIXEL_WIDTH + 10, 
            heroPos.y() * TILE_SIZE + 10,
            TILE_SIZE - 40,
            TILE_SIZE - 40
        );
        
        screen.setColor(Color.BLUE);
        screen.fill(heroRect);
    }

    private static Color getColorForRoom(RoomType type) {
        return switch (type) {
            case CORRIDOR -> Color.GRAY;
            case ENEMY -> Color.RED;
            case TREASURE -> Color.YELLOW;
            case MERCHANT -> Color.ORANGE;
            case HEALER -> Color.PINK;
            case EXIT -> Color.GREEN;
            default -> Color.DARK_GRAY;
        };
    }
}