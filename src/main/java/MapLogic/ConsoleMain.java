package MapLogic;

import static javafx.application.Application.launch;

public class ConsoleMain {

    public static void main(String[] args) {
        Map m = new GenerateBasicMap(10, 6).get();
        Utilities.printMap(m);
    }
}
