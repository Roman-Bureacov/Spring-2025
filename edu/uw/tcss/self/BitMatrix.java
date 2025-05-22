package uw.tcss.self;

/**
 * Implementation of the boolean matrix as a collection of longs.
 * <br>
 * Although the rows can extend indefinitely, the columns are limited to 128 distinct
 * columns since a 128-bit long integer can only define up to 128 unique bits.
 *
 * @author Roman Bureacov
 * @version 2025-05-21
 */
public class BitMatrix implements BooleanMatrix {
    /** The maximum columns that this bit matrix supports */
    public static final int MAX_COLUMNS = 128;

    final long[] iMatrix;
    final int iRows;
    final int iCols;

    /**
     * Constructs a bit matrix using an integers
     * @param pRows the number of rows (unlimited)
     * @param pColumns the number of columns (up to 64)
     */
    public BitMatrix(final int pRows, final int pColumns) {
        super();
        if (pColumns > MAX_COLUMNS)
            throw new IllegalArgumentException("Column count for IntBitMatrix must not exceed 64");
        if (0 >= pRows || 0 >= pColumns)
            throw new IllegalArgumentException("Row and column arguments must be greater than 0");

        this.iRows = pRows;
        this.iCols = pColumns;
        this.iMatrix = new long[this.iRows];
    }

    @Override
    public void flip(final int pRow, final int pCol) {
        this.boundsCheck(pRow, pCol);

        // bit shift and add (or subtract) based on existing value
        int lWorkingInt = 1 << pCol;
        if (this.get(pRow, pCol)) {
            lWorkingInt = (~lWorkingInt) + 1;
        }
        this.iMatrix[pRow] += lWorkingInt;
    }

    @Override
    public void set(final int pRow, final int pCol, final boolean pValue) {
        this.boundsCheck(pRow, pCol);

        int lWorkingInt = 1 << pCol;
        if (pValue) {
            // bitwise OR with ...00100... to assign bit
            this.iMatrix[pRow] = this.iMatrix[pRow] | lWorkingInt;
        } else {
            // bitwise AND with ...11011... to clear bit
            lWorkingInt = ~lWorkingInt;
            this.iMatrix[pRow] = this.iMatrix[pRow] & lWorkingInt;
        }
    }

    @Override
    public boolean get(final int pRow, final int pCol) {
        this.boundsCheck(pRow, pCol);

        final long lWorkingRow = this.iMatrix[pRow];
        // shift the bits and test the significant bit of interest
        return ((lWorkingRow >>> pCol) & 1) == 1;
    }

    private void boundsCheck(final int pRow, final int pCol) {
        if (pRow >= this.iRows) throw new IndexOutOfBoundsException(
                "%d out of bounds for row height %d".formatted(pRow, this.iRows)
        );
        if (pCol >= this.iCols) throw new IndexOutOfBoundsException(
                "%d out of bounds for col width %d".formatted(pCol, this.iCols)
        );
    }

    @Override
    public int getWidth() {
        return this.iCols;
    }

    @Override
    public int getHeight() {
        return this.iRows;
    }

    @Override
    public String toString() {
        final StringBuilder lStr = new StringBuilder();
        // the actual array is a mirror of what is expected, the columns are on
        // the right and go left rather than left, going right
        // mirroring is required
        for (final long row : this.iMatrix) {
            final String lRow = "%128s".formatted(Long.toBinaryString(row)).replace(" ", "0");
            for (int i = 0; i < this.iCols; i++) lStr.append(lRow.charAt(MAX_COLUMNS - 1 - i));
            lStr.append("\n");
        }
        lStr.deleteCharAt(lStr.length() - 1); // trailing newline

        return lStr.toString();
    }
}
