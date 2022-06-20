package Logic;

public class Main {
    public static void main(String[] args) {
        Map m = new GenerateBasicMap(10, 6).get();
        Utilities.printMap(m);
    }
}
