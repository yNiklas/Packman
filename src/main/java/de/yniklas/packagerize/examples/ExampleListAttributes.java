package de.yniklas.packagerize.examples;

import de.yniklas.packagerize.Package;

import java.util.ArrayList;
import java.util.List;

@Package
public class ExampleListAttributes {
    /**
     * The list will be included due to the @Package annotation of the class.
     * It will be represented as JSON-Array containing ints.
     */
    List<Integer> myInts = new ArrayList<>();

    /**
     * The list will be included due to the @Package annotation of the class.
     * It will be represented as JSON-Array containing bytes.
     */
    byte[] myBytes = new byte[]{1,2};

    public ExampleListAttributes() {
        myInts.add(1);
        myInts.add(5);
    }
}
