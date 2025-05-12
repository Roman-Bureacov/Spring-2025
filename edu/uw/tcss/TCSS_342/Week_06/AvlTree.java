package uw.tcss.TCSS_342.Week_06;

import java.lang.Comparable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// Comparable find( x )   --> Return item that matches x
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
public class AvlTree {
    
    /** The tree root. */
    private AvlNode root;

    /**
     * Construct the tree.
     */
    public AvlTree( ) {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert( Comparable x ) {
        root = insert( x, root );
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove( Comparable x ) {
        System.out.println( "Sorry, remove unimplemented" );
    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public Comparable findMin( ) {
        return elementAt( findMin( root ) );
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public Comparable findMax( ) {
        return elementAt( findMax( root ) );
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return the matching item or null if not found.
     */
    public Comparable find( Comparable x ) {
        return elementAt( find( x, root ) );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( ) {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( ) {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree( ) {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root );
    }

    /**
     * Internal method to get element field.
     * @param t the node.
     * @return the element field or null if t is null.
     */
    private Comparable elementAt( AvlNode t ) {
        return t == null ? null : t.element;
    }

    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the tree.
     * @return the new root.
     */
    private AvlNode insert( Comparable x, AvlNode t ) {
        if( t == null ) {
            t = new AvlNode( x, null, null );
        } else if( x.compareTo( t.element ) < 0 ) {
            t.left = insert( x, t.left );
            if( height( t.left ) - height( t.right ) == 2 )
                if( x.compareTo( t.left.element ) < 0 )
                    t = rotateWithLeftChild( t );
                else
                    t = doubleWithLeftChild( t );
        } else if( x.compareTo( t.element ) > 0 ) {
            t.right = insert( x, t.right );
            if( height( t.right ) - height( t.left ) == 2 )
                if( x.compareTo( t.right.element ) > 0 )
                    t = rotateWithRightChild( t );
                else
                    t = doubleWithRightChild( t );
        } else {
            t.elementCount++;
        }
        t.height = max( height( t.left ), height( t.right ) ) + 1;
        return t;
    }
    
    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode findMin( AvlNode t ) {
        if( t == null )
            return t;
            
        while( t.left != null )
            t = t.left;
        return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode findMax( AvlNode t ) {
        if( t == null )
            return t;
        while( t.right != null )
            t = t.right;
        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return node containing the matched item.
     */
    private AvlNode find( Comparable x, AvlNode t ) {
        while( t != null )
            if( x.compareTo( t.element ) < 0 )
                t = t.left;
            else if( x.compareTo( t.element ) > 0 )
                t = t.right;
            else
                return t;    // Match
         return null;   // No match
    }
    
    /**
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the tree.
     */
    private void printTree( AvlNode t ) {
        if( t != null ) {
            printTree( t.left );
            System.out.println( t.element );
            printTree( t.right );
        }
    }

    /**
     * Internal method to print a subtree rooted at t,
     * indicating depth.
     * @param t the node that roots the (sub)tree
     * @param depth the current depth to print the subtree
     */
    private void printTreeDepth( AvlNode t, int depth ) {
        if( t != null ) {
            printTreeDepth( t.right, depth + 1 );
            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }
            System.out.println(t.element);
            printTreeDepth( t.left, depth + 1 );
        }
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private static int height( AvlNode t ) {
        return t == null ? -1 : t.height;
    }

    /**
     * Return maximum of lhs and rhs.
     */
    private static int max( int lhs, int rhs ) {
        return lhs > rhs ? lhs : rhs;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private static AvlNode rotateWithLeftChild( AvlNode k2 ) {
        AvlNode k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = max( height( k2.left ), height( k2.right ) ) + 1;
        k1.height = max( height( k1.left ), k2.height ) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private static AvlNode rotateWithRightChild( AvlNode k1 ) {
        AvlNode k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = max( height( k1.left ), height( k1.right ) ) + 1;
        k2.height = max( height( k2.right ), k1.height ) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private static AvlNode doubleWithLeftChild( AvlNode k3 ) {
        k3.left = rotateWithRightChild( k3.left );
        return rotateWithLeftChild( k3 );
    }
    
    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private static AvlNode doubleWithRightChild( AvlNode k1 ) {
        k1.right = rotateWithLeftChild( k1.right );
        return rotateWithRightChild( k1 );
    }

    /**
     * Print the most frequently occurring words in the tree,
     * along with their frequency.
     */
    public void PrintMostFrequent() {
        final List<String> lTopWords = getMostFrequent(10, this.root);

        System.out.println("Most Frequent Words in AVL Tree:");
        lTopWords.forEach(System.out::println);
        System.out.println();
    }

    public List<String> getMostFrequent(final int pCount) {
        return getMostFrequent(pCount, this.root);
    }

    /**
     * Gets the most frequent nodes as a list of strings, sorted
     * in ascending order
     *
     * @param pCount the top n words
     * @return a list of the top n words
     */
    private static List<String> getMostFrequent(final int pCount, final AvlNode pRoot) {
        final AvlNode[] lAvlNodes = new AvlNode[pCount];

        // insert the elements based on counts
        lAvlNodes[0] = pRoot;

        addNodesToArray(lAvlNodes, pRoot, Comparator.comparingInt(node -> node.elementCount));

        // TODO: test print, need to remove
        System.out.println(Arrays.toString(lAvlNodes));
        Arrays.stream(lAvlNodes).toList().forEach(
                node -> {
                    if (node != null) System.out.format("word %-30s count %d\n", node.element, node.elementCount);
                });

        // convert nodes into their elements
        final List<String> lTopWords = new ArrayList<>(pCount);
        for (final AvlNode node : lAvlNodes) {
            if (node != null) lTopWords.add((String) node.element);
        }

        return List.copyOf(lTopWords);
    }

    /**
     * recursive method that adds the avl nodes into the array
     * @param pAvlNodes the array of nodes
     * @param pNode the node to examine
     * @param pComparator the comparison to be done
     */
    private static void addNodesToArray(final AvlNode[] pAvlNodes,
                                        final AvlNode pNode,
                                        final Comparator<AvlNode> pComparator) {

        if (pNode == null) return;

        final int lArraySize = pAvlNodes.length;

        // if the array is not full...
        if (pAvlNodes[lArraySize - 1] == null) {
            insertInNonFullArray(pAvlNodes, pNode, pComparator);
        } else { // we know the array is full
            if (pComparator.compare(pNode, pAvlNodes[0]) >= 0) { // if it's greater than the first element
                for (int i = lArraySize - 1; 0 <= i; i--) { // starting from the right (the largest)
                    final int lComparisonResult = pComparator.compare(pNode, pAvlNodes[i]);
                    if (lComparisonResult > 0) {
                        // shift everything left
                        for (int j = 0; j < i; j++) {
                            pAvlNodes[j] = pAvlNodes[j + 1];
                        }
                        pAvlNodes[i] = pNode;
                        break;
                    } else if (lComparisonResult == 0) {
                        pAvlNodes[i] = pNode;
                        break;
                    }
                }
            } else return; // every next element in the subtree will be less than the first element in the array
        }

        addNodesToArray(pAvlNodes, pNode.left, pComparator);
        addNodesToArray(pAvlNodes, pNode.right, pComparator);
    }

    private static void insertInNonFullArray(final AvlNode[] pAvlNodes,
                                             final AvlNode pNode,
                                             final Comparator<AvlNode> pComparator) {
        final int lArraySize = pAvlNodes.length;

        if (pComparator.compare(pNode, pAvlNodes[0]) < 0) {
            // shift elements to the right by 1 starting from the end
            for (int j = lArraySize - 1; 0 < j; j--) {
                pAvlNodes[j] = pAvlNodes[j - 1];
            }

            pAvlNodes[0] = pNode;
        } else {
            for (int i = 0; i < lArraySize; i++) {
                if (pAvlNodes[i] == null) {
                    pAvlNodes[i] = pNode;
                    break;
                } else {
                    final int lComparisonResult = pComparator.compare(pNode, pAvlNodes[i]);
                    if (lComparisonResult < 0) {
                        // shift elements to the right by 1 and insert the element
                        for (int j = lArraySize - 1; i < j; j--) {
                            pAvlNodes[j] = pAvlNodes[j - 1];
                        }
                        pAvlNodes[i] = pNode;
                        break;
                    } else if (lComparisonResult == 0) {
                        pAvlNodes[i] = pNode;
                        break;
                    }
                }

            }
        }
    }
    
    
    // a test program
    public static void test () {
        AvlTree avltree = new AvlTree();
        
        avltree.insert(new MyString("Happy"));
        avltree.insert(new MyString("weird"));
        avltree.insert(new MyString("sad"));
        avltree.insert(new MyString("Acropolis"));
        avltree.insert(new MyString("ZZZZZZYY"));
        avltree.printTreeDepth(avltree.root, 0);
    }
    
    // Test program
    public static void main( String [ ] args ) {
        AvlTree t = new AvlTree( );
        final int NUMS = 4000;
        final int GAP  =   37;
        System.out.println( "Checking... (no more output means success)" );

        for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
            t.insert( new Integer( i ) );
        if( NUMS < 40 )
            t.printTree( );
        if( ((Integer)(t.findMin( ))).intValue( ) != 1 ||
            ((Integer)(t.findMax( ))).intValue( ) != NUMS - 1 )
            System.out.println( "FindMin or FindMax error!" );

        for( int i = 1; i < NUMS; i++ )
             if( ((Integer)(t.find( new Integer( i ) ))).intValue( ) != i )
                 System.out.println( "Find error1!" );
    }
    

}
