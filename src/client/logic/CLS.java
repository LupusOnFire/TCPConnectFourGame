package client.logic;

import java.io.IOException;

public class CLS {
    public void clearConsole() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
}
