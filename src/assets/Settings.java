package assets;

import java.util.HashMap;
import java.util.Map;

public class Settings {

    private Settings() {}

    public static final int TILE_SIZE = 50;
    public static final int ROWS = 20;
    public static final int COLUMNS = 10;
    public static final int DELAY = 16; // ms
    public static final int ANIM_TIME_INTERVAL = 250_000_000; // ns
    public static final int WIDTH = 4;
    public static final int[] INIT_POS_OFFSET = {COLUMNS / 2, 0};
    public static final Map<String, int[]> MOVE_DIRECTIONS;

    static {
        MOVE_DIRECTIONS = new HashMap<>();
        MOVE_DIRECTIONS.put("left", new int[] {-1, 0});
        MOVE_DIRECTIONS.put("right", new int[] {1, 0});
        MOVE_DIRECTIONS.put("down", new int[] {0, 1});
    }
    
}
