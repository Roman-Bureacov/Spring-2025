package uw.tcss.self;

public class BitMatrixTest {
    public static void main(final String... pArgs) {
        final BooleanMatrix lMatrix = new BitMatrix(5, 5);
        System.out.println("initial matrix: " + lMatrix);
        lMatrix.set(2, 2, true);
        System.out.println("After set (2,2) to true: " + lMatrix);
        lMatrix.flip(2, 2);
        System.out.println("After flip (2,2): " + lMatrix);
        lMatrix.flip(3, 3);
        System.out.println("After flip (3,3): " + lMatrix);
        System.out.println("bit at (3,3): " + lMatrix.get(3, 3));
        lMatrix.set(3, 3, false);
        System.out.println("After set (3,3) to false: " + lMatrix);
    }
}
