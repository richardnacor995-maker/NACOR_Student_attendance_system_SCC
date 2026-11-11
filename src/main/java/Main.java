import Dao.DatabaseInitializer;
import ui.Authentication;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize tables on startup
        DatabaseInitializer.initialize();

        // Share a single Scanner instance
        Scanner scanner = new Scanner(System.in);
        Authentication auth = new Authentication(scanner);
        auth.start();
    }
}