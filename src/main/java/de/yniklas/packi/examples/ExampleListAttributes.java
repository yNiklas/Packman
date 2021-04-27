package de.yniklas.packi.examples;

import de.yniklas.packi.Exclude;
import de.yniklas.packi.IncludeOnly;
import de.yniklas.packi.Package;

import java.util.ArrayList;
import java.util.List;

@Package
public class ExampleListAttributes {
    /**
     * The list will be included due to the @Package annotation of the class.
     * It will be represented as JSON-Array containing ints.
     */
    @Exclude(scopes = {"advancedCollections"})
    List<Integer> myInts = new ArrayList<>();

    /**
     * The list will be included due to the @Package annotation of the class.
     * It will be represented as JSON-Array containing bytes.
     */
    @Exclude(scopes = {"advancedCollections"})
    byte[] myBytes = new byte[]{1,2};

    @IncludeOnly(scopes = {"advancedCollections"})
    String[][] twoDimensionsOverpower = new String[][]{{"have"}, {"a", "nice", "day"}};

    @IncludeOnly(scopes = {"advancedCollections"})
    List<ExampleUser> registeredUsers = new ArrayList<>();

    @IncludeOnly(key = "floats", scopes = {"advancedCollections"})
    List<List<Float>> floatsOfLists = new ArrayList<>();

    public ExampleListAttributes() {
        myInts.add(1);
        myInts.add(5);

        registeredUsers.add(new ExampleUser());
        registeredUsers.add(new ExampleUser());

        floatsOfLists.add(new ArrayList<>());
        List<Float> innerFloats = new ArrayList<>();
        innerFloats.add(6.8f);
        floatsOfLists.add(innerFloats);
    }
}
