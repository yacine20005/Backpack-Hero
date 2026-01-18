package fr.uge.backpackhero.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

import fr.uge.backpackhero.Main;
import fr.uge.backpackhero.logic.EnemyAction;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.logic.MerchantMode;
import fr.uge.backpackhero.logic.PopupType;
import fr.uge.backpackhero.logic.State;
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
 */
public class View {

    private final GameState state;

    /**
     * Creates a new View instance.
     * 
     * @param state the game state to render
     */
    public View(GameState state) {
        this.state = Objects.requireNonNull(state, "state cannot be null");
    }

    /** The size of each tile in pixels. */
    public static final int TILE_SIZE = 100;
    /** The width of the backpack display area in tiles. */
    public static final int BACKPACK_WIDTH_IN_TILES = 7;
    /** The width of the backpack display area in pixels. */
    public static final int BACKPACK_PIXEL_WIDTH = BACKPACK_WIDTH_IN_TILES * TILE_SIZE;

    // Popup positioning constants
    /** X position for all left sidebar popups. */
    public static final int POPUP_X = 10;
    /** Y position for all interactive popups. */
    public static final int POPUP_Y = 670;
    /** Width for all popups. */
    public static final int POPUP_WIDTH = BACKPACK_PIXEL_WIDTH - 20;
    /** Padding inside popups. */
    public static final int POPUP_PADDING = 10;
    /** Spacing between text lines in popups. */
    public static final int POPUP_LINE_SPACING = 25;
    /** Standard popup height. */
    public static final int POPUP_HEIGHT = 150;

    // Button constants
    /** Standard button width. */
    public static final int BUTTON_WIDTH = 110;
    /** Standard button height. */
    public static final int BUTTON_HEIGHT = 35;
    /** Small button width for mode switches. */
    public static final int SMALL_BUTTON_WIDTH = 80;
    /** Small button height for mode switches. */
    public static final int SMALL_BUTTON_HEIGHT = 25;
    /** Button spacing (horizontal gap between buttons). */
    public static final int BUTTON_SPACING = 50;
    /** Button text offset X. */
    public static final int BUTTON_TEXT_X = 20;
    /** Button text offset Y. */
    public static final int BUTTON_TEXT_Y = 23;
    
    // Common colors
    /** Dark background for popups. */
    public static final Color DARK_BG = new Color(20, 20, 20);
    /** Semi-transparent dark overlay. */
    public static final Color OVERLAY_BG = new Color(0, 0, 0, 180);
    /** Button/box gray. */
    public static final Color BOX_GRAY = new Color(60, 60, 60);
    
    // Font sizes
    /** Large title font size. */
    public static final float FONT_LARGE = 20f;
    /** Medium font size. */
    public static final float FONT_MEDIUM = 14f;
    /** Small font size. */
    public static final float FONT_SMALL = 12f;

    /**
     * Draws the entire game view including backpack, dungeon, hero, and combat
     * interface.
     * 
     * @param context the application context
     */
    public void draw(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        context.renderFrame(screen -> {

            var screenInfo = context.getScreenInfo();
            var width = (int) screenInfo.width();
            var height = (int) screenInfo.height();

            clearScreen(screen, width, height);
            drawBackpack(screen);
            drawStatsBox(screen);
            drawDungeon(screen, state.getCurrentFloor());
            drawHero(screen, state.getPosition());
            drawHealerPrompt(screen);
            drawMerchant(screen);
            drawLootScreen(screen);

            // Draw discard confirmation popup (must be after merchant to overlay it)
            if (state.getActivePopup() == PopupType.DISCARD_CONFIRM) {
                drawDiscardConfirmPopup(screen);
            }

            if (state.getState() == State.COMBAT && state.getState() != State.LOOT_SCREEN) {
                drawCombat(screen, state, height);
            }
            if (state.isGameOver()) {
                drawGameOver(screen, width, height, state);
            }
            if (state.isVictory()) {
                drawVictory(screen, width, height, state);
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
    private void drawItemCell(Graphics2D screen, Item item, Position cellPos, boolean isAnchor,
            boolean isSelected) {
        int x = cellPos.x() * TILE_SIZE;
        int y = cellPos.y() * TILE_SIZE;

        Color itemColor = switch (item) {
            case Armor _ -> new Color(139, 69, 19);
            default -> new Color(100, 0, 0);
        };

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
    private void drawBackpack(Graphics2D screen) {
        var backpack = state.getBackpack();
        var screenInfo = screen.getDeviceConfiguration().getBounds();
        int screenHeight = (int) screenInfo.getHeight();

        screen.setColor(DARK_BG);
        screen.fillRect(0, 0, BACKPACK_PIXEL_WIDTH, screenHeight);

        int backpackHeightInTiles = backpack.getHeight();

        drawBackpackGrid(screen, backpack, state, backpackHeightInTiles);

        drawItems(screen, backpack, state);
    }

    /**
     * Draws all items in the backpack.
     * 
     * @param screen   the graphics context to draw on
     * @param backpack the backpack containing the items to draw
     * @param state    the current game state
     */
    private void drawItems(Graphics2D screen, Backpack backpack, GameState state) {
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
     * Draws the grid of the backpack with locked cells grayed out.
     * 
     * @param screen        the graphics context to draw on
     * @param backpack      the backpack to get unlocked cells from
     * @param state         the game state to check for unlock mode
     * @param heightInTiles the height of the backpack in tiles
     */
    private void drawBackpackGrid(Graphics2D screen, Backpack backpack, GameState state, int heightInTiles) {
        screen.setStroke(new BasicStroke(1));

        for (int y = 0; y < heightInTiles; y++) {
            for (int x = 0; x < BACKPACK_WIDTH_IN_TILES; x++) {
                var pos = new Position(x, y);
                boolean isUnlocked = backpack.isUnlocked(pos);
                boolean canUnlock = state.getState() == State.CELL_UNLOCK && backpack.canUnlockCell(pos);
                
                // Draw cell background
                if (canUnlock) {
                    // Unlockable cells are highlighted in green
                    screen.setColor(new Color(0, 150, 0, 100));
                    screen.fillRect(
                            x * TILE_SIZE,
                            y * TILE_SIZE,
                            TILE_SIZE,
                            TILE_SIZE);
                } else if (!isUnlocked) {
                    // Locked cells are dark gray
                    screen.setColor(new Color(50, 50, 50));
                    screen.fillRect(
                            x * TILE_SIZE,
                            y * TILE_SIZE,
                            TILE_SIZE,
                            TILE_SIZE);
                }
                
                // Draw cell border
                screen.setColor(canUnlock ? Color.GREEN : (isUnlocked ? Color.GRAY : new Color(80, 80, 80)));
                screen.drawRect(
                        x * TILE_SIZE,
                        y * TILE_SIZE,
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
    private void drawDungeon(Graphics2D screen, Floor floor) {
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
    private void drawOneRoom(Graphics2D screen, Room room, int x, int y, int xOffset) {
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
    private void drawHero(Graphics2D screen, Position heroPos) {
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
    private Color getColorForRoom(RoomType type) {
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
    private void drawCombat(Graphics2D screen, GameState state, int screenHeight) {
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
        screen.setColor(OVERLAY_BG);
        screen.fillRect(x - POPUP_PADDING, y - POPUP_LINE_SPACING, panelWidth + POPUP_PADDING * 2, panelHeight + 40);
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
        textY += 20;

        // Display end turn hint
        screen.setFont(oldFont.deriveFont(FONT_MEDIUM));
        screen.setColor(Color.YELLOW);
        screen.drawString("Press X to end turn | CTRL to cycle target", textX, textY);
        screen.setColor(Color.WHITE);
        screen.setFont(oldFont.deriveFont(18f));
        textY += 20;

        screen.drawString("Ennemis :", textX, textY);
        textY += 20;

        int selectedIndex = combat.getSelectedEnemyIndex();
        int index = 0;
        for (Enemy enemy : enemies) {
            EnemyAction intent = combat.getEnemyIntent(enemy);
            String intentStr = getIntentDisplay(intent, enemy);

            // Check if enemy is dead
            boolean isDead = !enemy.isAlive();

            // Highlight selected target (show even if dead so player knows)
            if (index == selectedIndex) {
                screen.setColor(isDead ? Color.GRAY : Color.RED);
                screen.drawString("→ [TARGET]", textX, textY);
                screen.setColor(Color.WHITE);
            }

            // Gray out dead enemies
            if (isDead) {
                screen.setColor(Color.GRAY);
            }

            String statusIndicator = isDead ? " [DEAD]" : "";
            screen.drawString((index + 1) + ". " + enemy.getName()
                    + " HP=" + enemy.getHp()
                    + " Block=" + enemy.getDefense() + statusIndicator,
                    textX + (index == selectedIndex ? 100 : 0), textY);

            if (isDead) {
                screen.setColor(Color.WHITE);
            }
            textY += 20;

            // Don't show intent for dead enemies
            if (!isDead) {
                screen.setColor(getIntentColor(intent));
                screen.drawString("   → Intent: " + intentStr, textX, textY);
                screen.setColor(Color.WHITE);
                textY += 20;
            }

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
    private String getIntentDisplay(EnemyAction intent, Enemy enemy) {
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
    private Color getIntentColor(EnemyAction intent) {
        if (intent == null)
            return Color.GRAY;
        return switch (intent) {
            case ATTACK -> new Color(255, 100, 100);
            case DEFEND -> new Color(100, 150, 255);
        };
    }

    /**
     * Draws the merchant interface, including buy/sell modes and item listings.
     * 
     * @param screen the graphics context
     */
    private void drawMerchant(Graphics2D screen) {
        var room = state.getCurrentFloor().getRoom(state.getPosition());
        if (room == null || room.getType() != RoomType.MERCHANT || state.getState() == State.COMBAT) {
            return;
        }

        int x = POPUP_X;
        int y = POPUP_Y;
        int w = POPUP_WIDTH;
        int h = 280;

        screen.setColor(new Color(30, 30, 30));
        screen.fillRect(x, y, w, h);
        screen.setColor(Color.WHITE);
        screen.drawRect(x, y, w, h);
        screen.setFont(screen.getFont().deriveFont(Font.BOLD, FONT_LARGE - 4f));
        screen.drawString("MERCHANT", x + POPUP_PADDING, y + POPUP_LINE_SPACING);
        screen.setFont(screen.getFont().deriveFont(Font.PLAIN, FONT_MEDIUM));
        screen.drawString("Gold: " + state.getBackpack().goldAmount(), x + POPUP_PADDING, y + 45);

        // Draw BUY/SELL buttons
        int btnY = y + 60;
        int btnW = SMALL_BUTTON_WIDTH;
        int btnH = SMALL_BUTTON_HEIGHT;
        boolean isBuyMode = state.getMerchantMode() == MerchantMode.BUY;

        // BUY button
        screen.setColor(isBuyMode ? new Color(0, 150, 0) : BOX_GRAY);
        screen.fillRect(x + POPUP_PADDING, btnY, btnW, btnH);
        screen.setColor(Color.WHITE);
        screen.drawRect(x + POPUP_PADDING, btnY, btnW, btnH);
        screen.drawString("BUY (B)", x + POPUP_PADDING + 15, btnY + 18);

        // SELL button
        screen.setColor(!isBuyMode ? new Color(150, 0, 0) : BOX_GRAY);
        screen.fillRect(x + POPUP_PADDING + btnW + POPUP_PADDING, btnY, btnW, btnH);
        screen.setColor(Color.WHITE);
        screen.drawRect(x + POPUP_PADDING + btnW + POPUP_PADDING, btnY, btnW, btnH);
        screen.drawString("SELL (S)", x + POPUP_PADDING + btnW + BUTTON_TEXT_X + 3, btnY + 18);

        int lineY = y + 105;

        if (isBuyMode) {
            var items = room.getMerchantItems();
            if (items == null || items.isEmpty()) {
                screen.drawString("(no items for sale)", x + POPUP_PADDING, lineY);
                return;
            }
            var list = new ArrayList<>(items.entrySet());

            String keys = "AZERTYUIOP".substring(0, Math.min(list.size(), 10));
            screen.drawString("Press " + keys + " to select item", x + POPUP_PADDING, lineY);

            int itemStartY = lineY + 25;
            int itemHeight = 50;

            String itemKeys = "AZERTYUIOP";
            for (int i = 0; i < list.size() && i < 10; i++) {
                var entry = list.get(i);
                Item item = entry.getKey();
                int price = entry.getValue();
                int itemY = itemStartY + i * itemHeight;

                // Highlight selected item
                boolean isSelected = item.equals(state.getSelectedMerchantItem());
                if (isSelected) {
                    screen.setColor(new Color(100, 200, 100, 100));
                    screen.fill(new Rectangle2D.Float(x + 5, itemY - 5, w - 10, itemHeight));
                }

                // Item box
                screen.setColor(BOX_GRAY);
                screen.fill(new Rectangle2D.Float(x + POPUP_PADDING, itemY, w - POPUP_PADDING * 2, itemHeight - 5));
                screen.setColor(Color.WHITE);
                screen.draw(new Rectangle2D.Float(x + POPUP_PADDING, itemY, w - POPUP_PADDING * 2, itemHeight - 5));

                // Item name and info
                screen.setFont(screen.getFont().deriveFont(Font.BOLD, FONT_MEDIUM));
                screen.drawString(itemKeys.charAt(i) + ". " + item.getName(), x + POPUP_PADDING * 2, itemY + BUTTON_TEXT_X);

                screen.setFont(screen.getFont().deriveFont(Font.PLAIN, FONT_SMALL));
                var shape = item.getShape();
                screen.drawString("Size: " + shape.getWidth() + "x" + shape.getHeight() +
                        " | Price: " + price + "g", x + POPUP_PADDING * 2, itemY + 38);
            }
        } else {
            // SELL mode: instruction to click on items
            screen.drawString("Click on an item in your", x + POPUP_PADDING, lineY);
            screen.drawString("backpack to sell it.", x + POPUP_PADDING, lineY + 25);
        }

        // Draw sell confirmation popup
        if (state.getActivePopup() == PopupType.SELL_CONFIRM) {
            drawSellConfirmPopup(screen);
        }

        // Draw discard confirmation popup
        if (state.getActivePopup() == PopupType.DISCARD_CONFIRM) {
            drawDiscardConfirmPopup(screen);
        }
    }

    /**
     * Draws the confirmation popup for discarding an item.
     * 
     * @param screen the graphics context
     */
    private void drawDiscardConfirmPopup(Graphics2D screen) {
        var item = state.getDiscardConfirmItem();
        if (item == null)
            return;

        int boxX = POPUP_X;
        int boxY = POPUP_Y;
        int boxW = POPUP_WIDTH;
        int boxH = 150;

        screen.setColor(new Color(40, 0, 0));
        screen.fill(new Rectangle2D.Float(boxX, boxY, boxW, boxH));
        screen.setColor(Color.RED);
        screen.draw(new Rectangle2D.Float(boxX, boxY, boxW, boxH));

        screen.setColor(Color.ORANGE);
        screen.drawString("DISCARD ITEM?", boxX + POPUP_PADDING * 2, boxY + 30);
        screen.setColor(Color.WHITE);
        screen.drawString(item.getName(), boxX + POPUP_PADDING * 2, boxY + 55);
        screen.setColor(Color.YELLOW);
        screen.drawString("This item will be lost forever!", boxX + POPUP_PADDING * 2, boxY + 75);

        screen.setColor(Color.WHITE);
        drawButton(screen, boxX + POPUP_PADDING * 3, boxY + 100, BUTTON_WIDTH, BUTTON_HEIGHT, "YES (Y)");
        drawButton(screen, boxX + POPUP_PADDING * 3 + BUTTON_WIDTH + 50, boxY + 100, BUTTON_WIDTH, BUTTON_HEIGHT,
                "NO (N)");
    }

    /**
     * Draws the confirmation popup for selling an item.
     * 
     * @param screen the graphics context
     */
    private void drawSellConfirmPopup(Graphics2D screen) {
        var item = state.getSellConfirmItem();
        if (item == null)
            return;

        int sellPrice = item.getPrice() / 2;
        int boxX = POPUP_X;
        int boxY = POPUP_Y;
        int boxW = POPUP_WIDTH;
        int boxH = POPUP_HEIGHT;

        screen.setColor(DARK_BG);
        screen.fill(new Rectangle2D.Float(boxX, boxY, boxW, boxH));
        screen.setColor(Color.WHITE);
        screen.draw(new Rectangle2D.Float(boxX, boxY, boxW, boxH));

        screen.drawString("SELL ITEM?", boxX + POPUP_PADDING * 2, boxY + 30);
        screen.drawString(item.getName(), boxX + POPUP_PADDING * 2, boxY + 55);
        screen.drawString("Sell price: " + sellPrice + " gold", boxX + POPUP_PADDING * 2, boxY + 75);

        drawButton(screen, boxX + POPUP_PADDING * 3, boxY + 100, BUTTON_WIDTH, BUTTON_HEIGHT, "YES (Y)");
        drawButton(screen, boxX + POPUP_PADDING * 3 + BUTTON_WIDTH + 50, boxY + 100, BUTTON_WIDTH, BUTTON_HEIGHT,
                "NO (N)");
    }

    /**
     * Draws the healer prompt interface.
     * 
     * @param screen the graphics context
     */
    private void drawHealerPrompt(Graphics2D screen) {
        if (state.getState() != State.HEALER_PROMPT || state.getState() == State.COMBAT)
            return;

        int boxX = POPUP_X;
        int boxY = POPUP_Y;
        int boxW = POPUP_WIDTH;
        int boxH = POPUP_HEIGHT + 30; // Slightly taller for heal info

        screen.setColor(DARK_BG);
        screen.fill(new Rectangle2D.Float(boxX, boxY, boxW, boxH));

        screen.setColor(Color.WHITE);
        screen.draw(new Rectangle2D.Float(boxX, boxY, boxW, boxH));

        int heal = state.getHealerHealAmount();
        int cost = state.getHealerCost();

        screen.drawString("HEALER", boxX + POPUP_PADDING * 2, boxY + 30);
        screen.drawString("Heal: +" + heal + " HP", boxX + POPUP_PADDING * 2, boxY + 60);
        screen.drawString("Cost: " + cost + " gold", boxX + POPUP_PADDING * 2, boxY + 80);
        screen.drawString("Accept or Leave?", boxX + POPUP_PADDING * 2, boxY + 105);

        drawButton(screen, boxX + POPUP_PADDING * 3, boxY + 120, BUTTON_WIDTH, BUTTON_HEIGHT, "ACCEPT (Y)");
        drawButton(screen, boxX + POPUP_PADDING * 3 + BUTTON_WIDTH + 60, boxY + 120, BUTTON_WIDTH, BUTTON_HEIGHT,
                "LEAVE (N)");
    }

    /**
     * Helper method to draw a button with text.
     * 
     * @param screen the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param w the width of the button
     * @param h the height of the button
     * @param text the text to display on the button
     */
    private void drawButton(Graphics2D screen, int x, int y, int w, int h, String text) {
        screen.setColor(BOX_GRAY);
        screen.fill(new Rectangle2D.Float(x, y, w, h));
        screen.setColor(Color.WHITE);
        screen.draw(new Rectangle2D.Float(x, y, w, h));
        screen.drawString(text, x + BUTTON_TEXT_X, y + BUTTON_TEXT_Y);
    }

    /**
     * Draws the stats box displaying hero information.
     * 
     * @param screen the graphics context
     */
    private void drawStatsBox(Graphics2D screen) {
        int x = POPUP_X;
        int y = 510;
        int w = POPUP_WIDTH;
        int h = 150;

        var hero = state.getHero();
        int hp = hero.getHp();
        int maxHp = hero.getMaxHp();
        int gold = state.getBackpack().goldAmount();
        int level = hero.getLevel();
        int xp = hero.getXp();
        int xpToNext = hero.getXpToNextLevel();

        screen.setColor(DARK_BG);
        screen.fill(new Rectangle2D.Float(x, y, w, h));
        screen.setColor(Color.WHITE);
        screen.draw(new Rectangle2D.Float(x, y, w, h));

        screen.drawString("HP: " + hp + "/" + maxHp, x + POPUP_PADDING, y + 30);
        screen.drawString("Gold: " + gold, x + POPUP_PADDING, y + 55);
        screen.drawString("Level: " + level, x + POPUP_PADDING, y + 80);
        screen.drawString("XP: " + xp + "/" + xpToNext, x + POPUP_PADDING, y + 105);
        
        // Show unlock mode message
        if (state.getState() == State.CELL_UNLOCK) {
            screen.setColor(Color.GREEN);
            screen.drawString("Click " + state.getCellsToUnlock() + " cells to unlock", x + POPUP_PADDING, y + h - 10);
            screen.setColor(Color.WHITE);
        }
    }

    /**
     * Draws the loot screen interface.
     * 
     * @param screen the graphics context
     */
    private void drawLootScreen(Graphics2D screen) {
        if (state.getState() != State.LOOT_SCREEN)
            return;

        int boxX = POPUP_X;
        int boxY = POPUP_Y;
        int boxW = POPUP_WIDTH;
        int boxH = 420;

        // Background box
        screen.setColor(DARK_BG);
        screen.fill(new Rectangle2D.Float(boxX, boxY, boxW, boxH));

        // Outline
        screen.setColor(Color.WHITE);
        screen.draw(new Rectangle2D.Float(boxX, boxY, boxW, boxH));

        // Title
        screen.setFont(screen.getFont().deriveFont(Font.BOLD, FONT_LARGE));
        String title = state.getState() == State.COMBAT ? "VICTORY!" : "TREASURE!";
        screen.drawString(title, boxX + 120, boxY + 30);

        // Instructions
        screen.setFont(screen.getFont().deriveFont(Font.PLAIN, FONT_MEDIUM));
        screen.drawString("Choose items to take:", boxX + POPUP_PADDING, boxY + 55);

        var loot = state.getAvailableLoot();
        if (loot != null && !loot.isEmpty()) {
            String keys = "AZERTYUIOP".substring(0, Math.min(loot.size(), 10));
            screen.drawString("Press " + keys + " to select item", boxX + POPUP_PADDING, boxY + 75);
        }

        // Display loot items
        if (loot != null && !loot.isEmpty()) {
            int itemStartY = boxY + 100;
            int itemHeight = 50;

            String itemKeys = "AZERTYUIOP";
            for (int i = 0; i < loot.size() && i < 10; i++) {
                Item item = loot.get(i);
                int itemY = itemStartY + i * itemHeight;

                // Highlight selected item
                boolean isSelected = item.equals(state.getSelectedLootItem());
                if (isSelected) {
                    screen.setColor(new Color(100, 100, 200, 100));
                    screen.fill(new Rectangle2D.Float(boxX + 5, itemY - 5, boxW - 10, itemHeight));
                }

                // Item box
                screen.setColor(BOX_GRAY);
                screen.fill(new Rectangle2D.Float(boxX + POPUP_PADDING, itemY, boxW - POPUP_PADDING * 2, itemHeight - 5));
                screen.setColor(Color.WHITE);
                screen.draw(new Rectangle2D.Float(boxX + POPUP_PADDING, itemY, boxW - POPUP_PADDING * 2, itemHeight - 5));

                // Item name and info
                screen.setFont(screen.getFont().deriveFont(Font.BOLD, FONT_MEDIUM));
                screen.drawString(itemKeys.charAt(i) + ". " + item.getName(), boxX + POPUP_PADDING * 2, itemY + BUTTON_TEXT_X);

                screen.setFont(screen.getFont().deriveFont(Font.PLAIN, FONT_SMALL));
                var shape = item.getShape();
                screen.drawString("Size: " + shape.getWidth() + "x" + shape.getHeight() +
                        " | Rarity: " + item.getRarity(), boxX + POPUP_PADDING * 2, itemY + 38);
            }
        } else {
            screen.drawString("No items remaining", boxX + POPUP_PADDING, boxY + 120);
        }

        // Continue button
        int continueX = boxX + boxW - 130;
        int continueY = boxY + 370;
        drawButton(screen, continueX, continueY, BUTTON_WIDTH + 10, BUTTON_HEIGHT, "CONTINUE (C)");
    }

    /**
     * Draws the Game Over screen.
     * 
     * @param screen the graphics context
     * @param width the screen width
     * @param height the screen height
     * @param state the game state
     */
    private void drawGameOver(Graphics2D screen, int width, int height, GameState state) {
        screen.setColor(Color.RED);
        screen.setFont(new Font("Arial", Font.BOLD, 48));
        screen.drawString("GAME OVER", width / 2 - 160, height - 400);

        // Show final score
        screen.setColor(Color.WHITE);
        screen.setFont(new Font("Arial", Font.PLAIN, 24));
        int score = state.calculateScore();
        screen.drawString("Final Score: " + score, width / 2 - 80, height - 340);
        
        // Show Hall of Fame
        drawHallOfFame(screen, width, height - 280);

        // Controls
        screen.setFont(new Font("Arial", Font.PLAIN, 18));
        screen.drawString("Quit: Q", width / 2 - 70, height - 80);
        screen.drawString("Restart: Z", width / 2 - 90, height - 55);
    }

    /**
     * Draws the Victory screen.
     * 
     * @param screen the graphics context
     * @param width the screen width
     * @param height the screen height
     * @param state the game state
     */
    private void drawVictory(Graphics2D screen, int width, int height, GameState state) {
        // Background overlay
        screen.setColor(OVERLAY_BG);
        screen.fill(new Rectangle2D.Float(0, 0, width, height));

        // Victory text
        screen.setColor(Color.YELLOW);
        screen.setFont(new Font("Arial", Font.BOLD, 60));
        screen.drawString("VICTORY!", width / 2 - 150, 100);

        // Show final score
        screen.setColor(Color.WHITE);
        screen.setFont(new Font("Arial", Font.PLAIN, 24));
        int score = state.calculateScore();
        screen.drawString("Final Score: " + score, width / 2 - 80, 180);

        // Congratulations
        screen.setFont(new Font("Arial", Font.PLAIN, 20));
        screen.drawString("You escaped the dungeon!", width / 2 - 120, 220);
        
        // Show Hall of Fame
        drawHallOfFame(screen, width, 270);

        // Controls
        screen.setFont(new Font("Arial", Font.PLAIN, 18));
        screen.drawString("Quit: Q", width / 2 - 70, height - 80);
        screen.drawString("Restart: Z", width / 2 - 90, height - 50);
    }
    
    /**
     * Draws the Hall of Fame leaderboard.
     * 
     * @param screen the graphics context
     * @param width the screen width
     * @param startY the Y position to start drawing
     */
    private void drawHallOfFame(Graphics2D screen, int width, int startY) {
        var hallOfFame = Main.getHallOfFame();
        var entries = hallOfFame.getTopScores();
        
        if (entries.isEmpty()) {
            return;
        }
        
        // Title
        screen.setColor(Color.YELLOW);
        screen.setFont(new Font("Arial", Font.BOLD, 32));
        screen.drawString("HALL OF FAME", width / 2 - 100, startY);
        
        // Entries
        screen.setFont(new Font("Arial", Font.PLAIN, 20));
        int y = startY + 50;
        
        for (int i = 0; i < entries.size(); i++) {
            var entry = entries.get(i);
            String rankingTitle = switch (i) {
                case 0 -> "[First Place] - ";
                case 1 -> "[Second Place] - ";
                case 2 -> "[Third Place] - ";
                default -> (i + 1) + ".";
            };
            
            screen.setColor(Color.WHITE);
            String line = String.format("%s %s - Score: %d (Level %d)", 
                rankingTitle, entry.playerName(), entry.score(), entry.level());
            screen.drawString(line, width / 2 - 200, y);
            y += 35;
        }
    }

}
