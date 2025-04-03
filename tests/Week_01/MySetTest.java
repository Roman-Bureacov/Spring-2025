package Week_01;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uw.tcss.TCSS_342.Week_01.src.MySet;
import uw.tcss.TCSS_342.Week_01.src.Set;

/**
 * Testing class that checks if the set is properly implemented
 * @author Roman Bureacov
 * @version 2025-04-02
 */
public class MySetTest {

    private static Set sfSet;

    /**
     * Creates a new set for each test.
     */
    @BeforeEach
    public void setup() {
        sfSet = new MySet(0);
    }

    /**
     * Tests on isEmpty returning the right boolean for no elements and
     * with an element.
     */
    @Test
    public void testIsEmpty() {
        assertTrue(
                sfSet.isEmpty(),
                "set did not return empty when first instantiated"
        );
        sfSet.insert(new Object());
        assertFalse(
                sfSet.isEmpty(),
                "set did not return is not empty when inserting element"
        );
    }

    /**
     * test on makeEmpty. Tests if a set will consider itself empty propery after
     * making empty and if it will throw exceptions on successive calls.
     */
    @Test
    public void testMakeEmpty() {
        assertTrue(
                (new MySet(100)).isEmpty(),
                "constructed set with 100 spaces erroneously considered "
                        + "non-emtpy"
        );
        assertTrue(
                sfSet.isEmpty(),
                "Set was not empty on instantiation"
        );
        sfSet.insert(new Object());
        assertFalse(
                sfSet.isEmpty(),
                "set was considered empty after adding element"
        );
        assertDoesNotThrow(
                () -> sfSet.makeEmpty(),
                "set threw exception on make empty"
        );
        assertTrue(
                sfSet.isEmpty(),
                "set is not considered empty after making empty"
        );
        assertDoesNotThrow(
                () -> sfSet.makeEmpty(),
                "set threw exception on making empty after making empty"
        );
    }

    /**
     * Test for feeding null into insert and remove
     */
    @Test
    public void testNull() {
        assertThrows(
                NullPointerException.class,
                () -> sfSet.insert(null),
                "set did not throw correct exception on insertion of null"
        );
        assertThrows(
                NullPointerException.class,
                () -> sfSet.remove(null),
                "set did not throw correct exception of remove null"
        );
    }

    /**
     * Test for inserting various objects and removing throwing exceptions.
     */
    @Test
    public void testInsertion() {
        final Object lObj = new Object();
        assertDoesNotThrow(
                () -> sfSet.insert(lObj),
                "set threw an exception upon insertion of an object"
        );
        assertDoesNotThrow(
                () -> sfSet.remove(lObj),
                "set threw an exception upon attempting to remove an existing object"
        );
        assertDoesNotThrow(
                () -> sfSet.remove(new Object()),
                "set threw an exception when attempting to remove an object not in set"
        );
        assertThrows(
                NullPointerException.class,
                () -> sfSet.remove(null),
                "set did not throw correct exception for remove null"
        );
    }

    /**
     * Test if the set will properly resize when adding enough elements.
     */
    @Test
    public void testResize() {
        assertEquals(
                0,
                sfSet.size(),
                "set did not construct with size 0"
        );

        final int lNumberOfInsertions = 11;
        for (int i = 0; i < lNumberOfInsertions; i++) {
            sfSet.insert(new Object());
            assertEquals(
                    i + 1,
                    sfSet.size(),
                    "set did not increment when adding an item"
            );
        }
    }

    /**
     * tests on the size method, if the size method returns the right amount
     * and if duplicates change the size at all.
     */
    @Test
    public void testSize() {
        assertEquals(
                0,
                sfSet.size(),
                "set did not return 0 for a new set"
        );
        sfSet.insert(new Object());
        assertEquals(
                1,
                sfSet.size(),
                "set did not register an increase in size"
        );
        final Object lOther = new Object();
        sfSet.insert(lOther);
        assertEquals(
                2,
                sfSet.size(),
                "set did not register another increase in size"
        );
        sfSet.insert(lOther);
        assertEquals(
                2,
                sfSet.size(),
                "set incorrectly changes size when attempting to insert a duplicate"
        );
        sfSet.remove(lOther);
        assertEquals(
                1,
                sfSet.size(),
                "set did not register removing an object in size"
        );
    }

    /**
     * tests on isPresent.
     */
    @Test
    public void testPresent() {
        assertFalse(
                sfSet.isPresent(null),
                "set did not register that null is not present"
        );
        final Object lObj = new Object();
        sfSet.insert(lObj);
        assertTrue(
                sfSet.isPresent(lObj),
                "set did not recognize a present object"
        );
        assertFalse(
                sfSet.isPresent(new Object()),
                "set failed to not recognize an object not in the set"
        );
    }
}
