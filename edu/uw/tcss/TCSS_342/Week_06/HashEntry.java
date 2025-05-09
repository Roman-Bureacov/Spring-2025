package uw.tcss.TCSS_342.Week_06;// The basic entry stored in ProbingHashTable

class HashEntry {
    Hashable element;   // the element
    boolean  isActive;  // false is deleted
    int elementCount;

    public HashEntry( Hashable e ) {
        this( e, true );
    }

    public HashEntry( Hashable e, boolean i ) {
        element   = e;
        isActive  = i;
        elementCount = 1;
    }
}
