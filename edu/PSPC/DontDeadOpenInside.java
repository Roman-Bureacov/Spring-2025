package PSPC;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public final class DontDeadOpenInside {

    public static void main(final String... pArgs) throws FileNotFoundException {
        final File lSampleText;
        final Scanner lTextInput;
        lSampleText = new File("edu\\PSPC\\samples\\DontOpenDeadInsideSample01");

        lTextInput = new Scanner(lSampleText);

        final List<String[]> lTextRows = new LinkedList<>();
        // split into lines with tokens
        while (lTextInput.hasNextLine()) {
            final String lTextLine = lTextInput.nextLine();
            final String[] lLineTokens = lTextLine.split(" ");
            lTextRows.add(lLineTokens);
        }


        // output string
        final StringBuilder lBuilder = new StringBuilder();

        // process each token of each line
        final int lMaxLength = lTextRows.getFirst().length;
        // for each col
        for (int col = 0; col < lMaxLength; col++) {
            // for each row
            for (final String[] line : lTextRows) {
                if (col < line.length) lBuilder.append(" %s".formatted(line[col]));
            }
            lBuilder.append("\n");
        }

        System.out.println(lBuilder);
    }
}
