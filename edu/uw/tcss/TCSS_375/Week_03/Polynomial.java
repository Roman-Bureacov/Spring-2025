package uw.tcss.TCSS_375.Week_03;

import java.util.Iterator;

/**
 * Class that represents the polynomial.
 *
 * @author Roman Bureacov
 * @version 2025-04-16
 */
public final class Polynomial {
    private final ModifiedLinkedList iTerms;

    // Constructors
    public Polynomial() {
        super();
        this.iTerms = new ModifiedLinkedList();
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
            final Literal lNewLiteral;

            // check if the insertion needs to happen at the beginning of the list
            final Literal lFirstTerm = (Literal)(this.iTerms.iterator().next());
            final int lFirstExponent = lFirstTerm.getExponent();

            if (pExponent > lFirstExponent) {
                lNewLiteral = new Literal(pCoefficient, pExponent);
                this.iTerms.insert(lNewLiteral, this.iTerms.zeroth());
            } else if (pExponent == lFirstExponent) {
                lNewLiteral = new Literal(
                        pCoefficient + lFirstTerm.getCoefficient(),
                        pExponent
                );
                this.iTerms.remove(this.iTerms.zeroth());
                this.iTerms.insert(lNewLiteral, this.iTerms.zeroth());
            } else {
                // otherwise insertion or modification needs to happen in the middle or at the end
                final Iterator lTestIterator = this.iTerms.iterator();
                lTestIterator.next();
                if (!lTestIterator.hasNext()) { // edge case, only one node in list
                    this.iTerms.insert(new Literal(pCoefficient, pExponent), this.iTerms.iterator());
                } else { // this algorithm only works when the list contains at least two nodes
                    final Iterator lIterCurrent = this.iTerms.zeroth();
                    final Iterator lIterAhead = this.iTerms.iterator();
                    int lPreviousExp = ((Literal) lIterAhead.next()).getExponent();

                    while (lIterAhead.hasNext()) {
                        lIterCurrent.next();
                        final Literal lCurrentLiteral = (Literal) lIterAhead.next();

                        final int lNewCoefficient;

                        if (pExponent == lCurrentLiteral.getExponent()) {
                            this.iTerms.remove(lIterCurrent);
                            lNewCoefficient = lCurrentLiteral.getCoefficient() + pCoefficient;
                            if (lNewCoefficient != 0) {
                                lNewLiteral = new Literal(lNewCoefficient, pExponent);
                                this.iTerms.insert(lNewLiteral,  lIterCurrent);
                            }
                            return;
                        } else {
                            if (lPreviousExp > pExponent && pExponent > lCurrentLiteral.getExponent()) {
                                lNewLiteral = new Literal(pCoefficient, pExponent);
                                this.iTerms.insert(lNewLiteral,  lIterCurrent);
                                return;
                            }
                            lPreviousExp = lCurrentLiteral.getExponent();
                        }
                    }
                    // failed to insert term before and now at last term
                    lIterCurrent.next(); // advance iterator into position
                    this.iTerms.insert(new Literal(pCoefficient, pExponent),  lIterCurrent);
                }
            }
        }
    }

    /**
     * Sets this polynomial equal to zero.
     */
    public void zeroPolynomial() {
        this.iTerms.makeEmpty();
    }

    /**
     * Negates this polynomial.
     * @return the negation of this polynomial
     */
    public Polynomial negate() {
        final Iterator lThisIter = this.iTerms.iterator();
        final Polynomial lNegativePoly = new Polynomial();
        final Iterator lOtherIter = lNegativePoly.iTerms.zeroth();

        // reconstruct the polynomial with negative terms in O(n) time
        // using method insert would lead to O(n^2) time
        while (lThisIter.hasNext()) {
            final Literal lWorkingLiteral = (Literal) lThisIter.next();
            final Literal lNewLiteral = new Literal(
                    -1 * lWorkingLiteral.getCoefficient(),
                    lWorkingLiteral.getExponent()
            );

            lNegativePoly.iTerms.insert(lNewLiteral,  lOtherIter);

            lOtherIter.next();
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
        final Iterator lOtherIter = pPolynomial.iTerms.iterator();

        final Polynomial lSum = new Polynomial();
        final Iterator lSumIter = lSum.iTerms.zeroth();

        // if neither are empty
        if (lThisIter.hasNext() && lOtherIter.hasNext()) {
            Literal lThisLiteral = (Literal) lThisIter.next();
            Literal lOtherLiteral = (Literal) lOtherIter.next();

            // while we have yet to evaluate all elements
            while (lThisLiteral != null && lOtherLiteral != null) {

                final int lThisLiteralExp = lThisLiteral.getExponent();
                final int lOtherLiteralExp = lOtherLiteral.getExponent();

                if (lThisLiteralExp == lOtherLiteralExp) {
                    final int lNewCoefficient = lThisLiteral.getCoefficient() + lOtherLiteral.getCoefficient();
                    if (lNewCoefficient != 0) {
                        final Literal lNewLiteral = new Literal(lNewCoefficient, lThisLiteralExp);
                        lSum.iTerms.insert(lNewLiteral, lSumIter);
                        lSumIter.next();
                    }
                    lThisLiteral = requestNext(lThisIter);
                    lOtherLiteral = requestNext(lOtherIter);
                } else if (lThisLiteralExp > lOtherLiteralExp) {
                    lSum.iTerms.insert(lThisLiteral, lSumIter);
                    lThisLiteral = requestNext(lThisIter);
                    lSumIter.next();
                } else { // thisExp < otherExp
                    lSum.iTerms.insert(lOtherLiteral, lSumIter);
                    lOtherLiteral = requestNext(lOtherIter);
                    lSumIter.next();
                }
            }

            if (lThisLiteral != null) {
                lSum.iTerms.insert(lThisLiteral, lSumIter);
                lSumIter.next();
            } else if (lOtherLiteral != null) {
                lSum.iTerms.insert(lOtherLiteral, lSumIter);
                lSumIter.next();
            }
        }

        // appends remaining terms, if any
        if (lThisIter.hasNext()) insertRemainingSum(lSum, lSumIter, lThisIter);
        else if (lOtherIter.hasNext()) insertRemainingSum(lSum, lSumIter, lOtherIter);

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

    /**
     * multiplies this polynomial by the other
     * @param pPolynomial the polynomial to multiply with
     * @return this polynomial times the other
     */
    public Polynomial times(final Polynomial pPolynomial) {
        Polynomial lProduct = new Polynomial();
        final Iterator lThisIter = this.iTerms.iterator();

        // we can take the idea that we can just multiply a polynomial by each term, and then
        // sum up all the products
        // i.e. (x + 2) * (x^2 + 1) = x(x^2 + 1) + 2(x^2 + 1)
        while (lThisIter.hasNext()) {
            final Polynomial lSubProd = multiplyTerm((Literal) lThisIter.next(), pPolynomial);
            lProduct = lProduct.plus(lSubProd);
        }

        return lProduct;
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
            final Literal lOldLiteral = (Literal) lThisIter.next();

            final int lDerivCoeff = lOldLiteral.getCoefficient() * lOldLiteral.getExponent();
            final int lDerivExp = lOldLiteral.getExponent() - 1;

            if (lDerivCoeff != 0) {
                final Literal lDerivativeTerm = new Literal(lDerivCoeff, lDerivExp);
                lDerivative.iTerms.insert(lDerivativeTerm,  lDerivIter);
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
            // insert first element, which may or may not be negative
            final Literal lFirstLiteral = (Literal) lIterator.next();
            lPolynomialString.append("%s%s".formatted(
                    lFirstLiteral.getCoefficient() > 0 ? "" : "-",
                    termToString(lFirstLiteral)));

            while (lIterator.hasNext()) {
                final Literal lLiteral = (Literal) lIterator.next();
                final String lFormattedTerm = termToString(lLiteral);

                if (lLiteral.getCoefficient() > 0) {
                    lPolynomialString.append(" + %s".formatted(lFormattedTerm));
                } else {
                    lPolynomialString.append(" - %s".formatted(lFormattedTerm));
                }
            }
        } else {
            // polynomial is empty
            lPolynomialString.append(0);
        }

        return lPolynomialString.toString();
    }

    /* --- HELPERS --- */

    /**
     * Multiplies the term into the polynomial
     * @param pTerm the term to multiply
     * @param pPoly the polynomial to affect
     * @return the product of the term and the polynomial
     */
    private static Polynomial multiplyTerm(final Literal pTerm, final Polynomial pPoly) {
        final Polynomial lProduct = new Polynomial();
        final Iterator lProductIter = lProduct.iTerms.zeroth();

        pPoly.iTerms.iterator().forEachRemaining(
                term -> {
                    final Literal lPolyTerm = (Literal) term;
                    final int lNewCoefficient = lPolyTerm.getCoefficient() * pTerm.getCoefficient();
                    final int lNewExponent = lPolyTerm.getExponent() + pTerm.getExponent();
                    final Literal lNewLiteral = new Literal(lNewCoefficient, lNewExponent);
                    lProduct.iTerms.insert(lNewLiteral, lProductIter);
                    lProductIter.next();
                }
        );

        return lProduct;
    }

    /**
     * Converts the term provided into a string representation. Note that this method is a helper method for
     * the toString and will strip out the negative sign.
     * @param pTerm the term to turn into a string
     * @return the string representation of the term
     */
    private static String termToString(final Literal pTerm) {
        final String lTermString;
        final int lTermExponent = pTerm.getExponent();
        // we need the absolute value, the negative is already formatted
        final int lTermCoefficient = Math.abs(pTerm.getCoefficient());

        if (lTermCoefficient == 1) {
            if (lTermExponent > 1) {
                lTermString = "x^%d".formatted(lTermExponent);
            } else if (lTermExponent < 0) {
                lTermString = "x^(%d)".formatted(lTermExponent);
            } else {
                if (lTermExponent == 1) {
                    lTermString = "x";
                } else {
                    lTermString = "1";
                }
            }
        } else {
            if (lTermExponent > 1) {
                lTermString = "%dx^%d".formatted(lTermCoefficient, lTermExponent);
            } else if (lTermExponent < 0) {
                lTermString = "%dx^(%d)".formatted(Math.abs(lTermCoefficient), lTermExponent);
            } else {
                // if the exponent is one or zero
                if (lTermExponent == 1) {
                    lTermString = "%dx".formatted(lTermCoefficient);
                } else {
                    lTermString = "%d".formatted(lTermCoefficient);
                }
            }
        }

        return lTermString;
    }

    /**
     * Inserts the remaining elements in pFromIter, into pInto
     */
    private static void insertRemainingSum(final Polynomial pInto, final Iterator pIntoIter,
                                           final Iterator pFromIter) {
        while (pFromIter.hasNext()) {
            final Literal lFromLiteral = (Literal) pFromIter.next();
            pInto.iTerms.insert(lFromLiteral, pIntoIter);
            pIntoIter.next();
        }
    }

    /**
     * Requests the next element, null if there are no more elements.
     * @param pIter the iterator to request from
     * @return next literal if available, null otherwise
     */
    private static Literal requestNext(final Iterator pIter) {
        if (pIter.hasNext()) return (Literal) pIter.next();
        else return null;
    }

    /**
     * Wrapper class for the linked list class, to prevent
     * constant casting from interface to concrete class obfuscating code.
     */
    private class ModifiedLinkedList extends LinkedList {

        public void insert(final Object element, final java.util.Iterator pointer) {
            super.insert(element, (LinkedList.Iterator) pointer);
        }

        public void remove(final java.util.Iterator pointer) {
            super.remove((LinkedList.Iterator) pointer);
        }
    }
}
