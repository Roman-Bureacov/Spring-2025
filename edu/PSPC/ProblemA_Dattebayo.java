package PSPC;

import java.io.FileNotFoundException;
import java.util.Scanner;

public final class ProblemA_Dattebayo {
    public static void main(final String... pArgs) throws FileNotFoundException {
        final Scanner lInput = new Scanner(Utility.getFile("DattebayoSample"));

        final StringBuilder lBuilder = new StringBuilder();

        while (lInput.hasNextLine()) {
            final String lLine = lInput.nextLine();
            final Scanner lWords = new Scanner(lLine);
            while (lWords.hasNext()) {
                final String lWord = lWords.next();
                if ("desu.".equals(lWord)) {
                    lBuilder.append("dattebayo!");
                } else {
                    lBuilder.append("%s ".formatted(lWord));
                }
            }
            lBuilder.append("\n");
        }

        System.out.println(lBuilder);
    }
}
