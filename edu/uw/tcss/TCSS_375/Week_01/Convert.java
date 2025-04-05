/*
 * Convert.java
 * 
 * TCSS 371 assignment 1
 */

package uw.tcss.TCSS_375.Week_01;

/**
 * A class to provide static methods for converting numbers between bases.
 * 
 * @author Alan Fowler
 * @author Roman Bureacov
 * @version Winter 2025
 */
public final class Convert {

    private static final char ZERO = '0';
    private static final char ONE = '1';

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private Convert() {
        super();
        // Objects should not be instantiated from this class.
        // This class is just a home for static methods (functions).
        // This design is similar to the Math class in the Java language.
    }
    
    /**
     * Accepts an array of characters representing the bits in a two's complement number
     * and returns the decimal equivalent.
     * <p>
     * pre-condition:
     * This method requires that the maximum length of the parameter array is 16.
     * <p>
     * post-condition:
     * The value returned is the decimal equivalent of the two's complement parameter.
     * The parameter array is unchanged.
     * 
     * @param pBits an array representing the bits in a two's complement number
     * @throws IllegalArgumentException if the length of the parameter array > 16.
     * @return the decimal equivalent of the two's complement parameter
     */
    public static int convert2sCompToDecimal(final char[] pBits) {
        final int lBitsLength = pBits.length;
        final int lMaxBitsLength = 16;
        if (lBitsLength > lMaxBitsLength) {
            throw new IllegalArgumentException(
                    "bits length is too long (%d > %d)".formatted(lBitsLength, lMaxBitsLength)
            );
        }

        /*
         * This method specifically makes use of the
         * binary to decimal conversion mentioned in
         * textbook section 2.4.1, Binary to Decimal Conversion
         */
        final int lSign = signOf(pBits);
        final char[] lBits = convertTo2sPositive(pBits);
        final int lDecimal = binaryToDecimal(lBits);

        return lSign * lDecimal;
    }
    

    
    /**
     * Accepts a decimal parameter and returns an array of characters
     * representing the bits in the 16 bit two's complement equivalent.
     * <p>
     * pre-condition:
     * This method requires that the two's complement equivalent
     * won't require more than 16 bits
     * <p>
     * post-condition:
     * The returned array represents the 16 bit two's complement equivalent
     * of the decimal parameter.
     * 
     * @param pDecimal the decimal number to convert to two's complement
     * @throws IllegalArgumentException if the parameter cannot be represented in 16 bits.
     * @return a 16 bit two's complement equivalent of the decimal parameter
     */
    public static char[] convertDecimalTo2sComp(final int pDecimal) {
        // pre-condition
        final double lBase = 2;
        final double lMaxPower = 15;
        final int lUpperBound = (int) Math.pow(lBase, lMaxPower) - 1;
        final int lLowerBound = -1 * (int) Math.pow(lBase, lMaxPower);
        if (pDecimal < lLowerBound || lUpperBound < pDecimal) {
            throw new IllegalArgumentException(
                    "Unable to represent the specified decimal in 16 bits "
                            + "(decimal must be between %d and %d, inclusive)"
                            .formatted(lLowerBound, lUpperBound)
            );
        }

        /*
         * This method specifically makes use of the
         * decimal to binary conversion as described in
         * textbook section 2.4.2, Decimal to Binary Conversion
         */

        final int lDataSize = 16;
        final char[] lBit = new char[lDataSize];
        // starting from the end...
        int lValue = pDecimal;
        for (int i = lDataSize - 1; i >= 0; i--) {
            // because zero is even by definition
            // there is no need to check for evenness
            // when lValue == 0
            if (isOdd(lValue))  {
                lBit[i] = ONE;
                lValue -= 1;
            } else lBit[i] = ZERO;

            lValue /= 2;
        }
        
        return lBit;
    }

    /* ** helpers ** */

    private static char flipBit(final char pChar) {
        // pChar is either '0' or '1',
        // the difference concludes in an integer 0 or 1
        final int lBit = pChar - ZERO;
        // (bit + 1) % 2 = inversion of the bit as a char
        return (char) (ZERO + ((lBit + 1) % 2));
    }

    private static boolean isOne(final char pChar) {
        return pChar == ONE;
    }

    private static char[] convertTo2sPositive(final char[] pBits) {
        final int lBitsLength = pBits.length;
        final char[] lBitsCopy = new char[lBitsLength];
        if (isNegative(pBits)) {
            // method to find 2s complement as described in
            // textbook section 2.3, example 2.1
            // invert all bits
            for (int i = 0; i < lBitsLength; i++) {
                lBitsCopy[i] = flipBit(pBits[i]);
            }
            // add 1
            if (isOne(lBitsCopy[lBitsLength - 1])) {
                lBitsCopy[lBitsLength - 1] = ZERO;
                // starting from the bit one over from the right
                for (int i = lBitsLength - 2; i >= 0; i--) {
                    if (isOne(lBitsCopy[i])) {
                        // being one causes a carry
                        lBitsCopy[i] = ZERO;
                    } else {
                        // being zero does not cause a carry
                        lBitsCopy[i] = ONE;
                        break;
                    }
                }
            } else lBitsCopy[lBitsLength - 1] = ONE;

        } else {
            System.arraycopy(
                    pBits, 0,
                    lBitsCopy, 0,
                    lBitsLength
            );
        }

        return lBitsCopy;
    }

    private static boolean isNegative(final char[] pBits) {
        return isOne(pBits[0]);
    }

    private static int signOf(final char[] pBits) {
        return isNegative(pBits) ? -1 : 1;
    }

    private static int binaryToDecimal(final char[] pBits) {
        final int lBitsLength = pBits.length;
        double lSum = 0;
        final int lBase = 2;
        // start from the end of the array
        for (int i = lBitsLength - 1; i >= 0; i--) {
            if (isOne(pBits[i])) lSum += Math.pow(lBase, lBitsLength - (i + 1));
        }
        return (int) lSum;
    }

    private static boolean isOdd(final int pInt) {
        return pInt % 2 != 0;
    }
}
