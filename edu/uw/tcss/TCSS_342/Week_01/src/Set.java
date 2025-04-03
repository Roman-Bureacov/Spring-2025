package uw.tcss.TCSS_342.Week_01.src;

/**
 * Interface describing a set of objects.
 * @author Roman Bureacov
 * @version 2025-04-02
 */
public interface Set {
    /**
     * Checks if the set is empty.
     * @return true if the set is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Removes all elements from the set.
     */
    void makeEmpty();

    /**
     * Gets the number of elements in this set.
     * @return the number of elements in the set
     */
    int size();

    /**
     * Inserts the object into the set.
     * @param pObject the object to insert
     * @throws NullPointerException if the object argument is null
     */
    void insert(Object pObject);

    /**
     * Removes the object from the set.
     * @param pObject the object to remove
     * @throws NullPointerException if the object argument is null
     */
    void remove(Object pObject);

    /**
     * Checks if the object is present in this set.
     * @param pObject the object to check
     * @return true if the object argument is contained in the set, false otherwise
     */
    boolean isPresent(Object pObject);
}
