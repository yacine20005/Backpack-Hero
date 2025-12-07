package fr.uge.backpackhero.engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.github.forax.zen.ApplicationContext;

import fr.uge.backpackhero.model.Backpack;
import fr.uge.backpackhero.model.GameState;
import fr.uge.backpackhero.model.Enemy;
import fr.uge.backpackhero.model.item.Armor;
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;
import fr.uge.backpackhero.model.level.Room;
import fr.uge.backpackhero.model.level.RoomType;

/**
 * View class responsible for rendering the game state onto the screen.
 * It handles drawing the backpack, dungeon, hero, and combat interface.
 * 
 * @author @Naniiiii944
 */
public class View {

    private View() {
        // Private constructor to prevent warnings
    }

    /** The size of each tile in pixels. */
    public static final int TILE_SIZE = 100;
    /** The width of the backpack display area in tiles. */
    public static final int BACKPACK_WIDTH_IN_TILES = 5;
    /** The width of the backpack display area in pixels. */
    public static final int BACKPACK_PIXEL_WIDTH = BACKPACK_WIDTH_IN_TILES * TILE_SIZE;

    /**
     * Draws the entire game view including backpack, dungeon, hero, and combat
     * interface.
     * 
     * @param context the application context
     * @param state   the current game state
     */
    static void draw(ApplicationContext context, GameState state) {
        context.renderFrame(screen -> {

            var screenInfo = context.getScreenInfo();
            var width = (int) screenInfo.width();
            var height = (int) screenInfo.height();

            clearScreen(screen, width, height);
            drawBackpack(screen, state.getBackpack(), height);
            drawDungeon(screen, state.getCurrentFloor());
            drawHero(screen, state.getPosition());

            if (state.isInCombat()) {
                drawCombat(screen, state, height);
            }
        });
    }

    /**
     * Clears the screen with a black background.
     * 
     * @param screen the graphics context to draw on
     * @param width  the width of the screen
     * @param height the height of the screen
     */
    private static void clearScreen(Graphics2D screen, int width, int height) {
        screen.setColor(Color.BLACK);
        screen.fillRect(0, 0, width, height);
    }

    /**
     * Draws a single cell of an item in the backpack.
     * 
     * @param screen   the graphics context to draw on
     * @param item     the item to draw
     * @param cellPos  the position of the cell within the backpack
     * @param isAnchor whether the cell is the anchor cell of the item
     */
    private static void drawItemCell(Graphics2D screen, Item item, Position cellPos, boolean isAnchor) {
        int x = cellPos.x() * TILE_SIZE;
        int y = (cellPos.y() * TILE_SIZE) + TILE_SIZE;

        Color itemColor;
        if (item instanceof Armor) {
            itemColor = new Color(139, 69, 19);
        } else {
            itemColor = new Color(100, 0, 0);
        }

        screen.setColor(itemColor);
        screen.fillRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);

        screen.setColor(itemColor.brighter());
        screen.setStroke(new BasicStroke(2));
        screen.drawRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);

        if (isAnchor) {
            screen.setColor(Color.WHITE);
            String name = item.getName();
            screen.drawString(name, x + 5, y + 20);
        }
    }

    /**
     * Draws the backpack on the screen.
     * 
     * @param screen       the graphics context to draw on
     * @param backpack     the backpack to draw
     * @param screenHeight the height of the screen
     */
    private static void drawBackpack(Graphics2D screen, Backpack backpack, int screenHeight) {
        screen.setColor(new Color(20, 20, 20));
        screen.fillRect(0, 0, BACKPACK_PIXEL_WIDTH, screenHeight);

        screen.setColor(Color.WHITE);
        screen.drawString("Backpack", 10, 20);

        int backpackHeightInTiles = backpack.getHeight();

        drawBackpackGrid(screen, backpackHeightInTiles);

        drawItems(screen, backpack);
    }

    /**
     * Draws all items in the backpack.
     * 
     * @param screen   the graphics context to draw on
     * @param backpack the backpack containing the items to draw
     */
    private static void drawItems(Graphics2D screen, Backpack backpack) {
        backpack.getItems().forEach((anchor, item) -> {
            var cells = item.getShape().getAbsolutePositions(anchor);
            for (var cell : cells) {
                drawItemCell(screen, item, cell, cell.equals(anchor));
            }
        });
    }

    /**
     * Draws the grid of the backpack.
     * 
     * @param screen        the graphics context to draw on
     * @param heightInTiles the height of the backpack in tiles
     */
    private static void drawBackpackGrid(Graphics2D screen, int heightInTiles) {
        screen.setColor(Color.GRAY);
        screen.setStroke(new BasicStroke(1));

        for (int y = 0; y < heightInTiles; y++) {
            for (int x = 0; x < BACKPACK_WIDTH_IN_TILES; x++) {
                screen.drawRect(
                        x * TILE_SIZE,
                        (y * TILE_SIZE) + (TILE_SIZE),
                        TILE_SIZE,
                        TILE_SIZE);
            }
        }
    }

    /**
     * Draws the dungeon floor on the screen.
     * 
     * @param screen the graphics context to draw on
     * @param floor  the dungeon floor to draw
     */
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

    /**
     * Draws a single room of the dungeon.
     * 
     * @param screen  the graphics context to draw on
     * @param room    the room to draw
     * @param x       the x position of the room in the dungeon grid
     * @param y       the y position of the room in the dungeon grid
     * @param xOffset the horizontal offset to apply when drawing the room
     */
    private static void drawOneRoom(Graphics2D screen, Room room, int x, int y, int xOffset) {
        var rect = new Rectangle2D.Float(
                (x * TILE_SIZE) + xOffset,
                y * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE);

        screen.setColor(getColorForRoom(room.getType()));
        screen.fill(rect);

        screen.setColor(Color.WHITE);
        screen.draw(rect);

        screen.drawString(
                room.getType().getSymbol(),
                (x * TILE_SIZE) + xOffset + 15,
                y * TILE_SIZE + 25);
    }

    /**
     * Draws the "hero" on the screen.
     * 
     * @param screen  the graphics context to draw on
     * @param heroPos the position of the hero
     */
    private static void drawHero(Graphics2D screen, Position heroPos) {
        if (heroPos == null)
            return;

        var heroRect = new Rectangle2D.Float(
                (heroPos.x() * TILE_SIZE) + BACKPACK_PIXEL_WIDTH + 10,
                heroPos.y() * TILE_SIZE + 10,
                TILE_SIZE - 40,
                TILE_SIZE - 40);

        screen.setColor(Color.BLUE);
        screen.fill(heroRect);
    }

    /**
     * Returns the color associated with a room type.
     * 
     * @param type the type of the room
     * @return the color corresponding to the room type
     */
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

    /**
     * Draws the combat interface on the screen.
     * 
     * TODO : Why this method is so big ?
     * 
     * @param screen       the graphics context to draw on
     * @param state        the current game state
     * @param screenHeight the height of the screen
     */
    private static void drawCombat(Graphics2D screen, GameState state, int screenHeight) {
        var hero = state.getHero();
        var enemies = state.getCurrentEnemies();
        if (enemies == null) {
            return;
        }

        int width = BACKPACK_PIXEL_WIDTH
                + state.getCurrentFloor().getWidth() * TILE_SIZE;

        int panelWidth = 400;
        int panelHeight = 80 + enemies.size() * 20;
        int x = (width - panelWidth) / 2;
        int y = (screenHeight - panelHeight) / 2;
        screen.setColor(new Color(0, 0, 0, 180));
        screen.fillRect(x - 10, y - 25, panelWidth + 20, panelHeight + 40);
        var oldFont = screen.getFont();
        screen.setFont(oldFont.deriveFont(50f));
        screen.setColor(Color.WHITE);
        int textX = x;
        int textY = y;
        screen.drawString("Combat", textX, textY);
        screen.setFont(oldFont.deriveFont(18f));
        textY += 22;
        screen.drawString("Hero HP: " + hero.getHp(), textX, textY);
        textY += 20;
        screen.drawString("Energy: " + hero.getEnergy(), textX, textY);
        textY += 20;
        screen.drawString("Block: " + hero.getBlock(), textX, textY);
        textY += 25;

        screen.drawString("Ennemis :", textX, textY);
        textY += 20;

        int index = 1;
        for (Enemy enemy : enemies) {
            screen.drawString(index + ". " + enemy.getName()
                    + " HP=" + enemy.getHp()
                    + " Block=" + enemy.getBlock(), textX, textY);
            textY += 20;
            index++;
        }
    }
}
