package PSPC;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

// TODO: Not done

public final class ProblemD_ChuninExamTourny {
    public static void main(final String... pArgs) throws FileNotFoundException {
        final Scanner lInput = new Scanner(Utility.getFile("Din"));

        while (lInput.hasNextLine()) {
            final String lLine = lInput.nextLine();
            if (lLine.matches("^[^a-z^ ]\\d+|^\\d+")) { // match all lines with only an integer
                final int lNumberContestants = Integer.parseInt(lLine);
                final List<String> lContestants = new LinkedList<>();

                for (int i = 0; i < lNumberContestants; i++) {
                    lContestants.add(lInput.nextLine());
                }

                final List<String> lPairs = sortContestants(lContestants);
                final List<String> lSortedBracket = sortBracket(lPairs);

                printMatchingContestants(lSortedBracket);
            }
        }
    }

    /**
     * sorts the contestants based on highest-lowest
     *
     * @param pContestants
     * @return
     */
    private static List<String> sortContestants(final List<String> pContestants) {
        final int lContestantCount = pContestants.size();
        final String[] lContestantNames = new String[lContestantCount];
        final int[] lContestantScores = new int[lContestantCount];

        for (int i = 0; i < lContestantCount; i++) {
            final String[] lElementTokens = pContestants.get(i).split(" ");
            lContestantNames[i] = lElementTokens[0];
            lContestantScores[i] = Integer.parseInt(lElementTokens[1]);
        }

        // sort the two arrays based on the scores
        for (int i = 0; i < lContestantCount; i++) {
            for (int j = i + 1; j < lContestantCount; j++) {
                if (lContestantScores[i] < lContestantScores[j]) {
                    final int lTempScore = lContestantScores[i];
                    lContestantScores[i] = lContestantScores[j];
                    lContestantScores[j] = lTempScore;

                    final String lTempName = lContestantNames[i];
                    lContestantNames[i] = lContestantNames[j];
                    lContestantNames[j] = lTempName;
                }
            }
        }

        // TODO: not sorted right???

        final int lMatchingSize = lContestantCount / 2;
        final List<String> lContestantMatching = new ArrayList<>(lMatchingSize);

        // put pairs into list <highest>-<lowest>
        for (int i = 0; i < lMatchingSize; i++) {
            final String lFirstContestant = lContestantNames[i];
            final String lSecondContestant = lContestantNames[lContestantCount - i - 1];

            lContestantMatching.add("%s-%s".formatted(lFirstContestant, lSecondContestant));
        }

        return lContestantMatching;
    }

    private static void printMatchingContestants(final List<String> pContestantPairs) {
        for (final String pair : pContestantPairs) {
            System.out.println(pair);
        }
    }

    /**
     * returns the order of the bracket
     * @param pContestantsSorted
     * @return
     */
    private static List<String> sortBracket(final List<String> pContestantsSorted) {
        final List<String> lSortedBracket = new LinkedList<>(pContestantsSorted);
        if (lSortedBracket.size() <= 2) {
            return lSortedBracket;
        } else return getSubracket(lSortedBracket);
    }

    private static List<String> getSubracket(final List<String> pRemainingContestants) {
        final List<String> lNewContestants = new LinkedList<>();

        lNewContestants.add(pRemainingContestants.removeFirst());
        lNewContestants.add(pRemainingContestants.removeLast());

        final int lMiddleIndex = pRemainingContestants.size() / 2 - 1;
        lNewContestants.add(pRemainingContestants.remove(lMiddleIndex));
        lNewContestants.add(pRemainingContestants.remove(lMiddleIndex));

        if (!pRemainingContestants.isEmpty()) {
            lNewContestants.addAll(getSubracket(pRemainingContestants));
        }

        return lNewContestants;
    }
}
