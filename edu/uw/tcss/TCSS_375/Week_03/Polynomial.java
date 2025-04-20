package uw.tcss.TCSS_375.Week_03;

import java.util.Iterator;

/**
 * Class that represents the polynomial.
 *
 * @author Roman Bureacov
 * @version 2025-04-16
 */
public final class Polynomial {
    private final LinkedList iTerms;

    // Constructors
    public Polynomial() {
        super();
        this.iTerms = new LinkedList();
    }

    /**
     * Insert a term into the polynomial. Insertion will occur at the respective position.
     * <br>
     * For example, insertion of 3x^2 into 2x^3 + 1 will produce 2x^3 + 3x^2 + 1
     * <br>
     * This method will ignore zero terms
     * @param pCoefficient the coefficient of the term to be inserted
     * @param pExponent the exponent of the term to be inserted
     */
    public void insertTerm(final int pCoefficient, final int pExponent) {
        // zero check
        if (pCoefficient == 0) return;

        // is the list is empty...
        if (this.iTerms.isEmpty()) {
            this.iTerms.insert(
                    new Literal(pCoefficient, pExponent),
                    this.iTerms.zeroth()
            );
        } else {
            // check if the insertion needs to happen at the beginning of the list
            final ListNode lFirstNode = (ListNode)(this.iTerms.iterator().next());
            final int lFirstExponent = ((Literal)(lFirstNode.getElement())).getExponent();

            if (pExponent > lFirstExponent) {

                this.iTerms.insert(
                        new ListNode(new Literal(pCoefficient, pExponent)),
                        this.iTerms.zeroth()
                );
            }

            // otherwise insertion or modification needs to happen in the middle or at the end
            final Iterator lIterator = this.iTerms.iterator();

            while (lIterator.hasNext()) {
                final ListNode lNode = (ListNode) lIterator.next();
                final Literal lTerm = (Literal) lNode.getElement();

                final int lNewCoefficient;
                final Literal lNewLiteral;

                if (lTerm.getExponent() == pExponent) {
                    lNewCoefficient = lTerm.getCoefficient() + pCoefficient;
                    lNewLiteral = new Literal(lNewCoefficient, pExponent);
                    lNode.setElement(lNewLiteral);
                    return;
                } else {
                    if (lNode.getNext() != null) { // peek ahead
                        final ListNode lNodeAhead = lNode.getNext();
                        final Literal lTermAhead = (Literal) lNodeAhead.getElement();

                        // we already checked for term greater than (the first node)
                        // we already check for the term equal to
                        // the node we are at has an exponent strictly greater than pExponent
                        if (pExponent > lTermAhead.getExponent()) {
                            lNewLiteral = new Literal(pCoefficient, pExponent);
                            // method uses concrete class "Iterator" instead of interface "Iterator"
                            this.iTerms.insert(new ListNode(lNewLiteral), (LinkedList.Iterator) lIterator);
                            return;
                        }

                    } else { // last node remaining
                        lNewLiteral = new Literal(pCoefficient, pExponent);
                        // method uses concrete class "Iterator" instead of interface "Iterator"
                        this.iTerms.insert(lNewLiteral, (LinkedList.Iterator) lIterator);
                    }
                }

            }
        }
    }

    public void zeroPolynomial() {
        this.iTerms.makeEmpty();
    }

    public Polynomial negate() {
        final Iterator lIterator = this.iTerms.iterator();
        final Polynomial lNegativePoly = new Polynomial();

        // reconstruct the polynomial with negative terms in O(n) time
        // using method insert would lead to O(n^2) time
        ListNode lWorkingNode = lNegativePoly.iTerms.zeroth().getNode();
        while (lIterator.hasNext()) {
            final Literal lLiteral = (Literal) ((ListNode) lIterator.next()).getElement();
            final Literal lNegativeLiteral =
                    new Literal(-1 * lLiteral.getCoefficient(), lLiteral.getExponent());

            lWorkingNode.setNext(new ListNode(lNegativeLiteral));
            lWorkingNode = lWorkingNode.getNext(); // advance
        }
        
        return lNegativePoly;
    }

    /**
     * Adds this the other polynomial to this polynomial.
     * @param pPolynomial the polynomial to add onto this polynomial
     * @return the resulting sum of the two polynomials
     */
    public Polynomial plus(final Polynomial pPolynomial) {
        final Iterator lThisIter = this.iTerms.iterator();
        final Iterator lOtherIter = this.iTerms.iterator();

        final Polynomial lSum = new Polynomial();
        ListNode lSumNode = lSum.iTerms.zeroth().getNode();

        final ListNode lThisNode = (ListNode) lThisIter.next();
        final ListNode lOtherNode = (ListNode) lOtherIter.next();

        // add terms
        while (lThisIter.hasNext() && lOtherIter.hasNext()) {
            final Literal lThisLiteral = listNodeToLiteral(lThisNode);
            final Literal lOtherLiteral = listNodeToLiteral(lOtherNode);
            final int lThisExp = lThisLiteral.getExponent();
            final int lOtherExp = lOtherLiteral.getExponent();

            if (lThisExp == lOtherExp) {
                final int lNewCoefficient =
                        listNodeToLiteral(lThisNode).getCoefficient()
                        + listNodeToLiteral(lOtherNode).getCoefficient();
                if (lNewCoefficient != 0) {
                    lSumNode.setNext(literalToListNode(lNewCoefficient, lThisExp));
                }
            } else if (lThisExp > lOtherExp) {
                lSumNode.setNext(literalToListNode(lThisExp, lThisLiteral.getExponent()));
                lThisIter.next();
            } else { // lThisExp < lOtherExp
                lSumNode.setNext(literalToListNode(lOtherExp, lOtherLiteral.getExponent()));
            }
        }
        // insert the rest of the terms
        if (lThisIter.hasNext()) insertRemaining(lThisIter, lSumNode);
        else if (lOtherIter.hasNext()) insertRemaining(lOtherIter, lSumNode);

        return lSum;
    }

    /**
     * Subtracts the polynomial from this polynomial
     * @param pPolynomial the polynomial to subtract with
     * @return this polynomial minus the polynomial provided
     */
    public Polynomial minus(final Polynomial pPolynomial) {
        return this.plus(pPolynomial.negate());
    }

    // bonus method
    public Polynomial times(Polynomial pPolynomial) {
        return null;
    }

    /**
     * The derivative of this polynomial.
     * @return the derivative of this polynomial
     */
    public Polynomial derivative() {
        final Iterator lThisIter = this.iTerms.iterator();
        final Polynomial lDerivative = new Polynomial();
        final Iterator lDerivIter = lDerivative.iTerms.zeroth();


        while (lThisIter.hasNext()) {
            final Literal lOldLiteral = listNodeToLiteral((ListNode) lThisIter.next());

            final int lDerivCoeff = lOldLiteral.getCoefficient() * lOldLiteral.getExponent();
            final int lDerivExp = lOldLiteral.getExponent() - 1;

            if (lDerivCoeff != 0) {
                final ListNode lNewNode = literalToListNode(lDerivCoeff, lDerivExp);
                lDerivative.iTerms.insert(lNewNode, (LinkedList.Iterator) lDerivIter);
                lDerivIter.next();
            }
        }

        return lDerivative;
    }

    /**
     * Return a string representation of the object.
     *
     * @deprecated use identical toString instead
     * @return string representation of the object
     */
    @Deprecated
    public String print() {
        return this.toString();
    }

    @Override
    public String toString() {
        final StringBuilder lPolynomialString = new StringBuilder();
        final Iterator lIterator = this.iTerms.iterator();

        if (lIterator.hasNext()) {
            // insert first element
            final Literal lFirstLiteral = listNodeToLiteral((ListNode)lIterator.next());
            lPolynomialString.append(termToString(lFirstLiteral));

            while (lIterator.hasNext()) {
                final ListNode lNode = (ListNode) lIterator.next();
                final Literal lLiteral = (Literal) lNode.getElement();

                lPolynomialString.append(
                        " + %s".formatted(termToString(lLiteral))
                );
            }
        } else {
            // polynomial is empty
            lPolynomialString.append(0);
        }

        return lPolynomialString.toString();
    }

    /**
     * Converts the term provided into a string representation
     * @param pTerm the term to turn into a string
     * @return the string representation of the term
     */
    private static String termToString(final Literal pTerm) {
        final String lTermString;
        final int lTermExponent = pTerm.getExponent();

        if (lTermExponent > 1) {
            lTermString = "%dx^%d".formatted(pTerm.getCoefficient(), lTermExponent);
        } else if (lTermExponent < 0) {
            lTermString = "%dx^(%d)".formatted(pTerm.getCoefficient(), lTermExponent);
        } else {
            // if the exponent is one or zero
            if (lTermExponent == 1) {
                lTermString = "%dx".formatted(pTerm.getCoefficient());
            } else {
                lTermString = "%d".formatted(pTerm.getCoefficient());
            }
        }

        return lTermString;
    }

    /**
     * cast Node as Literal
     * @param pNode the node to get the literal from
     * @return the literal contained in the node
     */
    private static Literal listNodeToLiteral(final ListNode pNode) {
        return (Literal) pNode.getElement();
    }

    /**
     * make a node object with empty link based on Literal parameters
     * @param pCoefficient the coefficient of the literal to be stored
     * @param pExponent the exponent of the literal to be stored
     * @return a new list node storing the literal with the parameter data and a null next
     */
    private static ListNode literalToListNode(final int pCoefficient,
                                              final int pExponent) {

        final Literal lNewLiteral = new Literal(pCoefficient, pExponent);
        return new ListNode(lNewLiteral, null);
    }

    /**
     * Inserts the remaining terms from one iterator into the list node and after. For example,
     * an iterator with
     * <br>
     * {@code A -> B -> C}
     * <br>
     * remaining will insert into list node
     * <br>
     * {@code ... -> D}
     * <br>
     * to result in
     * <br>
     * {@code ... -> D -> A -> B -> C}.
     * @param pFrom the iterator that is being inserted from
     * @param pInto the node to insert terms into and after
     */
    private static void insertRemaining(final Iterator pFrom, final ListNode pInto) {
        while (pFrom.hasNext()) {
            final Literal lFromLiteral = listNodeToLiteral((ListNode) pFrom.next());
            pInto.setNext(literalToListNode(lFromLiteral.getCoefficient(), lFromLiteral.getExponent()));
        }
    }
}
