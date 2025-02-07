import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class A1B2 {

    public void run(int threads, int range) {
        Main.clearScreen();
        System.out.println("\nImmediate Printing - Threaded Division\n");

        ExecutorService executor = Executors.newFixedThreadPool(threads); // Thread pool

        for (int num = 2; num <= range; num++) {
            long startTime = System.currentTimeMillis(); // Timestamp Start
            int threadNumber = ((num - 2) % threads) + 1; // Cycle thread IDs from 1 → X

            if (isPrime(num, executor, threads)) {
                System.out.println("[Thread " + threadNumber + " | " + Main.getFormattedTimeStamp(startTime) + "] Found prime: " + num);
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all threads to complete
        }
    }

    private boolean isPrime(int number, ExecutorService executor, int threads) {
        if (number < 2) return false;
        if (number == 2) return true;
        if (number % 2 == 0) return false; // Remove all even numbers

        int sqrt = (int) Math.sqrt(number);
        AtomicBoolean isPrime = new AtomicBoolean(true);

        ExecutorService divisorExecutor = Executors.newFixedThreadPool(threads);

        for (int i = 3; i <= sqrt; i += 2) {
            int divisor = i;
            divisorExecutor.execute(() -> {
                if (number % divisor == 0) {
                    isPrime.set(false);
                }
            });
        }

        divisorExecutor.shutdown();
        while (!divisorExecutor.isTerminated()) {
            // Wait for all divisor-checking threads to complete
        }

        return isPrime.get();
    }

}
