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

    public void zeroPolynomial() {
        this.iTerms.makeEmpty();
    }

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
        final Iterator lOtherIter = this.iTerms.iterator();

        final Polynomial lSum = new Polynomial();
        final Iterator lSumIter = lSum.iTerms.zeroth();

        // add terms
        if (lThisIter.hasNext() && lOtherIter.hasNext()) {
            Literal lThisLiteral = (Literal) lThisIter.next();
            Literal lOtherLiteral = (Literal) lOtherIter.next();

            while (lThisIter.hasNext() && lOtherIter.hasNext()) {
                final int lThisExp = lThisLiteral.getExponent();
                final int lOtherExp = lOtherLiteral.getExponent();

                if (lThisExp == lOtherExp) {
                    final int lNewCoefficient = lThisLiteral.getCoefficient() + lOtherLiteral.getCoefficient();
                    if (lNewCoefficient != 0) {
                        lSum.iTerms.insert(new Literal(lNewCoefficient, lThisExp),  lSumIter);
                    }
                    lThisLiteral = (Literal) lThisIter.next();
                    lOtherLiteral = (Literal) lOtherIter.next();
                } else if (lThisExp > lOtherExp) {
                    final Literal lNewLiteral = new Literal(lThisLiteral.getCoefficient(), lThisExp);
                    lSum.iTerms.insert(lNewLiteral,  lSumIter);
                    lThisLiteral = (Literal) lThisIter.next();
                } else { // lThisExp < lOtherExp
                    final Literal lNewLiteral = new Literal(lOtherLiteral.getCoefficient(), lOtherExp);
                    lSum.iTerms.insert(lNewLiteral,  lSumIter);
                    lOtherLiteral = (Literal) lOtherIter.next();
                }

                lSumIter.next();
            }

            // final comparison
            // the final two terms have one or both with no hasNext, thus the loop breaks early
            final int lThisExp = lThisLiteral.getExponent();
            final int lOtherExp = lOtherLiteral.getExponent();

            if (lThisExp == lOtherExp) {
                final int lNewCoefficient = lThisLiteral.getCoefficient() + lOtherLiteral.getCoefficient();
                if (lNewCoefficient != 0) {
                    lSum.iTerms.insert(new Literal(lNewCoefficient, lThisExp),  lSumIter);
                }
            } else if (lThisExp > lOtherExp) {
                final Literal lNewLiteral = new Literal(lThisLiteral.getCoefficient(), lThisExp);
                lSum.iTerms.insert(lNewLiteral,  lSumIter);
            } else { // lThisExp < lOtherExp
                final Literal lNewLiteral = new Literal(lOtherLiteral.getCoefficient(), lOtherExp);
                lSum.iTerms.insert(lNewLiteral,  lSumIter);
            }

            lSumIter.next();
        }
        // insert the rest of the terms
        if (lThisIter.hasNext()) insertRemaining(lSum, lSumIter, lThisIter);
        else if (lOtherIter.hasNext()) insertRemaining(lSum, lSumIter, lOtherIter);

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
    private static void insertRemaining(final Polynomial pInto, final Iterator pIntoIter,
                                        final Iterator pFromIter) {
        while (pFromIter.hasNext()) {
            final Literal lFromLiteral = (Literal) pFromIter.next();
            pInto.iTerms.insert(lFromLiteral, pIntoIter);
            pIntoIter.next();
        }
    }

    /**
     * Wrapper class for the linked list class, to prevent constant casting from obfuscating code.
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
