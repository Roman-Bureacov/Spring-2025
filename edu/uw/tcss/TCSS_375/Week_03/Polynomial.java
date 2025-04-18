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
     * @param pCoefficient the coefficient of the term to be inserted
     * @param pExponent the exponent of the term to be inserted
     */
    public void insertTerm(final int pCoefficient, final int pExponent) {
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
        return null;
    }

    public Polynomial negate() {
        this.iTerms.
        return null;
    }

    public Polynomial plus(final Polynomial pPolynomial) {
        return null;
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

    public Polynomial derivative() {
        return null;
    }

    /**
     * Return a string representation of the object.
     *
     * @deprecated use toString instead
     * @return string representation of the object
     */
    @Deprecated
    public String print() {
        return this.toString();
    }

    @Override
    public String toString() {
        return null;
    }
}
