package uw.tcss.TCSS_342.Week_01.src;

/**
 * Class that implements the Set interface.
 * @author Roman Bureacov
 * @version 2025-04-02
 */
public class MySet implements Set {

    private final static int DEFAULT_SIZE = 5;

    private Object[] fSet;
    private int fElementCount;

    /**
     * Constructs a new set with default size of 5
     */
    public MySet() {
        this(DEFAULT_SIZE);
    }

    /**
     * Constructs a new set with the specified size.
     * @param pSize the size to start with
     */
    public MySet(final int pSize) {
        super();
        this.fSet = new Object[pSize];
    }

    @Override
    public boolean isEmpty() {
        return this.fElementCount == 0;
    }

    @Override
    public void makeEmpty() {
        this.fSet = new Object[DEFAULT_SIZE];
        this.fElementCount = 0;
    }

    @Override
    public int size() {
        return this.fElementCount;
    }

    @Override
    public void insert(final Object pObject) {
        if (pObject == null) throw new NullPointerException(
                "argument pObject to insert was null."
        );
        if (!this.isPresent(pObject)) {
            if (this.fElementCount >= this.fSet.length) {
                this.expandSetAndAdd(pObject);
            } else {
                this.replaceFirstNull(pObject);
            }

            this.fElementCount++;
        }
    }

    @Override
    public void remove(final Object pObject) {
        if (pObject == null) throw new NullPointerException(
                "remove argument pObject is null"
        );
        final int lRemoveAt = this.find(pObject);
        if (lRemoveAt > -1) {
            this.fSet[lRemoveAt] = null;
            this.fElementCount--;
        }
    }

    @Override
    public boolean isPresent(final Object pObject) {
        return this.find(pObject) > -1;
    }

    /**
     * attempts to find the object specified, returning
     * the position of the object.
     * @param pObject the object to look for
     * @return the position of the object in the array.
     * return -1 if pObject not found or null.
     */
    private int find(final Object pObject) {
        if (pObject != null) {
            final int lArraySize = this.fSet.length;
            for (int i = 0; i < lArraySize; i++) {
                final Object lOther = this.fSet[i];
                if (pObject == lOther || pObject.equals(lOther)) return i;
            }
        }

        return -1;
    }

    private void expandSetAndAdd(final Object pObject) {
        final Object[] lNewSet = new Object[this.fSet.length + 5];
        System.arraycopy(
                this.fSet,
                0,
                lNewSet,
                0,
                this.fSet.length
        );
        this.fSet = lNewSet;

        this.fSet[this.fElementCount] = pObject;
    }

    private void replaceFirstNull(final Object pObject) {
        final int lArraySize = this.fSet.length;
        for (int i = 0; i < lArraySize; i++) {
            if (this.fSet[i] == null) this.fSet[i] = pObject;
        }
    }
}
