package fr.uge.backpackhero.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.github.forax.zen.ApplicationContext;

import fr.uge.backpackhero.logic.EnemyAction;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.model.entity.Enemy;
import fr.uge.backpackhero.model.item.Armor;
import fr.uge.backpackhero.model.item.Backpack;
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
    public static void draw(ApplicationContext context, GameState state) {
        context.renderFrame(screen -> {

            var screenInfo = context.getScreenInfo();
            var width = (int) screenInfo.width();
            var height = (int) screenInfo.height();

            clearScreen(screen, width, height);
            drawBackpack(screen, state);
            drawStatsBox(screen, state);
            drawDungeon(screen, state.getCurrentFloor());
            drawHero(screen, state.getPosition());
            drawHealerPrompt(screen, state);
            drawMerchant(screen, state);

            if (state.isInCombat()) {
                drawCombat(screen, state, height);
            }
            if (state.isGameOver()) {
                drawGameOver(screen, width, height);
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
     * @param screen     the graphics context to draw on
     * @param item       the item to draw
     * @param cellPos    the position of the cell within the backpack
     * @param isAnchor   whether the cell is the anchor cell of the item
     * @param isSelected whether the item is currently selected
     */
    private static void drawItemCell(Graphics2D screen, Item item, Position cellPos, boolean isAnchor,
            boolean isSelected) {
        int x = cellPos.x() * TILE_SIZE;
        int y = (cellPos.y() * TILE_SIZE) + TILE_SIZE;

        Color itemColor;
        if (item instanceof Armor) {
            itemColor = new Color(139, 69, 19);
        } else {
            itemColor = new Color(100, 0, 0);
        }

        // If item is selected, make it brighter
        if (isSelected) {
            itemColor = itemColor.brighter().brighter();
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
    private static void drawBackpack(Graphics2D screen, GameState state) {
        var backpack = state.getBackpack();
        var screenInfo = screen.getDeviceConfiguration().getBounds();
        int screenHeight = (int) screenInfo.getHeight();
        
        screen.setColor(new Color(20, 20, 20));
        screen.fillRect(0, 0, BACKPACK_PIXEL_WIDTH, screenHeight);

        screen.setColor(Color.WHITE);
        screen.drawString("Gold: " + backpack.goldAmount(), 10, 40);


        int backpackHeightInTiles = backpack.getHeight();

        drawBackpackGrid(screen, backpackHeightInTiles);

        drawItems(screen, backpack, state);
    }

    /**
     * Draws all items in the backpack.
     * 
     * @param screen   the graphics context to draw on
     * @param backpack the backpack containing the items to draw
     * @param state    the current game state
     */
    private static void drawItems(Graphics2D screen, Backpack backpack, GameState state) {
        Position selectedAnchor = state.getSelectedItemAnchor();

        backpack.getItems().forEach((anchor, item) -> {
            boolean isSelected = anchor.equals(selectedAnchor);
            var cells = item.getShape().getAbsolutePositions(anchor);
            for (var cell : cells) {
                drawItemCell(screen, item, cell, cell.equals(anchor), isSelected);
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
     * Displays hero stats, enemy stats, and enemy intents.
     * 
     * @param screen       the graphics context to draw on
     * @param state        the current game state
     * @param screenHeight the height of the screen
     */
    private static void drawCombat(Graphics2D screen, GameState state, int screenHeight) {
        var hero = state.getHero();
        var combat = state.getCombatEngine();
        var enemies = combat.getCurrentEnemies();
        if (enemies == null) {
            return;
        }

        int width = BACKPACK_PIXEL_WIDTH
                + state.getCurrentFloor().getWidth() * TILE_SIZE;

        int panelWidth = 500;
        int panelHeight = 80 + enemies.size() * 40;
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
            EnemyAction intent = combat.getEnemyIntent(enemy);
            String intentStr = getIntentDisplay(intent, enemy);

            screen.drawString(index + ". " + enemy.getName()
                    + " HP=" + enemy.getHp()
                    + " Block=" + enemy.getDefense(), textX, textY);
            textY += 20;

            screen.setColor(getIntentColor(intent));
            screen.drawString("   → Intent: " + intentStr, textX, textY);
            screen.setColor(Color.WHITE);
            textY += 20;
            index++;
        }
    }

    /**
     * Returns a display string for the enemy's intent.
     * 
     * @param intent the enemy action intent
     * @param enemy  the enemy (for damage/defense values)
     * @return a formatted string describing the intent
     */
    private static String getIntentDisplay(EnemyAction intent, Enemy enemy) {
        if (intent == null)
            return "???";
        return switch (intent) {
            case ATTACK -> "ATTACK (" + enemy.getAttack() + " dmg)";
            case DEFEND -> "DEFEND (+" + enemy.getDefense() + " block)";
        };
    }

    /**
     * Returns a color for the enemy's intent.
     * 
     * @param intent the enemy action intent
     * @return a color representing the intent type
     */
    private static Color getIntentColor(EnemyAction intent) {
        if (intent == null)
            return Color.GRAY;
        return switch (intent) {
            case ATTACK -> new Color(255, 100, 100); 
            case DEFEND -> new Color(100, 150, 255); 
        };
    }
    
    private static void drawMerchant(Graphics2D screen, GameState state) {
        var room = state.getCurrentFloor().getRoom(state.getPosition());
        if (room == null || room.getType() != RoomType.MERCHANT || state.isInCombat()) {
            return;
        }

        int x = 520;
        int y = 40;
        int w = 250;
        int h = 220;

        screen.setColor(new Color(30, 30, 30));
        screen.fillRect(x, y, w, h);
        screen.setColor(Color.WHITE);
        screen.drawRect(x, y, w, h);
        screen.setFont(screen.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        screen.drawString("MERCHANT", x + 10, y + 25);
        screen.setFont(screen.getFont().deriveFont(java.awt.Font.PLAIN, 14f));
        screen.drawString("Click an item to buy", x + 10, y + 45);
        screen.drawString("Gold: " + state.getBackpack().goldAmount(), x + 10, y + 65);

        var items = room.getMerchantItems();
        if (items == null || items.isEmpty()) {
            screen.drawString("(empty)", x + 10, y + 95);
            return;
        }

        var list = new java.util.ArrayList<>(items.entrySet());
        list.sort(java.util.Comparator.comparing(e -> e.getKey().getName()));

        int lineY = y + 95;
        int lineH = 22;

        for (int i = 0; i < list.size() && i < 6; i++) {
            var entry = list.get(i);
            String name = entry.getKey().getName();
            int price = entry.getValue();
            screen.drawString((i + 1) + ") " + name + " - " + price + "g", x + 10, lineY + i * lineH);
        }
    }
    
    private static void drawHealerPrompt(Graphics2D screen, GameState state) {
        if (!state.isHealerPromptOpen() || state.isInCombat()) return;

        int boxX = BACKPACK_PIXEL_WIDTH + 60;
        int boxY = 80;
        int boxW = 320;
        int boxH = 180;

        screen.setColor(new Color(20, 20, 20));
        screen.fill(new Rectangle2D.Float(boxX, boxY, boxW, boxH));

        screen.setColor(Color.WHITE);
        screen.draw(new Rectangle2D.Float(boxX, boxY, boxW, boxH));

        int heal = state.getHealerHealAmount();
        int cost = state.getHealerCost();

        screen.drawString("HEALER", boxX + 20, boxY + 30);
        screen.drawString("Heal: +" + heal + " HP", boxX + 20, boxY + 60);
        screen.drawString("Cost: " + cost + " gold", boxX + 20, boxY + 80);
        screen.drawString("Accept or Leave?", boxX + 20, boxY + 105);

        drawButton(screen, boxX + 30, boxY + 120, 110, 35, "ACCEPT");
        drawButton(screen, boxX + 180, boxY + 120, 110, 35, "LEAVE");
    }

    private static void drawButton(Graphics2D screen, int x, int y, int w, int h, String text) {
        screen.setColor(new Color(60, 60, 60));
        screen.fill(new Rectangle2D.Float(x, y, w, h));
        screen.setColor(Color.WHITE);
        screen.draw(new Rectangle2D.Float(x, y, w, h));
        screen.drawString(text, x + 20, y + 23);
    }
    
    private static void drawStatsBox(Graphics2D screen, GameState state) {
        int x = 10;
        int y = 420; 
        int w = BACKPACK_PIXEL_WIDTH - 20;
        int h = 100;

        var hero = state.getHero();
        int hp = hero.getHp();
        int maxHp = hero.getMaxHp();
        int gold = state.getBackpack().goldAmount();

        screen.setColor(new Color(20, 20, 20));
        screen.fill(new Rectangle2D.Float(x, y, w, h));
        screen.setColor(Color.WHITE);
        screen.draw(new Rectangle2D.Float(x, y, w, h));

       

     
        screen.drawString("HP: " + hp + "/" + maxHp, x + 10, y + 45);
        screen.drawString("Gold: " + gold, x + 10, y + 70);
    }
    
    private static void drawGameOver(Graphics2D screen, int width, int height) {
        screen.setColor(Color.RED);
        screen.setFont(new Font("Arial", Font.BOLD, 48));
        screen.drawString("GAME OVER", width / 2 - 160, height - 120);

        screen.setFont(new Font("Arial", Font.PLAIN, 18));
        screen.drawString("Quit: Q", width / 2 - 70, height - 80);
        screen.drawString("Restart: Z", width / 2 - 90, height - 55);
    }




}
