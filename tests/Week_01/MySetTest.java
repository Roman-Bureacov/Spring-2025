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

    private static Set fSet;

    @BeforeEach
    public void setup() {
        fSet = new MySet(0);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(
                fSet.isEmpty(),
                "set did not return empty when first instantiated"
        );
        fSet.insert(new Object());
        assertFalse(
                fSet.isEmpty(),
                "set did not return is not empty when inserting element"
        );
    }

    @Test
    public void testMakeEmpty() {
        assertTrue(
                fSet.isEmpty(),
                "Set was not empty on instantiation"
        );
        fSet.insert(new Object());
        assertFalse(
                fSet.isEmpty(),
                "set was considered empty after adding element"
        );
        assertDoesNotThrow(
                () -> fSet.makeEmpty(),
                "set threw exception on make empty"
        );
        assertTrue(
                fSet.isEmpty(),
                "set is not considered empty after making empty"
        );
        assertDoesNotThrow(
                () -> fSet.makeEmpty(),
                "set threw exception on making empty after making empty"
        );
    }

    @Test
    public void testNull() {
        assertThrows(
                NullPointerException.class,
                () -> fSet.insert(null),
                "set did not throw correct exception on insertion of null"
        );
        assertThrows(
                NullPointerException.class,
                () -> fSet.remove(null),
                "set did not throw correct exception of remove null"
        );
    }

    @Test
    public void testInsertion() {
        final Object lObj = new Object();
        assertDoesNotThrow(
                () -> fSet.insert(lObj),
                "set threw an exception upon insertion of an object"
        );
        assertDoesNotThrow(
                () -> fSet.remove(lObj),
                "set threw an exception upon attempting to remove an existing object"
        );
        assertDoesNotThrow(
                () -> fSet.remove(new Object()),
                "set threw an exception when attempting to remove an object not in set"
        );
        assertThrows(
                NullPointerException.class,
                () -> fSet.remove(null),
                "set did not throw correct exception for remove null"
        );
    }

    @Test
    public void testResize() {
        assertEquals(
                0,
                fSet.size(),
                "set did not construct with size 0"
        );

        final int lNumberOfInsertions = 11;
        for (int i = 0; i < lNumberOfInsertions; i++) {
            fSet.insert(new Object());
            assertEquals(
                    i + 1,
                    fSet.size(),
                    "set did not increment when adding an item"
            );
        }
    }

    @Test
    public void testSize() {
        assertEquals(
                0,
                fSet.size(),
                "set did not return 0 for a new set"
        );
        fSet.insert(new Object());
        assertEquals(
                1,
                fSet.size(),
                "set did not register an increase in size"
        );
        final Object lOther = new Object();
        fSet.insert(lOther);
        assertEquals(
                2,
                fSet.size(),
                "set did not register another increase in size"
        );
        fSet.insert(lOther);
        assertEquals(
                2,
                fSet.size(),
                "set incorrectly changes size when attempting to insert a duplicate"
        );
        fSet.remove(lOther);
        assertEquals(
                1,
                fSet.size(),
                "set did not register removing an object in size"
        );
    }

    @Test
    public void testPresent() {
        assertFalse(
                fSet.isPresent(null),
                "set did not register that null is not present"
        );
        final Object lObj = new Object();
        fSet.insert(lObj);
        assertTrue(
                fSet.isPresent(lObj),
                "set did not recognize a present object"
        );
        assertFalse(
                fSet.isPresent(new Object()),
                "set failed to not recognize an object not in the set"
        );
    }
}
