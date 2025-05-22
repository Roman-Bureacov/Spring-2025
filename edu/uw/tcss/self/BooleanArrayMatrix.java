package uw.tcss.self;

public class BooleanArrayMatrix implements BooleanMatrix {
    final boolean[][] iMatrix;

    /**
     * Constructs a boolean matrix of any size.
     * @param pRows the number of rows this matrix should have
     * @param pColumns the number of columns this matrix should have
     */
    public BooleanArrayMatrix(final int pRows, final int pColumns) {
        super();
        this.iMatrix = new boolean[pRows][pColumns];
    }

    @Override
    public void flip(final int pRow, final int pCol) {
        this.iMatrix[pRow][pCol] = !this.iMatrix[pRow][pCol];
    }

    @Override
    public void set(final int pRow, final int pCol, final boolean pValue) {
        this.iMatrix[pRow][pCol] = pValue;
    }

    @Override
    public boolean get(final int pRow, final int pCol) {
        return this.iMatrix[pRow][pCol];
    }

    @Override
    public int getWidth() {
        return this.iMatrix[0].length;
    }

    @Override
    public int getHeight() {
        return this.iMatrix.length;
    }
}
