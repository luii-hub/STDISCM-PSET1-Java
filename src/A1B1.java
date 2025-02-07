import java.util.concurrent.atomic.AtomicBoolean;

public class A1B1 {

    public void run(int threads, int range) {
        Main.clearScreen();
        System.out.println("\nImmediate Printing - Straight Division\n");

        // Calculate base size & remainder
        int baseThreadSize = range / threads;
        int remainder = range % threads;

        // Create threads
        Thread[] threadArray = new Thread[threads];
        int start = 1;

        for (int i = 0; i < threads; i++) {
            int extra = (i < remainder) ? 1 : 0;  // Distribute remainder across first threads
            int end = start + baseThreadSize + extra - 1;

            int finalStart = start;
            int finalEnd = end;
            int threadNumber = i + 1; // Assign a manual thread ID (1-based)

            System.out.println("Thread " + threadNumber + " assigned range: " + finalStart + " to " + finalEnd);

            threadArray[i] = new Thread(() -> findPrimes(finalStart, finalEnd, threadNumber));
            threadArray[i].start();

            start = end + 1; // Move start to the next range
        }

        // Wait for all threads to complete
        for (Thread thread : threadArray) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void findPrimes(int start, int end, int threadNumber) {
        long startTime = System.currentTimeMillis();
        String timeStamp = Main.getFormattedTimeStamp(startTime);

        for (int num = start; num <= end; num++) {
            if (isPrime(num)) {
                System.out.println("[Thread " + threadNumber + " | " + timeStamp + "] Found prime: " + num);
            }
        }
    }

    private boolean isPrime(int number) {
        if (number < 2) return false;
        if (number == 2) return true; // 2 is the only even prime
        if (number % 2 == 0) return false; // Eliminate even numbers

        int sqrt = (int) Math.sqrt(number);
        for (int i = 3; i <= sqrt; i += 2) { // Only check odd divisors
            if (number % i == 0) return false; // If divisible, it's not prime
        }
        return true;
    }

}
