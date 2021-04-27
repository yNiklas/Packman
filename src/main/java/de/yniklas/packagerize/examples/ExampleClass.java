package de.yniklas.packagerize.examples;

import de.yniklas.packagerize.Include;

import java.util.ArrayList;
import java.util.List;

public class ExampleClass {
    /**
     * Not included since the attribute isn't annotated with an @Include and
     * the class hasn't the @Package annotation.
     */
    String soos = "träld";

    /**
     * todo: Currently unsupported.
     */
    List<Float> floats = new ArrayList<>();

    /**
     * Included in every package in the sub-object "user" with the key "username".
     */
    @Include(key = "user/username")
    String myUsername = "Jack";

    /**
     * Included in every package in the sub-object "user" but with the attributes name
     * as key (-> user/password).
     */
    @Include(key = "user/")
    String password = "topSecret1234";
}
