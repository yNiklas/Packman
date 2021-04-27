package de.yniklas.packi.examples;

import de.yniklas.packi.Include;

public class ExampleClass {
    /**
     * Not included since the attribute isn't annotated with an @Include and
     * the class hasn't the @Package annotation.
     */
    String soos = "träld";

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
