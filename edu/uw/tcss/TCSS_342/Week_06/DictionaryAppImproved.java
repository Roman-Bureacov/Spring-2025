package uw.tcss.TCSS_342.Week_06;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * A modern improvement over the DictionaryApp application.
 *
 * Does the timing and all the tests for the specified files
 */
public final class DictionaryAppImproved {
    private final static String WORKING_DIR_NAME = "edu/uw/tcss/TCSS_342/Week_06/text";
    private final static File WORKING_DIR = new File(WORKING_DIR_NAME);

    private final static String OUT_DIR_NAME =
            String.join("/", WORKING_DIR_NAME, "out");
    private final static File OUT_DIR = new File(OUT_DIR_NAME);

    private final static String OUTPUT_RT_STATS_FILE_NAME =
            String.join("/", OUT_DIR_NAME, "RunTimeStats.csv");
    private final static File OUTPUT_RT_STATS_FILE = new File(OUTPUT_RT_STATS_FILE_NAME);


    public static void main(final String... pArgs) throws IOException {
        final File[] lTextFiles =
                WORKING_DIR.listFiles((file, name) -> name.toLowerCase().endsWith(".txt"));

        setupOutput();

        // Run the tests on all files
        if (lTextFiles != null) {
            for (final File workingFile : lTextFiles) {
                final DataStructStats[] lDataStructureStats = runTests(workingFile);
                writeTopCommonWords(lDataStructureStats, workingFile);
                writeRuntimeStats(lDataStructureStats, workingFile);
            }
        } else {
            System.out.println("Failed to find directory! " + WORKING_DIR.getAbsolutePath());
            return;
        }

        System.out.println("All processing done, see out.csv");
    }

    /**
     * Sets up the output file for runtime stats
     * @throws IOException if a file or directory could not be made
     */
    private static void setupOutput() throws IOException {
        // set up the output directory
        if (!OUT_DIR.exists()) {
            if (!OUT_DIR.mkdir()) throw new IOException(
                    "Could not create directory! %s".formatted(OUT_DIR.getAbsolutePath())
            );
        }

        // set up the output file header

        // make a new file for output
        if (OUTPUT_RT_STATS_FILE.exists()) {
            if (!OUTPUT_RT_STATS_FILE.delete()) throw new IOException(
                    "Could not overwrite output file! %s".formatted(OUTPUT_RT_STATS_FILE.getAbsolutePath())
            );
        }

        if (!OUTPUT_RT_STATS_FILE.createNewFile()) throw new IOException(
                "Could not create stats file! %s".formatted(OUTPUT_RT_STATS_FILE.getAbsolutePath())
        );

        try (final BufferedWriter lWriter = new BufferedWriter(new FileWriter(OUTPUT_RT_STATS_FILE, true))) {
            lWriter.append(
                "file,Binary Search Tree Time, Splay Tree Time, AVL Tree Time, Quadratic Probing Hash Table Time\n");
        }

        System.out.println(OUT_DIR.getAbsolutePath());
    }

    /**
     * Runs the test with the data structures on the file.
     *
     * @param pFile the file to test
     * @return an array of the data structure stats on the file, in the order:
     * <br>Binary Search Tree, Splay Tree, AVL Tree, Hash Table
     */
    private static DataStructStats[] runTests(final File pFile) throws IOException {
        System.out.format("\n== Running test on %s\n", pFile.getName());
        final DataStructStats[] lStats = new DataStructStats[4];
        final Timer lTimer = new Timer();

        // reconstruct the file as valid tokens only
        System.out.format("Parsing file %s...\n", pFile.getName());
        lTimer.start();
        final String lCleanFile = clean(pFile);
        lTimer.stop();
        System.out.format("Finished parsing in %d milliseconds\n", lTimer.getTotalTime());


        // run the tests
        System.out.println("Running test on Binary Search Tree...");
        lStats[0] = runBinarySearchTree(new Scanner(lCleanFile));
        System.out.println("Running test on Splay Tree...");
        lStats[1] = runSplayTree(new Scanner(lCleanFile));
        System.out.println("Running test on AVL tree...");
        lStats[2] = runAVLTree(new Scanner(lCleanFile));
        System.out.println("Running test on Hash Table...");
        lStats[3] = runHashTable(new Scanner(lCleanFile));

        return lStats;
    }

    private static DataStructStats runBinarySearchTree(final Scanner pScanner) {
        final BinarySearchTree lBST = new BinarySearchTree();

        final int lRunTime = getRunTime(() -> {
            while (pScanner.hasNext()) {
                lBST.insert(pScanner.next());
            }
        });

        return new DataStructStats(lBST, lRunTime);
    }

    private static DataStructStats runSplayTree(final Scanner pScanner) {
        final SplayTree lSplay = new SplayTree();

        final int lRunTime = getRunTime(() -> {
            while (pScanner.hasNext()) {
                lSplay.insert(pScanner.next());
            }
        });

        return new DataStructStats(lSplay, lRunTime);
    }

    private static DataStructStats runHashTable(final Scanner pScanner) {
        final QuadraticProbingHashTable lHashTable = new QuadraticProbingHashTable();

        final int lRunTime = getRunTime(() -> {
            while (pScanner.hasNext()) {
                final Hashable lWord = new MyString(pScanner.next());
                lHashTable.insert(lWord);
            }
        });

        return new DataStructStats(lHashTable, lRunTime);
    }

    private static DataStructStats runAVLTree(final Scanner pScanner) {
        final AvlTree lAVLTree = new AvlTree();

        final int lRunTime = getRunTime(() -> {
            while (pScanner.hasNext()) {
                lAVLTree.insert(pScanner.next());
            }
        });

        return new DataStructStats(lAVLTree, lRunTime);
    }

    /**
     * Gets the runtime in milliseconds of the runnable
     *
     * @param pRunnable the code to execute
     * @return the time in milliseconds the code took to execute
     */
    private static int getRunTime(final Runnable pRunnable) {
        final Timer lTimer = new Timer();
        lTimer.start();
        pRunnable.run();
        lTimer.stop();
        return lTimer.getTotalTime();
    }

    /**
     * Cleans a file of non-word characters and returns a string
     * of the file contents with all lowercase and word-only characters.
     *
     * @param pFile the file to clean the input of
     * @return a clean string representing the file
     */
    private static String clean(final File pFile) throws IOException {
        final Scanner lInput = new Scanner(pFile, StandardCharsets.UTF_16BE);
        lInput.useDelimiter("\\W+"); // any non-word character
        final StringBuilder lOutput = new StringBuilder();

        int lWordCount = 0;

        while (lInput.hasNext()) {
            lWordCount++;
            lOutput.append("%s ".formatted(lInput.next().toLowerCase()));
        }

        System.out.format("%d words counted total\n", lWordCount);

        return lOutput.toString();
    }

    /**
     * Writes the 10 most common words for the file specified.
     * @param lStats the stats to extract the most commmon words from
     */
    private static void writeTopCommonWords(final DataStructStats[] lStats, final File pFile) throws IOException {
        final String lOutPath =
                String.join(
                        "/",
                        OUT_DIR_NAME,
                        "%s-topWordStats.txt".formatted(pFile.getName().split("\\.")[0])
                );
        final File lOutFile = new File(lOutPath);
        if (lOutFile.exists()) {
            if (!lOutFile.delete()) throw new IOException(
                    "Could not remove outdated data! %s".formatted(lOutFile.getAbsolutePath())
            );
        }

        if (!lOutFile.createNewFile()) throw new IOException(
                "Could not create the top words stat file! %s".formatted(lOutFile.getAbsolutePath())
        );

        try (final BufferedWriter lWriter = new BufferedWriter(new FileWriter(lOutPath))) {
            lWriter.append("Top 10 words for %s\n".formatted(pFile.getName()));
            final List<String> lTopWords = ((AvlTree) lStats[2].dataStructure).getMostFrequent(10);
            for (final String word : lTopWords) lWriter.append("%s\n".formatted(word));
        }
    }

    private static void writeRuntimeStats(final DataStructStats[] lStats, final File pFile) throws IOException {
        try (final BufferedWriter lWriter = new BufferedWriter(new FileWriter(OUTPUT_RT_STATS_FILE, true))) {

            final StringBuilder lLine = new StringBuilder();
            lLine.append("%s,".formatted(pFile.getName()));

            lLine.append(lStats[0].runningTimeMillis);

            final int lStatsLength = lStats.length;
            for (int i = 1; i < lStatsLength; i++) {
                lLine.append(",%s".formatted(lStats[i].runningTimeMillis));
            }

            lWriter.append("%s\n".formatted(lLine));
        }
    }

    private final static class Timer {
        long iStartTime;
        long iEndTime;

        public void start() {
            this.iStartTime = System.currentTimeMillis();
        }

        public void stop() {
            this.iEndTime = System.currentTimeMillis();
        }

        public int getTotalTime() {
            return (int) (this.iEndTime - this.iStartTime);
        }
    }

    /**
     * A record containing stats on the data structure
     * @param dataStructure the data structure
     * @param runningTimeMillis the time it spent processing the file
     */
    record DataStructStats(Object dataStructure, int runningTimeMillis) { }
}
