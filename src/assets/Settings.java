package assets;

import java.util.HashMap;
import java.util.Map;

public final class Settings {

    private Settings() {}

    public static final int TILE_SIZE = 50;
    public static final int ROWS = 20;
    public static final int COLUMNS = 10;
    public static final int MENU_WIDTH = 8;
    public static final int DELAY = 16; // ms
    public static final int ANIM_TIME_INTERVAL = 250_000_000; // ns
    public static final int WIDTH = 4;
    public static final int[] GAME_LABEL_POSITION = {COLUMNS, 2};
    public static final int[] NEXT_LABEL_POSITION = {COLUMNS+1, 7};
    public static final int[] SCORE_LABEL_POSITION = {COLUMNS+1, 14};
    public static final int[] SCORE_POSITION = {COLUMNS+1, 16};
    public static final int[] INIT_POS_OFFSET = {COLUMNS / 2, 0};
    public static final int[] NEXT_POS_OFFSET = {COLUMNS + MENU_WIDTH / 2 - 1, 10};
    public static final Map<String, int[]> MOVE_DIRECTIONS;

    static {
        MOVE_DIRECTIONS = new HashMap<>();
        MOVE_DIRECTIONS.put("left", new int[] {-1, 0});
        MOVE_DIRECTIONS.put("right", new int[] {1, 0});
        MOVE_DIRECTIONS.put("down", new int[] {0, 1});
    }
    
}
