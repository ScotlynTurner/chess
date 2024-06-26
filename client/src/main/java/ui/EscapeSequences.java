package ui;

/**
 * This class contains constants and functions relating to ANSI Escape Sequences that are useful in the Client display
 */
public class EscapeSequences {

  private static final String UNICODE_ESCAPE = "\u001b";
  private static final String ANSI_ESCAPE = "\033";

  public static final String ERASE_SCREEN = UNICODE_ESCAPE + "[H" + UNICODE_ESCAPE + "[2J";
  public static final String ERASE_LINE = UNICODE_ESCAPE + "[2K";

  public static final String SET_TEXT_BOLD = UNICODE_ESCAPE + "[1m";
  public static final String SET_TEXT_FAINT = UNICODE_ESCAPE + "[2m";
  public static final String RESET_TEXT_BOLD_FAINT = UNICODE_ESCAPE + "[22m";
  public static final String SET_TEXT_ITALIC = UNICODE_ESCAPE + "[3m";
  public static final String RESET_TEXT_ITALIC = UNICODE_ESCAPE + "[23m";
  public static final String SET_TEXT_UNDERLINE = UNICODE_ESCAPE + "[4m";
  public static final String RESET_TEXT_UNDERLINE = UNICODE_ESCAPE + "[24m";
  public static final String SET_TEXT_BLINKING = UNICODE_ESCAPE + "[5m";
  public static final String RESET_TEXT_BLINKING = UNICODE_ESCAPE + "[25m";

  private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
  private static final String SET_BG_COLOR = UNICODE_ESCAPE + "[48;5;";

  public static final String SET_TEXT_COLOR_BLACK = SET_TEXT_COLOR + "0m";
  public static final String SET_TEXT_COLOR_LIGHT_GREY = SET_TEXT_COLOR + "242m";
  public static final String SET_TEXT_COLOR_DARK_GREY = SET_TEXT_COLOR + "235m";
  public static final String SET_TEXT_COLOR_RED = SET_TEXT_COLOR + "160m";
  public static final String SET_TEXT_COLOR_GREEN = SET_TEXT_COLOR + "46m";
  public static final String SET_TEXT_COLOR_YELLOW = SET_TEXT_COLOR + "226m";
  public static final String SET_TEXT_COLOR_BLUE = SET_TEXT_COLOR + "12m";
  public static final String SET_TEXT_COLOR_MAGENTA = SET_TEXT_COLOR + "5m";
  public static final String SET_TEXT_COLOR_WHITE = SET_TEXT_COLOR + "15m";
  public static final String RESET_TEXT_COLOR = SET_TEXT_COLOR + "0m";

  public static final String SET_BG_COLOR_BLACK = SET_BG_COLOR + "0m";
  public static final String SET_BG_COLOR_LIGHT_GREY = SET_BG_COLOR + "242m";
  public static final String SET_BG_COLOR_DARK_GREY = SET_BG_COLOR + "235m";
  public static final String SET_BG_COLOR_RED = SET_BG_COLOR + "160m";
  public static final String SET_BG_COLOR_GREEN = SET_BG_COLOR + "46m";
  public static final String SET_BG_COLOR_DARK_GREEN = SET_BG_COLOR + "22m";
  public static final String SET_BG_COLOR_YELLOW = SET_BG_COLOR + "226m";
  public static final String SET_BG_COLOR_MAGENTA = SET_BG_COLOR + "5m";
  public static final String SET_BG_COLOR_WHITE = SET_BG_COLOR + "15m";
  public static final String RESET_BG_COLOR = SET_BG_COLOR + "0m";

  public static final String WHITE_KING = " ♔ ";
  public static final String WHITE_QUEEN = " ♕ ";
  public static final String WHITE_BISHOP = " ♗ ";
  public static final String WHITE_KNIGHT = " ♘ ";
  public static final String WHITE_ROOK = " ♖ ";
  public static final String WHITE_PAWN = " ♙ ";
  public static final String BLACK_KING = " ♚ ";
  public static final String BLACK_QUEEN = " ♛ ";
  public static final String BLACK_BISHOP = " ♝ ";
  public static final String BLACK_KNIGHT = " ♞ ";
  public static final String BLACK_ROOK = " ♜ ";
  public static final String BLACK_PAWN = " ♟ ";
  public static final String EMPTY = " \u2003 ";

  public static String moveCursorToLocation(int x, int y) { return UNICODE_ESCAPE + "[" + y + ";" + x + "H"; }

  public static final String SET_BG_BRIGHT_WHITE = "\u001B[48;2;255;255;255m";

  public static final String SET_BG_CUSTOM_MAROON = "\u001B[48;2;155;68;68m";
  public static final String SET_BG_CUSTOM_PINK = "\u001B[48;2;198;132;132m";
  public static final String SET_BG_CUSTOM_MINT = "\u001B[48;2;163;201;170m";
  public static final String SET_BG_CUSTOM_WHITE = "\u001B[48;2;238;238;238m";

  public static final String SET_BG_COLOR_BLUE = SET_BG_COLOR + "12m";

  public static final String SET_BG_CUSTOM_MAROON_FADED = "\u001B[48;2;205;169;169m";
  public static final String SET_BG_CUSTOM_PINK_FADED = "\u001B[48;2;222;195;195m";

  public static final String SET_TEXT_CUSTOM_MAROON = "\u001B[38;2;155;68;68m";
  public static final String SET_TEXT_CUSTOM_PINK = "\u001B[38;2;198;132;132m";
  public static final String SET_TEXT_CUSTOM_MINT = "\u001B[38;2;163;201;170m";
  public static final String SET_TEXT_CUSTOM_WHITE = "\u001B[38;2;238;238;238m";
}