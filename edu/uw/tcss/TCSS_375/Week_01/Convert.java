/*
 * Convert.java
 * 
 * TCSS 371 assignment 1
 */

package code;

/**
 * A class to provide static methods for converting numbers between bases.
 * 
 * @author Alan Fowler
 * @author Your name(s) here
 * @version Winter 2025
 */
public final class Convert {
    
    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private Convert() {
        // Objects should not be instantiated from this class.
        // This class is just a home for static methods (functions).
        // This design is similar to the Math class in the Java language.
    }
    
    /**
     * Accepts an array of characters representing the bits in a two's complement number
     * and returns the decimal equivalent.
     *
     * precondition:
     * This method requires that the maximum length of the parameter array is 16.
     *
     * postcondition:
     * The value returned is the decimal equivalent of the two's complement parameter.
     * The parameter array is unchanged.
     * 
     * @param theBits an array representing the bits in a two's complement number
     * @throws IllegalArgumentException if the length of the parameter array > 16.
     * @return the decimal equivalent of the two's complement parameter
     */
    public static int convert2sCompToDecimal(final char[] theBits) {
        
        /*
         * Write code here to implement this function.
         * Add comments, to explain any complex parts of your algorithm.
         * 
         * IMPORTANT: Be sure to include a comment to indicate which algorithm
         * from the chapter 2 lecture slides you are using.
         * Method 1 from slide 24
         * OR
         * Method 2 from slide 26
         */

        return 0; // Replace the zero return value with a correct return value.
    }
    

    
    /**
     * Accepts a decimal parameter and returns an array of characters
     * representing the bits in the 16 bit two's complement equivalent.
     * 
     * precondition:
     * This method requires that the two's complement equivalent
     * won't require more than 16 bits
     *
     * postcondition:
     * The returned array represents the 16 bit two's complement equivalent
     * of the decimal parameter.
     * 
     * @param theDecimal the decimal number to convert to two's complement
     * @throws IllegalArgumentException if the parameter cannot be represented in 16 bits.
     * @return a 16 bit two's complement equivalent of the decimal parameter
     */
    public static char[] convertDecimalTo2sComp(final int theDecimal) {
        
        /*
         * Write code here to implement this function.
         * Add comments, to explain any complex parts of your algorithm.
         * 
         * IMPORTANT: Be sure to include a comment to indicate which algorithm
         * from the chapter 2 lecture slides you are using.
         * Method 1 from slide 28
         * OR
         * Method 2 from slide 29
         */
        
        return null;  // Replace the null return value with a correct return value.
    }
    
    
    /*
     * NOTE: If you wish, you may also include private helper methods in this class.
     */

}
