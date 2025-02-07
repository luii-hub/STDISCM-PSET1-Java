import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class A2B2 {

    private final List<Integer> collectedPrimes = new ArrayList<>(); // Shared storage for primes

    public void run(int threads, int range) {
        Main.clearScreen();
        System.out.println("\nDelayed Printing - Threaded Division\n");

        long startTime = System.currentTimeMillis(); // Capture start time

        ExecutorService executor = Executors.newFixedThreadPool(threads); // Thread pool for divisibility checking


        // Reset collectedPrimes to prevent duplicate entries on multiple runs
        collectedPrimes.clear();

        for (int num = 2; num <= range; num++) {
            int finalNum = num;
            if (isPrime(finalNum, threads)) {
                synchronized (collectedPrimes) {
                    collectedPrimes.add(finalNum); // Store primes for delayed printing
                }
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all threads to complete
        }

        long endTime = System.currentTimeMillis(); // Capture end time
        long duration = endTime - startTime;

        // Print all collected primes at the end
        System.out.println("\n==== All Prime Numbers Found ====");

        int count = 0;
        for (int prime : collectedPrimes) {
            System.out.print(prime + " ");
            count++;

            if (count % 10 == 0) {
                System.out.println(); // Move to the next line after every 10 numbers
            }
        }
        System.out.println(); // Ensure the last line is printed properly

        // Print execution details
        System.out.println("\nStart Time: " + Main.getFormattedTimeStamp(startTime));
        System.out.println("End Time: " + Main.getFormattedTimeStamp(endTime));
        System.out.println("Duration: " + duration + " ms");
    }

    private boolean isPrime(int number, int threads) {
        if (number < 2) return false;
        if (number == 2) return true;
        if (number % 2 == 0) return false;

        int sqrt = (int) Math.sqrt(number);
        AtomicBoolean isPrime = new AtomicBoolean(true);

        ExecutorService divisorExecutor = Executors.newFixedThreadPool(threads); // Thread pool for divisor checking

        for (int i = 3; i <= sqrt; i += 2) {
            int divisor = i;
            divisorExecutor.execute(() -> {
                if (number % divisor == 0) {
                    isPrime.set(false);
                }
            });

            if (!isPrime.get()) break; // Stop checking if a divisor is found
        }

        divisorExecutor.shutdown();
        while (!divisorExecutor.isTerminated()) {
            // Wait for all divisor-checking threads to complete
        }

        return isPrime.get();
    }
}
