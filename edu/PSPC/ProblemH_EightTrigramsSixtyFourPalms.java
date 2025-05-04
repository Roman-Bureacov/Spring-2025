package PSPC;

import java.io.FileNotFoundException;
import java.util.Scanner;

public final class ProblemH_EightTrigramsSixtyFourPalms {
    public static void main(final String... pArgs) throws FileNotFoundException {
        final Scanner lInput = new Scanner(Utility.getFile("Hin"));

        while (lInput.hasNextInt()) {
            final int lStepsPerNode = 4;
            final int lNumberLeafNodes = lInput.nextInt();
            // log_2 (leaf count) by log base conversion
            final int lNumberLevels = (int) (Math.log(lNumberLeafNodes) / Math.log(2));

            int lStepSum = 0;
            for (int level = 0; level < lNumberLevels; level++) {
                lStepSum += lStepsPerNode * (int) Math.pow(2, level);
            }

            System.out.format("Neji makes %d steps completing the jutsu.\n", lStepSum);
        }
    }
}
