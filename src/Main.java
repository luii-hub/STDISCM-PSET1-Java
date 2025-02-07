
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {

    static int input;
    static int X =-1; // Number of threads
    static int y = -1; // Range of numbers to search for prime numbers
    static Scanner scan = new Scanner(System.in);
    static String filePath = "src/config.txt";

    public static void main(String[] args) {


        getConfigSettings();
        A1B1 a1b1 = new A1B1();
        A2B1 a2b1 = new A2B1();

        A1B2 a1b2 = new A1B2();
        A2B2 a2b2 = new A2B2();

        do {
            printUI();

            input = getIntegerInput();
            switch (input) {
                case 1 -> {
                    clearScreen();
                    a1b1.run(X, y);
                    waitContinue();
                }
                case 2 -> {
                    clearScreen();
                    a1b2.run(X, y);
                    waitContinue();
                }
                case 3 -> {
                    clearScreen();
                    a2b1.run(X, y);
                    waitContinue();
                }
                case 4 -> {
                    clearScreen();
                    a2b2.run(X, y);
                    waitContinue();
                }
                case 0 -> System.out.println("\n   Exiting...\n");
                default -> System.out.println("Invalid input. Please try again.");
            }
        } while (input != 0);

    }

    private static void getConfigSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read config file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(" ");

                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    try {
                        int parsedValue = Integer.parseInt(value);

                        if (key.equalsIgnoreCase("X")) {
                            X = parsedValue;
                        } else if (key.equalsIgnoreCase("y")) {
                            y = parsedValue;
                        }
                    } catch (NumberFormatException e) {
                        setConfigSettings();
                        return;
                    }
                }
            }

            // Validate X and y
            if (X <= 0 || y <= 0 || X > y) {
                System.out.println("Error: Invalid config values (X=" + X + ", y=" + y + "). Switching to manual input...");
                setConfigSettings();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: Config file not found at " + filePath + ". Switching to manual input...");
            setConfigSettings();
        } catch (IOException e) {
            System.out.println("Error reading config file. Switching to manual input...");
            setConfigSettings();
        }
    }
    private static void setConfigSettings() {

        do {
            System.out.println("\n Invalid Config configuration. Please enter the number of threads and limit manually. Note that the number of threads (X) should not be greater than the limit (y).");

            System.out.print("\n Enter the number of threads (X): ");
            X = scan.nextInt();

            System.out.print("\n Enter the maximum range (y): ");
            y = scan.nextInt();
        } while (X > y);

    }
    private static void printUI() {
        clearScreen();
        System.out.println("\n    STDISCM - Threaded Prime Number Search - Problem Set #1");
        System.out.println("    Threads: " + X + " | Range: " + y);
        System.out.println("\n    Immediate Print                 Delayed Print");
        System.out.println("    [1] Straight Division           [3] Straight Division");
        System.out.println("    [2] Threaded Division           [4] Threaded Division");
        System.out.println("\n    [0] - Exit");
        System.out.print("\n    Select Variant: ");
    }
    private static int getIntegerInput() {
        Scanner scanner = new Scanner(System.in);
        int input;

        while (true) { // Keep looping until valid input
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input >= 0 && input <= 4) {
                    return input; // Return valid input (0-4)
                } else {
                    System.out.print("   Select Variant:");
                }
            } else {
                printUI();
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Error clearing screen.");
        }
    }
    private static void waitContinue() {
        System.out.println("\nPress Enter to continue...\n");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
    public static String getFormattedTimeStamp(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // Format with milliseconds
        return sdf.format(new Date(time));
    }
}

