package PSPC.samples;

import PSPC.Utility;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class EvenOrOdd {

    public static void main(final String... pArgs) throws FileNotFoundException {
        final Scanner lInput = new Scanner(Utility.getFile("EvenOrOddSample"));

        final StringBuilder lBuilder = new StringBuilder();

        while (lInput.hasNext()) {
            final int lValue = lInput.nextInt();
            if (lValue == 0) {
                break;
            }
            final boolean isEven = lValue % 2 == 0;
            lBuilder.append("The number %d is %s.\n".formatted(lValue, isEven ? "EVEN" : "ODD"));
        }

        System.out.print(lBuilder);
    }
}
