package uw.tcss.self;

/**
 * A boolean matrix. Both rows and columns are 0-indexed.
 *
 * @author Roman Bureacov
 * @version 2025-05-20
 */
public interface BooleanMatrix {
    /**
     * Flips the boolean value contained at the row and column.
     * @param pRow the row to flip at
     * @param pCol the column to flip at
     */
    void flip(int pRow, int pCol);

    /**
     * Sets the boolean value at the row and column.
     * @param pRow the row to set at
     * @param pCol the column to set at
     * @param pValue the value to write in
     */
    void set(int pRow, int pCol, boolean pValue);

    /**
     * Retrieves the boolean value stored at the row and column.
     * @param pRow the row to get from
     * @param pCol the column to get from
     * @return the boolean value contained in the row and column
     */
    boolean get(int pRow, int pCol);

    /**
     * Retrieves the width of this matrix.
     * @return the width of this matrix
     */
    int getWidth();

    /**
     * Retrieves the height of this matrix.
     * @return the height of this matrix
     */
    int getHeight();
}
