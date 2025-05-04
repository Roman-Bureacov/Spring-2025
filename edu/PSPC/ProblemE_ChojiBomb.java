package PSPC;

import java.io.FileNotFoundException;
import java.util.Scanner;

public final class ProblemE_ChojiBomb {
    public static void main(final String... pArgs) throws FileNotFoundException {
        final Scanner lInput = new Scanner(Utility.getFile("Ein"));

        while (lInput.hasNextLine()) {
            final Scanner lLine = new Scanner(lInput.nextLine());

            int lChakraSum = 0;

            while (lLine.hasNextInt()) {
                lChakraSum += lLine.nextInt();
            }

            final int lVolume = (int) (4d/3d * Math.PI * Math.pow( lChakraSum / 100d, 3));
            System.out.println("Chouji can grow to %d meters cubed.".formatted(lVolume));
        }
    }
}
