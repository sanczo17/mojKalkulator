import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInteraction {
    private Scanner scanner;

    public UserInteraction() {
        this.scanner = new Scanner(System.in);
    }

    public String askForString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    public double askForDouble(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("To nie jest prawidlowa liczba. Sprobuj ponownie.");
            }
        }
    }

    public String askForFileName(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    public String askYesNoQuestion(String question) {
        System.out.println(question + " (tak/nie)");
        return scanner.nextLine().trim().toLowerCase();
    }
}
