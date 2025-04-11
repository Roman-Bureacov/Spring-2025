package uw.tcss.TCSS_375.Week_02;

/**
 * The idea of this program is to implement the half-adder as shown in 
 * "introduction to computing systems" Page 71 (100/801)
 * 
 * @author Roman Bureacov
 * @version 2025-04-11
 */
final class Personal_BitAdder {

    private Personal_BitAdder() {
        super();
    }

    public static void main(final String... pArgs) {
        final byte lSize = 32;
        // TODO: fix this behavior
        final boolean[] lBitsA = {true, true, false, true};
        final boolean[] lBitsB = {false, true, true, true};
        
        // add the two bit arrays
        final boolean[] lBitsC = sumBits(lBitsA, lBitsB, lSize);

        System.out.format("  %32s\n+ %32s\n= %32s",
                asBinaryString(lBitsA),
                asBinaryString(lBitsB),
                asBinaryString(lBitsC));
    }
    
    @SuppressWarnings("all")
    private static addResult add(final boolean pBitA, final boolean pBitB, final boolean pBitCarry) {
        // and gates
        final boolean lAnd111 = pBitA && pBitB && pBitCarry;
        final boolean lAnd011 = !pBitA && pBitB && pBitCarry;
        final boolean lAnd101 = pBitA && !pBitB && pBitCarry;
        final boolean lAnd001 = !pBitA && !pBitB && pBitCarry;
        final boolean lAnd110 = pBitA && pBitB && !pBitCarry;
        final boolean lAnd010 = !pBitA && pBitB && !pBitCarry;
        final boolean lAnd100 = pBitA && !pBitB && !pBitCarry;
        // final boolean lAnd000 = !pBitA && !pBitB && !pBitCarry;
        
        // connect the and gates to the or gates
        final boolean lBitSum = lAnd111 || lAnd100 || lAnd010 || lAnd001;
        final boolean lBitCarry = lAnd111 || lAnd110 || lAnd101 || lAnd011;
                
        // out
        return new addResult(lBitSum, lBitCarry);
    }
    
    private static boolean[] sumBits(final boolean[] pBitsA, final boolean[] pBitsB, final int pSize) {
        final boolean[] lBitsC = new boolean[pSize];

        if (Math.min(pBitsA.length, pBitsB.length) == 0 || pSize == 0) return lBitsC;
        else {
            final int lBitsAEnd = pBitsA.length - 1;
            final int lBitsBEnd = pBitsB.length - 1;
            final int lBitsCEnd = lBitsC.length - 1;
            final addResult lFirstSum = add(pBitsA[lBitsAEnd], pBitsB[lBitsBEnd], false);
            lBitsC[lBitsCEnd] = lFirstSum.sum();
            boolean lCarry = lFirstSum.carry();

            for (int lBitsAIndex = lBitsAEnd - 1, lBitsBIndex = lBitsBEnd - 1, lBitsCIndex = lBitsCEnd - 1;
                 lBitsAIndex >= 0 && lBitsBIndex >= 0 && lBitsCIndex >= 0;
                 lBitsAIndex--, lBitsBIndex--, lBitsCIndex--) {

                final addResult lSumResult = add(pBitsA[lBitsAIndex], pBitsB[lBitsBIndex], lCarry);
                lBitsC[lBitsCIndex] = lSumResult.sum();
                lCarry = lSumResult.carry();
            }
        }

        return lBitsC;
    }

    private static int asInteger(final boolean pBit) {
        return pBit ? 1 : 0;
    }

    private static String asBinaryString(final boolean[] pBits) {
        final StringBuilder lBuilder = new StringBuilder();
        for (final boolean pBit : pBits) lBuilder.append(asInteger(pBit));
        return lBuilder.toString();
    }
    
    private record addResult(boolean sum, boolean carry) {}
}

