package PSPC;

import java.io.FileNotFoundException;
import java.util.Scanner;

public final class ProblemB_RocksBalancingRocks {
    public static void main(final String... pArgs) throws FileNotFoundException {
        final Scanner lInput = new Scanner(Utility.getFile("Bin"));

        while (lInput.hasNextLine()) {
            final Scanner lLine = new Scanner(lInput.nextLine());
            final int lStartingRocks = lLine.nextInt();
            final int lDays = lLine.nextInt();

            int lTotalRocks = lStartingRocks;

            for (int currentDay = 1; currentDay < lDays; currentDay++) {
                final int lNewDayRocks = lStartingRocks * (int) Math.pow(2, currentDay);
                lTotalRocks += lNewDayRocks;
            }

            System.out.format(
                    "Rock Lee carries %d rocks in the springtime of his youth!\n", lTotalRocks
            );
        }

    }
}
