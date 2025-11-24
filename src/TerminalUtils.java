import java.io.IOException;

public final class TerminalUtils {

    private TerminalUtils() {}    //UTILITY CLASS---> no instantiation

    //Attempts to clear console screen based on the detected os
    //(falls back to ANSI if command fails)
    public static void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();

            try {
                if (os.contains("win")) {//windows
                    new ProcessBuilder("cmd", "/c", "cls")
                            .inheritIO()
                            .start()
                            .waitFor();
                } else {//unix-like (mac, linux etc)
                    System.out.println("\033[H\033[2J");
                    System.out.flush();
                }
            } catch (IOException | InterruptedException e) {
                //make sure screen at least "breaks"
                System.out.println("\n\n-----------------------------------\n\n");
            }
    }
}
