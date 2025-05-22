package uw.tcss.self;

import java.util.Random;

/**
 * A static class that tests the runtime difference between the int bit matrix and the
 * boolean array matrix.
 *
 * @author Roman Bureacov
 * @version 2025-05-22
 */
public final class BooleanMatrixRuntimeTest {
    private static final long RANDOM_SEED = 0x123456789ABCDEFL;
    private static final long LOOP_COUNT = 100_000_000L;

    private BooleanMatrixRuntimeTest() {
        super();
    }

    public static void main(final String... pArgs) {
        final int lTest1 = 8;
        final int lTest2 = 16;
        final int lTest3 = 32;
        final int lTest4 = 64;
        System.out.format("Run time tests for %d random flips\n\n", LOOP_COUNT);

        // test at size 8
        runAndPrintTest(lTest1);

        // test at size 16
        runAndPrintTest(lTest2);

        // test at size 32
        runAndPrintTest(lTest3);

        // test at size 64
        runAndPrintTest(lTest4);
    }

    private static void runAndPrintTest(final int pMatrixSize) {
        System.out.format(
                "Matrix size %d:\nRun time for bit matrix: %d ms\nRunTime for boolean Matrix %d ms\n\n",
                pMatrixSize,
                testBitMatrix(pMatrixSize),
                testBooleanArrayMatrix(pMatrixSize));
    }

    private static long testBitMatrix(final int pMatrixSize) {
        final BooleanMatrix lMatrix = new BitMatrix(pMatrixSize, pMatrixSize);
        return testMatrix(lMatrix);
    }

    private static long testBooleanArrayMatrix(final int pMatrixSize) {
        final BooleanMatrix lMatrix = new BooleanArrayMatrix(pMatrixSize, pMatrixSize);
        return testMatrix(lMatrix);
    }

    private static long testMatrix(final BooleanMatrix pMatrix) {
        final Random lNumberGen = new Random(RANDOM_SEED);
        return timeTask(
                () -> pMatrix.flip(
                        lNumberGen.nextInt(pMatrix.getHeight()),
                        lNumberGen.nextInt(pMatrix.getWidth())
                ),
                LOOP_COUNT
        );
    }

    private static long timeTask(final Runnable pTask, final long pLoops) {
        final long lStartTime = System.currentTimeMillis();
        for (long l = 0; l < pLoops; l++) pTask.run();
        return System.currentTimeMillis() - lStartTime;
    }
}
