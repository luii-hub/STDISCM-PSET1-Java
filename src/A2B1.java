import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class A2B1 {

    private final List<List<Integer>> collectedPrimes = new ArrayList<>(); // Store primes by thread

    public void run(int threads, int range) {
        Main.clearScreen();
        System.out.println("\nDelayed Printing - Straight Division\n");

        long startTime = System.currentTimeMillis(); // Capture start time
        ExecutorService executor = Executors.newFixedThreadPool(threads); // Thread pool

        // Reset collectedPrimes to prevent duplicate entries on multiple runs
        collectedPrimes.clear();
        for (int i = 0; i < threads; i++) {
            collectedPrimes.add(new ArrayList<>());
        }

        // Calculate base size & remainder
        int baseThreadSize = range / threads;
        int remainder = range % threads;

        int start = 1;
        for (int i = 0; i < threads; i++) {
            int extra = (i < remainder) ? 1 : 0; // Distribute remainder
            int end = start + baseThreadSize + extra - 1;
            int threadNumber = i + 1; // Manual thread ID

            System.out.println("Thread " + threadNumber + " assigned range: " + start + " to " + end);

            int finalStart = start;
            int finalEnd = end;
            int finalIndex = i; // Store thread index for proper storage

            executor.execute(() -> findPrimes(finalStart, finalEnd, finalIndex));

            start = end + 1; // Move to the next range
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all threads to finish
        }

        long endTime = System.currentTimeMillis(); // Capture end time
        long duration = endTime - startTime;

        // Print all collected primes in grouped format
        System.out.println("\n--- All Prime Numbers Found ---");
        for (int i = 0; i < threads; i++) {
            System.out.print("Thread " + (i + 1) + " Prime(s): ");
            for (int prime : collectedPrimes.get(i)) {
                System.out.print(prime + " ");
            }
            System.out.println();
        }

        // Print execution details
        System.out.println("\nStart Time: " + Main.getFormattedTimeStamp(startTime));
        System.out.println("End Time: " + Main.getFormattedTimeStamp(endTime));
        System.out.println("Duration: " + duration + " ms");
    }

    private void findPrimes(int start, int end, int index) {
        List<Integer> localPrimes = new ArrayList<>(); // Store primes for this thread

        for (int num = start; num <= end; num++) {
            if (isPrime(num)) {
                localPrimes.add(num);
            }
        }

        synchronized (collectedPrimes) {
            collectedPrimes.get(index).addAll(localPrimes); // Merge thread results
        }
    }

    private boolean isPrime(int number) {
        if (number < 2) return false;
        if (number == 2) return true;
        if (number % 2 == 0) return false;

        int sqrt = (int) Math.sqrt(number);
        for (int i = 3; i <= sqrt; i += 2) {
            if (number % i == 0) return false;
        }
        return true;
    }
}
