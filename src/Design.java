public class Design {
    public static String LINE = "----------------------------------------";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void escape(){
        System.out.println("0: 戻る");

    }
}
